FROM java:8
VOLUME /temp
ADD target/play-service-0.0.1-SNAPSHOT.jar play-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","play-service-0.0.1-SNAPSHOT.jar"]