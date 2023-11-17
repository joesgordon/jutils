package jutils.core.time;

import jutils.core.ValidationException;
import jutils.core.io.IParser;
import jutils.core.io.parsers.LongParser;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MicrosecondsParser implements IParser<Long>
{
    /**  */
    private final LongParser lv;

    /***************************************************************************
     * 
     **************************************************************************/
    public MicrosecondsParser()
    {
        this( null, null );
    }

    /***************************************************************************
     * @param min
     * @param max
     **************************************************************************/
    public MicrosecondsParser( Long min, Long max )
    {
        this.lv = new LongParser( min, max );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Long parse( String text ) throws ValidationException
    {
        int dot = text.indexOf( '.' );
        String seconds = text;

        if( text.isEmpty() )
        {
            throw new ValidationException( "Empty string" );
        }

        if( dot > -1 )
        {
            seconds = text.substring( 0, dot );
            String fraction = text.substring( dot + 1 );

            if( fraction.length() > 6 )
            {
                throw new ValidationException(
                    "Too many fractional digits. Expected <= 6, found " +
                        fraction.length() );
            }

            for( int i = fraction.length(); i < 6; i++ )
            {
                fraction += "0";
            }
            seconds += fraction;
        }
        else
        {
            seconds += "000000";
        }

        long micros = lv.parse( seconds );

        // LogUtils.printDebug( "parsed Long: " + micros );

        return micros;
    }
}
