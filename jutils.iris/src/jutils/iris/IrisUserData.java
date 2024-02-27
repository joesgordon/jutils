package jutils.iris;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jutils.core.utils.MaxQueue;

/*******************************************************************************
 * 
 ******************************************************************************/
public class IrisUserData
{
    /** The file last opened by the application. */
    public final MaxQueue<File> lastOpenedFiles;
    /** The file last saved by the application. */
    public final MaxQueue<File> lastSavedFiles;

    /***************************************************************************
     * 
     **************************************************************************/
    public IrisUserData()
    {
        this.lastOpenedFiles = new MaxQueue<File>( 10 );
        this.lastSavedFiles = new MaxQueue<File>( 10 );
    }

    /***************************************************************************
     * @param options
     **************************************************************************/
    public IrisUserData( IrisUserData options )
    {
        this();

        if( options.lastOpenedFiles != null )
        {
            lastOpenedFiles.addAll( options.lastOpenedFiles );
        }
        if( options.lastSavedFiles != null )
        {
            lastSavedFiles.addAll( options.lastSavedFiles );
        }
    }

    /***************************************************************************
     * Gets the file last opened by the application.
     * @return the last file opened or {@code null} if none exist.
     **************************************************************************/
    public File getLastOpenedFile()
    {
        return lastOpenedFiles.first();
    }

    /***************************************************************************
     * Gets the file last saved by the user.
     * @return the last file saved or {@code null} if none exist.
     **************************************************************************/
    public File getLastSavedFile()
    {
        return lastSavedFiles.first();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void removeNonExistentRecents()
    {
        List<File> toRemove = new ArrayList<File>();

        for( File f : lastOpenedFiles )
        {
            if( !f.exists() )
            {
                toRemove.add( f );
            }
        }

        for( File f : toRemove )
        {
            lastOpenedFiles.remove( f );
        }
    }
}
