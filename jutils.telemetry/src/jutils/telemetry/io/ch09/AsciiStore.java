package jutils.telemetry.io.ch09;

import java.util.Map;
import java.util.Map.Entry;

import jutils.core.ValidationException;
import jutils.core.io.FieldPrinter;
import jutils.core.io.FieldPrinter.ITierPrinter;
import jutils.core.io.parsers.IntegerParser;

/*******************************************************************************
 * 
 ******************************************************************************/
public class AsciiStore implements ITierPrinter
{
    /**  */
    private final Map<String, String> fields;
    /**  */
    private final IntegerParser intParser;

    /***************************************************************************
     * @param fields
     **************************************************************************/
    public AsciiStore( Map<String, String> fields )
    {
        this.fields = fields;
        this.intParser = new IntegerParser();
    }

    /***************************************************************************
     * @param key
     * @return
     **************************************************************************/
    public String getString( String key )
    {
        return fields.get( key );
    }

    /***************************************************************************
     * @param key
     * @return
     **************************************************************************/
    public Integer getInteger( String key )
    {
        Integer x = null;
        String str = getString( key );

        if( str != null )
        {
            try
            {
                x = intParser.parse( str );
            }
            catch( ValidationException ex )
            {
                x = null;
            }
        }

        return x;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printField( "Count", fields.size() );
        for( Entry<String, String> entry : fields.entrySet() )
        {
            printer.printField( entry.getKey(), entry.getValue() );
        }
    }
}
