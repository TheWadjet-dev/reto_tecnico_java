# Stage 1: Build the application
FROM maven:3.9.5-eclipse-temurin-17 AS build

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración de Maven
COPY pom.xml .
COPY src ./src

# Compilar la aplicación
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM openjdk:17-jdk-slim

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el archivo JAR desde el stage de build
COPY --from=build /app/target/ejercicio-tecnico-*.jar app.jar

# Variables de entorno para la base de datos (deben coincidir con application.properties)
ENV SERVER_PORT=8080
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://aws-0-us-east-1.pooler.supabase.com:6543/postgres?prepareThreshold=0&preparedStatementCacheQueries=0
ENV SPRING_DATASOURCE_USERNAME=postgres.qvpnoivjuqzpgyxjpizr
ENV SPRING_DATASOURCE_PASSWORD=Wadjet140400*
ENV SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=create-drop
ENV SPRING_JPA_SHOW_SQL=true
ENV SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.PostgreSQLDialect

# Exponer el puerto 8080
EXPOSE 8080

# Ejecutar la aplicación usando las variables de entorno
ENTRYPOINT ["java", "-jar", "app.jar"]
