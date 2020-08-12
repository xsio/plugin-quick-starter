package app

import com.convertlab.errors.CommonErrors
import grails.converters.JSON
import grails.gorm.transactions.Transactional
import groovy.json.JsonSlurper
import org.apache.oltu.oauth2.client.OAuthClient
import org.apache.oltu.oauth2.client.URLConnectionClient
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest
import org.apache.oltu.oauth2.client.request.OAuthClientRequest
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse
import org.apache.oltu.oauth2.common.OAuth
import org.apache.oltu.oauth2.common.message.types.GrantType
import org.apache.oltu.oauth2.common.message.types.ResponseType
import org.springframework.beans.factory.annotation.Value

class OauthService {

    @Value('${apiServer.serverUrl}')
    String apiServerUrl
    @Value('${app.serverUrl}')
    String appServerUrl
    @Value('${oauth.clientId}')
    String clientId
    @Value('${oauth.clientSecret}')
    String clientSecret

    def accessTokenCacheService

    def getCallbackUri() { 
        //对于内部或私有应用，使用如下URL
        //return "$appServerUrl/plugin/sample/oauth2/callback" 

        //对于公有应用，使用应用自己的域名
        return "${appUrl}/oauth2/callback"
    }

    def getCodeUrl(redirectUrl) {
        //state设置为install，说明本次授权是安装时的授权
        def state
        //安装应用时，请求会带有参数redirectUrl
        if(redirectUrl){
            state = "install"
        }
        //没有redirectUrl则说明是非安装时的授权
        else{
            state = new Date().getTime().toString()
        }
        OAuthClientRequest request = OAuthClientRequest
                .authorizationLocation("${appServerUrl}/oauth2/authorize")
                .setClientId(clientId)
                .setRedirectURI(getCallbackUri())
                .setResponseType(ResponseType.CODE.toString())
                .setState(state)
                .buildQueryMessage()
        return request.getLocationUri()
    }

    def getAccessToken(tenantId) {
        def token = Token.findByTenantId(tenantId)
        if (token) {
            def refreshToken = token.refreshToken
            def url = appServerUrl + "/oauth2/token"
            OAuthClientRequest request = OAuthClientRequest
                    .tokenLocation(url)
                    .setGrantType(GrantType.REFRESH_TOKEN)
                    .setClientId(clientId)
                    .setClientSecret(clientSecret)
                    .setRefreshToken(refreshToken)
                    .buildQueryMessage()
            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient())
            try {
                def res = oAuthClient.accessToken(request)
                log.info("get oauth token result ${res as JSON}")
                println("get oauth token result ${res as JSON}")
                return res.accessToken
            } catch (Exception e) {
                log.error("get oauth token failed", e)
            }
        } else {
            log.warn("can not find token for tenant: $tenantId")
        }
    }

    def getUserAndRefreshToken(request) {
        def code
        try {
            OAuthAuthzResponse oar = OAuthAuthzResponse.oauthCodeAuthzResponse(request)
            code = oar.getCode()
            log.info("===get code: $code")
        } catch (Exception e) {
            log.error("invalid oauth2 callback", e)
            return CommonErrors.VALIDATION_ERROR.errorObject("invalid oauth2 callback")
        }
        def url = "${appServerUrl}/oauth2/token"
        OAuthClientRequest oAuthClientRequest = OAuthClientRequest
                .tokenLocation(url)
                .setGrantType(GrantType.AUTHORIZATION_CODE)
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectURI(getCallbackUri())
                .setCode(code)
                .buildQueryMessage()

        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient())
        def res = oAuthClient.accessToken(oAuthClientRequest)
        log.info("getToken result ${res as JSON}")
        def accessToken = res.accessToken
        if (!accessToken) {
            return CommonErrors.VALIDATION_ERROR.errorObject("get accessToken failed")
        }
        
        def getUserUrl = "${apiServerUrl}/v1/users/current"
        OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(getUserUrl).setAccessToken(accessToken).buildQueryMessage()
        OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class)
        log.info("get user result: ${resourceResponse.body}")
        try {
            def user = new JsonSlurper().parseText(resourceResponse.body)
            if (user['tenantId']) {
                saveToken(user['tenantId'], res.refreshToken)
            } else if (user['error_code']) {
                user = [error: [code: user['error_code'], message: user['error_description']]]
            }
            return user
        } catch (Exception e) {
            log.error("get user failed", e)
            return CommonErrors.GENERAL_ERROR.errorObject("get user failed")
        }
    }

    @Transactional
    def saveToken(tenantId, refreshToken) {
        // def token = Token.findByTenantId(tenantId) ?: new Token(tenantId: tenantId)
        // token.refreshToken = refreshToken
        // token.save()
        accessTokenCacheService.set(tenantId, refreshToken)
    }
}
