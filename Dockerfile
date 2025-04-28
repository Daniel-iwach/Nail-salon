FROM eclipse-temurin:21.0.3_9-jdk

WORKDIR /root

COPY ./pom.xml /root
COPY ./.mvn /root/.mvn
COPY ./mvnw /root

RUN chmod +x ./mvnw

RUN ./mvnw dependency:go-offline

COPY ./src /root/src

RUN ./mvnw clean install -DskipTests

ENTRYPOINT ["java","-jar","root/target/nailsalon-0.0.1-SNAPSHOT.jar"]