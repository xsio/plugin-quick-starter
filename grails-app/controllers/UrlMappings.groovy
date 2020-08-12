class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?" {
            constraints {
                // apply constraints here
            }
        }

        "/ping"(controller: "ping", action: "pong", method: "GET")
        "/"(view: "/index")
        "500"(view: '/error')
        "404"(view: '/notFound')

        "/oauth2"(controller: "oauth", action: "index", method: "GET")
        "/oauth2/callback"(controller: "oauth", action: "callback")


        group "/sample", {
            "/send"(controller: "sample", action: "send", method: "POST")
        }
    }
}
