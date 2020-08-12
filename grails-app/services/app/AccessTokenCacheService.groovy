package app

import redis.clients.jedis.Jedis

class AccessTokenCacheService {

    final static String prefix = "dmhub::accessToken::mms::"
    final static String lock = "::lock"
    final static int expire = 6000 //second

    def redisService
    def oauthService

    def get(tenantId) {
        def value
        redisService.withRedis { Jedis jedis ->
            value = jedis.get("$prefix$tenantId")
        }
        if (!value) {
            log.warn("can not fetch dmhub access token from cache by tenantId: $tenantId")
            value = refreshIfNecessary(tenantId)
        }
        return value
    }

    def set(tenantId, token) {
        def tokenKey = "$prefix$tenantId"
        redisService.withRedis { Jedis jedis ->
            jedis.setex(tokenKey, expire, token)
        }
    }

    def refreshIfNecessary(tenantId) {
        def lockKey = "$prefix$tenantId$lock"
        def tokenKey = "$prefix$tenantId"
        def token
        redisService.withRedis { Jedis jedis ->
            def time = System.currentTimeMillis().toString()
            def lockFlag = jedis.setnx(lockKey, time)
            if (lockFlag) {
                jedis.expire(lockKey, 10)
                token = oauthService.getAccessToken(tenantId)
                if (token) {
                    jedis.setex(tokenKey, expire, token)
                }
                if (time == jedis.get(lockKey)) {
                    jedis.del(lockKey)
                }
            }
        }
        token
    }


    def refreshAll() {
        Token.list().each {
            refreshIfNecessary(it.tenantId)
        }
    }
}
