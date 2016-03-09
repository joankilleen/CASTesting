export CP=$M2_REPO/org/liquibase/liquibase-core/3.1.1/*
#CP=${CP}:$M2_REPO/com/h2database/h2/1.3.170/*
CP=${CP}:$M2_REPO/org/apache/derby/derby/10.11.1.1/*
CP=${CP}:$M2_REPO/org/apache/derby/derbyclient/10.11.1.1/*

#java  -classpath $CP liquibase.integration.commandline.Main --driver=org.h2.Driver --url=jdbc:h2:tcp://localhost/~/musicstore --username=sa --password= --diffTypes=tables,views,columns,indexes,foreignkeys,primarykeys,uniqueconstraints,data generateChangeLog
java -classpath $CP liquibase.integration.commandline.Main --driver=org.apache.derby.jdbc.ClientDriver --url=jdbc:derby://localhost:1527/musicstore --username=APP --password=s --changeLogFile=changelog.xml --diffTypes=tables,views,columns,indexes,foreignkeys,primarykeys,uniqueconstraints,data generateChangeLog
