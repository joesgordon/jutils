package jutils.core.pcap.ethernet;

import jutils.core.INamedValue;

/*******************************************************************************
 * <a href="https://en.wikipedia.org/wiki/List_of_IP_protocol_numbers">Wikipedia
 * List</a>
 ******************************************************************************/
public enum Protocol implements INamedValue
{
    /**  */
    HOPOPT( 0x00, "IPv6 Hop-by-Hop Option" ),
    /**  */
    ICMP( 0x01, "Internet Control Message Protocol" ),
    /**  */
    TCP( 0x06, "TCP" ),
    /**  */
    UDP( 0x11, "UDP" ),
    /**  */
    UNKNOWN( 0xFF, "Unknown" );

    /**  */
    public final int value;
    /**  */
    public final String name;

    /***************************************************************************
     * @param value
     * @param name
     **************************************************************************/
    private Protocol( int value, String name )
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

    @Override
    public int getValue()
    {
        return value;
    }

    /***************************************************************************
     * @param id
     * @return
     **************************************************************************/
    public static Protocol fromValue( int id )
    {
        return INamedValue.fromValue( id, values(), UNKNOWN );
    }
}
