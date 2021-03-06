package question2;

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
        Souscription s1 = null, s2 = null, s3 = null;
        PriseDeRDV priseDeRDV = null;
        try {
            s1 = new Souscription("agenda");
            s2 = new Souscription("agenda");
            s3 = new Souscription("agenda");

            priseDeRDV = new PriseDeRDV("agenda");

            priseDeRDV.publier(RendezVous.MAINTENANT);
            Thread.sleep(10000);
        } finally {
            s1.close();
            s2.close();
            s3.close();
            priseDeRDV.close();
        }
    }
}
