package jutils.telemetry.ch10;

import jutils.core.INamedValue;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum ChecksumPresence implements INamedValue
{
    /**  */
    NO_CHECKSUM( 0, "No Checksum" ),
    /**  */
    CS_8( 1, "8-bit Checksum" ),
    /**  */
    CS_16( 2, "16-bit Checksum" ),
    /**  */
    CS_32( 3, "32-bit Checksum" ),;

    /**  */
    public final int value;
    /**  */
    public final String name;

    /***************************************************************************
     * @param value
     * @param name
     **************************************************************************/
    private ChecksumPresence( int value, String name )
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
     * @param value
     * @return
     **************************************************************************/
    public static ChecksumPresence fromValue( int value )
    {
        return INamedValue.fromValue( value, values(), null );
    }

    /***************************************************************************
     * @param value
     * @return
     **************************************************************************/
    public static ChecksumPresence fromValue( byte value )
    {
        return fromValue( value & 0xFF );
    }
}
