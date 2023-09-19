# cdpq-mq
repository for engagement with CDPQ for MQ

## What this repo is for

This repo hosts sample code for specific use cases on CDPQ.

Also documents question on how to execute particular functions over MQ-Client and MQ-Server (hosted on cloud)

<br><br>

## 1.- Connection to MQ=Cloud Queue manager with java client version 9.0

On the folder [JavaClient9.0](https://github.ibm.com/tech-garage-canada/cdpq-mq/tree/main/JavaClient9.0) there is a simple Java app that puts a message by using client library 9.0.5.

---------------------------------------------------------------
<br><br>


## 2.- How to execute command runmqsc whitout provide password manually ?

Command waits for instructions from stdin, that could be interactive or by text file of MQSC commands.

When -u parameter is provided with a user ID, then a password is required in the prompt.

The password can be set in the first line of the text file of MQSC cmmands.

````
runmqsc -c -u <userID> -w 60 <Queue manager> < MQSCCommands.txt
````

or

````
cat MQSCCommands.txt | runmqsc -c -u <userID> -w 60 <Queue manager>
````

To avoid include the password on all the files of MQSC commands, the password could be set on another file.

````
(cat passfile.txt; cat MQSCCommands.txt) | runmqsc -c -u <userID> -w 60 <Queue manager>
````

### Related links
[runmqsc](https://www.ibm.com/docs/en/ibm-mq/9.3?topic=reference-runmqsc-run-mqsc-commands)

---------------------------------------------------------------
<br><br>


## 3.- Use of Internal Users on IBM-MQ Cloud 

[Identifying and authenticating users](https://www.ibm.com/docs/en/ibm-mq/9.3?topic=securing-identifying-authenticating-users)

---------------------------------------------------------------
<br><br>



## 4.- Traceability on MQ-Cloud to trace which apps is connecting to a QM and when an app put and get messages.

Customers can download by creating a logDNA instance in IBM Cloud.  
All the logs are pushed to LogDNA (similar to Grafana) where they can viewed filtered searched etc

[LogDNA Logs](https://cloud.ibm.com/docs/mqcloud?topic=mqcloud-logdna_logs)

IBM MQ on Cloud generates platform services logs that are displayed in LogDNA instances ONLY on:

- When the queue manager error logs are updated

To track how users and applications interact with the IBM MQ on Cloud service, create an IBM Cloud Activity Tracker service.

[Activity tracker](https://cloud.ibm.com/docs/mqcloud?topic=mqcloud-at_events)

Metrics can be retrieved by creating a Sysdig instance in IBM Cloud. (paid plans)
[Monitoring a Queue Manager](https://cloud.ibm.com/docs/mqcloud?topic=mqcloud-monitor_sysdig)

Any MQ tools will need to support client mode to connect to the qmgr which it looks like amqsact and dmpmqlog do not support. 

In this case the customer would need to implement a client themselves to access the event messages on the queue.

### Related links
[Logging application](https://cloud.ibm.com/docs/log-analysis?topic=log-analysis-app_logging)

---------------------------------------------------------------











