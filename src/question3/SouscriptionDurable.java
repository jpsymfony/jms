package question3;

import question2.*;

import javax.naming.*;
import javax.jms.*;
import java.util.Hashtable;
import java.util.Date;

/**
 * The type Souscription durable.
 */
public class SouscriptionDurable extends Souscription implements MessageListener
{
    /**
     * Instantiates a new Souscription durable.
     *
     * @param topicName  the topic name
     * @param identifier the identifier
     * @throws NamingException the naming exception
     * @throws JMSException    the jms exception
     */
    public SouscriptionDurable(String topicName, String identifier) throws NamingException, JMSException
    {
        super(topicName);
        topicSubscriber = topicSession.createDurableSubscriber(topic, identifier);
        topicSubscriber.setMessageListener(this);
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


