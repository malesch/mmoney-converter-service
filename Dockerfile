FROM java:8-alpine
MAINTAINER Marcus Spiegel <malesch@gmail.com>

ADD target/mmoney-converter-service-0.1.0-standalone.jar /mmoney-converter-service/app.jar
ADD resources/config/* /mmoney-converter-service/

EXPOSE 3000

CMD ["java", "-Dmmoney.config=/mmoney-converter-service/config.edn", "-jar", "/mmoney-converter-service/app.jar"]
