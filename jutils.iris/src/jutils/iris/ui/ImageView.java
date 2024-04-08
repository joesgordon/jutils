package jutils.iris.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;

import jutils.core.ui.model.IView;
import jutils.iris.IrisUtils;
import jutils.iris.colors.IColorizer;
import jutils.iris.data.DirectImage;
import jutils.iris.rasters.IRaster;
import jutils.math.Histogram;
import jutils.math.ui.HistogramView;

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
    private final HistogramView histView;
    /**  */
    private final HoverView hoverView;

    /***************************************************************************
     * 
     **************************************************************************/
    public ImageView()
    {
        this.rasterView = new RasterView();
        this.histView = new HistogramView();
        this.hoverView = new HoverView();
        this.view = createView();

        this.histView.getView().setPreferredSize( new Dimension( 150, 150 ) );

        rasterView.setHoverCallback( ( p ) -> hoverView.captureHoverImage( p.x,
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

        constraints = new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 4, 4, 4, 4 ), 0, 0 );
        panel.add( hoverView.getView(), constraints );

        constraints = new GridBagConstraints( 1, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 4, 4, 4, 4 ), 0, 0 );
        panel.add( histView.getView(), constraints );

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
        Histogram histogram = new Histogram( "Mono", 256,
            r.getConfig().getMaxPixelValue() );

        int count = r.getConfig().getPixelCount();
        for( int i = 0; i < count; i++ )
        {
            histogram.addValue( ( int )r.getPixel( i ) );
        }

        rasterView.set( r, c );
        histView.setData( histogram );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public IRaster getRaster()
    {
        return rasterView.getRaster();
    }

    public DirectImage getImage()
    {
        return rasterView.getImage();
    }
}
