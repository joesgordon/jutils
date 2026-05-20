package jutils.core.net;

import jutils.core.INamedValue;

/*******************************************************************************
 *
 ******************************************************************************/
public enum IpVersion implements INamedValue
{
    /** IP version 4 */
    IPV4( NetUtils.IPV4_SIZE, "IPv4" ),
    /** IP version 6 */
    IPV6( NetUtils.IPV6_SIZE, "IPv6" );

    /** The number of bytes in this IP version. */
    public final int byteCount;
    /** The name of this IP version. */
    public final String name;

    /**
     * @param byteCount the number of bytes in this IP version.
     * @param name the name of this IP version.
     */
    private IpVersion( int byteCount, String name )
    {
        this.byteCount = byteCount;
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName()
    {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getValue()
    {
        return byteCount;
    }

    /**
     * @param id
     * @return
     */
    public static IpVersion fromId( byte id )
    {
        return INamedValue.fromValue( id, values(), null );
    }
}
