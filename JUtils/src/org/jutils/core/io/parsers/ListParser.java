package org.jutils.core.io.parsers;

import java.util.*;

import org.jutils.core.Utils;
import org.jutils.core.ValidationException;
import org.jutils.core.io.IParser;

/*******************************************************************************
 * Defines a parser to read a list of items.
 ******************************************************************************/
public class ListParser<T> implements IParser<List<T>>
{
    /** The parser to read each item. */
    private final IParser<T> itemParser;
    /** Skips empty entries if {@code true}. */
    private final boolean skip;
    /** The characters used to delimit fields. */
    private final char [] delimeters;

    /***************************************************************************
     * Creates a new list parser that skips empty fields which are separated by
     * commas.
     * @param itemParser
     **************************************************************************/
    public ListParser( IParser<T> itemParser )
    {
        this( itemParser, true, ',' );
    }

    /***************************************************************************
     * Creates a new list parser that skips empty fields according to the
     * provided flag and uses the list of delimiters to separate fields.
     * @param itemParser the parser used to read each item.
     * @param skip skips empty fields if {@code true}.
     * @param delimeters the characters that separate fields.
     **************************************************************************/
    public ListParser( IParser<T> itemParser, boolean skip, char... delimeters )
    {
        this.itemParser = itemParser;
        this.skip = skip;
        this.delimeters = new char[delimeters.length];

        Arrays.copyOf( delimeters, delimeters.length );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public List<T> parse( String str ) throws ValidationException
    {
        List<String> strs = Utils.splitSkip( str, skip, delimeters );
        List<T> items = new ArrayList<>( strs.size() );

        for( String s : strs )
        {
            T item = itemParser.parse( s );

            items.add( item );
        }

        return items;
    }
}
