# Usamos imagen oficial de Eclipse Temurin (OpenJDK)
FROM eclipse-temurin:21.0.3_9-jdk

# Puerto expuesto (coincide con tu server.port)
EXPOSE 8080

# Directorio de trabajo
WORKDIR /app

# 1. PRIMERO copiamos solo lo necesario para cachear dependencias
COPY pom.xml mvnw ./
COPY .mvn/ .mvn/

# 2. Instalamos dependencias y resolvemos problemas DNS
RUN apt-get update && \
    apt-get install -y ca-certificates && \
    update-ca-certificates --fresh && \
    rm -rf /var/lib/apt/lists/* && \
    chmod +x mvnw && \
    ./mvnw dependency:go-offline -B

# 3. Copiamos el código fuente
COPY src/ src/

# 4. Construcción con parámetros para encoding UTF-8
RUN ./mvnw clean package -DskipTests \
    -Dfile.encoding=UTF-8 \
    -Dproject.build.sourceEncoding=UTF-8

# 5. Entrypoint optimizado
# NOTA: reemplaza "nombre-de-tu-app" por el nombre real del jar generado
ENTRYPOINT ["java", "-jar", "target/nailsalon-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]
