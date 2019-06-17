package org.jutils.io.parsers;

import org.jutils.ValidationException;
import org.jutils.io.IParser;

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
