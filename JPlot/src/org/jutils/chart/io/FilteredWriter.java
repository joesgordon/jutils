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

import org.jutils.chart.data.XYPoint;
import org.jutils.chart.model.ISeriesData;
import org.jutils.io.FilePrintStream;
import org.jutils.io.IOUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FilteredWriter
{
    /***************************************************************************
     * @param fromFile
     * @param toFile
     * @param data
     * @throws FileNotFoundException
     * @throws IOException
     **************************************************************************/
    public static void write( File fromFile, File toFile, ISeriesData<?> data )
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
             Reader r = new InputStreamReader( fis, IOUtils.US_ASCII );
             BufferedReader reader = new BufferedReader( r ) )
        {
            try( FilePrintStream stream = new FilePrintStream( toFile ) )
            {
                String line = null;
                int idx = 0;
                XYPoint point = null;

                while( ( line = reader.readLine() ) != null )
                {
                    point = DataLineReader.read( line );

                    if( point != null )
                    {
                        if( !data.isHidden( idx ) )
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

    /***************************************************************************
     * @param toFile
     * @param data
     * @throws FileNotFoundException
     * @throws IOException
     **************************************************************************/
    public static void write( File toFile, ISeriesData<?> data )
        throws FileNotFoundException, IOException
    {
        try( FilePrintStream stream = new FilePrintStream( toFile ) )
        {
            for( int i = 0; i < data.getCount(); i++ )
            {
                if( !data.isHidden( i ) )
                {
                    stream.println( data.getX( i ) + "\t" + data.getY( i ) );
                }
            }
        }
    }
}
