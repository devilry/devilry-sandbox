Compile and run with:
~$ mvn compile
~$ mvn exec:java -Dexec.mainClass="sandbox.Demo" -Dexec.args="Peter"

run exec multiple times with different names as argument to add multiple people 
to the database.
    
Browsing the database with HSQLDB database manager:

~$ java -cp ~/.m2/repository/hsqldb/hsqldb/1.8.0.7/hsqldb-1.8.0.7.jar \
    org.hsqldb.util.DatabaseManagerSwing --urlid file:hello-disk
