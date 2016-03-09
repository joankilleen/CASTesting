 
# EnterpriseMusicstore

## Build the project

	mvn clean install -DskipTests -DskipITs

The result is an EAR in the directory `deployments`


## Set up the Application Server

Start the Database:

	asadmin start-database

Start Glassfish:

	asadmin start-domain --verbose=true

Create the datasource:


	asadmin create-jdbc-connection-pool --datasourceclassname org.apache.derby.jdbc.ClientDataSource --restype javax.sql.DataSource --property ServerName=localhost:Port=1527:DatabaseName=musicstore:User=app:Password=app:ConnectionAttributes=create\\=true jdbc/musicstorePool

	asadmin create-jdbc-resource --connectionpoolid jdbc/musicstorePool jdbc/musicstore


## Deploy the project into Glassfish

	asadmin deploy --force=true deployments/*.ear


## Test the project:

* Navigate to: `http://localhost:8080/musicstore/faces/index.xhtml`
* Execute: `cd EnterpriseMusicStore-integration-tests; mvn integration-test`
* Execute: `cd EnterpriseMusicStore-selenium; mvn integration-test`

