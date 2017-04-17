package question1;

/**
 * The type Un scenario.
 */
public class UnScenario
{
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception
    {
        Provider provider = null;
        Consumer consumer = null;
        try {
            provider = new Provider("request", "reply");
            consumer = new Consumer("request", "reply");

            consumer.send("test_envoi");
            System.out.print("message recu : ");
            System.out.println(consumer.receive().toString());
            //Thread.sleep(3000);
        } finally {
            consumer.close();
            provider.close();
        }
    }
}
