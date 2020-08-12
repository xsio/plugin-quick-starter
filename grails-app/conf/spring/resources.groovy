import com.convertlab.cd.kafka.KafkaProducerService
import com.convertlab.cd.mqmigration.runner.KafkaDeclarer
import com.convertlab.cd.multitenancy.CurrentTenantThreadLocal
import com.convertlab.cd.multitenancy.TenantHelper
import com.convertlab.cd.multitenancy.resolver.ThreadLocalTenantResolver
import com.convertlab.cd.redis.RedisService
import com.convertlab.cd.utils.SchemaValidator


// Place your Spring DSL code here
beans = {
    currentTenant(CurrentTenantThreadLocal)
    tenantResolver(ThreadLocalTenantResolver)
    tenantHelper(TenantHelper, ref('currentTenant'))

    redisService(RedisService)
    //kafka
    kafkaProducerService(KafkaProducerService, "sample")
    topicValidator(SchemaValidator)

    kafkaDeclarer(KafkaDeclarer)

}
