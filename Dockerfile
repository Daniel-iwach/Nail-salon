FROM eclipse-temurin:21.0.3_9-jdk

WORKDIR /root

COPY ./pom.xml /root
COPY ./.mvn /root/.mvn
COPY ./mvnw /root

RUN ./mvnw dependency:go-offline

COPY ./src /root/src

RUN ./mvnw clean package -DskipTests

RUN mv target/*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]