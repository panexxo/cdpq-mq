# Basic setup for java file 

---------------------------------------------------------------
# 1. TLS Preparation
---------------------------------------------------------------
✅ Download the public certificate from the Cloud Instance:
![qmgrcert.pem](./../Pics/certificate.png)

✅ Create a Keystore file in PKCS12 format

````
runmqakm -keydb -create -db key.kdb -pw *your_password* -type pkcs12 -expire 0 -stash
````
_Keep this password for later add to the code (line 84)_

✅ Update the file permissions to make the keystore readable

````
chmod +rw key.*
````

✅ Check the certificate have been added.

````
runmqakm -cert -list -db key.kdb
````

---------------------------------------------------------------
# 2. User app setup
---------------------------------------------------------------
✅ Add an app credential valid user.
![qmgrcert.pem](./../Pics/appCredentials.png)
_Keep user and password for later add to the code (lines 60 and 61)_

---------------------------------------------------------------
# 3. App channel setup
---------------------------------------------------------------
✅ Open MQ Console
![qmgrcert.pem](./../Pics/mqconsole.png)

✅ Select Queue manager and select Applications tab
![qmgrcert.pem](./../Pics/appQueueManager.png)

✅ Click the app channels on the right, then click the elipsis at the end of the channel **CLOUD.APP.SVRCONN** line, on the menu select _View configuration_
![qmgrcert.pem](./../Pics/appChannelsConf.png)

✅ Scroll down until the SSL parameters and verify the SSL Cipher spec has the same value as the line 85 on the code
![qmgrcert.pem](./../Pics/cloudCipherSpec.png)

---------------------------------------------------------------
# 4. Download jar files
---------------------------------------------------------------
✅ Create a directory to save the files needed for the sample:
````
 mkdir MQClient
 cd MQClient
````

✅ From the MQClient folder, download the com.ibm.mq.allclient.jar file by using curl.
````
 curl -o com.ibm.mq.allclient-9.0.5.0.jar https://repo1.maven.org/maven2/com/ibm/mq/com.ibm.mq.allclient/9.3.0.0/com.ibm.mq.allclient-9.0.5.0.jar
````

✅ From the MQClient folder, download the JMS API file by using curl.
````
  curl -o javax.jms-api-2.0.1.jar https://repo1.maven.org/maven2/javax/jms/javax.jms-api/2.0.1/javax.jms-api-2.0.1.jar
````

✅ From the MQClient folder, download the JSON .jar file by using curl.
````
   curl -o json-20220320.jar https://repo1.maven.org/maven2/org/json/json/20220320/json-20220320.jar
````

---------------------------------------------------------------
# 5. Download sample java app JmsPut.java
---------------------------------------------------------------
✅ Create In your MQClient directory, create the following directory structure: com/ibm/mq/samples/jms.
````
  mkdir -p com/ibm/mq/samples/jms
  cd com/ibm/mq/samples/jms
````

✅ In the directory created download the JmsPut.java sample from GitHub by using curl:
````
  curl -o JmsPut.java https://github.ibm.com/tech-garage-canada/cdpq-mq/blob/main/JMS/JmsPut.java
````

---------------------------------------------------------------
# 5. Modify the java code as needed
---------------------------------------------------------------
✅ Change the values for the vars accordingly  (lines 55 to 62)
````
	private static final String HOST = "mqcloud-xxxx.qm.us-xxxxx.mq.appdomain.kloud"; // Host name or IP address
	private static final int PORT = 33367; // Listener port for your queue manager
	private static final String CHANNEL = "CLOUD.APP.SVRCONN"; // Channel name
	private static final String QMGR = "QMCLOUD"; // Queue manager name
	private static final String APP_USER = "userx"; // User name that application uses to connect to MQ
	private static final String APP_PASSWORD = "xxxxxx"; // Password that the application uses to connect to MQ
	private static final String QUEUE_NAME = "DEV.QUEUE.1"; // Queue that the application uses to put messages 
````

✅ Verify on line 83 the path to the keystore [Point 1](https://github.ibm.com/tech-garage-canada/cdpq-mq/blob/main/JMS/Setup.md#1-tls-preparation), and on line 24 set the password
````
			System.setProperty("javax.net.ssl.keyStore", "/path/to/your/keystore/key.kdb");
			System.setProperty("javax.net.ssl.keyStorePassword", "mqXXX");
			cf.setStringProperty(WMQConstants.WMQ_SSL_CIPHER_SPEC, "TLS_RSA_WITH_AES_256_CBC_SHA256");
````

---------------------------------------------------------------
# 6. Compile and Run the code
---------------------------------------------------------------
✅ Compile
````
 javac -cp ./com.ibm.mq.allclient-9.0.5.0.jar:./javax.jms-api-2.0.1.jar:./json-20220320.jar com/ibm/mq/samples/jms/JmsPut.java
````

When using IBM JRE
✅ Run
````
  java -cp ./com.ibm.mq.allclient-9.0.5.0.jar:./javax.jms-api-2.0.1.jar:./json-20220320.jar:. com.ibm.mq.samples.jms.JmsPutGet
````

When using Oracle JRE (OpenJDK) change JVM argument Dcom.ibm.mq.cfg.useIBMCipherMappings to false
✅ Run
````
  java -Dcom.ibm.mq.cfg.useIBMCipherMappings=false -cp ./com.ibm.mq.allclient-9.0.5.0.jar:./javax.jms-api-2.0.1.jar:./json-20220320.jar:. com.ibm.mq.samples.jms.JmsPutGet
````

[TLS CipherSpecs and CipherSuites](https://www.ibm.com/docs/en/ibm-mq/9.1?topic=jms-tls-cipherspecs-ciphersuites-in-mq-classes)


Should get an output like this:

![output](./../Pics/output.png)


LINKS RELATED
[MQ Java, TLS Ciphers](https://community.ibm.com/community/user/integration/viewdocument/mq-java-tls-ciphers-non-ibm-jres?CommunityKey=183ec850-4947-49c8-9a2e-8e7c7fc46c64&tab=librarydocuments)
