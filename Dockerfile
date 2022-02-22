FROM gradle:7.4-jdk17 as builder

USER root
WORKDIR /

RUN git clone https://github.com/JeffersonLab/bam \
   && cd bam \
   && gradle build -x test

FROM slominskir/smoothness:3.3.0

USER root

COPY --from=builder /bam/build/libs /opt/jboss/wildfly/standalone/deployments
COPY --from=builder /bam/docker/wildfly/standalone/configuration /opt/jboss/wildfly/standalone/configuration

RUN chown -R jboss:0 ${JBOSS_HOME} \
    && chmod -R g+rw ${JBOSS_HOME}

USER jboss