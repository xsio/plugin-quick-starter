package app

class TokenRefreshJob {

	def accessTokenCacheService

	static triggers = {
        simple name:"TokenRefreshJob", startDelay: 2000 , repeatInterval: 6000000 //refresh every 6000s
    }

    def execute(){
        // log.info("==== Starting RefreshTokenJob ====")
        // accessTokenCacheService.refreshAll()
    }

}