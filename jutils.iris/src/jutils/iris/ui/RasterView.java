package jutils.iris.ui;

import javax.swing.JComponent;

import jutils.core.ui.PaintingComponent;
import jutils.core.ui.model.IView;
import jutils.iris.colors.IColorizer;
import jutils.iris.data.DirectImage;
import jutils.iris.data.RasterImage;
import jutils.iris.rasters.IRaster;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RasterView implements IView<JComponent>
{
    /**  */
    private final PaintingComponent view;

    /**  */
    private final RasterImage image;

    /***************************************************************************
     * 
     **************************************************************************/
    public RasterView()
    {
        this.image = new RasterImage( 256, 256 );
        this.view = new PaintingComponent( image );
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
     * 
     **************************************************************************/
    public void reset()
    {
        image.reset();
        view.repaint();
    }

    /***************************************************************************
     * @param raster
     **************************************************************************/
    public void set( IRaster raster )
    {
        image.set( raster );
        view.repaint();
    }

    /***************************************************************************
     * @param raster
     * @param colorModel
     **************************************************************************/
    public void set( IRaster raster, IColorizer colorModel )
    {
        this.image.set( raster, colorModel );

        view.repaint();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public IRaster getRaster()
    {
        return image.getRaster();
    }

    /**
     * @return
     */
    public DirectImage getImage()
    {
        return image.getImage();
    }
}
