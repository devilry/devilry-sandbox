
all: 
	javac -cp $(J2EE_CLASSPATH):. org/devilry/client/*.java
	javac -cp $(J2EE_CLASSPATH):. org/devilry/session/*.java
	javac -cp $(J2EE_CLASSPATH):. org/devilry/entity/*.java
	echo "Creating TreeManager.jar:"
	jar cf TreeManager.jar META-INF/* org/devilry/session/*.class org/devilry/entity/*.class 

install: 
	asadmin deploy --createtables=true TreeManager.jar

uninstall:
	asadmin undeploy --droptables=true TreeManager

run:
	java -classpath .:$(J2EE_CLASSPATH) org.devilry.client.Client

clean:
	@rm org/devilry/client/*.class
	@rm org/devilry/session/*.class
	@rm org/devilry/entity/*.class
	@rm TreeManager.jar

