package ejb30;

import javax.annotation.Resource;
import javax.jms.*;

public class MDBAppClient {

    @Resource(mappedName="MDBQueueConnectionFactory")
    private static QueueConnectionFactory queueCF;

    @Resource(mappedName="MDBQueue")
    private static Queue mdbQueue;

    public static void main(String args[]) {

        try {

            QueueConnection queueCon = queueCF.createQueueConnection();

            QueueSession queueSession = queueCon.createQueueSession
                (false, Session.AUTO_ACKNOWLEDGE);

            QueueSender queueSender = queueSession.createSender(null);

            TextMessage msg = queueSession.createTextMessage("hello");

            queueSender.send(mdbQueue, msg);

            System.out.println("Sent message to MDB");
            
            queueCon.close();

        } catch(Exception e) {
            e.printStackTrace();
        }

    }

}
