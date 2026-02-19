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
     * @param g
     **************************************************************************/
    @Override
    public void paint( Graphics2D g )
    {
        Rectangle bounds = g.getClipBounds();

        int tickFontHeight = properties.primaryAxes.domain.majorTicks.textFont.getSize();
        int tickFontMargin = 5;

        int margin = 4;

        // ---------------------------------------------------------------------
        // Determine top area
        // ---------------------------------------------------------------------
        Rectangle top = new Rectangle();

        // ---------------------------------------------------------------------
        // Determine bottom area
        // ---------------------------------------------------------------------
        Rectangle bottom = new Rectangle();

        // ---------------------------------------------------------------------
        // Determine left area
        // ---------------------------------------------------------------------
        Rectangle left = new Rectangle();

        // ---------------------------------------------------------------------
        // Determine top area
        // ---------------------------------------------------------------------
        Rectangle right = new Rectangle();

        int areaMargin = margin + tickFontHeight + tickFontMargin;

        Rectangle areaBounds = new Rectangle();

        areaBounds.x = margin;
        areaBounds.y = areaBounds.x;
        areaBounds.width = bounds.width - 2 * areaBounds.x;
        areaBounds.height = bounds.height - 2 * areaBounds.x;

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
