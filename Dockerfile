# Usamos la imagen oficial de OpenJDK 17 para el build
FROM openjdk:17-slim AS build

# Instalamos Maven en la imagen
RUN apt-get update && apt-get install -y maven

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos el archivo pom.xml y el código fuente
COPY pom.xml .
COPY src ./src

# Ejecutamos el build con Maven
RUN mvn clean package -DskipTests

# Segunda etapa: imagen base para correr la aplicación
FROM openjdk:17-slim

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos el jar generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto
EXPOSE 8080

# Configuramos el entrypoint para correr la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
