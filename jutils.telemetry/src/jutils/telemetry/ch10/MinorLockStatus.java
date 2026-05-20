package jutils.telemetry.ch10;

import jutils.core.INamedValue;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum MinorLockStatus implements INamedValue
{
    /**  */
    RESERVED0( 0, "Reserved" ),
    /**  */
    RESERVED1( 1, "Reserved" ),
    /**  */
    CHECK( 2, "Check" ),
    /**  */
    LOCKED( 3, "Locked" ),;

    /**  */
    public final int value;
    /**  */
    public final String name;

    /***************************************************************************
     * @param value
     * @param name
     **************************************************************************/
    private MinorLockStatus( int value, String name )
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

    /***************************************************************************
     * @param field
     * @return
     **************************************************************************/
    public static MinorLockStatus fromValue( int field )
    {
        return INamedValue.fromValue( field, values(), null );
    }
}
