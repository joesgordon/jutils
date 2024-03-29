package jutils.plot.data;

import java.awt.Dimension;
import java.io.File;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SaveOptions
{
    /**  */
    public File file;
    /**  */
    public final Dimension size;

    /***************************************************************************
     * 
     **************************************************************************/
    public SaveOptions()
    {
        this.file = null;
        this.size = new Dimension();
    }

    /***************************************************************************
     * @param size
     **************************************************************************/
    public void setSize( Dimension size )
    {
        this.size.height = size.height;
        this.size.width = size.width;
    }
}
