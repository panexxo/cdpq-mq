package com.ibm.mq.samples.jms;

import java.io.IOException;

/*
* (c) Copyright IBM Corporation 2019, 2023
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import java.util.logging.*;

import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.jms.JMSRuntimeException;
import javax.jms.JMSException;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;

import com.ibm.mq.headers.MQDataException;

public class JmsSub {
    private static final Level LOGLEVEL = Level.ALL;
    private static final Logger logger = Logger.getLogger("com.ibm.mq.samples.jms");

	private static String CHANNEL = "CLOUD.APP.SVRCONN"; // Channel name
	private static String QMGR = "QMCLOUD"; // Queue manager name
	private static String APP_USER = "testjava"; // User name that application uses to connect to MQ
	private static String APP_PASSWORD = "qiN1AxXxXxX_z8885xXxqixxX88887e88X_Caxx"; // Password that the application uses to connect to MQ
    private static String TOPIC_NAME = "$SYS/MQ/INFO/QMGR/QMCLOUD/ActivityTrace/ApplName/JmsPutLocalCloud"; // Topic to subscribe
//    private static String TOPIC_NAME = "$SYS/MQ/INFO/QMGR/QM1/ActivityTrace/ChannelName/DEV.APP.SVRCONN"; // Topic to subscribe
    
    private static String SUBSCRIPTION_NAME = "JmsSub - SampleSubscriber";
    private static String CIPHER_SUITE = "ANY_TLS12_OR_HIGHER";
    private static String CCDTURL = "/Users/path/to/ccdt/json/file/connection_info_ccdt.json";
    private static Boolean BINDINGS = false;

	private static final String HOST = "qmcloud-xx50.qm.us-north.mq.appdomain.cloud"; // Host name or IP address
	private static final int PORT = 32067; // Listener port for queue manager

	private static CommonUtilsPCF pdfUtils = new CommonUtilsPCF();
	
	public static void main(String[] args) throws JMSException, IOException, MQDataException {
        initialiseLogging();
        logger.info("Sub application is starting");

        JMSContext context = null;
        Destination destination = null;
        JMSConsumer subscriber = null;

        JmsConnectionFactory connectionFactory = createJMSConnectionFactory();
        setJMSProperties(connectionFactory);

        logger.info("created connection factory");

        context = connectionFactory.createContext();
        logger.info("context created");
        destination = context.createTopic("topic://" + TOPIC_NAME);
        logger.info("destination created");
        subscriber = context.createConsumer(destination);
        logger.info("consumer created");

        while (true) {
            try {
                Message receivedMessage = subscriber.receive();
                pdfUtils.getAndDisplayMessageBody(receivedMessage);
            } catch (JMSRuntimeException jmsex) {

                jmsex.printStackTrace();
                try {
                    logger.info("nada");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
        }
    }
	
    private static JmsConnectionFactory createJMSConnectionFactory() {
        JmsFactoryFactory ff;
        JmsConnectionFactory cf;
        try {
            ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
            cf = ff.createConnectionFactory();
        } catch (JMSException jmsex) {
            recordFailure(jmsex);
            cf = null;
        }
        return cf;
    }

    private static void setJMSProperties(JmsConnectionFactory cf) {
        try {
			System.setProperty("javax.net.ssl.keyStore", "/Users/path/to/local/keystore/file/key.kdb");
			System.setProperty("javax.net.ssl.keyStorePassword", "mq123");
			cf.setStringProperty(WMQConstants.WMQ_SSL_CIPHER_SPEC, CIPHER_SUITE);

			cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, HOST);
			cf.setIntProperty(WMQConstants.WMQ_PORT, PORT);
			cf.setStringProperty(WMQConstants.WMQ_CHANNEL, CHANNEL);
			cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
			cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, QMGR);
			cf.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, "SimpleSub (JMS)");
			cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
			cf.setStringProperty(WMQConstants.USERID, APP_USER);
			cf.setStringProperty(WMQConstants.PASSWORD, APP_PASSWORD);

        } catch (JMSException jmsex) {
            recordFailure(jmsex);
        }
        return;
    }

    private static void recordFailure(Exception ex) {
        if (ex != null) {
            if (ex instanceof JMSException) {
                processJMSException((JMSException) ex);
            } else {
                logger.warning(ex.getMessage());
            }
        }
        logger.info("FAILURE");
        return;
    }

    private static void processJMSException(JMSException jmsex) {
        logger.warning(jmsex.getMessage());
        Throwable innerException = jmsex.getLinkedException();
        logger.warning("Exception is: " + jmsex);
        if (innerException != null) {
            logger.warning("Inner exception(s):");
        }
        while (innerException != null) {
            logger.warning(innerException.getMessage());
            innerException = innerException.getCause();
        }
        return;
    }

    private static void initialiseLogging() {
        Logger defaultLogger = Logger.getLogger("");
        Handler[] handlers = defaultLogger.getHandlers();
        if (handlers != null && handlers.length > 0) {
            defaultLogger.removeHandler(handlers[0]);
        }

        Handler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(LOGLEVEL);
        logger.addHandler(consoleHandler);

        logger.setLevel(LOGLEVEL);
        logger.finest("Logging initialised");
    }

}
