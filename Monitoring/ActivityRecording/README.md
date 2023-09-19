Activity recording is a technique for determining the routes that messages take through a queue manager network. 

To determine the route that a message has taken, the activities performed on behalf of the message are recorded.

The activity performed by a message is recorded in an activity report.

Activity reports are PCF messages containing a message descriptor and message data:

The message descriptor

- An MQMD structure

Message data

- An embedded PCF header (MQEPH)
- Activity report message data

# Enabling activity reports

To enable activity reports for a message :

- Define the message to request activity reports

    1. In the message descriptor of the message, specify **MQRO_ACTIVITY** in the **_Report_** field.
    2. In the message descriptor of the message, specify the name of a **reply-to** queue in the **_ReplyToQ_** field.

- Enable the queue manager for activity recording by using the MQSC command ALTER QMGR, specifying the parameter ACTIVREC.

    1. MSG, Any activity reports generated are delivered to the **reply-to** queue specified in the message descriptor of the message. This is the default value. 
    2. QUEUE, Any activity reports generated are delivered to the local system queue SYSTEM.ADMIN.ACTIVITY.QUEUE.
    3. DISABLED, No activity reports generated

````
ALTER QMGR ACTIVREC(QUEUE)
````

# Running a Java app that enable message activity recording.

The file file (JmsPutLocalMQMD.java) send a message, and enables activity recording modifyng the message descriptor.

To modify the message descriptor the Destination object property WMQ_MQMD_WRITE_ENABLED must set to true for the setting of MQMD properties to have any effect. 

Then the property setting methods of the message (for example setStringProperty) are used to assign values to the MQMD fields. 

````
// Enable MQMD write
((JmsDestination) destination).setBooleanProperty(WMQConstants.WMQ_MQMD_WRITE_ENABLED, true);			
.
.
.
//specify **MQRO_ACTIVITY** in the **_Report_** field.
message.setIntProperty(WMQConstants.JMS_IBM_MQMD_REPORT, CMQC.MQRO_ACTIVITY);
// specify the name of a **reply-to** queue in the **_ReplyToQ_** field.
message.setStringProperty(WMQConstants.JMS_IBM_MQMD_REPLYTOQ, "DEV.QUEUE.1");
````

The queue manager was configured as MSG, meaning that any activity reports generated are delivered to the local system queue specified DEV.QUEUE.1

As the message moves through the network, the channels reports the get and send, and the receive and put of the message.   

The report produced have an embedded PCF message within the payload.  (A message of format Embedded PCF,  MQHEPCF, allow you to have a PCF followed by other data, for example application data).

This report has to be read accordingly to extract the PCF message, one approach is use the command dspmqrte that will display the hops between queues. 

````
/opt/mqm/bin/dspmqrte  -i 0 -q DEV.QUEUE.1 -m QM1 -w1
````

![dspmqrteDisp](./../../Pics/dspmqrteDisp.png)


It is up to the user to build up the true end to end path, and manage the responses yourself.

The java file JmsGetActivityQ.java read from the queue set and just prints out the activity report it.

---------------------------------------------------------------
# Useful links

[Modifying the message descriptor from an JMS application](https://www.ibm.com/docs/en/ibm-mq/9.3?topic=amffmcja-reading-writing-message-descriptor-from-mq-classes-jms-application)

[Activity recording](https://www.ibm.com/docs/en/ibm-mq/9.3?topic=monitoring-activity-recording)

[PCF Data Parser java](https://github.com/Cinimex/mq-java-exporter/blob/master/src/main/java/ru/cinimex/exporter/mq/pcf/PCFDataParser.java)

[PCF Processing](https://community.ibm.com/community/user/integration/viewdocument/pcf-processing-in-jms-a-gotcha?CommunityKey=183ec850-4947-49c8-9a2e-8e7c7fc46c64&tab=librarydocuments&lang=en)

[SimpleMQMDWrite.java](https://github.com/rosierui/jms/blob/master/ibm-mq/wmq-8.0-simple-jms/simple/SimpleMQMDWrite.java)