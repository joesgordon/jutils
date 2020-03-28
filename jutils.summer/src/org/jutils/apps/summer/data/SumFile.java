package org.jutils.apps.summer.data;

import java.io.File;
import java.util.Objects;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SumFile
{
    /**  */
    public File file;
    /**  */
    public String path;
    /**  */
    public long length;
    /**  */
    public String checksum;

    /***************************************************************************
     * 
     **************************************************************************/
    public SumFile()
    {
        this.file = null;
        this.path = null;
        this.length = -1L;
        this.checksum = null;
    }

    /***************************************************************************
     * @param sf
     **************************************************************************/
    public SumFile( SumFile sf )
    {
        this.file = sf.file;
        this.path = sf.path;
        this.length = sf.length;
        this.checksum = sf.checksum;
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    public SumFile( File file )
    {
        this.file = file;
        this.path = file.getAbsolutePath();
        this.length = file.length();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean equals( Object obj )
    {
        if( obj == null )
        {
            return false;
        }

        if( obj instanceof SumFile )
        {
            SumFile that = ( SumFile )obj;

            return that.path.equals( path ) && that.checksum.equals( checksum );
        }

        return false;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int hashCode()
    {
        return Objects.hash( path, checksum );
    }
}
