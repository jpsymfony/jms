package question1;

/**
 * The type Un autre scenario.
 */
public class UnAutreScenario
{

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
            Thread prod = new Thread(new Runnable()
            {
                public void run()
                {
                    Provider provider = null;
                    try {
                        provider = new Provider("request", "reply");
                    } catch (Exception e) {
                    } finally {
                        try {
                            provider.close();
                        } catch (Exception e) {
                        }
                    }
                }
            });


            consumer = new Consumer("request", "reply");

            consumer.send("test_envoi");
            prod.start();
            System.out.print("message recu : ");
            System.out.println(consumer.receive().toString());
            //Thread.sleep(1000000);
        } finally {
            consumer.close();
        }
    }
}
