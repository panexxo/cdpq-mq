Here two files to setup a local queue manager (running in a container) to a remote queue manager (running in cloud)

# mqqm1_docker.mqsc
MQSC commands to set up a local queue manager and channels to communicate with a remote queue manager.

- Connect to local queue manager (on docker)
````
docker exec -it 90300f7b7b07 bash
````

- Execuyte runmqsc using as stdin this file and as stdout a file to keep possible process errors
````
runmqsc QM1 < mqqm1_docker.mqsc > mqqm1_docker.out
````


# mqqmcloud_cloud.mqsc
MQSC commands to set up a remote  queue manager and channels to communicate with a local queue manager.
- Execuyte runmqsc using as stdin this file and as stdout a file to keep possible process errors
````
runmqsc -c -u <your-user> -w 60 QMCLOUD < mqqmcloud_cloud.mqsc > mqqmcloud_cloud.out
````

