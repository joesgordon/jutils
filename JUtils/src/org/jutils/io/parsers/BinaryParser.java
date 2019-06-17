package org.jutils.io.parsers;

import org.jutils.ValidationException;
import org.jutils.io.IParser;
import org.jutils.utils.BitArray;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BinaryParser implements IParser<BitArray>
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public BitArray parse( String text ) throws ValidationException
    {
        BitArray bits = new BitArray();

        if( text.isEmpty() )
        {
            throw new ValidationException( "The string is empty" );
        }

        try
        {
            bits.set( text );
        }
        catch( NumberFormatException ex )
        {
            throw new ValidationException( ex.getMessage(), ex );
        }

        return bits;
    }
}
