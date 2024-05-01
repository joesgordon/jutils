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
import jutils.core.utils.ByteOrdering;
import jutils.iris.albums.IRasterAlbum;
import jutils.iris.albums.RasterListAlbum;
import jutils.iris.colors.AbgrColorizer;
import jutils.iris.colors.ArgbColorizer;
import jutils.iris.colors.BgrColorizer;
import jutils.iris.colors.IColorizer;
import jutils.iris.colors.MonoColorizer;
import jutils.iris.data.BufferedImageType;
import jutils.iris.io.IRasterAlbumReader;
import jutils.iris.rasters.AbgrRaster;
import jutils.iris.rasters.ArgbRaster;
import jutils.iris.rasters.BgrRaster;
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
        IRaster raster = null;
        IColorizer colorizer = null;

        BufferedImageType imageType = BufferedImageType.fromValue(
            image.getType() );

        LogUtils.printDebug( "Reading image type %s",
            imageType.getDescription() );

        switch( imageType )
        {
            // case CUSTOM:
            // break;

            // case RGB_INT:
            // break;

            case ARGB_INT:
                raster = createArgbRaster( image );
                colorizer = new ArgbColorizer();
                break;

            // case ARGB_PRE_INT:
            // break;

            // case BGR_INT:
            // break;

            case BGR_3BYTE:
                raster = createBgrRaster( image );
                colorizer = new BgrColorizer();
                break;

            case ABGR_4BYTE:
                raster = createAbgrRaster( image );
                colorizer = new AbgrColorizer();
                break;

            // case ABGR_PRE_4BYTE:
            // break;

            // case RGB_565_SHORT:
            // break;

            // case RGB_555_SHORT:
            // break;

            case MONO_BYTE:
                raster = createMono8Raster( image );
                colorizer = new MonoColorizer();
                break;

            case MONO_SHORT:
                raster = createMono16Raster( image, 16 );
                colorizer = new MonoColorizer();
                break;

            // case BINARY_BYTE:
            // break;

            // case INDEXED_BYTE:
            // break;

            default:
                LogUtils.printWarning( "Unhandled image type %s",
                    imageType.getDescription() );
                raster = createMono8Raster( convertToGray8( image ) );
                colorizer = new MonoColorizer();
                break;
        }

        if( raster != null )
        {
            album.addRaster( raster );
            album.setColorizer( colorizer );
        }
        else
        {
            album.setColorizer( new MonoColorizer() );
        }

        return album;
    }

    /***************************************************************************
     * @param image
     * @param depth
     * @return
     **************************************************************************/
    private static IRaster createMono16Raster( BufferedImage image, int depth )
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
            r.setPixel( i, imgBytes[i] & 0xFFFF );
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

        r.setBufferData( imgBytes, ByteOrdering.BIG_ENDIAN );

        return r;
    }

    /***************************************************************************
     * @param image
     * @return
     **************************************************************************/
    private static AbgrRaster createAbgrRaster( BufferedImage image )
    {
        int w = image.getWidth();
        int h = image.getHeight();

        AbgrRaster r = new AbgrRaster( w, h );

        DataBufferByte buf = ( DataBufferByte )image.getRaster().getDataBuffer();
        byte [] buffer = buf.getData();

        r.setBufferData( buffer, ByteOrdering.BIG_ENDIAN );

        return r;
    }

    /***************************************************************************
     * @param image
     * @return
     **************************************************************************/
    private static BgrRaster createBgrRaster( BufferedImage image )
    {
        int w = image.getWidth();
        int h = image.getHeight();

        BgrRaster r = new BgrRaster( w, h );

        DataBufferByte buf = ( DataBufferByte )image.getRaster().getDataBuffer();
        byte [] buffer = buf.getData();

        r.setBufferData( buffer, ByteOrdering.BIG_ENDIAN );

        return r;
    }

    /***************************************************************************
     * @param image
     * @return
     **************************************************************************/
    private static ArgbRaster createArgbRaster( BufferedImage image )
    {
        int w = image.getWidth();
        int h = image.getHeight();

        ArgbRaster r = new ArgbRaster( w, h );

        DataBufferByte buf = ( DataBufferByte )image.getRaster().getDataBuffer();
        byte [] buffer = buf.getData();

        r.setBufferData( buffer, ByteOrdering.BIG_ENDIAN );

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
