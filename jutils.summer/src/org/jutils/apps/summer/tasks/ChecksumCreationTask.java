package org.jutils.apps.summer.tasks;

import java.io.IOException;

import org.jutils.apps.summer.data.ChecksumResult;
import org.jutils.apps.summer.data.SumFile;
import org.jutils.core.io.cksum.CheckSumFactory;
import org.jutils.core.io.cksum.ChecksumGenenerator;
import org.jutils.core.io.cksum.IChecksum;
import org.jutils.core.task.IStatusTask;
import org.jutils.core.task.ITaskStatusHandler;
import org.jutils.core.task.TaskError;
import org.jutils.core.task.TaskUpdater;
import org.jutils.core.ui.hex.HexUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChecksumCreationTask implements IStatusTask
{
    /**  */
    private final ChecksumResult input;

    /***************************************************************************
     * @param input
     **************************************************************************/
    public ChecksumCreationTask( ChecksumResult input )
    {
        this.input = input;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return "Checksum Creation";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void run( ITaskStatusHandler handler )
    {
        try
        {
            createChecksums( handler, input );
        }
        catch( IOException ex )
        {
            handler.signalError( new TaskError( "I/O Error", ex ) );
        }
        finally
        {
            handler.signalFinished();
        }
    }

    /***************************************************************************
     * @param handler
     * @param input
     * @throws IOException
     **************************************************************************/
    public static void createChecksums( ITaskStatusHandler handler,
        ChecksumResult input ) throws IOException
    {
        TaskUpdater updater = new TaskUpdater( handler, input.calculateSize() );
        IChecksum checksummer = CheckSumFactory.createSummer( input.type );
        ChecksumGenenerator generator = new ChecksumGenenerator( checksummer,
            updater );

        long bytesProcessed = 0;

        for( int i = 0; i < input.files.size() && handler.canContinue(); i++ )
        {
            SumFile sf = input.files.get( i );
            byte [] csBytes;
            String cs;

            handler.signalMessage( "Processing " + ( i + 1 ) + " of " +
                input.files.size() + ": " + sf.path );

            csBytes = generator.generateChecksum( sf.file, bytesProcessed );
            cs = HexUtils.toHexString(
                HexUtils.asList( csBytes ) ).toLowerCase();

            if( cs == null )
            {
                throw new IllegalStateException( "checksum null" );
            }
            sf.checksum = cs;

            bytesProcessed += sf.length;

            updater.update( bytesProcessed );
        }
    }
}
