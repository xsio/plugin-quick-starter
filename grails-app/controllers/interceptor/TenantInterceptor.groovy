package interceptor

import com.convertlab.cd.multitenancy.CurrentTenant
import common.LogUtils
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired

@CompileStatic
class TenantInterceptor {

    @Autowired
    CurrentTenant currentTenant

    int order = 99

    TenantInterceptor() {
        matchAll()
                .except(uri: "/dist/**")
                .except(uri: "/oauth2")
                .except(uri: "/oauth2/callback")
                .except(controller: 'ping', action: 'pong')

    }

    boolean before() {
        /**
            内部插件和私有云插件如果使用了产品app服务的dynamic接口，则用下面的方法获取tenantId。否则使用公有云对接时的方法。

        */
        // def tenantParameter = request.getHeader(LogUtils.X_TENANT_ID)
        // if (!tenantParameter) {
        //     tenantParameter = params.x_tenant_id

        //     if (!tenantParameter) {
        //         return false
        //     }
        // }

        // try {
        //     Long tenantId = tenantParameter as Long
        //     currentTenant.set(tenantId)
        // } catch (NumberFormatException e) {
        //     log.error "invalid tenant id $tenantParameter"
        //     return false
        // }

        /**
            在公有云插件中，使用下面的代码来获取当前tenant和用户的信息。
        */
        // Map user = (Map)session.getAttribute('user')
        // if (!user) {
        //     redirect(uri: "/oauth2?${request.queryString?:''}", absolute: true)
        // } else {
        //     def tenantId
        //     try{
        //         log.info("get user: ${user as JSON}")
        //         tenantId = user.tenantId as Long
        //     } catch (Exception e) {
        //         log.error ("invalid tenant id ${user as JSON}", e)
        //         render status : 400, text : "invalid tenant id"
        //         return false
        //     }
        //     currentTenant.set(tenantId)
        // }

        return true
    }

    boolean after() {
        currentTenant.set(null)
        true
    }

    void afterView() {
    }

}
