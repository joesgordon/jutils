package org.jutils.apps.jhex.task;

import java.io.IOException;

import org.jutils.core.io.IStream;
import org.jutils.core.task.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SearchTask implements IStatusTask
{
    /** The bytes this task is searching for. */
    private final byte [] bytes;
    /** The stream to be searched. */
    private final IStream stream;
    /** The offset into the stream to start searching. */
    private final long offset;
    /** Search forward if {@code true}; backwards otherwise. */
    private final boolean forward;

    /**  */
    public long foundOffset;

    /***************************************************************************
     * @param bytes the bytes this task is searching for.
     * @param stream the stream to be searched.
     * @param offset the offset into the stream to start searching.
     * @param isForward search forward if {@code true}; backwards otherwise.
     **************************************************************************/
    public SearchTask( byte [] bytes, IStream stream, long offset,
        boolean isForward )
    {
        this.bytes = bytes;
        this.stream = stream;
        this.offset = offset;
        this.forward = isForward;

        this.foundOffset = -1;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void run( ITaskStatusHandler handler )
    {
        try
        {
            search( handler );
        }
        catch( IOException ex )
        {
            handler.signalError( new TaskError( "I/O Error", ex ) );
        }
        catch( Throwable ex )
        {
            handler.signalError( new TaskError( "Error Searching", ex ) );
        }
        finally
        {
            handler.signalFinished();
        }
    }

    /***************************************************************************
     * @param handler handler the handler that preempts execution and reports
     * status and errors.
     * @throws IOException any I/O error that occurs.
     **************************************************************************/
    private void search( ITaskStatusHandler handler ) throws IOException
    {
        // @SuppressWarnings( "resource")
        // BufferedStream stream = new BufferedStream( this.stream,
        // 8 * 1024 * 1024 );

        // LogUtils.printDebug( "Searching for: " + HexUtils.toHexString( bytes
        // ) +
        // " @ " + String.format( "%016X", offset ) + " " +
        // ( forward ? "Forward" : "Backward" ) );

        stream.seek( offset );

        long count = ( forward ? stream.getAvailable() : offset ) -
            bytes.length + 1;
        byte b;
        boolean found = false;
        int seekInc = forward ? 0 : 2;

        TaskUpdater updater = new TaskUpdater( handler, count );

        for( long idx = 0; idx < count && !found &&
            handler.canContinue(); idx++ )
        {
            if( bytes.length > stream.getAvailable() )
            {
                break;
            }

            found = true;

            for( int i = 0; i < bytes.length; i++ )
            {
                b = stream.read();

                if( b != bytes[i] )
                {
                    found = false;
                    long seek = -( i + seekInc );
                    if( seekInc != 0 )
                    {
                        // LogUtils.printDebug( "skipping: " + seek );
                        stream.skip( seek );
                    }
                    break;
                }
            }

            updater.update( idx );
        }

        foundOffset = -1;

        if( found )
        {
            foundOffset = stream.getPosition() - bytes.length;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        return "Byte Search";
    }
}
