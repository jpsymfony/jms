package question2;

import javax.naming.*;
import javax.jms.*;
import java.util.Hashtable;
import java.util.Date;

/**
 * The type Souscription.
 */
public class Souscription implements MessageListener
{
    private Context contexte;
    /**
     * The Topic connection.
     */
    private TopicConnection connexion;
    /**
     * The Topic session.
     */
    protected TopicSession topicSession;
    /**
     * The Topic.
     */
    protected Topic topic;
    /**
     * The Topic subscriber.
     */
    protected TopicSubscriber topicSubscriber;

    /**
     * Instantiates a new Souscription.
     *
     * @param topicName the topic name
     * @throws NamingException the naming exception
     * @throws JMSException    the jms exception
     */
    public Souscription(String topicName) throws NamingException, JMSException
    {
        Hashtable<String, String> properties = new Hashtable<String, String>();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.exolab.jms.jndi.InitialContextFactory");
        properties.put(Context.PROVIDER_URL, "tcp://localhost:3035/");
        // a completer
        // a completer
        // a completer
        // Get the initial contexte
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
        topicSession = connexion.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

        // Look up the destination
        topic = (Topic) contexte.lookup(topicName);

        // Create a publisher
        topicSubscriber = topicSession.createSubscriber(topic);

        topicSubscriber.setMessageListener(this);
        System.out.println("topicSubscriber subscribed to topic: " + topicName);
        connexion.start();
    }

    public void onMessage(Message message)
    {
        try {
            // réception du rdv
            RendezVous rdv = null;

            if (message instanceof ObjectMessage) {
                ObjectMessage oMessage = (ObjectMessage) message;
                if (oMessage.getObject() instanceof RendezVous) {
                    rdv = (RendezVous) oMessage.getObject();
                }
            }

            if (null != rdv) {
                System.out.println("rdv reçu: " + rdv.toString());
            }
        } catch (JMSException e) {
            e.printStackTrace();
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
        // fermeture du contexte et de la connexion
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
        Souscription souscription = null;
        try {
            souscription = new Souscription("agenda");
            Thread.sleep(Long.MAX_VALUE); // ...
        } finally {
            souscription.close();
        }
    }
}


