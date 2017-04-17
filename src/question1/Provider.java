package question1;

import javax.naming.*;
import javax.jms.*;
import java.util.Hashtable;
import java.util.Date;

/**
 * The type Provider.
 */
public class Provider implements MessageListener
{
    private Context contexte;
    private Connection connexion;
    private MessageProducer sender = null;
    private MessageConsumer receiver = null;
    private Session session = null;

    /**
     * Creation du Consumer
     *
     * @param requestChannel le nom de la file pour l'envoi
     * @param replyChannel   le nom de la file pour la reception
     * @throws NamingException the naming exception
     * @throws JMSException    the jms exception
     */
    public Provider(String requestChannel, String replyChannel) throws NamingException, JMSException
    {
        // nsy102 lignes extraites de http://openjms.sourceforge.net/usersguide/using.html
        Hashtable<String, String> props = new Hashtable<String, String>();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "org.exolab.jms.jndi.InitialContextFactory");
        props.put(Context.PROVIDER_URL, "tcp://localhost:3035/");

        contexte = new InitialContext(props);

        // look up the ConnectionFactory
        ConnectionFactory factory = (ConnectionFactory) contexte.lookup("ConnectionFactory");

        // look up the Destination
        Destination request = (Destination) contexte.lookup(requestChannel);
        Destination reply = (Destination) contexte.lookup(replyChannel);

        // create the connection
        connexion = factory.createConnection();

        // create the session
        session = connexion.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // create the receiver
        receiver = session.createConsumer(request);

        // create the sender
        sender = session.createProducer(reply);
        receiver.setMessageListener(this);

        // start the connection, to enable message sends
        connexion.start();
    }

    public void onMessage(Message message)
    {
        try {
            // réception du message
            MessageQ1 recu = null;

            if (message instanceof ObjectMessage) {
                ObjectMessage oMessage = (ObjectMessage) message;
                if (oMessage.getObject() instanceof MessageQ1) {
                    recu = (MessageQ1) oMessage.getObject();
                    recu.setEstampille(new Date());
                }
            }

            // envoi du message estampillé
            if (null != recu) {
                ObjectMessage msgRenvoi = session.createObjectMessage();
                msgRenvoi.setObject(recu);
                sender.send(msgRenvoi);
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
        Provider provider = null;
        try {
            provider = new Provider("request", "reply");
            Thread.sleep(Long.MAX_VALUE); // ...
        } finally {
            provider.close();
        }
    }

}
