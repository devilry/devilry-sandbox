
all: 
	javac -cp $(J2EE_CLASSPATH):. ejb30/client/*.java
	javac -cp $(J2EE_CLASSPATH):. ejb30/session/*.java
	echo "Creating bean jar.."
	jar cf Echo.jar ejb30/session/*.class

install: 
	asadmin deploy Echo.jar

uninstall:
	asadmin undeploy Echo

run:
	java -Djava.security.auth.login.config=jaas.conf -classpath .:$(J2EE_CLASSPATH) ejb30.client.Client

clean:
	@rm ejb30/client/*.class
	@rm ejb30/session/*.class
	@rm Echo.jar

