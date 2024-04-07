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

import javax.swing.JComponent;
import javax.swing.border.AbstractBorder;
import javax.swing.border.BevelBorder;

import jutils.core.ui.IPaintable;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DirectImage implements IPaintable
{
    /**  */
    public final int width;
    /**  */
    public final int height;
    /**  */
    public final int [] pixels;
    /**  */
    public final BufferedImage image;

    /***************************************************************************
     * @param width
     * @param height
     **************************************************************************/
    public DirectImage( int width, int height )
    {
        this.width = width;
        this.height = height;
        this.pixels = new int[width * height];
        this.image = createImage();

    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private BufferedImage createImage()
    {
        int amask = 0xFF000000;
        int rmask = 0x00FF0000;
        int gmask = 0x0000FF00;
        int bmask = 0x000000FF;

        // ColorModel mdl = new DirectColorModel( 32, rmask, gmask, bmask, amask
        // );
        // SampleModel sampleModel = mdl.createCompatibleSampleModel( width,
        // height );

        int [] masks = new int[] { rmask, gmask, bmask };
        ColorModel mdl = new DirectColorModel( 32, rmask, gmask, bmask );
        SinglePixelPackedSampleModel sampleModel = new SinglePixelPackedSampleModel(
            DataBuffer.TYPE_INT, width, height, masks );

        DataBuffer dataBuf = new DataBufferInt( pixels, pixels.length );
        WritableRaster raster = WritableRaster.createWritableRaster(
            sampleModel, dataBuf, new Point( 0, 0 ) );

        return new BufferedImage( mdl, raster, false, null );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void paint( JComponent c, Graphics2D g )
    {
        draw( g, 4, 4, c );

        AbstractBorder bdr = new BevelBorder( BevelBorder.LOWERED );
        bdr.paintBorder( c, g, 4, 4, width, height );
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
        double zoomScale = 1.0; // config.getZoomScale();
        double reScale = 1.0 / zoomScale;

        g.scale( zoomScale, zoomScale );
        g.drawImage( image, x, y, observer );
        g.scale( reScale, reScale );
    }

    /***************************************************************************
     * @param x
     * @param y
     * @return
     **************************************************************************/
    public int getPixel( int x, int y )
    {
        return pixels[y * width + x];
    }

    /***************************************************************************
     * @param x
     * @param y
     * @param pixel
     **************************************************************************/
    public void setPixel( int x, int y, int pixel )
    {
        pixels[y * width + x] = pixel;
    }
}
