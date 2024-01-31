package jutils.iris.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JPanel;

import jutils.core.ui.model.IView;
import jutils.iris.colors.IColorizer;
import jutils.iris.data.IRaster;
import jutils.iris.data.RasterConfig;
import jutils.math.Histogram;
import jutils.math.ui.HistogramView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ImageView implements IView<JComponent>
{
    /**  */
    private final RasterView rasterView;
    /**  */
    private final HistogramView histView;

    /***************************************************************************
     * 
     **************************************************************************/
    public ImageView()
    {
        this.rasterView = new RasterView();
        this.histView = new HistogramView();

        this.histView.getView().setPreferredSize( new Dimension( 150, 150 ) );

    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( rasterView.getView(), BorderLayout.CENTER );
        panel.add( histView.getView(), BorderLayout.SOUTH );

        return panel;
    }

    /***************************************************************************
     * @param r
     * @param c
     **************************************************************************/
    public void setRaster( IRaster r, IColorizer c )
    {
        RasterConfig config = r.getConfig();
        int binCount = config.channels[0].getPixelValueCount();
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
}
