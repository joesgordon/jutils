package org.jutils.apps.filespy.data;

import java.io.File;

/*******************************************************************************
 *
 ******************************************************************************/
public class FileSpyData
{
    /**  */
    public File lastSavedLocation = null;

    /**  */
    public SearchParams lastParams;

    /***************************************************************************
     * 
     **************************************************************************/
    public FileSpyData()
    {
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    public FileSpyData( FileSpyData data )
    {
        this();

        this.lastSavedLocation = data.lastSavedLocation;

        if( data.lastParams != null )
        {
            this.lastParams = new SearchParams( data.lastParams );
        }
    }
}
