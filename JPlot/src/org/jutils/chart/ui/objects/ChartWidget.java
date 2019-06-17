package org.jutils.chart.ui.objects;

import java.awt.*;

import org.jutils.chart.model.Chart;
import org.jutils.chart.model.HorizontalAlignment;
import org.jutils.chart.ui.IChartWidget;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChartWidget implements IChartWidget
{
    /**  */
    public final PlotContext context;
    /**  */
    public final LegendWidget legend;
    /**  */
    public final TextWidget title;
    /**  */
    public final TextWidget subtitle;
    /**  */
    public final AxesWidget axes;
    /**  */
    public final PlotsWidget plots;
    /**  */
    public final TextWidget topBottom;

    /**  */
    final Chart chart;

    /***************************************************************************
     * @param chart
     **************************************************************************/
    public ChartWidget( Chart chart )
    {
        this.chart = chart;

        this.context = new PlotContext( chart.domainAxis, chart.rangeAxis,
            chart.secDomainAxis, chart.secRangeAxis );
        this.topBottom = new TextWidget( chart.topBottomLabel );
        this.title = new TextWidget( chart.title );
        this.subtitle = new TextWidget( chart.subtitle );
        this.plots = new PlotsWidget( context );
        this.axes = new AxesWidget( context, chart );
        this.legend = new LegendWidget( chart.legend, plots.plots );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Dimension calculateSize( Dimension canvasSize )
    {
        return layout( canvasSize );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public Dimension layout( Dimension size )
    {
        // TODO Auto-generated method stub
        return size;
    }

    /***************************************************************************
     * Draws the chart in the following order:<ol> <li>Top/Bottom Labels</li>
     * <li>Title</li> <li>Subtitle</li> <li>Legend</li> <li>Axes</li>
     * <li>Plot</li> </ol>
     **************************************************************************/
    @Override
    public void draw( Graphics2D graphics, Point location, Dimension size )
    {
        // LogUtils.printDebug( "chart: w: " + width + ", h: " + height );

        Point wLoc = new Point( location.x, location.y );
        Dimension wSize = new Dimension( size );

        // ---------------------------------------------------------------------
        // Draw top/bottom
        // ---------------------------------------------------------------------
        wLoc.x += 10;
        wSize.width -= 20;
        graphics.setColor( Color.white );
        graphics.fillRect( location.x, location.y, size.width, size.height );

        drawTopBottom( graphics, wLoc, wSize );

        drawTitle( graphics, wLoc, wSize );

        drawSubtitle( graphics, wLoc, wSize );

        // ---------------------------------------------------------------------
        //
        // ---------------------------------------------------------------------
        wLoc.y += 10;
        wSize.height -= 20;

        drawLegend( graphics, wLoc, wSize );

        // graphics.setStroke( new BasicStroke( 1 ) );
        // graphics.setColor( Color.red );
        // graphics.drawRect( wLoc.x, wLoc.y, wSize.width - 1, wSize.height - 1
        // );

        // ---------------------------------------------------------------------
        // Draw axes.
        // ---------------------------------------------------------------------
        axes.draw( graphics, wLoc, wSize );

        // ---------------------------------------------------------------------
        // Draw plot.
        // ---------------------------------------------------------------------
        wLoc = new Point( context.x, context.y );
        wSize = new Dimension( context.width, context.height );
        plots.layout( wSize );
        plots.draw( ( Graphics2D )graphics.create( wLoc.x, wLoc.y, wSize.width,
            wSize.height ), null, null );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void clear()
    {
        plots.plots.clear();
    }

    /***************************************************************************
     * @param graphics
     * @param wLoc
     * @param wSize
     **************************************************************************/
    private void drawSubtitle( Graphics2D graphics, Point wLoc,
        Dimension wSize )
    {
        if( chart.subtitle.visible )
        {
            int titleHeight = subtitle.calculateSize( wSize ).height;

            wLoc.y += 4;

            subtitle.draw( graphics, wLoc, wSize );

            wLoc.y += titleHeight;

            titleHeight += 4;

            wSize.height -= titleHeight;
        }
    }

    /***************************************************************************
     * @param graphics
     * @param wLoc
     * @param wSize
     **************************************************************************/
    private void drawTopBottom( Graphics2D graphics, Point wLoc,
        Dimension wSize )
    {
        if( chart.topBottomLabel.visible )
        {
            Dimension size = topBottom.calculateSize( wSize );
            Point loc = new Point( wLoc );

            size.width = wSize.width;

            loc.y += 4;
            chart.topBottomLabel.alignment = HorizontalAlignment.LEFT;
            topBottom.draw( graphics, loc, size );

            loc.y = wSize.height - size.height - 4;
            chart.topBottomLabel.alignment = HorizontalAlignment.RIGHT;
            topBottom.draw( graphics, loc, size );

            wLoc.y += size.height + 4;
            wSize.height -= ( 2 * size.height + 8 );
        }
    }

    /***************************************************************************
     * @param graphics
     * @param wLoc
     * @param wSize
     **************************************************************************/
    private void drawTitle( Graphics2D graphics, Point wLoc, Dimension wSize )
    {
        if( chart.title.visible )
        {
            Dimension ts = title.calculateSize( wSize );

            ts.width = wSize.width;

            wLoc.y += 10;

            title.draw( graphics, wLoc, ts );

            wLoc.y += ts.height;

            wSize.height -= ( ts.height + 10 );
        }
    }

    /***************************************************************************
     * @param graphics
     * @param wLoc
     * @param wSize
     **************************************************************************/
    private void drawLegend( Graphics2D graphics, Point wLoc, Dimension wSize )
    {
        // graphics.setColor( Color.green );
        // graphics.drawRect( wLoc.x, wLoc.y, wSize.width, wSize.height );

        if( chart.legend.visible )
        {
            Dimension size = new Dimension( wSize );
            Point loc = new Point( wLoc );
            size = legend.layout( size );

            switch( chart.legend.side )
            {
                case TOP:
                    size.width = wSize.width;
                    break;

                case LEFT:
                    size.height = wSize.height;
                    break;

                case BOTTOM:
                    loc.y = wLoc.y + wSize.height - size.height;
                    size.width = wSize.width;
                    break;

                case RIGHT:
                    loc.x = wLoc.x + wSize.width - size.width;
                    size.height = wSize.height;
                    break;
            }

            size = legend.layout( size );

            legend.repaint();
            legend.draw( ( Graphics2D )graphics.create( loc.x, loc.y,
                size.width, size.height ), null, null );

            switch( chart.legend.side )
            {
                case TOP:
                    wLoc.y += size.height + 8;
                    wSize.height -= ( size.height + 8 );
                    break;

                case LEFT:
                    wLoc.x += size.width + 8;
                    wSize.width -= ( size.width + 8 );
                    break;

                case BOTTOM:
                    wSize.height -= ( size.height + 8 );
                    break;

                case RIGHT:
                    wSize.width -= ( size.width + 8 );
                    break;
            }
        }
    }

    /***************************************************************************
     * @param visible
     **************************************************************************/
    public void setTrackingVisible( boolean visible )
    {
        for( PlotWidget s : plots.plots )
        {
            s.trackPoint = visible;
        }

        plots.highlightLayer.repaint = true;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void restoreAutoBounds()
    {
        context.restoreAutoBounds();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void calculateAutoBounds()
    {
        context.calculateAutoBounds( chart.series );
    }

    /***************************************************************************
     * @param b
     **************************************************************************/
    public void latchBounds()
    {
        context.latchCoords();

        axes.axesLayer.repaint = true;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void repaint()
    {
        title.repaint();
        subtitle.repaint();
        topBottom.repaint();
        legend.repaint();
        plots.repaint();
        axes.repaint();
    }
}
