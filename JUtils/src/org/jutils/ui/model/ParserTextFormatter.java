package org.jutils.ui.model;

import java.text.ParseException;

import javax.swing.JFormattedTextField.AbstractFormatter;

import org.jutils.ValidationException;
import org.jutils.io.IParser;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class ParserTextFormatter<T> extends AbstractFormatter
{
    /**  */
    private static final long serialVersionUID = 1230950769382739188L;
    /**  */
    private final IParser<T> parser;

    /***************************************************************************
     * @param parser
     **************************************************************************/
    public ParserTextFormatter( IParser<T> parser )
    {
        this.parser = parser;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public T stringToValue( String text ) throws ParseException
    {
        try
        {
            return parser.parse( text );
        }
        catch( ValidationException ex )
        {
            throw new ParseException( ex.getMessage(), -1 );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String valueToString( Object value ) throws ParseException
    {
        // LogUtils.printDebug( "value class: " +
        // ( value == null ? "null" : value.getClass().getName() ) );
        return value == null ? "" : value.toString();
    }
}
