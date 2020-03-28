package org.jutils.core.io.parsers;

import java.util.List;

import org.jutils.core.ValidationException;
import org.jutils.core.io.IParser;
import org.jutils.core.ui.hex.HexUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HexBytesParser implements IParser<byte []>
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public byte [] parse( String text ) throws ValidationException
    {
        List<Byte> bytes;
        byte [] bytearray = null;

        text = text.replaceAll( "\\s", "" );

        try
        {
            bytes = HexUtils.fromHexString( text );
        }
        catch( NumberFormatException ex )
        {
            throw new ValidationException( ex.getMessage(), ex );
        }

        if( bytes.size() > 0 )
        {
            bytearray = new byte[bytes.size()];

            for( int i = 0; i < bytes.size(); i++ )
            {
                bytearray[i] = bytes.get( i );
            }
        }
        else
        {
            throw new ValidationException( "Empty string." );
        }

        return bytearray;
    }
}
