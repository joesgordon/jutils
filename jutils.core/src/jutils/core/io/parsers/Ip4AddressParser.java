package jutils.core.io.parsers;

import java.util.List;

import jutils.core.Utils;
import jutils.core.ValidationException;
import jutils.core.io.IParser;
import jutils.core.net.IpAddress;
import jutils.core.net.IpVersion;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Ip4AddressParser implements IParser<IpAddress>
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IpAddress parse( String str ) throws ValidationException
    {
        IpAddress address = new IpAddress( IpVersion.IPV4 );
        IntegerParser fieldParser = new IntegerParser( 0, 255 );
        List<String> fields = Utils.splitSkip( str, false, '.' );

        if( fields.size() > 4 )
        {
            throw new ValidationException(
                "Too many fields in an IP4 address: " + fields.size() );
        }
        else if( fields.size() == 0 )
        {
            throw new ValidationException(
                "An IP4 address must contain three periods delimiting four octets" );
        }
        else if( fields.size() < 4 )
        {
            throw new ValidationException(
                "Too few fields in an IP4 address: " + fields.size() );
        }

        for( int i = 0; i < fields.size(); i++ )
        {
            try
            {
                int num = fieldParser.parse( fields.get( i ) );

                address.address[i] = ( byte )num;
            }
            catch( ValidationException ex )
            {
                throw new ValidationException(
                    "Cannot parse octect " + ( i + 1 ) + ": " + ex.getMessage(),
                    ex );
            }
        }

        return address;
    }
}
