package jutils.plot.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import jutils.core.ValidationException;
import jutils.core.io.IOUtils;
import jutils.core.io.IReader;
import jutils.plot.data.DefaultSeries;
import jutils.plot.data.XYPoint;
import jutils.plot.model.ISeriesData;

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
             Reader r = new InputStreamReader( is, IOUtils.get8BitEncoding() );
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
