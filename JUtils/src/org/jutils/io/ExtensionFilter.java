package org.jutils.io;

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
     * 
     **************************************************************************/
    @Override
    public boolean accept( File f )
    {
        return f.isDirectory() ||
            ( f.isFile() && f.getName().endsWith( extension ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getDescription()
    {
        return ( "*." + extension ).toUpperCase();
    }
}
