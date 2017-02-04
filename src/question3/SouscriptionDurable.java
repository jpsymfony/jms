package question3;

import question2.*;

import javax.naming.*;
import javax.jms.*;
import java.util.Hashtable;
import java.util.Date;

public class SouscriptionDurable extends Souscription implements MessageListener
{
    public SouscriptionDurable(String topicName, String identifier) throws NamingException, JMSException
    {
        super(topicName);
        topicSubscriber = topicSession.createDurableSubscriber(topic, identifier);
    }

    public void onMessage(Message message)
    {
        try {
            RendezVous rdv = (RendezVous) ((ObjectMessage) message).getObject();
            System.out.println("rdv reçu : " + rdv);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }


}


