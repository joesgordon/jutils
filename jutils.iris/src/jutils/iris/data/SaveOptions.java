package jutils.iris.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SaveOptions
{
    /**  */
    public int index;
    /**  */
    public int count;
    /**  */
    public SaveFormat format;
    /**  */
    public File dir;
    /**  */
    public final List<String> names;

    /***************************************************************************
     * 
     **************************************************************************/
    public SaveOptions()
    {
        this.names = new ArrayList<>();
    }
}
