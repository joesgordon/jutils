package jutils.core.iana;

import jutils.core.INamedValue;

/*******************************************************************************
 * <a href="https://en.wikipedia.org/wiki/EtherType">Wiki</a><br /> <a
 * href="https://www.iana.org/assignments/ieee-802-numbers/ieee-802-numbers.xhtml">IANA</a>
 ******************************************************************************/
public enum EtherType implements INamedValue
{
    /**  */
    IPV4( 0x0800, "IPv4" ),
    /**  */
    ARP( 0x0806, "ARP" ),
    /**  */
    IPV6( 0x86DD, "IPv6" ),
    /**  */
    UNKNOWN( 0xFF, "Unknown" );

    /** Length if less than this. */
    public static final int MIN_TYPE = 0x0600;

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
