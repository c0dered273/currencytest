FROM gradle:7.2.0-jdk17
RUN mkdir currencytest
WORKDIR currencytest
COPY . .
RUN gradle bootJar -x test
ARG JAR_FILE=build/libs/currencytest-0.0.1.jar
ADD ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]