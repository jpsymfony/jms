package question1;

import java.util.Date;
import java.io.Serializable;

/**
 * The type Message q 1.
 */
public class MessageQ1 implements Serializable
{
    private String texte;
    private Date estampille;

    /**
     * Instantiates a new Message q 1.
     *
     * @param texte the texte
     */
    public MessageQ1(String texte)
    {
        this.texte = texte;
        this.estampille = null;
    }

    /**
     * Gets texte.
     *
     * @return the texte
     */
    public String getTexte()
    {
        return this.texte;
    }

    /**
     * Sets estampille.
     *
     * @param d the d
     */
    public void setEstampille(Date d)
    {
        this.estampille = d;
    }

    public String toString()
    {
        return "<" + texte + ":" + estampille + ">";
    }
}
