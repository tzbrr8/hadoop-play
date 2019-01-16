package testpojo;

import com.experian.stratman.datasources.runtime.DABean;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * DataBean for Java data source 'LAYOUT'.
 *
 * @author C55333A
 * @version Tue Jan 15 09:23:36 GMT 2019
 */
public class LAYOUT extends DABean
{
    /** serial version id */
    private static final long serialVersionUID = 1L;

    /** cached method lookup */
    private static transient Map methodMap_ = null;

    /** Characteristic Classes0. */
    private String classes0;

    /** Characteristic Classes1. */
    private String classes1;

    /** Characteristic Score0. */
    private double score0;

    /** Characteristic Score1. */
    private double score1;

    /** Characteristic Addresstime. */
    private double addresstime;

    /** Characteristic Age. */
    private double age;

    /** Characteristic Banktime. */
    private double banktime;

    /** Characteristic Income. */
    private double income;

    /** Create a new LAYOUT DataBean. */
    public LAYOUT()
    {
    }

    /**
     * Get the layout name for this data source.
     *
     * @return The layout name for this data source.
     */
    public String getLayout()
    {
        return "LAYOUT";
    }

    /**
     * Get the value of 'Classes0'.
     * <br><br>
     * DA Logical name: 'CLASSES_0'
     * <br>
     * DA Physical name: 'Classes0'
     *
     * @return The value of 'Classes0'.
     */
    public String getClasses0()
    {
        return classes0;
    }

    /**
     * Get the value of 'Classes1'.
     * <br><br>
     * DA Logical name: 'CLASSES_1'
     * <br>
     * DA Physical name: 'Classes1'
     *
     * @return The value of 'Classes1'.
     */
    public String getClasses1()
    {
        return classes1;
    }

    /**
     * Get the value of 'Score0'.
     * <br><br>
     * DA Logical name: 'SCORE_0'
     * <br>
     * DA Physical name: 'Score0'
     *
     * @return The value of 'Score0'.
     */
    public double getScore0()
    {
        return score0;
    }

    /**
     * Get the value of 'Score1'.
     * <br><br>
     * DA Logical name: 'SCORE_1'
     * <br>
     * DA Physical name: 'Score1'
     *
     * @return The value of 'Score1'.
     */
    public double getScore1()
    {
        return score1;
    }

    /**
     * Get the value of 'Addresstime'.
     * <br><br>
     * DA Logical name: 'address_time'
     * <br>
     * DA Physical name: 'Addresstime'
     *
     * @return The value of 'Addresstime'.
     */
    public double getAddresstime()
    {
        return addresstime;
    }

    /**
     * Get the value of 'Age'.
     * <br><br>
     * DA Logical name: 'age'
     * <br>
     * DA Physical name: 'Age'
     *
     * @return The value of 'Age'.
     */
    public double getAge()
    {
        return age;
    }

    /**
     * Get the value of 'Banktime'.
     * <br><br>
     * DA Logical name: 'bank_time'
     * <br>
     * DA Physical name: 'Banktime'
     *
     * @return The value of 'Banktime'.
     */
    public double getBanktime()
    {
        return banktime;
    }

    /**
     * Get the value of 'Income'.
     * <br><br>
     * DA Logical name: 'income'
     * <br>
     * DA Physical name: 'Income'
     *
     * @return The value of 'Income'.
     */
    public double getIncome()
    {
        return income;
    }

    /**
     * Set the value of 'Classes0'.
     * <br><br>
     * DA Logical name: 'CLASSES_0'
     * <br>
     * DA Physical name: 'Classes0'
     *
     * @param classes0 The new value of 'Classes0'.
     */
    public void setClasses0(String classes0)
    {
        this.classes0 = classes0;
    }

    /**
     * Set the value of 'Classes1'.
     * <br><br>
     * DA Logical name: 'CLASSES_1'
     * <br>
     * DA Physical name: 'Classes1'
     *
     * @param classes1 The new value of 'Classes1'.
     */
    public void setClasses1(String classes1)
    {
        this.classes1 = classes1;
    }

    /**
     * Set the value of 'Score0'.
     * <br><br>
     * DA Logical name: 'SCORE_0'
     * <br>
     * DA Physical name: 'Score0'
     *
     * @param score0 The new value of 'Score0'.
     */
    public void setScore0(double score0)
    {
        this.score0 = score0;
    }

    /**
     * Set the value of 'Score1'.
     * <br><br>
     * DA Logical name: 'SCORE_1'
     * <br>
     * DA Physical name: 'Score1'
     *
     * @param score1 The new value of 'Score1'.
     */
    public void setScore1(double score1)
    {
        this.score1 = score1;
    }

    /**
     * Set the value of 'Addresstime'.
     * <br><br>
     * DA Logical name: 'address_time'
     * <br>
     * DA Physical name: 'Addresstime'
     *
     * @param addresstime The new value of 'Addresstime'.
     */
    public void setAddresstime(double addresstime)
    {
        this.addresstime = addresstime;
    }

    /**
     * Set the value of 'Age'.
     * <br><br>
     * DA Logical name: 'age'
     * <br>
     * DA Physical name: 'Age'
     *
     * @param age The new value of 'Age'.
     */
    public void setAge(double age)
    {
        this.age = age;
    }

    /**
     * Set the value of 'Banktime'.
     * <br><br>
     * DA Logical name: 'bank_time'
     * <br>
     * DA Physical name: 'Banktime'
     *
     * @param banktime The new value of 'Banktime'.
     */
    public void setBanktime(double banktime)
    {
        this.banktime = banktime;
    }

    /**
     * Set the value of 'Income'.
     * <br><br>
     * DA Logical name: 'income'
     * <br>
     * DA Physical name: 'Income'
     *
     * @param income The new value of 'Income'.
     */
    public void setIncome(double income)
    {
        this.income = income;
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
