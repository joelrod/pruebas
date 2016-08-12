/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mdb;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;

/**
 *
 * @author ihsa
 */
//@JMSDestinationDefinition(name = "Queue01", interfaceName = "javax.jms.Queue", resourceAdapter = "jmsra", destinationName = "Queue01")

@MessageDriven(mappedName="MDBQueue")
public class NewMessageBean implements MessageListener {

    @Inject
    JMSContext jmsContext;

    @Resource(mappedName = "GlassFishBookQueue")
    private Queue queue;

    public NewMessageBean() {
    }

    @Override
    public void onMessage(Message message) {
        try {

            String mensaje = message.getBody(String.class);
            System.out.println("######### Mensaje recibido siguiendo el mensaje " + mensaje);
            
            jmsContext.createProducer().send(queue, "ECHO "+mensaje);
        
        } catch (JMSException ex) {
            System.out.println("Excepcion al recibir el mensaje " + ex.getMessage());
            Logger.getLogger(NewMessageBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
