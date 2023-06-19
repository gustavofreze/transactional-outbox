FROM gradle:7.4.2-jdk18

WORKDIR /opt/application

COPY build/quarkus-app/lib /opt/application/lib
COPY build/quarkus-app/app /opt/application/app
COPY build/quarkus-app/quarkus /opt/application/quarkus
COPY build/quarkus-app/quarkus-run.jar /opt/application/quarkus-run.jar
COPY build/quarkus-app/quarkus-app-dependencies.txt /opt/application/quarkus-app-dependencies.txt

CMD ["java", "-jar", "quarkus-run.jar"]
