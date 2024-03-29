package jutils.core.io;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/*******************************************************************************
 * Defines a file filter for a file extension.
 ******************************************************************************/
public class ExtensionFilter extends FileFilter implements java.io.FileFilter
{
    /** The file extension to be filtered. */
    private final String extension;

    /***************************************************************************
     * Creates a new filter with the provided extension.
     * @param ext the file extension to be filtered.
     **************************************************************************/
    public ExtensionFilter( String ext )
    {
        this.extension = ext;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean accept( File f )
    {
        LogUtils.printDebug( "Checking %s", f.getAbsoluteFile() );
        return f.isDirectory() ||
            ( f.isFile() && f.getName().endsWith( extension ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getDescription()
    {
        return ( "*." + extension ).toUpperCase();
    }
}
