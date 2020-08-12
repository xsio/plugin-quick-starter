package health

class PingController {

    def pong() {
        render status : 200, text : "pong"
    }

}
