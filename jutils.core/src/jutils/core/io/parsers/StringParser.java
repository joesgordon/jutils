package jutils.core.io.parsers;

import jutils.core.ValidationException;
import jutils.core.io.IParser;

/*******************************************************************************
 * 
 ******************************************************************************/
public class StringParser implements IParser<String>
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String parse( String str ) throws ValidationException
    {
        return str;
    }
}
