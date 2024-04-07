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
        this.image = new DirectImage( width, height );
        reset( width, height );
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
        set( new Mono8Raster( width, height ), new MonoColorizer() );
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
     * @return
     **************************************************************************/
    public boolean set( IRaster raster, IColorizer colorizer )
    {
        boolean resized = needsResize( raster );

        this.raster = raster;
        this.colorizer = colorizer;

        if( resized )
        {
            this.image = new DirectImage( raster.getWidth(),
                raster.getHeight() );
        }

        update();

        return resized;
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
     * @return
     **************************************************************************/
    public DirectImage getImage()
    {
        return image;
    }
}
