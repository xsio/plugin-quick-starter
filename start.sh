#! /bin/sh

exec java ${JAVA_OPTS} \
   ${JACOCO_OPTS} \
   -XX:+HeapDumpOnOutOfMemoryError \
   -XX:HeapDumpPath=/opt/log/${SERVICE_NAME} \
   -Xloggc:/opt/log/stash/${SERVICE_NAME}/${SERVICE_NAME}.gc \
   -XX:ErrorFile=/tmp/${SERVICE_NAME}_java_error%p.log \
   -Dgrails.env=${ENV} \
   -jar ${WAR} 1>/dev/null 2>>/tmp/${SERVICE_NAME}.log

