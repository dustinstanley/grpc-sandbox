FROM azul/zulu-openjdk:11

COPY ./build/libs/grpc-0.0.1-SNAPSHOT.jar /opt/cts/grpc-sandbox.jar

EXPOSE 8081:8081

CMD ["java", "-jar", "/opt/cts/grpc-sandbox.jar"]