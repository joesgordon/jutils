package jutils.duak;

import java.io.File;

import jutils.core.utils.MaxQueue;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DuakOptions
{
    /**  */
    public final MaxQueue<File> recentDirs;

    /*******************************************************************************
     * 
     ******************************************************************************/
    public DuakOptions()
    {
        this.recentDirs = new MaxQueue<>( 20 );
    }

    /*******************************************************************************
     * @param options
     ******************************************************************************/
    public DuakOptions( DuakOptions options )
    {
        this();

        if( options.recentDirs != null )
        {
            recentDirs.addAll( options.recentDirs );
        }
    }
}
