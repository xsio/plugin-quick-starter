FROM nexus.xsio.cn/alpine-oraclejdk8

WORKDIR /opt/

# used by start.sh
ENV WAR=extmms-latest.war \
    ENV=test \
    SERVICE_NAME=extmms

COPY start.sh* build/libs/${WAR} /opt/
RUN chmod +x start.sh

CMD ["./start.sh"]
