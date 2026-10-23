# --- Étape 1 : Build (On utilise une image avec Maven et le JDK complet) ---
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /alert360

# Copier les fichiers de configuration Maven
COPY pom.xml .
COPY .mvn ./.mvn
COPY mvnw .

# Donner les droits d'exécution au script Maven Wrapper
RUN chmod +x mvnw

# Télécharger les dépendances pour les mettre en cache Docker
RUN ./mvnw dependency:go-offline

# Copier le code source et compiler le fichier JAR final
COPY src ./src
RUN ./mvnw clean package -DskipTests

# --- Étape 2 : Exécution (Image finale ultra-légère avec le JRE seul) ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /alert360

# Récupérer uniquement le fichier JAR généré à l'étape 1
COPY --from=build /alert360/target/*.jar alert360.jar

# Configuration du port (5151 par défaut, ou celui imposé dynamiquement par Render)

ENV PORT=8080
EXPOSE ${PORT}

# Lancer l'application avec la syntaxe JSON correcte (avec guillemets)
ENTRYPOINT ["sh", "-c", "java -jar alert360.jar --server.port=${PORT:8080}"]
