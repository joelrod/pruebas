/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managed.beans;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author ihsa
 */
public class EchoMDBClient implements MessageListener {

//    public static void main(String[] args) throws JMSException, NamingException {
//        
//        EchoMDBClient echoMDBClient = new EchoMDBClient();
//        
//        Context context = getInitialContext();
//        
//        Queue queueCorreo = (Queue) context.lookup("GlassFishBookQueue");
//        
//        JMSContext jmsContext = 
//                ((ConnectionFactory) context.lookup("jms/GlassFishBookConnectionFactory")).createContext();
//        
//        jmsContext.createConsumer(queueCorreo).setMessageListener(echoMDBClient);
//        
//        JMSProducer jmsProducer = jmsContext.createProducer();
//        
//        for (int i = 0; i < 10; i++) {
//            
//            jmsProducer.send(queueCorreo, " --- psssssstt "+i);
//            
//        }         
//        
//        jmsContext.close();
//        
//    }            
    @Override
    public void onMessage(Message message) {
        try {
            System.out.println(" en EchoMDBClient ");
            System.out.println("Mensaje en cliente :" + message.getBody(String.class));
        } catch (JMSException ex) {
            Logger.getLogger(EchoMDBClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static Context getInitialContext() throws JMSException, NamingException {

        Properties prop = new Properties();
        prop.setProperty("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
        prop.setProperty("java.naming.factory.url.pkgs", "com.sun.enterprise.naming");
        prop.setProperty("java.naming.factory.state", "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");//        prop.setProperty("java.naming.provider.url", "iiop://localhost:3700");
        prop.setProperty("org.omg.CORBA.ORBInitialHost", "192.168.3.127");
        prop.setProperty("org.omg.CORBA.ORBInitialPort", "3700");

        return new InitialContext(prop);

    }

}
