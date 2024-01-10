package jutils.iris.ui;

import java.awt.Graphics2D;

import javax.swing.JComponent;

import jutils.core.ui.PaintingComponent;
import jutils.core.ui.model.IView;
import jutils.iris.data.IRaster;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RasterView implements IView<JComponent>
{
    /**  */
    private final PaintingComponent view;

    /**  */
    private IRaster raster;

    /***************************************************************************
     * 
     **************************************************************************/
    public RasterView()
    {
        this.view = new PaintingComponent( ( c, g ) -> paintRaster( c, g ) );
        this.raster = null;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        return view;
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

        // TODO Auto-generated method stub
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }
}
