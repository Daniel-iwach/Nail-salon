#IMAGEN MODELO
FROM eclipse-temurin:21.0.3_9-jdk

EXPOSE 8080

#DEFINIR DIRECTORIO RAIZ
WORKDIR /root

#COPIAR Y PEGAR ARCHIVOS ENE L CONTENEDOR
COPY ./pom.xml /root
COPY ./.mvn /root/.mvn
COPY ./mvnw /root

#ACCESO A DEPENDENCIAS
RUN chmod +x ./mvnw

#DESCARGA DE LAS DEPENDENCIAS
RUN ./mvnw dependency:go-offline

#COPIAR EL CODIGO FUENTE
COPY ./src /root/src

RUN apt-get update && apt-get install -y dos2unix
RUN dos2unix /root/src/main/resources/application.properties

#CONSTRUIR APLICACION
RUN ./mvnw clean install -DskipTests

#LEVANTAR NUESTRA APLICACION CUANDO EL CONTENEDOR INICIE
ENTRYPOINT ["java","-jar","/root/target/nailsalon-0.0.1-SNAPSHOT.jar"]