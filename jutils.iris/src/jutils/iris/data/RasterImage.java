package jutils.iris.data;

import java.awt.Graphics2D;

import javax.swing.JComponent;

import jutils.core.ui.IPaintable;
import jutils.iris.colors.IColorizer;
import jutils.iris.colors.MonoColorizer;
import jutils.iris.rasters.IRaster;
import jutils.iris.rasters.Mono8Raster;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RasterImage implements IPaintable
{
    /**  */
    public static final int DEFAULT_SIZE = 256;

    /**  */
    private IRaster raster;
    /**  */
    private IColorizer colorizer;
    /**  */
    private DirectImage image;

    /***************************************************************************
     * 
     **************************************************************************/
    public RasterImage()
    {
        this( DEFAULT_SIZE, DEFAULT_SIZE );
    }

    /***************************************************************************
     * @param width
     * @param height
     **************************************************************************/
    public RasterImage( int width, int height )
    {
        reset( width, height );
    }

    /***************************************************************************
     * @param raster
     **************************************************************************/
    public void set( IRaster raster )
    {
        set( raster, colorizer );
    }

    /***************************************************************************
     * @param colorizer
     **************************************************************************/
    public void set( IColorizer colorizer )
    {
        set( raster, colorizer );
    }

    /***************************************************************************
     * @param raster
     * @param colorizer
     **************************************************************************/
    public void set( IRaster raster, IColorizer colorizer )
    {
        this.raster = raster;
        this.colorizer = colorizer;

        if( needsResize( raster ) )
        {
            this.image = new DirectImage( raster.getWidth(),
                raster.getHeight() );
        }

        update();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void update()
    {
        colorizer.colorize( raster, image.pixels );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public IRaster getRaster()
    {
        return raster;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getWidth()
    {
        return image.width;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getHeight()
    {
        return image.height;
    }

    /***************************************************************************
     * @param raster
     * @return
     **************************************************************************/
    private boolean needsResize( IRaster raster )
    {
        return image.width != raster.getWidth() ||
            image.height != raster.getHeight();
    }

    /***************************************************************************
     * @param x
     * @param y
     * @return
     **************************************************************************/
    public int getColorizedPixel( int x, int y )
    {
        return image.getPixel( x, y );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void paint( JComponent c, Graphics2D g )
    {
        image.paint( c, g );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void reset()
    {
        reset( image.width, image.height );
    }

    /***************************************************************************
     * @param width
     * @param height
     **************************************************************************/
    private void reset( int width, int height )
    {
        this.raster = new Mono8Raster( width, height );
        this.colorizer = new MonoColorizer();
        this.image = new DirectImage( width, height );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public DirectImage getImage()
    {
        return image;
    }
}
