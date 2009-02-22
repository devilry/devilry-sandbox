How to  run this on a real openejb server (not embedded like the tests)
-----------------------------------------------------------------------

1) Download openejb from openejb.apache.org. $OPENEJB_HOME is the root directory of openejb.

2) Build the class-files as a jar file::

    ~# mvn package

3) Deploy the bean::

    ~# $OPENEJB_HOME/bin/openejb deploy target/devilry-sandbox-openejb-simple-stateless-0.1.jar 

4) Run the client::

   ~# java -cp $OPENEJB_HOME/lib/openejb-client-3.0.jar:$OPENEJB_HOME/lib/javaee-5.0-1.jar:target/devilry-sandbox-ejb-openejb_stateful_entity-0.1.jar org.devilry.HelloClient2
