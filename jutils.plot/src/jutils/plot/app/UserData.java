package jutils.plot.app;

import java.io.File;

import jutils.core.utils.MaxQueue;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UserData
{
    /**  */
    public final MaxQueue<File> recentFiles;
    /**  */
    public File lastImageFile;
    /**  */
    public File lastDataFile;

    /***************************************************************************
     * 
     **************************************************************************/
    public UserData()
    {
        this.recentFiles = new MaxQueue<>( 10 );
    }
}
