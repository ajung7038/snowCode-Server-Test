FROM eclipse-temurin:17-jdk

RUN apt-get update && \
    apt-get install -y python3 python3-pip && \
    rm -rf /var/lib/apt/lists/*

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
COPY src/main/resources/application.yml application.yml
ENTRYPOINT java -javaagent:/pinpoint/pinpoint-bootstrap-2.2.2.jar -Dpinpoint.agentId=snowcode -Dpinpoint.applicationName=snowcode -Dpinpoint.config=/pinpoint/pinpoint-root.config -Dprofiler.transport.grpc.collector.ip=$PINPOINT_COLLECTOR_IP -Dprofiler.transport.grpc.collector.stat.port=9991 -Dprofiler.transport.grpc.collector.span.port=9993 -Dprofiler.transport.grpc.collector.metadata.port=9992 -Dprofiler.transport.grpc.collector.command.port=9994 -Dspring.profiles.active=local -Duser.timezone=Asia/Seoul -jar /app.jar
