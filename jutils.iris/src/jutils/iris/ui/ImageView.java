package jutils.iris.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;

import jutils.core.ui.ComponentView;
import jutils.core.ui.model.IView;
import jutils.iris.IrisUtils;
import jutils.iris.colors.IColorizer;
import jutils.iris.data.DirectImage;
import jutils.iris.rasters.IRaster;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ImageView implements IView<JComponent>
{
    /**  */
    private final JComponent view;
    /**  */
    private final RasterView rasterView;
    /**  */
    private final ComponentView rightView;
    /**  */
    private final ImageInfoView infoView;

    /***************************************************************************
     * 
     **************************************************************************/
    public ImageView()
    {
        this.rasterView = new RasterView();
        this.infoView = new ImageInfoView();
        this.rightView = new ComponentView();

        this.view = createView();

        rasterView.setHoverCallback( ( p ) -> infoView.captureHoverImage( p.x,
            p.y, rasterView.getRasterImage() ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( createPane(), constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( rightView.getView(), constraints );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createPane()
    {
        JScrollPane pane = new JScrollPane( rasterView.getView() );

        pane.setBorder( new LineBorder( IrisUtils.BORDER_COLOR ) );
        pane.getVerticalScrollBar().setUnitIncrement( 12 );
        pane.setHorizontalScrollBarPolicy(
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS );
        pane.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );

        return pane;
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
    public void resetRaster()
    {
        rasterView.reset();
    }

    /***************************************************************************
     * @param r
     * @param c
     **************************************************************************/
    public void setRaster( IRaster r, IColorizer c )
    {
        // RasterConfig config = r.getConfig();
        // int binCount = config.channels[0].getPixelValueCount();

        rasterView.set( r, c );
        infoView.setRaster( rasterView.getRasterImage() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public IRaster getRaster()
    {
        return rasterView.getRaster();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public DirectImage getImage()
    {
        return rasterView.getImage();
    }

    /***************************************************************************
     * @param visible
     **************************************************************************/
    public void setInfoVisible( boolean visible )
    {
        if( visible )
        {
            rightView.setComponent( infoView.getView() );
        }
        else if( !visible )
        {
            rightView.setComponent( null );
        }

        view.invalidate();
        view.revalidate();
        view.repaint();
    }
}
