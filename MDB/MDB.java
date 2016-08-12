package ejb30;

import javax.ejb.MessageDriven;
import javax.jms.MessageListener;
import javax.jms.Message;

@MessageDriven(mappedName="MDBQueue")
public class MDB implements MessageListener {

    public void onMessage(Message msg) {
        System.out.println("Got message!");
    }

}
