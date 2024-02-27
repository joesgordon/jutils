package jutils.iris.io;

import java.awt.Component;
import java.io.File;

import jutils.core.INamedItem;
import jutils.iris.data.IRasterAlbum;

/*******************************************************************************
 * 
 ******************************************************************************/
public interface IRasterAlbumReader extends INamedItem
{
    /***************************************************************************
     * The name of this reader; typically the type of image read. Do not include
     * the word "Reader" (e.g., "Raw Image" instead of "Raw Image Reader").
     **************************************************************************/
    @Override
    public String getName();

    /***************************************************************************
     * List of extensions that can be read by this reader sans '.'.
     * @return list of this reader's extensions.
     **************************************************************************/
    public String [] getExtensions();

    /***************************************************************************
     * Tests the provided file and returns {@code true} if this reader can read
     * the file. Perform simple content checking (magic number, format, etc.) if
     * possible here.
     * @param file the file to be checked.
     * @return {@code true} if this reader can read the provided file.
     **************************************************************************/
    public boolean isReadable( File file );

    /***************************************************************************
     * Reads an album of rasters from the provided file.
     * @param file
     * @param parent component used for showing dialogs.
     * @return
     **************************************************************************/
    public IRasterAlbum readFile( File file, Component parent );
}
