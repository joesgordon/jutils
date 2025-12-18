package jutils.math.charts;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;

import jutils.core.ui.IPainter;
import jutils.math.charts.props.ChartProperties;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChartPainter implements IPainter
{
    /**  */
    private ChartProperties properties;
    /**  */
    private IChartPainter chart;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChartPainter()
    {
        this.properties = new ChartProperties();
        this.chart = new PlotChartPainter();
    }

    /***************************************************************************
     * @param c
     * @param g
     **************************************************************************/
    @Override
    public void paint( Graphics2D g )
    {
        Rectangle bounds = g.getClipBounds();

        int margin = 14;
        int fontHeight = properties.primaryAxes.domain.majorTicks.textFont.getSize();
        int fontMargin = 5;
        int areaMargin = margin + fontHeight + fontMargin;

        Rectangle areaBounds = new Rectangle();

        areaBounds.x = areaMargin;
        areaBounds.y = areaMargin;
        areaBounds.width = bounds.width - 2 * areaMargin;
        areaBounds.height = bounds.height - 2 * areaMargin;

        areaBounds.width = Math.max( 100, areaBounds.width );
        areaBounds.height = Math.max( 100, areaBounds.height );

        Stroke solid = new BasicStroke( 2, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER );

        g.setStroke( solid );

        g.setColor( properties.background );
        g.fill( bounds );

        // Rectangle2D.Float plot = new Rectangle2D.Float( px, py, pw, ph );

        g.setColor( properties.axisColor );
        g.draw( areaBounds );

        chart.paint( g );
    }
}
