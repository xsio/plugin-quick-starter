package app

import com.convertlab.cd.http.HttpClient
import grails.converters.JSON
import org.springframework.beans.factory.annotation.Value

class DmhubInternalService {

	@Value('${apiServer.serverUrl}')
	def serverUrl
	@Value('${internalService.serverUrl}')
	def internalUrl
	def accessTokenCacheService

	def getCustomersByListId(list, rows, sinceId,tenantId){
		def token = accessTokenCacheService.get(tenantId)
		def url = "${serverUrl}/v1/listMembers?" +"access_token=${token}&listId=${list}&rows=${rows}&sinceId=${sinceId}&sidx=customerId"
		def res = HttpClient.getForObject(url)
		log.info("get list members res is ${res as JSON}")
		return res.items
	}

	def getCustomerContext(customerIds,tenantId){
		def token = accessTokenCacheService.get(tenantId)
		def idList = customerIds.join(',')
		def url = "${serverUrl}/v1/customers?idList=${idList}&access_token=${token}&select=id,mobile&decrypt=true"
		if(customerIds.size() == 0){
			log.info("no customer Id")
			return
		}
		def res = HttpClient.getForObject(url)
		log.info("getCustomerContext result: ${res as JSON}")
		return res.rows
	}


	def sendSystemMessage(templateName,mobiles,vars,tenantId){
		def url = "${internalUrl}/sms/templates/sendSystemSms?x_tenant_id=${tenantId}"
		def req = [templateName: templateName, mobiles: mobiles, vars: vars]
		def res = HttpClient.postForObject(url, req)
		log.info("send system result is ${res as JSON}")
		return res
	}
}