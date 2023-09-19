This folder shows an example of an independent application to monitor system resources.

Each queue manager publishes resource usage data to topics. This data is consumed by subscribers to those topics. 

When a queue manager starts, the queue manager publishes a set of messages on meta-topics. 

These messages describe which resource usage topics are supported by the queue manager, and the content of the messages published to those topics. 

Administrative tools can subscribe to the metadata to discover what resource usage information is available, and on what topics, and then subscribe to the advertised topics.

---------------------------------------------------------------

The following repository contains a collection of IBM monitoring agents based on IBM-MQ GO library.

[Monitoring IBM MQ queue depth in Cloud Pak for Integration](https://community.ibm.com/community/user/integration/blogs/matt-roberts1/2021/05/03/monitoring-mq-qdepth-cp4i)

The repo have several scripts to run the monitor using Docker containers.

Here we provide the modified version of the script runMonitorTLSx.sh, the script builds a container that holds the runtime for a Prometheus monitor and
then runs it with TLS options for a secure connection to the queue managerthe script will.  
(Change the path to the ccdt.json file, the path to the public certiticate download and the MQ-Cloud user and password.)

---------------------------------------------------------------
# Useful links

[Message monitoring](https://www.ibm.com/docs/en/ibm-mq/9.3?topic=network-message-monitoring)

[System topics for monitoring and tracing](https://www.ibm.com/docs/en/ibm-mq/9.3?topic=network-system-topics-monitoring-activity-trace)

[Develop a monitor resource](https://www.ibm.com/docs/en/ibm-mq/9.3?topic=trace-developing-your-own-resource-monitoring-application)
