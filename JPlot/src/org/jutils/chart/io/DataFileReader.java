package org.jutils.chart.io;

import java.io.*;
import java.util.ArrayList;

import org.jutils.ValidationException;
import org.jutils.chart.data.DefaultSeries;
import org.jutils.chart.data.XYPoint;
import org.jutils.chart.model.ISeriesData;
import org.jutils.io.IOUtils;
import org.jutils.io.IReader;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DataFileReader implements IReader<ISeriesData<?>, File>
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public DefaultSeries read( File f ) throws IOException, ValidationException
    {
        ArrayList<XYPoint> points = new ArrayList<>();
        String line;
        XYPoint point;

        // LogUtils.printDebug( "Reading " + f.getName() );

        try( InputStream is = new FileInputStream( f );
             Reader r = new InputStreamReader( is, IOUtils.US_ASCII );
             BufferedReader reader = new BufferedReader( r ) )
        {
            while( ( line = reader.readLine() ) != null )
            {
                point = DataLineReader.read( line );

                if( point != null )
                {
                    points.add( point );
                }
            }
        }

        // LogUtils.printDebug( "Done Reading " + f.getName() );

        // XYPoint [] array = points.toArray( new XYPoint[points.size()] );
        //
        // return new ArraySeries( array );

        return new DefaultSeries( points );
    }
}
