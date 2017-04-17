package question2;

import java.util.Date;
import java.io.Serializable;

/**
 * The type Rendez vous.
 */
public class RendezVous implements Serializable
{
    private Date date;
    private String motif;

    /**
     * The constant MAINTENANT.
     */
    public static final RendezVous MAINTENANT = new RendezVous(new Date(System.currentTimeMillis()), "urgent ...");

    /**
     * Instantiates a new Rendez vous.
     *
     * @param date  the date
     * @param motif the motif
     */
    public RendezVous(Date date, String motif)
    {
        this.date = date;
        this.motif = motif;
    }

    /**
     * Gets date.
     *
     * @return the date
     */
    public Date getDate()
    {
        return this.date;
    }

    /**
     * Gets motif.
     *
     * @return the motif
     */
    public String getMotif()
    {
        return this.motif;
    }

    public String toString()
    {
        return "[" + getDate() + ":" + getMotif() + "]";
    }

    public boolean equals(Object obj)
    {
        RendezVous r = (RendezVous) obj;
        return getDate().equals(r.getDate());
    }
}
