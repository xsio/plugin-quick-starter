package common

import grails.gorm.transactions.Transactional
import org.grails.orm.hibernate.cfg.GrailsDomainBinder

/**
 *  Created by blackJ on 2018/3/16
 */
class NativeInsertService {

    def currentTenant
    def nativeSqlService

    @Transactional
    def insertIgnore(table, domain, values){
        try {
            if (!values){
                return 0
            }
            def nameMap = getDomainFieldNameList(domain)
            def fieldNames =nameMap.keySet()
            def columns = nameMap.values()
            def sql = "insert ignore into ${table}(${columns.join(",")}) VALUES"
            values = getCommonValues(values)
            def valuesSqls = []
            for(int i=0; i<values.size();i++){
                def placeHolder = fieldNames.collect{fieldName->
                    return ":${fieldName}${i}".toString()
                }
                valuesSqls += "(${placeHolder.join(",")})"
            }
            if (valuesSqls.size()!=0){
                sql += valuesSqls.join(",")
                def q = nativeSqlService.createSqlQuery(sql)
                values.eachWithIndex{row,i ->
                    fieldNames.each{fieldName->
                        def name = "${fieldName}${i}".toString()
                        def value = row."$fieldName"
                        q.setParameter(name, value)
                    }
                }
                return q.executeUpdate()
            }
        } catch (Exception e) {
            log.warn("Error occur when save ${domain?.simpleName}: ${e.message}", e)
            return 0
        }
    }

    def getDomainFieldNameList(domain){
        def fieldNames = getDomainProperties(domain)
        def names = [:]
        fieldNames.each{String field->
            names."$field" = nativeSqlService.getColumnName(field)
        }
        return names
    }

    def getCommonValues(List values){
        def date = (new Date()).format("yyyy-MM-dd HH:mm:ss")
        return values.collect{Map value->
            value.tenantId = currentTenant.get()
            value.version = 0
            value.dateCreated = date
            value.lastUpdated = date
            return value
        }
    }

    private static def getDomainProperties(domain){
        return domain.gormPersistentEntity.persistentPropertyNames
    }
    static def getDomainTableName(domain){
        return GrailsDomainBinder.getMapping(domain).table.name
    }

}
