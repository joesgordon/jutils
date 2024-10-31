package jutils.strip.ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JComponent;

import jutils.core.ui.PaintingComponent;
import jutils.core.ui.model.IView;
import jutils.strip.StripUtils;
import jutils.strip.data.DataBuffer;

/*******************************************************************************
 * 
 ******************************************************************************/
public class StripChartView implements IView<JComponent>
{
    /**  */
    private final StripChartPaintable strip;
    /**  */
    private final PaintingComponent view;

    /***************************************************************************
     * 
     **************************************************************************/
    public StripChartView()
    {
        this.strip = new StripChartPaintable();
        this.view = new PaintingComponent( strip );

        view.setBackground( new Color( StripUtils.BG_DEFAULT ) );
        view.setForeground( new Color( StripUtils.FG_DEFAULT ) );

        view.setMinimumSize( new Dimension( 20, 20 ) );
    }

    /***************************************************************************
     * @param rate
     * @param seriesCount
     **************************************************************************/
    public StripChartView( double rate )
    {
        this();
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
     * @param rate
     * @param seconds
     * @return
     **************************************************************************/
    public DataBuffer addBuffer( double rate, double seconds )
    {
        return strip.addBuffer( rate, seconds );
    }

    /***************************************************************************
     * @param buffer the series to be added.
     **************************************************************************/
    public void addBuffer( DataBuffer buffer )
    {
        strip.addBuffer( buffer );
    }

    /***************************************************************************
     * @param index the index of the buffer (series).
     * @param x the x-value.
     * @param y the y-value.
     **************************************************************************/
    public void add( int index, double x, double y )
    {
        strip.add( index, x, y );
    }
}
