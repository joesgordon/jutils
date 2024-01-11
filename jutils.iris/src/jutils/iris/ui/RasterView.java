package jutils.iris.ui;

import java.awt.Graphics2D;

import javax.swing.JComponent;

import jutils.core.ui.PaintingComponent;
import jutils.core.ui.model.IView;
import jutils.iris.data.IColorModel;
import jutils.iris.data.IRaster;
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
    /**  */
    private IRaster raster;
    /**  */
    private IColorModel colorModel;

    /***************************************************************************
     * 
     **************************************************************************/
    public RasterView()
    {
        this.view = new PaintingComponent( ( c, g ) -> paintRaster( c, g ) );
        this.raster = null;
    }

    /***************************************************************************
     * @param c
     * @param g
     **************************************************************************/
    private void paintRaster( JComponent c, Graphics2D g )
    {
        if( null == raster )
        {
            return;
        }

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
        set( raster, colorModel );
    }

    /***************************************************************************
     * @param raster
     * @param colorModel
     **************************************************************************/
    private void set( IRaster raster, IColorModel colorModel )
    {
        this.raster = raster;
        this.colorModel = colorModel;

        view.repaint();
    }
}
