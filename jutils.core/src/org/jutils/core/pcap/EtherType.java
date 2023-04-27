package org.jutils.core.pcap;

import org.jutils.core.INamedValue;

/*******************************************************************************
 * <a href="https://en.wikipedia.org/wiki/List_of_IP_protocol_numbers">Wikipedia
 * List</a>
 ******************************************************************************/
public enum EtherType implements INamedValue
{
    /**  */
    IPV4( 0x0800, "IPv4" ),
    /**  */
    IPV6( 0x86DD, "IPv6" ),
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
    private EtherType( int value, String name )
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
    public static EtherType fromId( int id )
    {
        return INamedValue.fromValue( id, values(), UNKNOWN );
    }
}
