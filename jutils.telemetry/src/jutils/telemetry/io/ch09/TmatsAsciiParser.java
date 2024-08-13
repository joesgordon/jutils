package jutils.telemetry.io.ch09;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import jutils.core.ValidationException;
import jutils.core.io.IParser;
import jutils.telemetry.data.ch09.Tmats;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TmatsAsciiParser implements IParser<Tmats>, IAsciiReader<Tmats>
{
    /**  */
    private final GeneralInfoAsciiReader generalReader;

    /***************************************************************************
     * 
     **************************************************************************/
    public TmatsAsciiParser()
    {
        this.generalReader = new GeneralInfoAsciiReader();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Tmats parse( String str ) throws ValidationException
    {
        Map<String, String> fields = parseKeyValues( str );

        Tmats setup = new Tmats();

        read( setup, new AsciiStore( fields ) );

        return setup;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void read( Tmats setup, AsciiStore store )
    {
        // LogUtils.printDebug( "%s", FieldPrinter.toString( store ) );
        generalReader.read( setup.general, store );
    }

    /***************************************************************************
     * @param str
     * @return
     **************************************************************************/
    private Map<String, String> parseKeyValues( String str )
    {
        Map<String, String> fields = new HashMap<>();
        BufferedReader reader = new BufferedReader( new StringReader( str ) );
        String line;

        try
        {
            while( ( line = reader.readLine() ) != null )
            {
                int colonIdx = line.indexOf( ':' );
                int semiIdx = line.indexOf( ';' );

                if( colonIdx > 0 && semiIdx > colonIdx )
                {
                    String key = line.substring( 0, colonIdx );
                    String value = line.substring( colonIdx + 1, semiIdx );

                    fields.put( key.trim(), value.trim() );
                }
            }
        }
        catch( IOException ex )
        {
            throw new RuntimeException( ex );
        }

        return fields;
    }
}
