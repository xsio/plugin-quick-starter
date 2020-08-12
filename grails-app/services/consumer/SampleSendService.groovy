package consumer

import com.convertlab.cd.kafka.KafkaConsumerManager
import org.springframework.beans.factory.annotation.Value
import redis.clients.jedis.Jedis

class SampleSendService extends KafkaConsumerManager {

    @Value('${kafkaServer.bootstrap.servers}')
    String bootstrapServers

    @Value('${kafka.flowSend.topic}')
    String topic

    @Value('${kafka.flowSend.groupId}')
    String groupId

    @Value('${kafka.templateSend.numConsumers}')
    Integer numConsumers

    def redisService

    @Override
    void processKafkaMessage(String key, Map message) {
        log.info("flowsend receive message is:${message}")

        def flag = "mms::flowSend:${key}"
        def notBlocked = false

        redisService.withRedis { Jedis jedis ->
            def time = System.currentTimeMillis().toString()
            notBlocked = jedis.set(flag, time, "nx", "ex", 5 * 60)
        }

        if (!notBlocked) {
            log.info("mms flow mass send ${key} already consumed by others")
            return
        }
        // Deal with message here
    }
}
