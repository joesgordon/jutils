package jutils.core.io.parsers;

import jutils.core.ValidationException;
import jutils.core.io.IParser;
import jutils.core.net.IpAddress;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MulticastGroupParser implements IParser<IpAddress>
{
    /**  */
    private final IpAddressParser parser;

    /***************************************************************************
     * 
     **************************************************************************/
    public MulticastGroupParser()
    {
        this.parser = new IpAddressParser();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IpAddress parse( String str ) throws ValidationException
    {
        IpAddress address = parser.parse( str );

        if( !address.isMulticast() )
        {
            String msg = null;

            switch( address.getVersion() )
            {
                case IPV4:
                    msg = String.format(
                        "Invalid address %s; not between multicast groups %s to %s",
                        str,
                        new IpAddress( IpAddress.IP4_MIN_GROUP ).toString(),
                        new IpAddress( IpAddress.IP4_MAX_GROUP ).toString() );
                    break;

                case IPV6:
                    msg = String.format(
                        "Invalid prefix for IPv6 multicast; Expected FF00::/8 in %s",
                        str );
                    break;

                default:
                    break;
            }
            throw new ValidationException( msg );
        }

        return address;
    }
}
