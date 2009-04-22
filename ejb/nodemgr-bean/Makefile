
all: 
	javac -cp $(J2EE_CLASSPATH):. ejb30/client/*.java
	javac -cp $(J2EE_CLASSPATH):. ejb30/session/*.java
	javac -cp $(J2EE_CLASSPATH):. ejb30/entity/*.java
	echo "Creating bean jar.."
	jar cf NodeMgr.jar META-INF/* ejb30/session/*.class ejb30/entity/*.class

install: 
	asadmin deploy --createtables=true NodeMgr.jar

uninstall:
	asadmin undeploy --droptables=true NodeMgr

run:
	java -Djava.security.auth.login.config=jaas.conf -classpath .:$(J2EE_CLASSPATH) ejb30.client.Client

clean:
	@rm ejb30/client/*.class
	@rm ejb30/session/*.class
	@rm NodeMgr.jar

