FROM gradle:6.6.1-jdk11 as builder

ARG CUSTOM_CRT_URL

USER root
WORKDIR /

RUN git clone https://github.com/JeffersonLab/bam \
   && cd bam \
   && if [ -z "$CUSTOM_CRT_URL" ] ; then echo "No custom cert needed"; else \
        wget -O /usr/local/share/ca-certificates/customcert.crt $CUSTOM_CRT_URL \
      && update-ca-certificates \
      && keytool -import -alias custom -file /usr/local/share/ca-certificates/customcert.crt -cacerts -storepass changeit -noprompt \
      && export OPTIONAL_CERT_ARG=-Djavax.net.ssl.trustStore=$JAVA_HOME/lib/security/cacerts \
        ; fi \
   && gradle build -x test $OPTIONAL_CERT_ARG

FROM slominskir/smoothness:3.1.0

USER root

COPY --from=builder /bam/build/libs /opt/jboss/wildfly/standalone/deployments
COPY --from=builder /bam/docker/wildfly/standalone/configuration /opt/jboss/wildfly/standalone/configuration

RUN chown -R jboss:0 ${JBOSS_HOME} \
    && chmod -R g+rw ${JBOSS_HOME}

USER jboss