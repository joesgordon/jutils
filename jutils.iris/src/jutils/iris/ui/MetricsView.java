package jutils.iris.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jutils.core.ui.model.IView;
import jutils.iris.IrisUtils;
import jutils.iris.data.RasterImage;
import jutils.iris.rasters.IChannel;
import jutils.iris.rasters.IRaster;
import jutils.math.Histogram.HistogramCalc;
import jutils.math.ui.HistogramView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MetricsView implements IView<JComponent>
{
    /**  */
    private final JComponent view;
    /**  */
    private final HistogramView [] histViews;
    /**  */
    private final HoverView hoverView;

    /***************************************************************************
     * 
     **************************************************************************/
    public MetricsView()
    {
        this.histViews = new HistogramView[IRaster.MAX_CHANNELS];
        this.hoverView = new HoverView();

        for( int c = 0; c < histViews.length; c++ )
        {
            histViews[c] = new HistogramView();

            JComponent v = histViews[c].getView();
            v.setPreferredSize( new Dimension( 512, 150 ) );
            v.setMaximumSize( v.getPreferredSize() );
        }

        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JScrollPane pane = new JScrollPane( createPanel() );

        pane.getVerticalScrollBar().setUnitIncrement( 12 );

        return pane;
    }

    private JComponent createPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        int r = 0;

        constraints = new GridBagConstraints( 0, r++, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 4, 4, 4, 4 ), 0, 0 );
        panel.add( hoverView.getView(), constraints );

        for( int c = 0; c < histViews.length; c++ )
        {
            JComponent hv = histViews[c].getView();

            constraints = new GridBagConstraints( 0, r++, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 4, 4, 4, 4 ), 0, 0 );
            panel.add( hv, constraints );
        }

        constraints = new GridBagConstraints( 0, r++, 3, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 4, 4, 4, 4 ), 0, 0 );
        panel.add( Box.createHorizontalStrut( 0 ), constraints );

        return panel;
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
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * @param r
     **************************************************************************/
    public void setRaster( IRaster r )
    {
        int channelCount = r.getChannelCount();

        for( int c = 0; c < histViews.length; c++ )
        {
            if( c < channelCount )
            {
                setHist( r, c );
                histViews[c].getView().setVisible( true );
            }
            else
            {
                histViews[c].getView().setVisible( false );
            }
        }
    }

    /***************************************************************************
     * @param r
     * @param c
     **************************************************************************/
    private void setHist( IRaster r, int c )
    {
        IChannel channel = r.getChannel( c );
        int maxValue = IrisUtils.getMaxValue( channel.getBitDepth() );
        int count = channel.getSize();

        HistogramCalc histCalc = new HistogramCalc( 256, 0, maxValue );

        for( int i = 0; i < count; i++ )
        {
            histCalc.addValue( channel.getValue( i ) );
        }

        histViews[c].setData( histCalc.histogram );
    }
}
