package org.jutils.core.pcap;

import org.jutils.core.INamedItem;

/*******************************************************************************
 * <a href="https://en.wikipedia.org/wiki/List_of_IP_protocol_numbers">Wikipedia
 * List</a>
 ******************************************************************************/
public enum Protocol implements INamedItem
{
    /**  */
    TCP( 0x06, "TCP" ),
    /**  */
    UDP( 0x11, "UDP" ),
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
    private Protocol( int id, String name )
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
    public static Protocol fromId( int id )
    {
        for( Protocol v : values() )
        {
            if( v.id == id )
            {
                return v;
            }
        }

        return UNKNOWN;
    }

}
