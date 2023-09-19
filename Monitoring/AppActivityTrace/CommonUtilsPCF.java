package com.ibm.mq.samples.jms;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.*;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;

import com.ibm.mq.MQMessage;
import com.ibm.mq.headers.MQDataException;
import com.ibm.mq.headers.pcf.PCFMessage;
import com.ibm.mq.headers.pcf.PCFParameter;

import com.ibm.mq.constants.CMQCFC;

public class CommonUtilsPCF {

    private static Logger logger;

    static Map reasonCodes = new HashMap();
	/** Map of MQ command names and values. */
	static Map commands = new HashMap();
	/** Map of MQ string names and values. */
	static Map stringNames = new HashMap();

	public CommonUtilsPCF()
	{
		logger = Logger.getLogger("com.ibm.mq.samples.jms");
		setupMaps();
	}

	public void getAndDisplayMessageBody(Message receivedMessage) throws JMSException, IOException, MQDataException {
		// get a response into a JMS Message
	      BytesMessage bytesMessage = (BytesMessage) receivedMessage;
	      byte[] bytesreceived = new byte[(int) bytesMessage.getBodyLength()];
	      bytesMessage.readBytes(bytesreceived); 
	      
	      // convert to MQMessage then to PCFMessage
	      MQMessage mqMsg = new MQMessage();
	      mqMsg.write(bytesreceived);
	      mqMsg.encoding = receivedMessage.getIntProperty("JMS_IBM_Encoding");
	      mqMsg.format = receivedMessage.getStringProperty("JMS_IBM_Format");
	      mqMsg.seek(0); 
	      PCFMessage pcfMsg = new PCFMessage(mqMsg);
	      
	      //Reconstitute the PCF response. (Option-2)
//	      ByteArrayInputStream bais = new ByteArrayInputStream(((BytesMessage)receivedMessage).getBody(byte[].class));
//	      DataInput di = new DataInputStream(bais);
//	      PCFMessage pcfMsg = new PCFMessage(di);

	      
	      Enumeration pcfEnum = pcfMsg.getParameters();
	      
	      String stdout = "";      
	      stdout = stdout + "REASON: " + getReasonName(pcfMsg.getReason()) + "\n";

	      //logger.info("Message PCF:" + pcfMsg.getReason() + "\n");
			while (pcfEnum.hasMoreElements())
			{
				PCFParameter p = (PCFParameter) pcfEnum.nextElement ();
			
				String parameterName = getStringName(p.getParameter());
				
				stdout = stdout + "\t Attribute " + parameterName + " = ";
				if (p.getType() == CMQCFC.MQCFT_STRING_LIST)
				{
					String strings[] = (String[])p.getValue();
			        for (int i = 0; i < strings.length; i++) {
			        	stdout = stdout + " " + strings[i] + " \n";
			        }
				}
				else
				  stdout = stdout + p.getValue().toString();
				
				stdout = stdout + "\n";
			     //System.out.println ("\t attribute id " + p.getParameter () + "=" + p.getValue ());
				//System.out.println ("\t attribute id " + parameterName + "=" + p.getValue ());
			}
			System.out.println(stdout);
		
	}

	/**
	* Converts an MQ integer to its string constant name.
	* @param stringInt the MQ integer.
	* @return the MQ constant name of the integer.
	*/
	private static String getStringName(int stringInt)
	{
		return (String)stringNames.get(Integer.valueOf(stringInt));
	}

	/**
	* Converts an MQ reason code to a string explaining it.
	* @param stringInt the MQ reason code integer.
	* @return the string identifying the MQ reason code.
	*/
	public String getReasonName(int stringInt)
	{
		return (String)reasonCodes.get(Integer.valueOf(stringInt));
	}

	/**
	 * Converts a constant integer to its MQ command name.
	 * @param stringInt the MQ integer.
	 * @return the MQ command name represented by the constant integer.
	 */
	private String getCommandName(int stringInt)
	{
	  return (String)commands.get(Integer.valueOf(stringInt));
	}

	public static void setupMaps()
    {
      setupReasonNameSub("com.ibm.mq.constants.CMQC", "MQRC", reasonCodes);
      setupReasonNameSub("com.ibm.mq.constants.CMQCFC", "MQRC", reasonCodes);
      setupReasonNameSub("com.ibm.mq.constants.CMQCFC", "MQCMD", commands);
      setupReasonNameSub("com.ibm.mq.constants.CMQC", "MQCA", stringNames);
      setupReasonNameSub("com.ibm.mq.constants.CMQCFC", "MQCA", stringNames);
      setupReasonNameSub("com.ibm.mq.constants.CMQC", "MQIA", stringNames);
    }

    /**
     * Sets up the maps for converting MQ constants to strings by querying the MQ class libraries. This is a subroutine called by setupMaps().
     * @param className the MQ class to be queried for the constants.
     * @param prefix the prefix of the constants names to identify the appropriate ones.
     * @param theMap the map to store the information in.
     */
    public static void setupReasonNameSub(String className, String prefix, Map theMap)
    {
      Class c;
      logger.info("about to load class " + className);
      try
      {
        c = Class.forName(className);
      }
      catch (Exception ex)
      {
        logger.severe("Class loading error" + ex.toString());
        return;
      }
      logger.info("loaded class");
      Field[] fields = c.getFields();
      for (int i = 0; i < fields.length; i++)
      {
        String name;
        if (fields[i].getType() == int.class)
        {
          name = fields[i].getName();
          if (name.startsWith(prefix))
          {
            try
            {
              Integer theInt = Integer.valueOf(fields[i].getInt(fields[i]));
              theMap.put(theInt, fields[i].getName());
            }
            catch (Exception ex)
            {
              logger.log(Level.SEVERE, "Class loading error" + ex.toString(), ex);
              return;
            }
          }
        }
      }
      logger.info("break");
    }

}
