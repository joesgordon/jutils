package org.jutils.io.parsers;

import org.jutils.ValidationException;
import org.jutils.io.IParser;
import org.jutils.net.Ip4Address;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MulticastGroupParser implements IParser<Ip4Address>
{
    /**  */
    private static final int [] MINS = new int[] { 224, 0, 0, 0, };
    /**  */
    private static final int [] MAXS = new int[] { 239, 255, 255, 255 };

    /**  */
    private final Ip4AddressParser parser;

    /***************************************************************************
     * 
     **************************************************************************/
    public MulticastGroupParser()
    {
        this.parser = new Ip4AddressParser();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Ip4Address parse( String str ) throws ValidationException
    {
        Ip4Address address = parser.parse( str );

        for( int i = 0; i < address.address.length; i++ )
        {
            int octet = Byte.toUnsignedInt( address.address[i] );

            if( octet < MINS[i] || octet > MAXS[i] )
            {
                String msg = String.format(
                    "Octet %d (%d) is out of range for Multicast groups: %d <= o <= %d",
                    i + 1, octet, MINS[i], MAXS[i] );
                throw new ValidationException( msg );
            }
        }

        return address;
    }
}
