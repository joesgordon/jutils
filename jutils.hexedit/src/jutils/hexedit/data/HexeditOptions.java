package jutils.hexedit.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jutils.core.utils.MaxQueue;

/*******************************************************************************
 * Defines the user options for the Hexedit application.
 ******************************************************************************/
public class HexeditOptions
{
    /** The file last accessed by the application. */
    public final MaxQueue<File> lastAccessedFiles;

    /***************************************************************************
     * Creates a new set of user options with default values.
     **************************************************************************/
    public HexeditOptions()
    {
        this.lastAccessedFiles = new MaxQueue<File>( 10 );
    }

    /***************************************************************************
     * Creates a new, "deep" copy of the provided set of options.
     * @param options the options to be copied.
     * @throws NullPointerException if the provided options are null.
     **************************************************************************/
    public HexeditOptions( HexeditOptions options )
    {
        this();

        if( options.lastAccessedFiles != null )
        {
            lastAccessedFiles.addAll( options.lastAccessedFiles );
        }
    }

    /***************************************************************************
     * Gets the file last accessed by the user.
     * @return the last file access or {@code null} if none exist.
     **************************************************************************/
    public File getLastFile()
    {
        File f = null;

        if( !lastAccessedFiles.isEmpty() )
        {
            f = lastAccessedFiles.first();
        }

        return f;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void removeNonExistentRecents()
    {
        List<File> toRemove = new ArrayList<File>();
        for( File f : lastAccessedFiles )
        {
            if( !f.exists() )
            {
                toRemove.add( f );
            }
        }
        for( File f : toRemove )
        {
            lastAccessedFiles.remove( f );
        }
    }
}
