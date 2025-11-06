FROM docker-release.devrepo.globalpay.cz/liberica-jre-21-alpaquita-cds AS builder
WORKDIR /builder
ARG JAVA_ARTEFACT_NAME=gp-webpay-surcharge.jar
COPY ${JAVA_ARTEFACT_NAME} application.jar
RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted

FROM docker-release.devrepo.globalpay.cz/liberica-jre-21-alpaquita-cds

RUN addgroup -S gp-webpay-surcharge && adduser -S gp-webpay-surcharge -G gp-webpay-surcharge -u 5600
USER gp-webpay-surcharge

WORKDIR /application

COPY --from=builder /builder/extracted/dependencies/ ./
COPY --from=builder /builder/extracted/spring-boot-loader/ ./
COPY --from=builder /builder/extracted/snapshot-dependencies/ ./
COPY --from=builder /builder/extracted/application/ ./

RUN java -XX:ArchiveClassesAtExit=application.jsa -Dspring.context.exit=onRefresh -Dspring.profiles.active=local -jar application.jar

ENTRYPOINT ["java", "-XX:SharedArchiveFile=application.jsa", "-jar", "application.jar"]