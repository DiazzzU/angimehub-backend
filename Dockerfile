FROM maven:3.8.4-openjdk-11

WORKDIR /usr/src/app

COPY . /usr/src/app
RUN mvn clean install package

ENV PORT 8080
EXPOSE $PORT

CMD [ "sh", "-c", "mvn -Dserver.port=${PORT} spring-boot:run" ]