package jutils.core.io.cksum;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import jutils.core.io.FileStream;
import jutils.core.io.IStream;
import jutils.core.task.TaskUpdater;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChecksumGenenerator
{
    /**  */
    private final IChecksum checksummer;
    /**  */
    private final TaskUpdater updater;
    /**  */
    private final byte [] buffer;

    /***************************************************************************
     * @param checksummer
     * @param updater
     **************************************************************************/
    public ChecksumGenenerator( IChecksum checksummer, TaskUpdater updater )
    {
        this.checksummer = checksummer;
        this.updater = updater;
        this.buffer = new byte[32 * 1024 * 1024];
    }

    /***************************************************************************
     * @param file
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     **************************************************************************/
    public byte [] generateChecksum( File file )
        throws FileNotFoundException, IOException
    {
        return generateChecksum( file, 0 );
    }

    /***************************************************************************
     * @param file
     * @param progressStart
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     **************************************************************************/
    public byte [] generateChecksum( File file, long progressStart )
        throws FileNotFoundException, IOException
    {
        // try( FileStream fs = new FileStream( file, true );
        // IStream stream = new BufferedReadOnlyStream( fs ) )
        // try( FileStream fs = new FileStream( file, true );
        // IStream stream = new BufferedStream( fs ) )
        try( IStream stream = new FileStream( file, true ) )
        {
            return generateChecksum( stream, progressStart );
        }
    }

    /***************************************************************************
     * @param stream
     * @param progressStart
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     **************************************************************************/
    public byte [] generateChecksum( IStream stream, long progressStart )
        throws FileNotFoundException, IOException
    {
        long totalRead = 0;
        final long streamLen = stream.getLength();

        int numRead;

        checksummer.reset();

        do
        {
            updater.update( progressStart + stream.getPosition() );
            numRead = stream.read( buffer );

            if( numRead > 0 )
            {
                this.checksummer.update( buffer, 0, numRead );
                totalRead += numRead;
            }

            // LogUtils.printDebug( "Read " + totalRead + " of " + streamLen
            // +
            // " @ " + stream.getPosition() );

            // if( stream.getPosition() != totalRead )
            // {
            // throw new IllegalStateException( "wrong" );
            // }

        } while( totalRead < streamLen && numRead != -1 &&
            updater.handler.canContinue() );

        return checksummer.getChecksum();
    }
}
