/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managed.beans;

import static com.managed.beans.EchoMDBClient.getInitialContext;
import javax.annotation.Resource;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.naming.Context;
import javax.naming.NamingException;

/**
 *
 * @author ihsa
 */
@ViewScoped
public class ClientBean {

    
    @Resource(mappedName="jms/GlassFishBookConnectionFactory")
    private static QueueConnectionFactory queueCF;

    @Resource(mappedName="MDBQueue")
    private static Queue mdbQueue;
    
    /**
     * Creates a new instance of ClientBean
     */
    public ClientBean() {
    }
    
    
    public void mensaje() throws NamingException, JMSException{
          EchoMDBClient echoMDBClient = new EchoMDBClient();
        
        Context context = getInitialContext();
        
        Queue queueCorreo = (Queue) context.lookup("GlassFishBookQueue");
        
        JMSContext jmsContext = 
                ((ConnectionFactory) context.lookup("jms/GlassFishBookConnectionFactory")).createContext();
        
        jmsContext.createConsumer(queueCorreo).setMessageListener(echoMDBClient);
        
        JMSProducer jmsProducer = jmsContext.createProducer();
        
        for (int i = 0; i < 10; i++) {
            
            jmsProducer.send(queueCorreo, " --- psssssstt "+i);
            
        }         
        
        jmsContext.close();
    }
    
    
}
