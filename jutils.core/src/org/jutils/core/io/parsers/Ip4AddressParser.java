package org.jutils.core.io.parsers;

import org.jutils.core.ValidationException;
import org.jutils.core.io.IParser;
import org.jutils.core.net.Ip4Address;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Ip4AddressParser implements IParser<Ip4Address>
{
    /**  */
    private final IntegerParser fieldParser;

    /***************************************************************************
     * 
     **************************************************************************/
    public Ip4AddressParser()
    {
        fieldParser = new IntegerParser( 0, 255 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Ip4Address parse( String str ) throws ValidationException
    {
        Ip4Address address = new Ip4Address();
        String[] fields = str.split( "\\.", -1 );

        if( fields.length > 4 )
        {
            throw new ValidationException(
                "Too many fields in an IP4 address: " + fields.length );
        }
        else if( fields.length < 4 )
        {
            throw new ValidationException(
                "Too few fields in an IP4 address: " + fields.length );
        }

        for( int i = 0; i < fields.length; i++ )
        {
            try
            {
                int num = fieldParser.parse( fields[i] );

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
