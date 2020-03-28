package org.jutils.core.io.parsers;

import org.jutils.core.ValidationException;
import org.jutils.core.io.IParser;

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
