package jutils.iris.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import jutils.core.ui.hex.ByteBuffer;
import jutils.core.ui.hex.HexPanel;
import jutils.core.ui.model.IView;
import jutils.iris.data.RasterImage;
import jutils.iris.rasters.IRaster;
import jutils.math.Histogram;
import jutils.math.ui.HistogramView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ImageInfoView implements IView<JComponent>
{
    /**  */
    private final JComponent view;

    /**  */
    private final HistogramView histView;
    /**  */
    private final HoverView hoverView;

    /**  */
    private final PixelsView pixelsView;
    /**  */
    private final PlanesView planesView;
    /**  */
    private final ChannelsView rawPixelView;
    /**  */
    private final HexPanel bufferView;

    /***************************************************************************
     * 
     **************************************************************************/
    public ImageInfoView()
    {
        this.histView = new HistogramView();
        this.hoverView = new HoverView();
        this.pixelsView = new PixelsView();
        this.planesView = new PlanesView();
        this.rawPixelView = new ChannelsView();
        this.bufferView = new HexPanel();

        this.view = createView();

        this.histView.getView().setPreferredSize( new Dimension( 150, 150 ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab( "Metrics", createMetricsPanel() );
        tabs.addTab( "Pixels", pixelsView.getView() );
        tabs.addTab( "Planes", planesView.getView() );
        tabs.addTab( "Raw Pixels", rawPixelView.getView() );
        tabs.addTab( "Raw Bytes", bufferView.getView() );

        return tabs;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createMetricsPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 4, 4, 4, 4 ), 0, 0 );
        panel.add( hoverView.getView(), constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 4, 4, 4, 4 ), 0, 0 );
        panel.add( histView.getView(), constraints );

        constraints = new GridBagConstraints( 0, 1, 3, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 4, 4, 4, 4 ), 0, 0 );
        panel.add( Box.createHorizontalStrut( 0 ), constraints );

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
     * @param x
     * @param y
     * @param image
     **************************************************************************/
    public void captureHoverImage( int x, int y, RasterImage image )
    {
        hoverView.captureHoverImage( x, y, image );
    }

    /***************************************************************************
     * @param image
     **************************************************************************/
    public void setRaster( RasterImage image )
    {
        IRaster r = image.getRaster();

        Histogram histogram = new Histogram( "Mono", 256,
            r.getConfig().getMaxPixelValue() );

        int count = r.getConfig().getPixelCount();
        for( int i = 0; i < count; i++ )
        {
            histogram.addValue( ( int )r.getPixel( i ) );
        }

        histView.setData( histogram );
        pixelsView.setRaster( image.getImage() );
        rawPixelView.setRaster( r );
        bufferView.setBuffer( new ByteBuffer( r.getBufferData() ) );
    }
}
