package jutils.telemetry.ch10;

import jutils.core.INamedValue;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum ChecksumSize implements INamedValue
{
    /**  */
    NO_CHECKSUM( 0, 0, "No Checksum (0-bit)" ),
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
     * @param size
     * @param name
     **************************************************************************/
    private ChecksumSize( int value, int size, String name )
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
    public static ChecksumSize fromValue( int value )
    {
        return INamedValue.fromValue( value, values(), null );
    }

    /***************************************************************************
     * @param value
     * @return
     **************************************************************************/
    public static ChecksumSize fromValue( byte value )
    {
        return fromValue( value & 0xFF );
    }
}
