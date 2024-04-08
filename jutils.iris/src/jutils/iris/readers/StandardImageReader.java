package jutils.iris.readers;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferShort;
import java.awt.image.DataBufferUShort;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import jutils.core.OptionUtils;
import jutils.core.io.IOUtils;
import jutils.core.io.LogUtils;
import jutils.iris.colors.MonoColorizer;
import jutils.iris.data.IRasterAlbum;
import jutils.iris.data.RasterListAlbum;
import jutils.iris.io.IRasterAlbumReader;
import jutils.iris.rasters.IRaster;
import jutils.iris.rasters.Mono8Raster;
import jutils.iris.rasters.MonoIntRaster;

/*******************************************************************************
 * 
 ******************************************************************************/
public class StandardImageReader implements IRasterAlbumReader
{
    /**  */
    private static final String [] EXTENSIONS = new String[] { "bmp", "jpg",
        "jpeg", "png", "jfif" };

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return "Standard Image";
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
        String ext = IOUtils.getFileExtension( f );
        boolean isExt = isImageFile( ext );

        if( isExt )
        {
            // TODO Check for magic #s
        }

        return isExt;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IRasterAlbum readFile( File f, Component parent )
    {
        BufferedImage image = null;

        try
        {
            image = ImageIO.read( f );
        }
        catch( IOException ex )
        {
            OptionUtils.showErrorMessage( parent,
                "Unable to read image: " + ex.getMessage(), "I/O Error" );
            return null;
        }

        if( image == null )
        {
            OptionUtils.showErrorMessage( parent,
                "Unable to read standard image " + f.getAbsolutePath(),
                "File Error" );
            return null;
        }

        RasterListAlbum album = new RasterListAlbum();

        switch( image.getType() )
        {
            case BufferedImage.TYPE_BYTE_GRAY:
                album.addRaster( createMono8Raster( image ) );
                break;

            case BufferedImage.TYPE_USHORT_GRAY:
                album.addRaster( createMonoIntRaster( image, 16 ) );
                break;
            //
            // case BufferedImage.TYPE_3BYTE_BGR:
            // break;
            //
            // case BufferedImage.TYPE_BYTE_INDEXED:
            // break;
            //
            // case BufferedImage.TYPE_INT_BGR:
            // break;

            case BufferedImage.TYPE_4BYTE_ABGR:
                album.addRaster( createMono8Raster( convertToGray8( image ) ) );
                break;

            // case BufferedImage.TYPE_INT_ARGB:
            // break;
            //
            // case BufferedImage.TYPE_INT_RGB:
            // break;

            default:
                LogUtils.printWarning( "Unhandled image type %d",
                    image.getType() );
                album.addRaster( createMono8Raster( convertToGray8( image ) ) );
                break;
        }

        album.setColorizer( new MonoColorizer() );

        return album;
    }

    /***************************************************************************
     * @param image
     * @param depth
     * @return
     **************************************************************************/
    private static IRaster createMonoIntRaster( BufferedImage image, int depth )
    {
        int w = image.getWidth();
        int h = image.getHeight();

        MonoIntRaster r = new MonoIntRaster( w, h, depth );

        short [] imgBytes = null;
        DataBuffer dbuf = image.getRaster().getDataBuffer();

        if( dbuf instanceof DataBufferUShort )
        {
            DataBufferUShort buf = ( DataBufferUShort )dbuf;
            imgBytes = buf.getData();
        }
        else if( dbuf instanceof DataBufferShort )
        {
            DataBufferShort buf = ( DataBufferShort )dbuf;
            imgBytes = buf.getData();
        }
        else
        {
            throw new IllegalStateException(
                "Unhandled image type: " + image.getType() );
        }

        for( int i = 0; i < imgBytes.length; i++ )
        {
            r.pixels[i] = imgBytes[i] & 0xFFFF;
        }

        return r;
    }

    /***************************************************************************
     * @param image
     * @return
     **************************************************************************/
    private static Mono8Raster createMono8Raster( BufferedImage image )
    {
        int w = image.getWidth();
        int h = image.getHeight();

        Mono8Raster r = new Mono8Raster( w, h );

        DataBufferByte buf = ( DataBufferByte )image.getRaster().getDataBuffer();
        byte [] imgBytes = buf.getData();

        r.setPixelData( imgBytes );

        return r;
    }

    /***************************************************************************
     * @param image
     * @return
     **************************************************************************/
    private static BufferedImage convertToGray8( BufferedImage image )
    {
        BufferedImage gsImage = new BufferedImage( image.getWidth(),
            image.getHeight(), BufferedImage.TYPE_BYTE_GRAY );
        Graphics g = gsImage.getGraphics();
        g.drawImage( image, 0, 0, null );
        g.dispose();

        return gsImage;
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
