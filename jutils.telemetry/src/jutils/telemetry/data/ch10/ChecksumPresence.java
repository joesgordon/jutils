package jutils.telemetry.data.ch10;

import jutils.core.INamedValue;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum ChecksumPresence implements INamedValue
{
    /**  */
    NO_CHECKSUM( 0, 0, "No Checksum" ),
    /**  */
    CS_8( 1, 1, "8-bit Checksum" ),
    /**  */
    CS_16( 2, 2, "16-bit Checksum" ),
    /**  */
    CS_32( 3, 4, "32-bit Checksum" ),;

    /**  */
    public final int value;
    /**  */
    public final int size;
    /**  */
    public final String name;

    /***************************************************************************
     * @param value
     * @param name
     **************************************************************************/
    private ChecksumPresence( int value, int size, String name )
    {
        this.value = value;
        this.size = size;
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
