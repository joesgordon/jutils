package jutils.telemetry.data.ch10.streaming;

import jutils.core.INamedValue;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum StreamingFormat implements INamedValue
{
    /**  */
    RESERVED( 0x00, "Reserved" ),
    /**  */
    FORMAT_1( 0x01, "Format 1" ),
    /**  */
    FORMAT_2( 0x01, "Format 2" ),
    /**  */
    FORMAT_3( 0x01, "Format 3" ),;

    /**  */
    public final int value;
    /**  */
    public final String name;

    /***************************************************************************
     * @param value
     * @param name
     **************************************************************************/
    private StreamingFormat( int value, String name )
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
    public static StreamingFormat fromValue( byte value )
    {
        return fromValue( value & 0xFF );
    }

    /***************************************************************************
     * @param value
     * @return
     **************************************************************************/
    public static StreamingFormat fromValue( int value )
    {
        return INamedValue.fromValue( value, values(), null );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public byte getByteValue()
    {
        return ( byte )value;
    }
}
