package jutils.telemetry.ch09;

import java.io.File;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TmatsFile
{
    /**  */
    public final Tmats tmats;

    /**  */
    public File file;

    /***************************************************************************
     * 
     **************************************************************************/
    public TmatsFile()
    {
        this( null, new Tmats() );
    }

    /***************************************************************************
     * @param file
     * @param tmats
     **************************************************************************/
    public TmatsFile( File file, Tmats tmats )
    {
        this.file = file;
        this.tmats = tmats;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String getName()
    {
        return file == null ? "New File" : file.getName();
    }
}
