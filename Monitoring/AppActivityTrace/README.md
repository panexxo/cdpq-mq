It's possible to subscribe to an IBM® MQ system topic to collect application activity trace information.

A subscription can trace activity on one of the following resources:

    A specified application
    A specified IBM MQ channel
    An existing IBM MQ connection

The topic strings have the following format: 

````
$SYS/MQ/INFO/QMGR/qmgr_name/ActivityTrace/resource_type/resource_identifier
````

- *qmgr_name* specifies the queue manager to trace on
- *resource_type* specifies the type of resource to trace:

    **ApplName** An application. 
    **ChannelName** An IBM MQ channel.
    **ConnectionId** An IBM MQ connection.

- *resource_identifier* identifies the actual resource. 

# Running the amqsact programm

To better understand run the program **amqsact** on a MQ server.

The **amqsact** program is an IBM MQ sample.  

The executable file is located in the samples directory: 
-    On Linux® and UNIX platforms, MQ_INSTALLATION_PATH/samp/bin
-    On Windows platforms, MQ_INSTALLATION_PATH\tools\c\Samples\Bin

Run the program as follows: (on the **QM1** queue manager and collecting messages for application named **JmsPutLocalCloud**):

````
/opt/mqm/samp/bin/amqsact -m QM1 -w 60 -a JmsPutLocalCloud
````

The console will show the topic string that the application is listening to:

![topicString](./../../Pics/topicString.png)

The java file here (JmsPut.java) will send a message to the cloud queue manager acting as application **JmsPutLocalCloud**, the program **amqsact** will display the trace on the application:

![amqsactDisp](./../../Pics/amqsactDisp.png)


# Running a Java app subscribing to the topic

The java file (JmsSub.java) subscribe to the same topic displayed on the former example with the program **amqsact**.

The messages on the system topics are in PCF format (Programmable Command Format), based on the code from (amqsact0.c), the java file (CommonUtilsPCF.java) tries to decodify the format and print in a more legible form.

---------------------------------------------------------------
# Useful links

[Message monitoring](https://www.ibm.com/docs/en/ibm-mq/9.3?topic=network-message-monitoring)

[Subscriptions to application activity trace](https://www.ibm.com/docs/en/ibm-mq/9.3?topic=information-creating-subscriptions-application-activity-trace)

[amwsact to trace](https://www.ibm.com/docs/en/ibm-mq/9.3?topic=information-using-amqsact-view-trace-messages)

[Application activity trace information](https://www.ibm.com/docs/en/ibm-mq/9.3?topic=trace-configuring-central-collection-application-activity-information)

[Trace levels using mqat.ini](https://www.ibm.com/docs/en/ibm-mq/9.3?topic=information-configuring-trace-levels-using-mqatini)

