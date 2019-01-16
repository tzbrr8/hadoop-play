package testpojo;

import com.experian.stratman.datasources.runtime.IBean;
import com.experian.stratman.datasources.runtime.IData;
import java.io.Serializable;

/**
 * Data container for Java data sources in objective 'Btree1k'.
 *
 * @author C55333A
 * @version Tue Jan 15 09:23:36 GMT 2019
 */
public class Btree1k implements IBean, Serializable
{
    /**serial version id */
    private static final long serialVersionUID = 1L;

    /* Data source OCONTROL. */
    private OCONTROL oCONTROL = new OCONTROL();

    /** Data source 'LAYOUT'. */
    private LAYOUT lAYOUT = new LAYOUT();

    /** Create a new Btree1k data container. */
    public Btree1k()
    {
    }

    /**
     * Sets all the data sources from the IData array into the
     * bean.
     * <br><br>
     * The IData array is required to interface with the Decision Agent.
     *
     * @param idata All the data sources in an IData array.
     */
    public void fromIDataArray(IData[] idata)
    {
        oCONTROL = (OCONTROL)idata[0];
        lAYOUT = (LAYOUT)idata[1];
    }

    /**
     * Get the 'OCONTROL' data source.
     *
     * @return The 'OCONTROL' data source.
     */
    public OCONTROL getOCONTROL()
    {
        return oCONTROL;
    }

    /**
     * Get the 'LAYOUT' data source.
     *
     * @return The 'LAYOUT' data source.
     */
    public LAYOUT getLAYOUT()
    {
        return lAYOUT;
    }

    /**
     * Set the 'OCONTROL' data source.
     *
     * @param ocontrol The new value of the 'OCONTROL' data source.
     */
    public void setOCONTROL(OCONTROL oCONTROL)
    {
        this.oCONTROL = oCONTROL;
    }

    /**
     * Set the 'LAYOUT' data source.
     *
     * @param lAYOUT The new value of the 'LAYOUT' data source.
     */
    public void setLAYOUT(LAYOUT lAYOUT)
    {
        this.lAYOUT = lAYOUT;
    }

    /**
     * Gets all the data sources in the bean as an IData array.
     *
     * @return All the data sources in the bean as an IData array.
     */
    public IData[] toIDataArray()
    {
        return new IData[] {oCONTROL, lAYOUT};
    }

}
