FROM java:8

COPY  /target/file2pdf-0.0.1.jar  app.jar

ADD ./pdf  /pdf
ADD font  /font

RUN bash -c "touch /app.jar"

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]