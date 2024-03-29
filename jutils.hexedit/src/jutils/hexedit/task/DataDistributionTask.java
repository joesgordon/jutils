package jutils.hexedit.task;

import java.io.IOException;

import jutils.core.datadist.DataDistribution;
import jutils.core.datadist.DistributionBuilder;
import jutils.core.io.IStream;
import jutils.core.task.*;

/*******************************************************************************
 * Defines a task that builds a data distribution for a stream.
 ******************************************************************************/
public class DataDistributionTask implements IStatusTask
{
    /** The stream containing the data to be analyzed. */
    private final IStream stream;

    /** The distribution generated. */
    private DataDistribution dist;

    /***************************************************************************
     * Creates a new task with the provided stream.
     * @param stream the stream to be analyzed.
     **************************************************************************/
    public DataDistributionTask( IStream stream )
    {
        this.stream = stream;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void run( ITaskStatusHandler handler )
    {
        try
        {
            analyzeStream( handler );
        }
        catch( IOException ex )
        {
            handler.signalError( new TaskError( "I/O Error",
                "Unable to analyze data: " + ex.getMessage() ) );
        }
    }

    /***************************************************************************
     * Analyzes the provided stream.
     * @param handler the handler that preempts execution and reports status and
     * errors.
     * @throws IOException any I/O error that occurs.
     **************************************************************************/
    private void analyzeStream( ITaskStatusHandler handler ) throws IOException
    {
        TaskUpdater updater = new TaskUpdater( handler, stream.getLength() );
        byte [] buffer = new byte[2 * 1024 * 1024];
        DistributionBuilder builder = new DistributionBuilder();
        long analyzed = 0;
        long count = stream.getLength() - 3;

        stream.seek( 0 );

        while( analyzed < count && handler.canContinue() )
        {
            long offset = stream.getPosition();
            int bytesRead = stream.read( buffer );

            if( bytesRead < 4 )
            {
                break;
            }

            updater.update( offset );

            builder.addData( buffer, 0, bytesRead );

            stream.seek( offset + bytesRead - 3 );
            analyzed += bytesRead;
        }

        dist = builder.buildDistribution();

        // LogUtils.printInfo( Utils.NEW_LINE + dist.getDescription() );
    }

    /***************************************************************************
     * Returns the distribution generated by this task.
     * @return the distribution generated by this task or {@code null} if the
     * task has not finished or was interrupted.
     **************************************************************************/
    public DataDistribution getDistribution()
    {
        return dist;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return "Analyzing Data";
    }
}
