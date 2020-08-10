FROM java:8
VOLUME /temp
ADD target/view-service-0.0.1-SNAPSHOT.jar view-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","view-service-0.0.1-SNAPSHOT.jar"]