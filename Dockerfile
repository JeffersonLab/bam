ARG BUILD_IMAGE=gradle:7.4-jdk17
ARG RUN_IMAGE=slominskir/smoothness:3.3.0

# BUILD_TYPE should be one of 'remote-src', 'local-src', 'local-artifact'
ARG BUILD_TYPE=remote-src

###
# Remote source scenario
###
FROM ${BUILD_IMAGE} as remote-src

USER root
WORKDIR /

RUN git clone https://github.com/JeffersonLab/bam \
   && cd bam \
   && gradle build -x test

###
# Local source scenario
#
# This scenario is the only one that needs .dockerignore
###
FROM ${BUILD_IMAGE} as local-src

USER root
WORKDIR /

RUN mkdir /bam

COPY . /bam

RUN cd /bam && gradle build -x test --no-watch-fs

###
# Local Artifact scenario
#
# If we used local-src here we'd trigger Docker cache changes before this stage/layer is reached
# and the whole point of local-artifact is to narrowly target an artifact and leverage caching
###
FROM remote-src as local-artifact

USER root
WORKDIR /

# Single out deployment artifact to leverage Docker build caching
COPY ./smoothness-demo/build/libs/smoothness-demo.war /smoothness/smoothness-demo/build/libs/

###
# Build type chooser / resolver stage
#
# The "magic" is due to Docker honoring dynamic arguments for an image to run.
#
###
FROM ${BUILD_TYPE} as builder-chooser

###
# Final product stage brings it all together in as small and few layers as possible.
###
FROM ${RUN_IMAGE} as final-product

USER root

COPY --from=builder-chooser /bam/docker/wildfly/standalone/configuration /opt/jboss/wildfly/standalone/configuration

# This must be last and separate from other copy command for caching purposes (local-artifact scenario)
COPY --from=builder-chooser /bam/build/libs /opt/jboss/wildfly/standalone/deployments

# TODO: Base RUN_IMAGE should not contain any deployments!
RUN rm -rf /opt/jboss/wildfly/standalone/deployments/smoothness-demo.war

RUN chown -R jboss:0 ${JBOSS_HOME} \
    && chmod -R g+rw ${JBOSS_HOME}

USER jboss