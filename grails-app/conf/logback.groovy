import ch.qos.logback.classic.AsyncAppender
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.util.FileSize
import grails.util.BuildSettings
import grails.util.Environment
import groovy.json.JsonBuilder
import net.logstash.logback.encoder.LogstashEncoder
import org.springframework.boot.logging.logback.ColorConverter

conversionRule("clr", ColorConverter)

def LOG_LEVEL_PATTERN = System.getProperty("LOG_LEVEL_PATTERN") ?: '%5p'
def PID = System.getProperty("PID") ?: "- "
def LOG_EXCEPTION_CONVERSION_WORD = System.getProperty("LOG_EXCEPTION_CONVERSION_WORD") ?: '%xEx'

def CONSOLE_LOG_PATTERN = "%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} " +
        "%clr(${LOG_LEVEL_PATTERN}) %clr(${PID}){magenta} " +
        "%clr(---){faint} %clr([%15.15t]){faint}" +
        "%clr(%-40.40logger{39}){cyan} " +
        "%clr(:){faint} %m%n${LOG_EXCEPTION_CONVERSION_WORD}"

appender('CONSOLE', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = CONSOLE_LOG_PATTERN
    }
}

def additionalFields = new JsonBuilder([
        "env"    : System.getProperty("grails.env"),
        "service": "sample"
]).toPrettyString()

appender("FILE", RollingFileAppender) {
    file = "/opt/log/stash/sample/sample.log"
    encoder(LogstashEncoder) {
        customFields = additionalFields
        includeMdc = true
    }
    rollingPolicy(SizeAndTimeBasedRollingPolicy) {
        fileNamePattern =  "/opt/log/stash/sample/sample-%d{yyyy-MM-dd-HH}.%i.zip"
        maxFileSize = "300MB"
        maxHistory = 60
        totalSizeCap = FileSize.valueOf("20GB")
    }
}

appender("ASYNC", AsyncAppender) {
    appenderRef("FILE")
    queueSize = 524288
    discardingThreshold = 32768
}

if (Environment.current == Environment.DEVELOPMENT) {
    def targetDir = BuildSettings.TARGET_DIR
    if (targetDir) {

        appender("FULL_STACKTRACE", FileAppender) {

            file = "${targetDir}/stacktrace.log"
            append = true
            encoder(PatternLayoutEncoder) {
                pattern = "%level %logger - %msg%n"
            }
        }
        logger("StackTrace", ERROR, ['FULL_STACKTRACE'], false)
    }
    root(INFO, ["CONSOLE", "ASYNC"])
} else {
    logger("StackTrace", OFF)
    root(INFO, ["ASYNC"])
}

logger("org.hibernate", ERROR)