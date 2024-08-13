package jutils.telemetry.io.ch09.ascii;

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
    /**  */
    private final String prefix;
    /**  */
    private final String suffix;

    /***************************************************************************
     * @param fields
     **************************************************************************/
    public AsciiStore( Map<String, String> fields )
    {
        this( fields, "", "" );
    }

    /***************************************************************************
     * @param fields
     * @param prefix
     * @param suffix
     **************************************************************************/
    private AsciiStore( Map<String, String> fields, String prefix,
        String suffix )
    {
        this.fields = fields;
        this.intParser = new IntegerParser();
        this.prefix = prefix;
        this.suffix = suffix;
    }

    /***************************************************************************
     * @param key
     * @return
     **************************************************************************/
    public String getString( String key )
    {
        return fields.get( prefix + key + suffix );
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

    /***************************************************************************
     * @param prefix
     * @return
     **************************************************************************/
    public AsciiStore createSubstore( String prefix )
    {
        return createSubstore( prefix, "" );
    }

    /***************************************************************************
     * @param prefix
     * @param suffix
     * @return
     **************************************************************************/
    public AsciiStore createSubstore( String prefix, String suffix )
    {
        return new AsciiStore( fields, this.prefix + prefix,
            this.suffix + suffix );
    }
}
