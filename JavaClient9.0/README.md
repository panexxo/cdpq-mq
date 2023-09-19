# Simple Java app for put a message to a IBM-MQ-Cloud instance

**JmsPut.java** - Puts message to a queue _DEV.QUEUE.1_ on a queue manager _QMCLOUD_ using client jar 9.0.5

**SETUP.md** - Detailed instructions to setup environment and execute code sample using jar 9.0.5

---------------------------------------------------------------

Current documentation just send a sample message using JMS jar version 9.2.4.

https://developer.ibm.com/tutorials/mq-develop-mq-jms/


The samples here are using the jar com.ibm.mq.allclient-9.0.5.0.jar

# IBM MQ JMS samples
The JMS samples are based on the the existing samples shipped with IBM MQ Server and Client packages.
These have been tested with Java(TM) SE Runtime Environment (build 1.8.0_181-b13).

Download

[Maven repository](https://repo1.maven.org/maven2/com/ibm/mq/com.ibm.mq.allclient/)

[latest IBM MQ allclient jar](https://search.maven.org/search?q=a:com.ibm.mq.allclient)

[JMS API 2.0.1 jar](https://search.maven.org/search?q=a:javax.jms-api)

[JSON parser](https://central.sonatype.com/artifact/org.json/json/20230227)


# Errors found in the code that works with latest jar

**ERR**
```diff
- The jar 9.0.5 doesn't recognize the Cipher-spec "ANY_TLS12_OR_HIGHER"
```
Change the cipher spec **ANY_TLS12_OR_HIGHER** in line 85 to **TLS_RSA_WITH_AES_256_CBC_SHA256**

[Valid Cipher-spec list](https://www.ibm.com/docs/en/ibm-mq/9.0?topic=messages-enabling-cipherspecs#q014260___d79e4696)

CipherSpec corresponding to the signature algorithm of the keystore file, run the following to get the signature:
````
runmqakm -cert -details -db key.kdb -pw mq123 -label qmgrcert
````

**ERR**
```diff
- Unsupported CipherSuite: SSL_RSA_WITH_AES_256_CBC_SHA256
```
Apply this jvm option: -Dcom.ibm.mq.cfg.useIBMCipherMappings=false

[MQ-Java](https://community.ibm.com/community/user/integration/viewdocument/mq-java-tls-ciphers-non-ibm-jres?CommunityKey=183ec850-4947-49c8-9a2e-8e7c7fc46c64&tab=librarydocuments)


