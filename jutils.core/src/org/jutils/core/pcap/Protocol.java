package org.jutils.core.pcap;

import org.jutils.core.INamedValue;

/*******************************************************************************
 * <a href="https://en.wikipedia.org/wiki/List_of_IP_protocol_numbers">Wikipedia
 * List</a>
 ******************************************************************************/
public enum Protocol implements INamedValue
{
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
     * @param id
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
    public static Protocol fromId( int id )
    {
        return INamedValue.fromValue( id, values(), UNKNOWN );
    }
}
