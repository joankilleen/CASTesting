@echo off

if '"%JAVA_HOME%"'=='' goto error
if '"%MAVEN_HOME%"'=='' goto error
if '"%GLASSFISH_HOME%"'=='' goto error

set PATH="%JAVA_HOME%\bin";"%GLASSFISH_HOME%\bin";"%MAVEN_HOME%\bin";$PATH

set GF_DOMAIN=bookstore
set GF_ADMIN_USER=admin
set GF_ADMIN_PASSWORD=

set DATASOURCE_NAME=jdbc/bookstore
set DB_HOST=localhost
set DB_PORT=1527
set DB_HOME="%GLASSFISH_HOME%\javadb\data"
set DB_NAME=bookstore
set DB_USER=app
set DB_PASSWORD=app

set JMS_QUEUE_NAME=jms/orderQueue
set JMS_CONNECTION_FACTORY=jms/connectionFactory

set MAIL_SESSION_NAME=mail/bookstore
set MAIL_HOST=hermes.bfh.ch
set MAIL_PORT=25
set MAIL_USER_NAME=
set MAIL_USER_PASSWORD=
set MAIL_USER_ADDRESS=

set APPLICATION_NAME=bookstore
set CONTEXT_ROOT=bookstore


:menu
	cls
	echo 1 - Create GlassFish domain
	echo 2 - Start GlassFish and Java DB server
	echo 3 - Configure GlassFish domain
	echo 4 - Build Bookstore application
	echo 5 - Deploy Bookstore application
	echo 6 - Run Bookstore integration tests
	echo 7 - Undeploy and clean Bookstore application
	echo 8 - Stop GlassFish and Java DB server
	echo 9 - Delete GlassFish domain
	echo x - Exit
	echo.
	set /p choice=Choice: 
	if %choice%==1 call:createDomain
	if %choice%==2 call:startServers
	if %choice%==3 call:configureDomain
	if %choice%==4 call:buildApplication
	if %choice%==5 call:deployApplication
	if %choice%==6 call:runIntegrationTests
	if %choice%==7 call:undeployApplication
	if %choice%==8 call:stopServers
	if %choice%==9 call:deleteDomain
	if %choice%==x goto :eof
	echo.
	pause
goto menu

:createDomain
	echo.
	echo *** Creating GlassFish domain...
	set /p GF_ADMIN_PASSWORD=GlassFish admin password: 
	echo AS_ADMIN_PASSWORD=%GF_ADMIN_PASSWORD%> password.txt
	call asadmin --user %GF_ADMIN_USER% --passwordfile password.txt create-domain --savelogin true %GF_DOMAIN%
	del password.txt
goto :eof

:startServers
	echo.
	echo *** Starting Java DB server...
	call asadmin start-database --dbhost=%DB_HOST% --dbport=%DB_PORT% --dbhome=%DB_HOME%
	echo.
	echo *** Starting GlassFish server...
	call asadmin start-domain %GF_DOMAIN%
goto :eof

:configureDomain
	echo.
	echo *** Creating JDBC data source...
	call asadmin create-jdbc-connection-pool --datasourceclassname org.apache.derby.jdbc.ClientDataSource --restype javax.sql.DataSource --property ServerName=%DB_HOST%:Port=%DB_PORT%:DatabaseName=%DB_NAME%:User=%DB_USER%:Password=%DB_PASSWORD%:ConnectionAttributes='create=true' ConnectionPool
	call asadmin create-jdbc-resource --connectionpoolid ConnectionPool %DATASOURCE_NAME%
	echo.
	echo *** Creating JMS message queue and connection factory...
	call asadmin create-jmsdest --desttype queue PhysicalQueue
	call asadmin create-jms-resource --restype javax.jms.Queue --property Name=PhysicalQueue %JMS_QUEUE_NAME%
	call asadmin create-jms-resource --restype javax.jms.ConnectionFactory %JMS_CONNECTION_FACTORY%
	echo.
	echo *** Creating JavaMail session...
	set /p MAIL_USER_NAME=Mail user name: 
	set /p MAIL_USER_PASSWORD=Mail user password: 
	set /p MAIL_USER_ADDRESS=Mail user address: 
	call asadmin create-javamail-resource --mailhost %MAIL_HOST% --mailuser %MAIL_USER_NAME% --fromaddress=%MAIL_USER_ADDRESS% --property mail.smtp.port=%MAIL_PORT%:mail.smtp.auth=true:mail.smtp.password=%MAIL_USER_PASSWORD%:mail.smtp.starttls.enable=true %MAIL_SESSION_NAME%
goto :eof

:buildApplication
	echo.
	echo *** Building Bookstore application...
	call mvn clean install
goto :eof

:deployApplication
	echo.
	echo *** Deploying Bookstore application...
	call asadmin deploy --force=true %APPLICATION_NAME%-app/target/*.ear
	echo Application available at http://localhost:8080/%CONTEXT_ROOT%
goto :eof

:runIntegrationTests
	echo.
	echo *** Running Bookstore integration tests...
	call mvn failsafe:integration-test failsafe:verify
goto :eof

:undeployApplication
	echo.
	echo *** Undeploying Bookstore application...
	call asadmin undeploy %APPLICATION_NAME%-app
	echo.
	echo *** Cleaning Bookstore application...
	call mvn clean
goto :eof

:stopServers
	echo.
	echo *** Stopping GlassFish server...
	call asadmin stop-domain %GF_DOMAIN%
	echo.
	echo *** Stopping Java DB server...
	call asadmin stop-database --dbhost=%DB_HOST% --dbport=%DB_PORT%
goto :eof

:deleteDomain
	echo.
	echo *** Deleting GlassFish domain...
	call asadmin delete-domain %GF_DOMAIN%
goto :eof

:error
	echo Environment variable JAVA_HOME, MAVEN_HOME or GLASSFISH_HOME not set
goto :eof
