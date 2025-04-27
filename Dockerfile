# 1. Imagen base de Java 21
FROM eclipse-temurin:21-jdk

# 2. Directorio de trabajo
WORKDIR /app

# 3. Copiar el jar construido
COPY target/*.jar app.jar

# 4. Comando para correr la app
ENTRYPOINT ["java", "-jar", "app.jar"]
