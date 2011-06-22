
all: 
	javac -cp $(J2EE_CLASSPATH):. org/devilry/core/client/*.java
	javac -cp $(J2EE_CLASSPATH):. org/devilry/core/session/*.java
	javac -cp $(J2EE_CLASSPATH):. org/devilry/core/session/dao/*.java
	javac -cp $(J2EE_CLASSPATH):. org/devilry/core/entity/*.java
	echo "Creating TreeManager.jar:"
	jar cf TreeManager.jar META-INF/* \
		org/devilry/core/session/*.class \
		org/devilry/core/session/dao/*.class \
		org/devilry/core/entity/*.class 

install: 
	asadmin deploy --createtables=true TreeManager.jar

uninstall:
	asadmin undeploy --droptables=true TreeManager

run:
	java -classpath .:$(J2EE_CLASSPATH) org.devilry.core.client.Client

clean:
	@rm org/devilry/core/client/*.class
	@rm org/devilry/core/session/*.class
	@rm org/devilry/core/session/dao/*.class
	@rm org/devilry/core/entity/*.class
	@rm TreeManager.jar

