FROM java:8
VOLUME /temp
ADD target/account-service-0.0.1-SNAPSHOT.jar account-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","account-service-0.0.1-SNAPSHOT.jar"]