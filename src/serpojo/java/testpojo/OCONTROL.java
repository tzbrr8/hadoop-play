package testpojo;

import com.experian.stratman.datasources.runtime.DABean;
import java.util.Date;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * DataBean for Java data source 'OCONTROL'.
 *
 * @author C55333A
 * @version Tue Jan 15 09:23:36 GMT 2019
 */
public class OCONTROL extends DABean
{
    /** serial version id */
    private static final long serialVersionUID = 1L;

    /** cached method lookup */
    private static transient Map methodMap_ = null;

    /** Characteristic ALIAS. */
    private String ALIAS;

    /** Characteristic SIGNATURE. */
    private String SIGNATURE;

    /** Characteristic OBJECTIVE. */
    private String OBJECTIVE;

    /** Characteristic EDITION. */
    private int EDITION;

    /** Characteristic EDITIONDATE. */
    private Date EDITIONDATE;

    /** Characteristic ERRORCOUNT. */
    private String ERRORCOUNT;

    /** Characteristic ERROR. */
    private String[] ERROR = new String[100];

    /** Create a new OCONTROL DataBean. */
    public OCONTROL()
    {
    }

    /**
     * Get the layout name for this data source.
     *
     * @return The layout name for this data source.
     */
    public String getLayout()
    {
        return "OCONTROL";
    }

    /**
     * Get the value of 'ALIAS'.
     * <br><br>
     * DA Logical name: 'ALIAS'
     * <br>
     * DA Physical name: 'ALIAS'
     *
     * @return The value of 'ALIAS'.
     */
    public String getALIAS()
    {
        return ALIAS;
    }

    /**
     * Get the value of 'SIGNATURE'.
     * <br><br>
     * DA Logical name: 'SIGNATURE'
     * <br>
     * DA Physical name: 'SIGNATURE'
     *
     * @return The value of 'SIGNATURE'.
     */
    public String getSIGNATURE()
    {
        return SIGNATURE;
    }

    /**
     * Get the value of 'OBJECTIVE'.
     * <br><br>
     * DA Logical name: 'OBJECTIVE'
     * <br>
     * DA Physical name: 'OBJECTIVE'
     *
     * @return The value of 'OBJECTIVE'.
     */
    public String getOBJECTIVE()
    {
        return OBJECTIVE;
    }

    /**
     * Get the value of 'EDITION'.
     * <br><br>
     * DA Logical name: 'EDITION'
     * <br>
     * DA Physical name: 'EDITION'
     *
     * @return The value of 'EDITION'.
     */
    public int getEDITION()
    {
        return EDITION;
    }

    /**
     * Get the value of 'EDITIONDATE'.
     * <br><br>
     * DA Logical name: 'EDITIONDATE'
     * <br>
     * DA Physical name: 'EDITIONDATE'
     *
     * @return The value of 'EDITIONDATE'.
     */
    public Date getEDITIONDATE()
    {
        return EDITIONDATE;
    }

    /**
     * Get the value of 'ERRORCOUNT'.
     * <br><br>
     * DA Logical name: 'ERRORCOUNT'
     * <br>
     * DA Physical name: 'ERRORCOUNT'
     *
     * @return The value of 'ERRORCOUNT'.
     */
    public String getERRORCOUNT()
    {
        return ERRORCOUNT;
    }

    /**
     * Get the value of 'ERROR'.
     * <br><br>
     * DA Logical name: 'ERROR'
     * <br>
     * DA Physical name: 'ERROR'
     *
     * @return The value of 'ERROR'.
     */
    public String[] getERROR()
    {
        return ERROR;
    }

    /**
     * Get the value of 'ERROR' at the specified index.
     * <br><br>
     * DA Logical name: 'ERROR'
     * <br>
     * DA Physical name: 'ERROR'
     *
     * @param index The index in the array.
     * @return The value of 'ERROR' at the specified index.
     */
    public String getERROR(int index)
    {
        try
        {
            return ERROR[index];
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            if (index >= 0 && index < 100)
            {
                return null;
            }
            else
            {
                throw e;
            }
        }
    }

