# beam-auth
A [Java EE 8](https://en.wikipedia.org/wiki/Jakarta_EE) web application for Beam authorization at Jefferson Lab.

## Build
This [Java 8](https://adoptopenjdk.net/) project uses the [Gradle 5](https://gradle.org/) build tool to automatically download dependencies and build the project from source:

```
git clone https://github.com/JeffersonLab/beam-auth
cd beam-auth
gradlew build
```
**Note**: If you do not already have Gradle installed, it will be installed automatically by the wrapper script included in the source

**Note**: Jefferson Lab has an intercepting [proxy](https://gist.github.com/slominskir/92c25a033db93a90184a5994e71d0b78)

## Configure

### Environment Variables
| Name | Description |
|---|---|
| PROXY_HOSTNAME | Name of outermost proxy host (for use in hyperlinks in generated emails and log entries) |
| LOGBOOK_HOSTNAME | Hostname of Jefferson Lab logbook server |
| LOGBOOK_OPS_BOOKS_CSV | Comma separated list of logbook names |

### Database
The Beam Auth application requires an Oracle database with the following [schema](https://github.com/JeffersonLab/beam-auth/tree/main/schema) installed.   The application server hosting the Beam Auth app must also be configured with a JNDI datasource.

