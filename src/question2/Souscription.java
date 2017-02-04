package question2;

import javax.naming.*;
import javax.jms.*;
import java.util.Hashtable;
import java.util.Date;

public class Souscription implements MessageListener
{
    private Context context;
    protected TopicConnection topicConnection;
    protected TopicSession topicSession;
    protected Topic topic;
    protected TopicSubscriber topicSubscriber;

    // a completer
    // a completer
    // a completer

    public Souscription(String topicName) throws NamingException, JMSException
    {
        Hashtable<String, String> properties = new Hashtable<String, String>();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.exolab.jms.jndi.InitialContextFactory");
        properties.put(Context.PROVIDER_URL, "tcp://localhost:3035/");
        // a completer
        // a completer
        // a completer
        // Get the initial context
        System.out.println("Getting Initial Context:");
        context = new InitialContext(properties);
        System.out.println("Got Initial Context:" + context);

        // Get the connection factory
        System.out.println("Getting Topic Factory:");
        TopicConnectionFactory topicFactory = (TopicConnectionFactory) context.lookup("JmsTopicConnectionFactory");
        System.out.println("Got Topic Factory:" + topicFactory);

        // Create the connection
        topicConnection = topicFactory.createTopicConnection();

        // Create the session
        topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

        // Look up the destination
        topic = (Topic) context.lookup(topicName);

        // Create a publisher
        topicSubscriber = topicSession.createSubscriber(topic);

        topicSubscriber.setMessageListener(this);
        System.out.println("topicSubscriber subscribed to topic: " + topicName);
        topicConnection.start();
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


    public void close() throws NamingException, JMSException
    {
        // fermeture du contexte et de la connexion
        context.close();
        topicConnection.close();
    }

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


