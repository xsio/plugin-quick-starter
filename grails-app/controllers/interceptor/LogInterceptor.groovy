package interceptor

import com.convertlab.cd.multitenancy.CurrentTenant
import common.LogUtils
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Autowired

import javax.servlet.http.HttpServletRequest

@CompileStatic
@Slf4j
class LogInterceptor {

    int order = 100

    @Autowired
    CurrentTenant currentTenant

    LogInterceptor() {
        matchAll()
                .except(uri:"/dist/**")
                .except(uri:"/scripts/**")
                .except(uri:"/public/**")
                .except(uri:"/static/**")
                .except(controller: 'ping', action: 'pong')
    }

    boolean before() {
        HttpServletRequest req = request as HttpServletRequest
        String x_request_id = req.getHeader(LogUtils.X_REQUEST_ID)
        if(!x_request_id){
            x_request_id = UUID.randomUUID().toString().replace("-", "")
        }
        MDC.put(LogUtils.X_REQUEST_ID, x_request_id)
        MDC.put(LogUtils.X_TENANT_ID, "${currentTenant.get()}")

        def queryString = req.queryString
        if (queryString) {
            queryString = "?" + queryString
        } else {
            queryString = ""
        }

        log.info "==== request url: ${req.method} ${req.getPathInfo()}$queryString ===="
        return true
    }

    boolean after() {
        MDC.remove(LogUtils.X_REQUEST_ID)
        MDC.remove(LogUtils.X_TENANT_ID)

        return true
    }

}
