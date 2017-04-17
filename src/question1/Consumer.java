package question1;

import javax.naming.*;
import javax.jms.*;
import java.util.Hashtable;

/**
 * The type Consumer.
 */
public class Consumer
{
    private Context contexte;
    private Connection connexion;
    private Session session = null;
    private MessageProducer sender = null;
    private MessageConsumer receiver = null;

    /**
     * Creation du Consumer
     *
     * @param requestChannel le nom de la file pour l'envoi
     * @param replyChannel   le nom de la file pour la reception
     * @throws NamingException the naming exception
     * @throws JMSException    the jms exception
     */
    public Consumer(String requestChannel, String replyChannel) throws NamingException, JMSException
    {
        // nsy102 lignes extraites de http://openjms.sourceforge.net/usersguide/using.html
        Hashtable<String, String> props = new Hashtable<String, String>();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "org.exolab.jms.jndi.InitialContextFactory");
        props.put(Context.PROVIDER_URL, "tcp://localhost:3035/");
        // a completer
        contexte = new InitialContext(props);

        // look up the ConnectionFactory
        ConnectionFactory factory = (ConnectionFactory) contexte.lookup("ConnectionFactory");

        // look up the Destination
        Destination reply = (Destination) contexte.lookup(replyChannel);
        Destination request = (Destination) contexte.lookup(requestChannel);

        // create the connection
        connexion = factory.createConnection();

        // create the session
        session = connexion.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // create the sender
        sender = session.createProducer(request);

        // create the receiver
        receiver = session.createConsumer(reply);

        // start the connection, to enable message sends
        connexion.start();
    }

    /**
     * Send.
     *
     * @param s the s
     * @throws JMSException the jms exception
     */
    public void send(String s) throws JMSException
    {
        ObjectMessage message = session.createObjectMessage();
        message.setObject(new MessageQ1(s));
        sender.send(message);
    }

    /**
     * Receive message q 1.
     *
     * @return the message q 1
     * @throws JMSException the jms exception
     */
    public MessageQ1 receive() throws JMSException
    {
        MessageQ1 recu = null;
        Message message = receiver.receive();
        if (message instanceof ObjectMessage) {
            ObjectMessage oMessage = (ObjectMessage) message;
            if (oMessage.getObject() instanceof MessageQ1) {
                recu = (MessageQ1) oMessage.getObject();
            }
        }
        return recu;
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
        Consumer consumer = null;
        try {
            consumer = new Consumer("request", "reply");
            consumer.send("test_envoi");
            //System.out.print("message recu : ");
            //System.out.println(consumer.receive());
        } finally {
            consumer.close();
        }
    }
}
