package org.jutils.core.io;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

import org.jutils.core.utils.ByteOrdering;
import org.jutils.core.utils.Stopwatch;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MappedBenchmarkMain
{
    /**  */
    private static final long FILE_SIZE = 1024 * 1024 * 1024;
    /**  */
    private static final long REQ_SIZE = 2 * FILE_SIZE;
    /**  */
    private static final int BUF_SIZE = 8 * 1024 * 1024;

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        try
        {
            File mappedFile = File.createTempFile( "mapped", ".bin" );
            File buffedFile = File.createTempFile( "buffed", ".bin" );

            mappedFile.deleteOnExit();
            buffedFile.deleteOnExit();

            if( mappedFile.getFreeSpace() < REQ_SIZE )
            {
                LogUtils.printError(
                    "Not enough free space on the volume containing " +
                        mappedFile + ". " + REQ_SIZE + " required." );
            }

            LogUtils.printDebug( "Creating mapped file." );
            fillFile( mappedFile );
            LogUtils.printDebug( "Creating buffered file." );
            fillFile( buffedFile );

            Stopwatch sw = new Stopwatch();
            long mapElapsed;
            long bufElapsed;

            LogUtils.printDebug( "Reading mapped file." );
            sw.start();
            readMapFile( mappedFile );
            sw.stop();
            mapElapsed = sw.getElapsed();

            LogUtils.printDebug( "Reading buffered file." );
            sw.start();
            readBufFile( buffedFile );
            sw.stop();
            bufElapsed = sw.getElapsed();

            LogUtils.printDebug( "Deleting mapped file: " +
                ( mappedFile.delete() ? "success" : "failure" ) );
            LogUtils.printDebug( "Deleting buffered file: " +
                ( buffedFile.delete() ? "success" : "failure" ) );

            long diff = mapElapsed - bufElapsed;
            String winner = diff == 0 ? "cat"
                : diff < 0 ? "Mapped" : "Buffered";
            float percent = ( float )( Math.abs( diff ) /
                ( double )Math.max( mapElapsed, bufElapsed ) * 100.0 );

            LogUtils.printDebug( String.format(
                "The winner is: %s by %02.1f%%!", winner, percent ) );
            LogUtils.printDebug( "Mapped time: " + mapElapsed );
            LogUtils.printDebug( "Buffered time: " + bufElapsed );
        }
        catch( IOException ex )
        {
            LogUtils.printError( "I/O Error", ex );
        }
    }

    /***************************************************************************
     * @param file
     * @throws FileNotFoundException
     * @throws IOException
     **************************************************************************/
    private static void readMapFile( File file )
        throws FileNotFoundException, IOException
    {
        try( MappedStream stream = new MappedStream( file, true,
            ByteOrdering.BIG_ENDIAN, BUF_SIZE ) )
        {
            readStream( stream );
        }
    }

    /***************************************************************************
     * @param file
     * @throws FileNotFoundException
     * @throws IOException
     **************************************************************************/
    private static void readBufFile( File file )
        throws FileNotFoundException, IOException
    {
        try( FileStream fs = new FileStream( file );
             BufferedStream stream = new BufferedStream( fs, BUF_SIZE ) )
        {
            readStream( stream );
        }
    }

    /***************************************************************************
     * @param stream
     * @throws EOFException
     * @throws IOException
     **************************************************************************/
    private static void readStream( IStream stream )
        throws EOFException, IOException
    {
        byte [] buf = new byte[BUF_SIZE - 1];
        long remaining = stream.getLength();
        int readCount;

        while( remaining > 0 )
        {
            readCount = stream.read( buf );
            remaining -= readCount;
        }
    }

    /***************************************************************************
     * @param file
     * @throws FileNotFoundException
     * @throws IOException
     **************************************************************************/
    private static void fillFile( File file )
        throws FileNotFoundException, IOException
    {
        byte [] buf = new byte[BUF_SIZE];
        long count = FILE_SIZE / buf.length;
        Random rand = new Random();

        try( FileStream stream = new FileStream( file ) )
        {
            for( long i = 0; i < count; i++ )
            {
                rand.nextBytes( buf );
                stream.write( buf );
            }
        }
    }
}
