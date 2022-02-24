# bam [![Java CI with Gradle](https://github.com/JeffersonLab/bam/actions/workflows/gradle.yml/badge.svg)](https://github.com/JeffersonLab/bam/actions/workflows/gradle.yml) [![Docker](https://img.shields.io/docker/v/slominskir/bam?sort=semver&label=DockerHub)](https://hub.docker.com/r/slominskir/bam)
A [Java EE 8](https://en.wikipedia.org/wiki/Jakarta_EE) web application for beam authorization at Jefferson Lab built with the [Smoothness](https://github.com/JeffersonLab/smoothness) web template.

![Screenshot](https://github.com/JeffersonLab/bam/raw/main/Screenshot.png?raw=true "Screenshot")

---
 - [Overview](https://github.com/JeffersonLab/bam#overview)
 - [Usage](https://github.com/JeffersonLab/bam#usage)
   - [Quick Start with Compose](https://github.com/JeffersonLab/bam#quick-start-with-compose) 
   - [Install](https://github.com/JeffersonLab/bam#install)
 - [Configure](https://github.com/JeffersonLab/bam#configure)
 - [Build](https://github.com/JeffersonLab/bam#build)
---

## Overview
The Beam Authorization application allows the Director of Operations (or a delegate) to clearly communicate and document the maximum current and beam mode ("permissions") that are authorized for a given beam destination.  This information is stored in a database and presented via the web for easy access.   There are three machines, each with their own set of beam destinations and beam modes: CEBAF, LERF, UITF.  In addition to director authorization, the app also tracks Credited Controls and their verification.  Each beam destination is assigned a set of controls and each control is assigned to a particular responsible group.  A beam desintation is ready for beam only if all the controls assigned are verified by their responsible group.  Both group verifications and director permissions have expirations.   Emails and Jefferson Lab logbook entires are created to aid communication of new director permissions, responsible group verifications (upgrades and downgrades), and verification and permissions expirations.

### Roles
 - **Operations Director** - Responsible for authrozing beam
 - **Operability / Admin** - Responsible for process administration and continuous improvement
 - **Operator** - Must honor permissions set by the Director when running the machines
 - **Group Leader** - Responsible for verifying Credited Control readiness and assigning expiration dates for when new checks are required

### JLab Internal Docs
 - [ASE Document](https://jlabdoc.jlab.org/docushare/dsweb/Get/Document-187898)
 - [FSAD Document](https://jlabdoc.jlab.org/docushare/dsweb/Get/Document-21395)

## Usage

### Quick Start with Compose
1. Grab project
```
git clone https://github.com/JeffersonLab/bam
cd bam
```
2. Launch Docker
```
docker compose up
```
3. Navigate to page
```
http://localhost:8080/smoothness-demo
```

See: [Docker Compose Strategy](https://gist.github.com/slominskir/a7da801e8259f5974c978f9c3091d52c)


### Install
   1. Download [Wildfly 26](https://www.wildfly.org/downloads/)
   1. Download [bam.war](https://github.com/JeffersonLab/bam/releases) and deploy it to Wildfly
   1. Navigate your web browser to localhost:8080/bam

## Configure

### Environment Variables
Uses the [Smoothness Environment Variables](https://github.com/JeffersonLab/smoothness#environment-variables) plus the following application specific:

| Name | Description |
|---|---|
| BAM_BOOKS_CSV | Comma separated list of Jefferson Lab Logbook names to log to when permissions are updated or controls are downgraded |
| BAM_PERMISSIONS_EMAIL_CSV | Comma separated list of email recipients of permissions updated email |
| BAM_UPCOMING_EXPIRATION_EMAIL_CSV | Comma separated list of email recipients of both expired and upcoming expirations (admin heads-up email) |
| BAM_EXPIRED_EMAIL_CSV | Comma separated list of email recipients of expired permissions and control verifications (ops semi-synchronous notification of expiration) |
| BAM_DOWNGRADED_EMAIL_CSV | Comma separated list of email recipients for control verifications downgrades |
| BAM_PERMISSIONS_SUBJECT | Subject of log entries and emails indicating permissions updated |
| BAM_UPCOMING_EXPIRATION_SUBJECT | Subject of emails indicating both expired and upcoming expiration of permissions and control verifications (admin heads-up email) |
| BAM_EXPIRED_SUBJECT | Subject of emails indicating expiration of permissions and control verifications (ops semi-synchrounous notification of expiration) |
| BAM_DOWNGRADED_SUBJECT | Subject of emails indicating downgrade of control verifications |
| BAM_EMAIL_SENDER | Sender (and from address) of emails |

### Database
The Beam Auth application requires an Oracle 21+ database with the following [schema](https://github.com/JeffersonLab/bam/tree/main/docker/oracle/setup) installed.   The application server hosting the Beam Auth app must also be configured with a JNDI datasource.

## Build
This [Java 17](https://adoptium.net/) project uses the [Gradle 7](https://gradle.org/) build tool to automatically download dependencies and build the project from source:

```
git clone https://github.com/JeffersonLab/bam
cd bam
gradlew build
```
**Note**: If you do not already have Gradle installed, it will be installed automatically by the wrapper script included in the source

**Note for JLab On-Site Users**: Jefferson Lab has an intercepting [proxy](https://gist.github.com/slominskir/92c25a033db93a90184a5994e71d0b78)

**Note**: The dependency jars (except Java EE 8 jars required to be available in the server) are included in the war file that is generated by the build by default, but you can optionally exclude them (if you intend to install them into the application server) with the flag -Pprovided like so:
```
gradlew -Pprovided build
```

**See**: [Docker Development Quick Reference](https://gist.github.com/slominskir/a7da801e8259f5974c978f9c3091d52c#development-quick-reference)
