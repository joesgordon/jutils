package jutils.platform.data;

import jutils.core.INamedValue;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum StopBits implements INamedValue
{
    /**  */
    ONE( 0, "1 Bit" ),
    /**  */
    ONE5( 1, "1.5 Bits" ),
    /**  */
    TWO( 2, "2 Bits" );

    /**  */
    public final int value;
    /**  */
    public final String name;

    /***************************************************************************
     * @param value
     * @param name
     **************************************************************************/
    private StopBits( int value, String name )
    {
        this.value = value;
        this.name = name;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return name;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getValue()
    {
        return value;
    }
}
