How to  run this on a real openejb server (not embedded like the tests)
-----------------------------------------------------------------------

1) Install tomcat6 and openejb.

2) Install openejb.war into tomcat6 as described here:

    http://openejb.apache.org/tomcat.html

3) Create a tomcat manager role and the admin user with this role. You can just 
   copy tomcat-users.xml into conf/ in the tomcat6 install directory. Restart 
   tomcat.

4) Deploy the webapp (including the bean) on the tomcat server::

    ~# mvn tomcat:deploy

5) Run the client::

   ~# mvn exec:java -Dexec.args="en Hello\ World"


You will find the openejb config file, conf/openejb.xml, in tomcat6. Add the 
something like the content described in ./openejb.xml to this file to tune 
openejb to your needs.
