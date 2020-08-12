package app

import grails.converters.JSON
import org.springframework.beans.factory.annotation.Value

class OauthController {
    @Value('${app.serverUrl}')
    String appServerUrl
    def oauthService

    def index() {
        def url = oauthService.getCodeUrl(params.redirectUrl)
        redirect(url: url)
    }

    def callback() {
        //授权完成后调用该接口，参数为：code={code}&tamp={timestamp}&hmac={hmac}&state={state}
        def user = oauthService.getUserAndRefreshToken(request)
        if (user['error']) {
            return render(user as JSON)
        }
        session.setAttribute("user", user)

        //跳回应用市场，将cp_sample替换为应用到ID
        if(params.state && params.state.equals("install"))
        {
            redirect(url: "${appServerUrl}/application/mktapp/index.html?install=cp_sample#/market")
        }
        //跳转到应用首页，注意更换为正确的路径
        else{
            redirect(url: "/dist/index.html")
        }
        
    }
}
