package org.duak.task;

import java.io.File;
import java.util.List;

import org.duak.data.FileInfo;
import org.jutils.core.task.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DuakTask implements IStatusTask
{
    /**  */
    private final File dir;

    /**  */
    private FileInfo results;

    /***************************************************************************
     * @param dir
     **************************************************************************/
    public DuakTask( File dir )
    {
        this.dir = dir;
    }

    /***************************************************************************
     * @param handler
     **************************************************************************/
    @Override
    public void run( ITaskStatusHandler handler )
    {
        results = new FileInfo( dir );

        handler.signalPercent( -1 );
        handler.signalMessage( "Building file list..." );

        results.refresh( handler );

        getSize( results, handler, 0, results.getNumFiles() );
    }

    /***************************************************************************
     * @param fileInfo
     * @param i
     * @param len
     **************************************************************************/
    private int getSize( FileInfo fileInfo, ITaskStatusHandler handler, int i,
        int len )
    {
        if( !handler.canContinue() )
        {
            return i;
        }

        // long size = ( long )( Math.random() * ( Long.MAX_VALUE ) );
        long size = 0;
        List<FileInfo> childResults = fileInfo.getChildren();
        TaskUpdater updater = new TaskUpdater( handler, len );

        for( FileInfo fileResult : childResults )
        {
            if( !fileResult.isDir() )
            {
                fileResult.setSize( fileResult.getFile().length() );
                size += fileResult.getSize();

                updater.update( ++i );
            }
            else
            {
                handler.signalMessage( fileResult.getFile().getAbsolutePath() );
                i = getSize( fileResult, handler, i, len );
                size += fileResult.getSize();
            }

            if( !handler.canContinue() )
            {
                break;
            }
        }

        fileInfo.setSize( size );

        return i;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        return "Duak Analysis";
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public FileInfo getResults()
    {
        return results;
    }
}
