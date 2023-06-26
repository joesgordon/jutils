package org.jutils.serial;

import org.jutils.core.INamedItem;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum StopBits implements INamedItem
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
}
