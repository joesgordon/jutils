package jutils.iris.ui;

import java.awt.Dialog.ModalityType;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;

import jutils.core.ui.OkDialogView;
import jutils.core.ui.OkDialogView.OkDialogButtons;
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
    private final JPanel view;
    /**  */
    private final RasterView rasterView;
    /**  */
    private final ImageInfoView infoView;

    /**  */
    private OkDialogView infoDialog;

    /***************************************************************************
     * 
     **************************************************************************/
    public ImageView()
    {
        this.rasterView = new RasterView();
        this.infoView = new ImageInfoView();
        this.view = createView();

        this.infoDialog = null;

        rasterView.setHoverCallback( ( p ) -> infoView.captureHoverImage( p.x,
            p.y, rasterView.getRasterImage() ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        JScrollPane pane = new JScrollPane( rasterView.getView() );

        pane.setBorder( new LineBorder( IrisUtils.BORDER_COLOR ) );
        pane.getVerticalScrollBar().setUnitIncrement( 12 );
        pane.setHorizontalScrollBarPolicy(
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS );
        pane.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );

        constraints = new GridBagConstraints( 0, 0, 2, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 4, 4, 4, 4 ), 0, 0 );
        panel.add( pane, constraints );

        return panel;
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
        if( visible && infoDialog == null )
        {
            infoDialog = new OkDialogView( getView(), infoView.getView(),
                ModalityType.MODELESS, OkDialogButtons.OK_CANCEL );
            infoDialog.setTitle( "Image Information" );
            infoDialog.setSize( 512, 512 );
            infoDialog.getView().setLocationRelativeTo( getView() );
        }

        infoDialog.getView().setVisible( visible );
    }
}
