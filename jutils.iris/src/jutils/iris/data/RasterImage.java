package jutils.iris.data;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RasterImage
{
    /**  */
    private final int [] pixels;
    /**  */
    public final BufferedImage bufImage;
    /**  */
    public IRaster raster;
    /**  */
    public IColorModel colors;

    /***************************************************************************
     * @param width
     * @param height
     **************************************************************************/
    public RasterImage( int width, int height )
    {
        int pixelCount = width * height;

        int [] pixels = new int[pixelCount];

        int [] masks = new int[] { 0x00FF0000, 0x00FF00, 0x00FF };
        ColorModel mdl = new DirectColorModel( 32, masks[0], masks[1],
            masks[2] );
        SinglePixelPackedSampleModel sampleModel = new SinglePixelPackedSampleModel(
            DataBuffer.TYPE_INT, width, height, masks );
        DataBuffer dataBuf = new DataBufferInt( pixels, pixels.length );
        WritableRaster raster = WritableRaster.createWritableRaster(
            sampleModel, dataBuf, new Point( 0, 0 ) );

        this.pixels = pixels;
        this.bufImage = new BufferedImage( mdl, raster, false, null );
        this.raster = null;
        this.colors = null;
    }

    /***************************************************************************
     * @param g the graphics on which this image is to be drawn.
     * @param observer object to be notified as more of the image is converted.
     **************************************************************************/
    public void draw( Graphics2D g, ImageObserver observer )
    {
        draw( g, 0, 0, observer );
    }

    /***************************************************************************
     * @param g the graphics on which this image is to be drawn.
     * @param x the x coordinate at which the image is to be drawn.
     * @param y the y coordinate at which the image is to be drawn.
     * @param observer object to be notified as more of the image is converted.
     **************************************************************************/
    public void draw( Graphics2D g, int x, int y, ImageObserver observer )
    {
        double fz = 1.0; // config.getZoomScale();
        g.scale( fz, fz );
        g.drawImage( bufImage, x, y, observer );
        g.scale( 1.0 / fz, 1.0 / fz );
    }

    /***************************************************************************
     * @param raster
     **************************************************************************/
    public void set( IRaster raster )
    {
        set( raster, colors );
    }

    /***************************************************************************
     * @param colors
     **************************************************************************/
    public void set( IColorModel colors )
    {
        set( raster, colors );
    }

    /***************************************************************************
     * @param raster
     * @param colors
     **************************************************************************/
    public void set( IRaster raster, IColorModel colors )
    {
        this.raster = raster;
        this.colors = colors;

        update();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void update()
    {
        for( int i = 0; i < pixels.length; i++ )
        {
            long pixel = raster.getPixel( i );
            int rgb = colors.getColorValue( pixel );
            pixels[i] = rgb;

            // WritableRaster wr = image.getRaster();
            // wr.setDataElements( 0, 0, getWidth(), getHeight(), bp );
        }
    }
}