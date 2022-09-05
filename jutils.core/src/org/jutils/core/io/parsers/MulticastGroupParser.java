package org.jutils.core.io.parsers;

import org.jutils.core.ValidationException;
import org.jutils.core.io.IParser;
import org.jutils.core.net.IpAddress;
import org.jutils.core.net.IpAddress.IpVersion;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MulticastGroupParser implements IParser<IpAddress>
{
    /**  */
    private static final int [] IP4_MINS = new int[] { 224, 0, 0, 0, };
    /**  */
    private static final int [] IP4_MAXS = new int[] { 239, 255, 255, 255 };

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

        if( address.getVersion() == IpVersion.IPV4 )
        {
            for( int i = 0; i < IpVersion.IPV4.byteCount; i++ )
            {
                int octet = Byte.toUnsignedInt( address.address[i] );

                if( octet < IP4_MINS[i] || octet > IP4_MAXS[i] )
                {
                    String msg = String.format(
                        "Octet %d (%d) is out of range for Multicast groups: %d <= o <= %d",
                        i + 1, octet, IP4_MINS[i], IP4_MAXS[i] );
                    throw new ValidationException( msg );
                }
            }
        }
        else
        {
            int prefix = ( address.address[0] & 0xFF ) << 8;
            prefix |= address.address[1] & 0xFF;

            if( prefix != 0xFF00 )
            {
                String msg = String.format(
                    "Invalid prefix for IPv6 multicast; Expected FF00::/8" );
                throw new ValidationException( msg );
            }
        }

        return address;
    }
}
