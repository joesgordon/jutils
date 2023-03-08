package org.jutils.core.io.parsers;

import org.jutils.core.ValidationException;
import org.jutils.core.io.IParser;
import org.jutils.core.net.EndPoint;
import org.jutils.core.net.IpAddress;

/***************************************************************************
 *
 **************************************************************************/
public class EndPointParser implements IParser<EndPoint>
{
    /**  */
    private final IParser<IpAddress> addressParser;
    /**  */
    private final IParser<Integer> portParser;

    /***************************************************************************
     * 
     **************************************************************************/
    public EndPointParser()
    {
        this( new IpAddressParser() );
    }

    /***************************************************************************
     * @param addressParser
     **************************************************************************/
    public EndPointParser( IParser<IpAddress> addressParser )
    {
        this.addressParser = addressParser;
        this.portParser = new IntegerParser( 0, 65535 );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public EndPoint parse( String str ) throws ValidationException
    {
        int index = str.lastIndexOf( ':' );

        if( index < 0 )
        {
            throw new ValidationException( "No port delimeter found ':'" );
        }

        String addrStr = str.substring( 0, index );
        String portStr = str.substring( index + 1 );

        IpAddress ip = addressParser.parse( addrStr );
        int port = portParser.parse( portStr );

        EndPoint end = new EndPoint( ip, port );

        return end;
    }
}
