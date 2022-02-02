#!/bin/bash

echo "-------------------------------------------------"
echo "Step 1: Waiting for Oracle DB to start listening "
echo "-------------------------------------------------"
until java -cp /:/opt/jboss/wildfly/modules/com/oracle/database/jdbc/main/ojdbc11-21.3.0.0.jar /TestOracleConnection.java
do
  echo -e $(date) " Still waiting for Oracle to start..."
  sleep 5
done

echo -e $(date) " Oracle connection successful!"

/opt/jboss/wildfly/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0