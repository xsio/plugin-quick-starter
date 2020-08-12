package sample

import grails.converters.JSON
import org.springframework.beans.factory.annotation.Value

class SampleController {
    def kafkaProducerService
    def currentTenant

    @Value('${kafka.flowSend.topic}')
    String flowSendTopic

    def send(){
        // def data = request.JSON
        // def tenantId = currentTenant.get()
        // if(!tenantId){
        //     tenantId = 26
        // }
        // def templateId = data.selectMms.value
        // def messageBody = [
        //         templateId:templateId,
        //         campaignUuid: data.selectMms.campaignUuid,
        //         tenantId:tenantId,
        //         mobile:data.customerPhone,
        //         customerId:data.customerId,
        //         flowId:data.flowId
        // ]


        // def key = messageBody.flowId +'_' + messageBody.customerId + '_' + messageBody.templateId
        // kafkaProducerService.send(flowSendTopic, "${key}", messageBody)
    }

}