    /**
     * Set the value of 'ALIAS'.
     * <br><br>
     * DA Logical name: 'ALIAS'
     * <br>
     * DA Physical name: 'ALIAS'
     *
     * @param ALIAS The new value of 'ALIAS'.
     */
    public void setALIAS(String ALIAS)
    {
        this.ALIAS = ALIAS;
    }

    /**
     * Set the value of 'SIGNATURE'.
     * <br><br>
     * DA Logical name: 'SIGNATURE'
     * <br>
     * DA Physical name: 'SIGNATURE'
     *
     * @param SIGNATURE The new value of 'SIGNATURE'.
     */
    public void setSIGNATURE(String SIGNATURE)
    {
        this.SIGNATURE = SIGNATURE;
    }

    /**
     * Set the value of 'OBJECTIVE'.
     * <br><br>
     * DA Logical name: 'OBJECTIVE'
     * <br>
     * DA Physical name: 'OBJECTIVE'
     *
     * @param OBJECTIVE The new value of 'OBJECTIVE'.
     */
    public void setOBJECTIVE(String OBJECTIVE)
    {
        this.OBJECTIVE = OBJECTIVE;
    }

    /**
     * Set the value of 'EDITION'.
     * <br><br>
     * DA Logical name: 'EDITION'
     * <br>
     * DA Physical name: 'EDITION'
     *
     * @param EDITION The new value of 'EDITION'.
     */
    public void setEDITION(int EDITION)
    {
        this.EDITION = EDITION;
    }

    /**
     * Set the value of 'EDITIONDATE'.
     * <br><br>
     * DA Logical name: 'EDITIONDATE'
     * <br>
     * DA Physical name: 'EDITIONDATE'
     *
     * @param EDITIONDATE The new value of 'EDITIONDATE'.
     */
    public void setEDITIONDATE(Date EDITIONDATE)
    {
        this.EDITIONDATE = EDITIONDATE;
    }

    /**
     * Set the value of 'ERRORCOUNT'.
     * <br><br>
     * DA Logical name: 'ERRORCOUNT'
     * <br>
     * DA Physical name: 'ERRORCOUNT'
     *
     * @param ERRORCOUNT The new value of 'ERRORCOUNT'.
     */
    public void setERRORCOUNT(String ERRORCOUNT)
    {
        this.ERRORCOUNT = ERRORCOUNT;
    }

    /**
     * Set the value of 'ERROR'.
     * <br><br>
     * DA Logical name: 'ERROR'
     * <br>
     * DA Physical name: 'ERROR'
     *
     * @param ERROR The new value of 'ERROR'.
     */
    public void setERROR(String[] ERROR)
    {
        this.ERROR = ERROR;
    }

    /**
     * Set the value of 'ERROR' at the specified index.
     * <br><br>
     * DA Logical name: 'ERROR'
     * <br>
     * DA Physical name: 'ERROR'
     *
     * @param index The index in the array.
     * @param ERROR The new value of 'ERROR' at the specified index.
     */
    public void setERROR(int index, String ERROR)
    {
        if (this.ERROR.length <= index)
        {
            String[] tempArray = new String[100];
            System.arraycopy(this.ERROR, 0, tempArray, 0, this.ERROR.length);
            this.ERROR = tempArray;
        }
        this.ERROR[index] = ERROR;
    }

    /**
     * @see DABean#addMethod(String, Method)
     */
    protected void addMethod(String name, Method method)
    {
         if(methodMap_ == null)
         {
             createCache();
         }
         methodMap_.put(name, method);
     }

    /**
     * create the method cache
     */
    private synchronized void createCache()
    {
        methodMap_ = Collections.synchronizedMap(new HashMap());
    }

    /**
     * @see DABean#lookupMethod(String)
     */
    protected Method lookupMethod(String name)
    {
        if(methodMap_ == null)
        {
             createCache();
        }
        return (Method)methodMap_.get(name);
    }
}
