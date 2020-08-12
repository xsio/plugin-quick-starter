package common


import groovy.util.logging.Slf4j
import org.hibernate.SQLQuery
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.cfg.ImprovedNamingStrategy

@Slf4j
class NativeSqlService {
//	Logger log = LoggerFactory.getLogger(NativeSqlService);
	
	SessionFactory sessionFactory
	ImprovedNamingStrategy nameStrategy = new ImprovedNamingStrategy()


	SQLQuery createSqlQuery(String query, Class entityClass) {
		//log.info "==== create native sql query ===="
		//log.info "==== ${query} ===="
		Session session = sessionFactory.currentSession
		SQLQuery sqlQuery = session.createSQLQuery(query) as SQLQuery
		sqlQuery.addEntity(entityClass)
		
		return sqlQuery
	}

	SQLQuery createSqlQuery(String query) {
		//log.info "==== create native sql query ===="
		//log.info "==== ${query} ===="
		Session session = sessionFactory.currentSession
		SQLQuery sqlQuery = session.createSQLQuery(query) as SQLQuery

		return sqlQuery
	}

	String getColumnName(String name){
		return nameStrategy.columnName(name)
	}


}
