package jutils.iris.ui;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import jutils.core.ui.hex.ByteBuffer;
import jutils.core.ui.hex.HexPanel;
import jutils.core.ui.model.IView;
import jutils.iris.data.RasterImage;
import jutils.iris.rasters.IRaster;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ImageInfoView implements IView<JComponent>
{
    /**  */
    private final JComponent view;

    /**  */
    private final MetricsView metricsView;
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
        this.metricsView = new MetricsView();
        this.pixelsView = new PixelsView();
        this.planesView = new PlanesView();
        this.rawPixelView = new ChannelsView();
        this.bufferView = new HexPanel();

        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab( "Metrics", metricsView.getView() );
        tabs.addTab( "Pixels", pixelsView.getView() );
        tabs.addTab( "Planes", planesView.getView() );
        tabs.addTab( "Raw Pixels", rawPixelView.getView() );
        tabs.addTab( "Raw Bytes", bufferView.getView() );

        return tabs;
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
     * @param image
     **************************************************************************/
    public void setRaster( RasterImage image )
    {
        IRaster r = image.getRaster();

        metricsView.setRaster( r );
        pixelsView.setImage( image.getImage() );
        planesView.setRaster( r );
        rawPixelView.setRaster( r );
        bufferView.setBuffer( new ByteBuffer( r.getBufferData() ) );
    }

    /***************************************************************************
     * @param x
     * @param y
     * @param rasterImage
     **************************************************************************/
    public void captureHoverImage( int x, int y, RasterImage rasterImage )
    {
        metricsView.captureHoverImage( x, y, rasterImage );
    }
}
