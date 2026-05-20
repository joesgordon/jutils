package jutils.math.charts.ui;

import javax.swing.JComponent;

import jutils.core.ui.PainterComponent;
import jutils.core.ui.model.IView;
import jutils.math.charts.ChartPainter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DistributionView implements IView<JComponent>
{
    /**  */
    public final ChartPainter chart;
    /**  */
    private final PainterComponent view;

    /***************************************************************************
     * 
     **************************************************************************/
    public DistributionView()
    {
        this.chart = new ChartPainter();
        this.view = new PainterComponent( chart );
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
