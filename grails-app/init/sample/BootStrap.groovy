package sample

import com.convertlab.cd.http.HttpClient
import org.joda.time.DateTimeZone

class BootStrap {

    //Define your services here

    def redisService
    def kafkaDeclarer
    def kafkaProducerService

    //def sampleSendService

    def init = { servletContext ->
        log.info("==> bootstrap start==")
        DateTimeZone.setDefault(DateTimeZone.UTC)
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
        HttpClient.service = "sample"

        redisService.init()
        kafkaDeclarer.run()
        kafkaProducerService.init()

        //sampleSendService.start()

    }
    def destroy = {
        kafkaProducerService.close()
        //sampleSendService.shutdown()
    }
}
