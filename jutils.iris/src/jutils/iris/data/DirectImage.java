package jutils.iris.data;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;

import javax.swing.JComponent;

import jutils.core.INamedValue;
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

    /**  */
    private ZoomLevel zoom;
    /**  */
    private double zoomScale;

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

        this.zoom = ZoomLevel.FIT;
        this.zoomScale = 0.0;

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

        ColorModel mdl = new DirectColorModel( 32, rmask, gmask, bmask, amask );
        SampleModel sampleModel = mdl.createCompatibleSampleModel( width,
            height );

        // int [] masks = new int[] { rmask, gmask, bmask };
        // ColorModel mdl = new DirectColorModel( 32, rmask, gmask, bmask );
        // SinglePixelPackedSampleModel sampleModel = new
        // SinglePixelPackedSampleModel(
        // DataBuffer.TYPE_INT, width, height, masks );

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
        double scale;

        if( zoom == ZoomLevel.FIT )
        {
            Rectangle bounds = g.getClipBounds();
            double cd = Math.min( bounds.width, bounds.height );
            double id = Math.min( width, height );

            scale = cd / id;
        }
        else
        {
            scale = zoom.value;
        }

        zoomScale = scale;

        double reScale = 1.0 / scale;

        g.scale( scale, scale );
        g.drawImage( image, 0, 0, c );
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

    /***************************************************************************
     * @return
     **************************************************************************/
    public Dimension getDisplaySize()
    {
        int width;
        int height;

        if( zoom == ZoomLevel.FIT )
        {
            width = 10;
            height = 10;
        }
        else
        {
            width = this.width * zoom.value;
            height = this.height * zoom.value;
        }

        return new Dimension( width, height );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public ZoomLevel getZoom()
    {
        return zoom;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public double getZoomScale()
    {
        return zoomScale;
    }

    /***************************************************************************
     * @param zoom
     **************************************************************************/
    public void setZoom( ZoomLevel zoom )
    {
        this.zoom = zoom;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static enum ZoomLevel implements INamedValue
    {
        /**  */
        FIT( 0, "Fit" ),
        /**  */
        ZOOM01( 1, "Zoom 1x" ),
        /**  */
        ZOOM02( 2, "Zoom 2x" ),
        /**  */
        ZOOM04( 4, "Zoom 4x" ),
        /**  */
        ZOOM08( 8, "Zoom 8x" ),
        /**  */
        ZOOM16( 16, "Zoom 16x" ),;

        /**  */
        public final int value;
        /**  */
        public final String name;

        /**
         * @param value
         * @param name
         */
        private ZoomLevel( int value, String name )
        {
            this.value = value;
            this.name = name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getName()
        {
            return name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getValue()
        {
            return value;
        }

        /**
         * @param zoom
         * @return
         */
        public static ZoomLevel previousLevel( ZoomLevel zoom )
        {
            switch( zoom )
            {
                case FIT:
                    return ZOOM16;

                case ZOOM01:
                    return FIT;

                case ZOOM02:
                    return ZOOM01;

                case ZOOM04:
                    return ZOOM02;

                case ZOOM08:
                    return ZOOM04;

                case ZOOM16:
                    return ZOOM08;
            }

            return null;
        }

        /**
         * @param zoom
         * @return
         */
        public static ZoomLevel nextLevel( ZoomLevel zoom )
        {
            switch( zoom )
            {
                case FIT:
                    return ZOOM01;

                case ZOOM01:
                    return ZOOM02;

                case ZOOM02:
                    return ZOOM04;

                case ZOOM04:
                    return ZOOM08;

                case ZOOM08:
                    return ZOOM16;

                case ZOOM16:
                    return FIT;
            }

            return null;
        }
    }
}
