package jutils.iris.ui;

import java.awt.Graphics2D;

import javax.swing.JComponent;

import jutils.core.ui.PaintingComponent;
import jutils.core.ui.model.IView;
import jutils.iris.colors.IColorizer;
import jutils.iris.data.IRaster;
import jutils.iris.data.RasterConfig;
import jutils.iris.data.RasterImage;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RasterView implements IView<JComponent>
{
    /**  */
    private final PaintingComponent view;

    /**  */
    private RasterImage image;

    /***************************************************************************
     * 
     **************************************************************************/
    public RasterView()
    {
        this.view = new PaintingComponent( ( c, g ) -> paintRaster( c, g ) );
        this.image = new RasterImage( 256, 256 );
    }

    /***************************************************************************
     * @param c
     * @param g
     **************************************************************************/
    private void paintRaster( JComponent c, Graphics2D g )
    {
        image.draw( g, c );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * @param raster
     **************************************************************************/
    public void set( IRaster raster )
    {
        image.set( raster );
    }

    /***************************************************************************
     * @param raster
     * @param colorModel
     **************************************************************************/
    public void set( IRaster raster, IColorizer colorModel )
    {
        RasterConfig config = raster.getConfig();

        if( image.needsResize( config.width, config.height ) )
        {
            int w = config.width;
            int h = config.height;
            this.image = new RasterImage( w, h );
        }
        this.image.set( raster, colorModel );

        view.repaint();
    }
}
