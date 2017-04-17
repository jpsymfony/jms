package question2;

import javax.naming.*;
import javax.jms.*;
import java.util.Hashtable;

/**
 * The type Prise de rdv.
 */
public class PriseDeRDV
{
    private Context contexte = null;
    private TopicConnection connexion = null;
    private TopicSession session = null;
    private TopicPublisher sender = null;
    private Topic topic = null;

    /**
     * Instantiates a new Prise de rdv.
     *
     * @param topicName the topic name
     * @throws NamingException the naming exception
     * @throws JMSException    the jms exception
     */
    public PriseDeRDV(String topicName) throws NamingException, JMSException
    {
        Hashtable<String, String> properties = new Hashtable<String, String>();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.exolab.jms.jndi.InitialContextFactory");
        properties.put(Context.PROVIDER_URL, "tcp://localhost:3035/");

        System.out.println("Getting Initial Context:");
        contexte = new InitialContext(properties);
        System.out.println("Got Initial Context:" + contexte);

        // Get the connection factory
        System.out.println("Getting Topic Factory:");
        TopicConnectionFactory topicFactory = (TopicConnectionFactory) contexte.lookup("JmsTopicConnectionFactory");
        System.out.println("Got Topic Factory:" + topicFactory);

        // Create the connection
        connexion = topicFactory.createTopicConnection();

        // Create the session
        session = connexion.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

        // Look up the destination
        topic = (Topic) contexte.lookup(topicName);

        // Create a publisher
        sender = session.createPublisher(topic);
        connexion.start();
    }

    /**
     * Publier.
     *
     * @param rdv the rdv
     * @throws JMSException the jms exception
     */
    public void publier(RendezVous rdv) throws JMSException
    {
        try {
            // Create a message
            // a completer
            ObjectMessage message = session.createObjectMessage();
            message.setObject(rdv);

            // Publish the message
            // a completer
            sender.publish(message);
            //System.out.println("Published " + rdv + " to agenda Topic");
        } catch (Exception ex) {
            System.err.println("Could not handle message: " + ex);
            ex.printStackTrace();
        }
    }

    /**
     * Close.
     *
     * @throws NamingException the naming exception
     * @throws JMSException    the jms exception
     */
    public void close() throws NamingException, JMSException
    {
        contexte.close();
        connexion.close();
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception
    {

        PriseDeRDV priseDeRDV = null;
        try {
            priseDeRDV = new PriseDeRDV("agenda");
            priseDeRDV.publier(RendezVous.MAINTENANT);
        } finally {
            priseDeRDV.close();
        }
    }
}
