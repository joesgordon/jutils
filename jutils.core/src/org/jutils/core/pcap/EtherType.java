package org.jutils.core.pcap;

import org.jutils.core.INamedItem;

/*******************************************************************************
 * <a href="https://en.wikipedia.org/wiki/List_of_IP_protocol_numbers">Wikipedia
 * List</a>
 ******************************************************************************/
public enum EtherType implements INamedItem
{
    /**  */
    IPV4( 0x0800, "IPv4" ),
    /**  */
    IPV6( 0x86DD, "IPv6" ),
    /**  */
    UNKNOWN( 0xFF, "Unknown" );

    /**  */
    public final int id;
    /**  */
    public final String name;

    /***************************************************************************
     * @param id
     * @param name
     **************************************************************************/
    private EtherType( int id, String name )
    {
        this.id = id;
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
     * @param id
     * @return
     **************************************************************************/
    public static EtherType fromId( int id )
    {
        for( EtherType v : values() )
        {
            if( v.id == id )
            {
                return v;
            }
        }

        return UNKNOWN;
    }

}
