package org.jutils.chart.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jutils.chart.data.XYPoint;
import org.jutils.chart.model.ChartOptions.PointRemovalMethod;
import org.jutils.core.io.FilePrintStream;
import org.jutils.core.io.IOUtils;
import org.jutils.chart.model.ISeriesData;

/*******************************************************************************
 * Writes {@link ISeriesData} to a file (or from one file to another).
 ******************************************************************************/
public class FilteredWriter
{
    /***************************************************************************
     * Writes the series data to the specified file in a tab-delimited format.
     * @param toFile the file to write the series data to.
     * @param data the series data to be written.
     * @param removalMethod specifies how hidden points are handled.
     * @throws FileNotFoundException if a problem occurs opening the file for
     * writing.
     * @throws IOException if a problem occurs when writing the file.
     **************************************************************************/
    public static void write( File toFile, ISeriesData<?> data,
        PointRemovalMethod removalMethod )
        throws FileNotFoundException, IOException
    {
        try( FilePrintStream stream = new FilePrintStream( toFile ) )
        {
            for( int i = 0; i < data.getCount(); i++ )
            {
                if( data.isHidden( i ) )
                {
                    if( removalMethod != PointRemovalMethod.DELETE )
                    {
                        stream.println(
                            data.getX( i ) + "\t" + removalMethod.value );
                    }
                }
                else
                {
                    stream.println( data.getX( i ) + "\t" + data.getY( i ) );
                }
            }
        }
    }

    /***************************************************************************
     * Writes the series data to the specified file by copying the data from the
     * original file (retains non-data columns of data).
     * @param fromFile The original data file.
     * @param toFile the file to write the series data to.
     * @param data the series data to be written.
     * @param removalMethod specifies how hidden points are handled.
     * @throws FileNotFoundException if a problem occurs opening the file for
     * writing.
     * @throws IOException if a problem occurs when writing the file.
     **************************************************************************/
    public static void write( File fromFile, File toFile, ISeriesData<?> data,
        PointRemovalMethod removalMethod )
        throws FileNotFoundException, IOException
    {
        File temp = fromFile;
        boolean overwrite = fromFile.equals( toFile );

        if( overwrite )
        {
            temp = File.createTempFile( "SeriesData_", ".txt" );
            Files.copy( fromFile.toPath(), temp.toPath(),
                StandardCopyOption.REPLACE_EXISTING );
        }

        try( FileInputStream fis = new FileInputStream( temp );
             Reader r = new InputStreamReader( fis, IOUtils.get8BitEncoding() );
             BufferedReader reader = new BufferedReader( r ) )
        {
            try( FilePrintStream stream = new FilePrintStream( toFile ) )
            {
                String line = null;
                int idx = 0;
                XYPoint point = null;

                Pattern p = Pattern.compile( "(.+)\\s\\S+\\S*$" );

                while( ( line = reader.readLine() ) != null )
                {
                    point = DataLineReader.read( line );

                    if( point != null )
                    {
                        if( data.isHidden( idx ) )
                        {
                            if( removalMethod != PointRemovalMethod.DELETE )
                            {
                                Matcher m = p.matcher( line );

                                if( m.matches() )
                                {
                                    stream.println(
                                        m.group( 1 ) + removalMethod.value );
                                }

                                // stream.println( line );
                            }
                        }
                        else
                        {
                            stream.println( line );
                        }

                        idx++;
                    }
                    else
                    {
                        stream.println( line );
                    }
                }
            }
        }

        if( overwrite )
        {
            if( !temp.delete() )
            {
                throw new IOException(
                    "Cannot delete temporary file: " + temp.getAbsolutePath() );
            }
        }
    }
}
