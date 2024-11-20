package jutils.core.io.parsers;

import jutils.core.ValidationException;
import jutils.core.io.IParser;
import jutils.core.net.IpAddress;
import jutils.core.net.IpAddress.IpVersion;

/*******************************************************************************
 * 
 ******************************************************************************/
public class IpAddressParser implements IParser<IpAddress>
{
    /**  */
    private final IpVersion version;
    /**  */
    private final Ip4AddressParser ip4Parser;
    /**  */
    private final Ip6AddressParser ip6Parser;

    /***************************************************************************
     * 
     **************************************************************************/
    public IpAddressParser()
    {
        this( null );
    }

    /***************************************************************************
     * @param version
     **************************************************************************/
    public IpAddressParser( IpVersion version )
    {
        this.version = version;
        this.ip4Parser = new Ip4AddressParser();
        this.ip6Parser = new Ip6AddressParser();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IpAddress parse( String str ) throws ValidationException
    {
        boolean canBeAny = version == null;
        boolean canBe4 = canBeAny || version == IpVersion.IPV4;
        boolean canBe6 = canBeAny || version == IpVersion.IPV6;

        if( canBe4 && str.contains( "." ) )
        {
            return ip4Parser.parse( str );
        }
        else if( canBe6 && str.contains( ":" ) )
        {
            return ip6Parser.parse( str );
        }

        if( canBeAny )
        {
            throw new ValidationException(
                "An IP address must contain either a period or a colon" );
        }
        else if( canBe4 )
        {
            throw new ValidationException(
                "An IP address must contain a period" );
        }

        throw new ValidationException( "An IP address must contain a colon" );
    }
}
