package org.jutils.plot.io;

import java.util.List;

import org.jutils.core.Utils;
import org.jutils.plot.data.XYPoint;
import org.jutils.plot.model.ChartOptions;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DataLineReader
{
    /***************************************************************************
     * @param line
     * @return
     **************************************************************************/
    public static XYPoint read( String line )
    {
        double x;
        double y;

        line = line.trim();

        if( line.isEmpty() || line.charAt( 0 ) == '%' )
        {
            return null;
        }

        List<String> values = Utils.splitSkip( line );

        if( values.size() < 2 )
        {
            return null;
        }

        try
        {
            x = Double.parseDouble( values.get( 0 ) );

            String val = values.get( values.size() - 1 );

            if( val.equals( ChartOptions.NINE_NINES_SENTINEL_VALUE ) )
            {
                y = Double.NaN;
            }
            else
            {
                y = Double.parseDouble( val );
            }
        }
        catch( NumberFormatException ex )
        {
            return null;
        }

        return new XYPoint( x, y );
    }
}
