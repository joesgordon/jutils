package org.jutils.chart.app;

import java.io.File;

import org.jutils.utils.MaxQueue;

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
