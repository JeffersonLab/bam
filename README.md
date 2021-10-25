# bam [![Java CI with Gradle](https://github.com/JeffersonLab/beam-auth/workflows/Java%20CI%20with%20Gradle/badge.svg)](https://github.com/JeffersonLab/beam-auth/actions?query=workflow%3A%22Java+CI+with+Gradle%22)
A [Java EE 8](https://en.wikipedia.org/wiki/Jakarta_EE) web application for beam authorization at Jefferson Lab built with the [Smoothness](https://github.com/JeffersonLab/smoothness) web template.

![Screenshot](https://github.com/JeffersonLab/beam-auth/raw/main/Screenshot.png?raw=true "Screenshot")

---
 - [Background](https://github.com/JeffersonLab/beam-auth#background)
 - [Build](https://github.com/JeffersonLab/beam-auth#build)
 - [Configure](https://github.com/JeffersonLab/beam-auth#configure)
 - [Install](https://github.com/JeffersonLab/beam-auth#install)
---

## Background
The Beam Authorization application allows the Director of Operations (or a delegate) to clearly communicate and document the maximum current and beam mode ("permissions") that are authorized for a given beam destination.  This information is stored in a database and presented via the web for easy access.   There are three machines, each with their own set of beam destinations and beam modes: CEBAF, LERF, UITF.  In addition to director authorization, the app also tracks Credited Controls and their verification.  Each beam destination is assigned a set of controls and each control is assigned to a particular responsible group.  A beam desintation is ready for beam only if all the controls assigned are verified by their responsible group.  Both group verifications and director permissions have expirations.   Emails and Jefferson Lab logbook entires are created to aid communication of new director permissions, responsible group verifications (upgrades and downgrades), and verification and permissions expirations.

### Roles
 - **Operations Director** - Responsible for authrozing beam
 - **Operability / Admin** - Responsible for process administration and continuous improvement
 - **Operator** - Must honor permissions set by the Director when running the machines
 - **Group Leader** - Responsible for verifying Credited Control readiness and assigning expiration dates for when new checks are required


## Build
This [Java 11](https://adoptopenjdk.net/) project uses the [Gradle 5](https://gradle.org/) build tool to automatically download dependencies and build the project from source:

```
git clone https://github.com/JeffersonLab/beam-auth
cd beam-auth
gradlew build
```
**Note**: If you do not already have Gradle installed, it will be installed automatically by the wrapper script included in the source

**Note**: Jefferson Lab has an intercepting [proxy](https://gist.github.com/slominskir/92c25a033db93a90184a5994e71d0b78)

## Configure

### Environment Variables
Uses the [Smoothness Environment Variables](https://github.com/JeffersonLab/smoothness#environment-variables) plus the following application specific:

| Name | Description |
|---|---|
| BA_BOOKS_CSV | Comma separated list of Jefferson Lab Logbook names to log to when permissions are updated or controls are downgraded |
| BA_PERMISSIONS_EMAIL_CSV | Comma separated list of email recipients of permissions updated email |
| BA_UPCOMING_EXPIRATION_EMAIL_CSV | Comma separated list of email recipients of both expired and upcoming expirations (admin heads-up email) |
| BA_EXPIRED_EMAIL_CSV | Comma separated list of email recipients of expired permissions and control verifications (ops semi-synchronous notification of expiration) |
| BA_DOWNGRADED_EMAIL_CSV | Comma separated list of email recipients for control verifications downgrades |
| BA_PERMISSIONS_SUBJECT | Subject of log entries and emails indicating permissions updated |
| BA_UPCOMING_EXPIRATION_SUBJECT | Subject of emails indicating both expired and upcoming expiration of permissions and control verifications (admin heads-up email) |
| BA_EXPIRED_SUBJECT | Subject of emails indicating expiration of permissions and control verifications (ops semi-synchrounous notification of expiration) |
| BA_DOWNGRADED_SUBJECT | Subject of emails indicating downgrade of control verifications |
| BA_EMAIL_SENDER | Sender (and from address) of emails |

### Database
The Beam Auth application requires an Oracle 18 database with the following [schema](https://github.com/JeffersonLab/beam-auth/tree/main/schema) installed.   The application server hosting the Beam Auth app must also be configured with a JNDI datasource.

## Install
   1. Download [Wildfly 16](https://www.wildfly.org/downloads/)
   1. Download [beam-auth.war](https://github.com/JeffersonLab/beam-auth/releases) and deploy it to Wildfly
   1. Navigate your web browser to localhost:8080/beam-auth

**Note:** beam-auth presumably works with any Java EE 8 compatible server such as [GlassFish](https://javaee.github.io/glassfish/) or [TomEE](https://tomee.apache.org/).

**Note:** The dependency jars (except Java EE 8 jars required to be available in the server) are included in the _war_ file that is generated by the build by default, but you can optionally exclude them (if you intend to install them into your application server) with the flag _-Pprovided_ like so:
```
gradlew -Pprovided build
```

