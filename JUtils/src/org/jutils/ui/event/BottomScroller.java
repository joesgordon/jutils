package org.jutils.ui.event;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JScrollBar;

/*******************************************************************************
 * Listener that keeps a JScrollPane at the bottom when it was at the bottom.
 ******************************************************************************/
public final class BottomScroller implements AdjustmentListener
{
    /** The vertical scrollbar to be monitored. */
    private final JScrollBar verticalBar;

    /**
     * The as maximum value as returned by {@link JScrollBar#getMaximum()} minus
     * the extent ({@code JScrollBar.getModel().getExtent()}).
     */
    private int lastMax;

    /***************************************************************************
     * Creates a new scroller.
     * @param verticalBar the vertical scroll bar to be monitored.
     **************************************************************************/
    public BottomScroller( JScrollBar verticalBar )
    {
        this.verticalBar = verticalBar;

        this.lastMax = 0;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void adjustmentValueChanged( AdjustmentEvent e )
    {
        int value = verticalBar.getValue();
        int extent = verticalBar.getModel().getExtent();
        int max = verticalBar.getMaximum() - extent;

        if( !e.getValueIsAdjusting() )
        {
            // LogUtils.printDebug(
            // "value = %d, max = %d, last value = %d, last max = %d, extent
            // = %d",
            // value, max, lastValue, lastMax, extent );

            if( value == lastMax && lastMax != max )
            {
                // LogUtils.printDebug( "setting to %d", max );
                verticalBar.setValue( max );
            }
        }

        lastMax = max;
    }
}
