package jutils.iris.readers;

import java.awt.Component;
import java.io.File;
import java.io.IOException;

import jutils.core.OptionUtils;
import jutils.iris.albums.IRasterAlbum;
import jutils.iris.albums.RawRasterAlbum;
import jutils.iris.data.RawConfig;
import jutils.iris.io.IRasterAlbumReader;
import jutils.iris.ui.RawImportView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RawImageReader implements IRasterAlbumReader
{
    /**  */
    private static final String [] EXTENSIONS = new String[] { "raw", "bin",
        "img" };

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return "Raw Image";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String [] getExtensions()
    {
        return EXTENSIONS;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean isReadable( File f )
    {
        return true;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IRasterAlbum readFile( File file, Component parent )
    {
        IRasterAlbum album = null;

        RawImportView importView = new RawImportView();
        RawConfig config = importView.showDialog( parent, file );

        if( config == null )
        {
            return null;
        }

        try
        {
            album = new RawRasterAlbum( file, config );
        }
        catch( IOException ex )
        {
            OptionUtils.showErrorMessage( parent, ex.getMessage(),
                "Unable to read file " + file.getName() );
        }

        return album;
    }

    /***************************************************************************
     * @param ext
     * @return
     **************************************************************************/
    public static boolean isImageFile( String ext )
    {
        ext = ext.toLowerCase();

        for( String e : EXTENSIONS )
        {
            if( e.equals( ext ) )
            {
                return true;
            }
        }

        return false;
    }
}
