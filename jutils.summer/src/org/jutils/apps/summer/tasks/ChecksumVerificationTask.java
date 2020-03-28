package org.jutils.apps.summer.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jutils.apps.summer.data.*;
import org.jutils.core.task.*;

/*******************************************************************************
 * 
 *******************************************************************************/
public class ChecksumVerificationTask implements IStatusTask
{
    /**  */
    private final ChecksumResult input;

    /**  */
    public final List<InvalidChecksum> invalidSums;

    /***************************************************************************
     * @param input
     **************************************************************************/
    public ChecksumVerificationTask( ChecksumResult input )
    {
        this.input = input;

        this.invalidSums = new ArrayList<>();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return "Checksum Verification";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void run( ITaskStatusHandler handler )
    {
        ChecksumResult calcInput = new ChecksumResult( input );

        try
        {
            ChecksumCreationTask.createChecksums( handler, calcInput );

            TaskUpdater updater = new TaskUpdater( handler,
                calcInput.files.size() );

            for( int i = 0; i < calcInput.files.size() &&
                handler.canContinue(); i++ )
            {
                SumFile inputSum = input.files.get( i );
                SumFile calcSum = calcInput.files.get( i );
                updater.update( i );

                if( !inputSum.checksum.equalsIgnoreCase( calcSum.checksum ) )
                {
                    invalidSums.add( new InvalidChecksum( inputSum, calcSum ) );
                }
            }

            updater.update( calcInput.files.size() );
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
}
