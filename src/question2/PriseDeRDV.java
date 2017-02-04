package question2;

import javax.naming.*;
import javax.jms.*;
import java.util.Hashtable;

public class PriseDeRDV
{
    private Context context = null;
    private TopicConnection topicConnection = null;
    private TopicSession topicSession = null;
    private TopicPublisher topicPublisher = null;
    private Topic topic = null;

    public PriseDeRDV(String topicName) throws NamingException, JMSException
    {
        Hashtable<String, String> properties = new Hashtable<String, String>();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.exolab.jms.jndi.InitialContextFactory");
        properties.put(Context.PROVIDER_URL, "tcp://localhost:3035/");

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
        topicPublisher = topicSession.createPublisher(topic);
        topicConnection.start();
    }

    public void publier(RendezVous rdv) throws JMSException
    {
        try {
            System.out.println("Will publish " + rdv + " Message to Device topic");

            // Look up the destination
            // a completer
            topic = (Topic) context.lookup("agenda");
            System.out.println("Found the agenda Topic");

            // Create a publisher
            // a completer
            topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            topicPublisher = topicSession.createPublisher(topic);

            // Create a message
            // a completer
            ObjectMessage message = topicSession.createObjectMessage();
            message.setObject(rdv);

            // Publish the message
            // a completer
            topicPublisher.publish(message);
            //System.out.println("Published " + rdv + " to agenda Topic");
        } catch (Exception ex) {
            System.err.println("Could not handle message: " + ex);
            ex.printStackTrace();
        }
    }

    public void close() throws NamingException, JMSException
    {
        context.close();
        topicConnection.close();
    }

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
