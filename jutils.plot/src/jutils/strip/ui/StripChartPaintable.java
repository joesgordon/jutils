package jutils.strip.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import jutils.core.ui.IPaintable;
import jutils.strip.StripUtils;
import jutils.strip.data.AxisConfig;
import jutils.strip.data.DataBuffer;
import jutils.strip.data.DataMetrics;
import jutils.strip.data.DataPoint;

/*******************************************************************************
 * 
 ******************************************************************************/
public class StripChartPaintable implements IPaintable
{
    /**  */
    public static final int INNER = 6;

    /**  */
    private final AxisConfig xAxis;
    /**  */
    private final AxisConfig yAxis;
    /**  */
    private final List<DataBuffer> buffers;

    /**  */
    private Color background;
    /**  */
    private Color foreground;

    /***************************************************************************
     * 
     **************************************************************************/
    public StripChartPaintable()
    {
        this.xAxis = new AxisConfig();
        this.yAxis = new AxisConfig();
        this.buffers = new ArrayList<>( 5 );

        this.background = new Color( StripUtils.BG_DEFAULT );
        this.foreground = new Color( StripUtils.FG_DEFAULT );

        xAxis.autoBounds = false;
        xAxis.ticks.minValue = -30;
        xAxis.ticks.sectionCount = 6;
        xAxis.ticks.tickWidth = 5;
    }

    /***************************************************************************
     * @param rate
     * @param seconds
     * @return
     **************************************************************************/
    public DataBuffer addBuffer( double rate, double seconds )
    {
        double r = rate < 1.0 ? 1.0 : rate;
        int count = ( int )( 1000 * seconds / r * 2 );
        DataBuffer buf = new DataBuffer( count );

        addBuffer( buf );

        return buf;
    }

    /***************************************************************************
     * @param buffer the series to be added.
     **************************************************************************/
    public void addBuffer( DataBuffer buffer )
    {
        buffers.add( buffer );
    }

    /***************************************************************************
     * @param index the index of the buffer (series).
     * @param x the x-value.
     * @param y the y-value.
     **************************************************************************/
    public void add( int index, double x, double y )
    {
        buffers.get( index ).add( x, y );
    }

    /***************************************************************************
     * @param c the component to be painted.
     * @param g the graphics used for painting.
     * @see ICompaintable#paint(JComponent, Graphics2D)
     **************************************************************************/
    @Override
    public void paint( JComponent c, Graphics2D g )
    {
        g.setRenderingHint( RenderingHints.KEY_ALPHA_INTERPOLATION,
            RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY );
        g.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON );
        g.setRenderingHint( RenderingHints.KEY_COLOR_RENDERING,
            RenderingHints.VALUE_COLOR_RENDER_QUALITY );
        g.setRenderingHint( RenderingHints.KEY_DITHERING,
            RenderingHints.VALUE_DITHER_ENABLE );
        g.setRenderingHint( RenderingHints.KEY_FRACTIONALMETRICS,
            RenderingHints.VALUE_FRACTIONALMETRICS_ON );
        g.setRenderingHint( RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BILINEAR );
        g.setRenderingHint( RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY );
        g.setRenderingHint( RenderingHints.KEY_STROKE_CONTROL,
            RenderingHints.VALUE_STROKE_PURE );
        g.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON );

        FontMetrics fm = g.getFontMetrics();
        Rectangle bounds = g.getClipBounds();
        Insets margin = new Insets( INNER, INNER, INNER, INNER );

        int w = bounds.width - 8;
        int h = bounds.height - 2;

        margin.top += 4;
        margin.bottom = 2 * INNER + ( int )( fm.getHeight() * 1.2 );

        int ch = h - margin.bottom - margin.top;
        ch = ch < 20 ? 20 : ch;

        DataMetrics xMets = new DataMetrics();
        DataMetrics yMets = new DataMetrics();

        for( int i = 0; i < buffers.size(); i++ )
        {
            DataBuffer b = buffers.get( i );

            DataMetrics m;

            m = new DataMetrics();
            b.getXMetrics( m );
            xMets.update( m );

            m = new DataMetrics();
            b.getYMetrics( m );
            yMets.update( m );
        }

        if( xAxis.autoBounds )
        {
            DataMetrics m = new DataMetrics();

            m.range.max = 0;
            m.range.min = -xMets.getRange();
            m.count = xMets.count;

            StripUtils.calcTicks( xAxis.ticks, m.range, ch );
        }

        if( yAxis.autoBounds )
        {
            StripUtils.calcTicks( yAxis.ticks, yMets.range, ch );
        }

        // LogUtils.printDebug( "Metrics: %s", metrics );

        margin.left = ( int )( fm.stringWidth( "" + yAxis.ticks.minValue ) *
            1.15 ) + 2 * INNER;

        int cw = w - margin.left - margin.right;
        cw = cw < 50 ? 50 : cw;

        Rectangle2D chartOutline = new Rectangle2D.Float( margin.left,
            margin.top, cw, ch );

        g.setColor( background );
        g.fillRect( 0, 0, w, h );

        g.setColor( foreground );
        g.draw( chartOutline );

        // g.drawLine( margin.left - 5, margin.top, margin.left, margin.top );

        // LogUtils.printDebug( "Tick count %d", tm.count );
        for( int i = 0; i < xAxis.ticks.sectionCount; i++ )
        {
            paintxTick( i, chartOutline, fm, g );
        }

        paintxTick( xAxis.ticks.sectionCount, chartOutline, fm, g );

        for( int i = 0; i < yAxis.ticks.sectionCount; i++ )
        {
            boolean drawMajor = i != 0;
            paintyTick( i, chartOutline, fm, g, drawMajor );
        }

        paintyTick( yAxis.ticks.sectionCount, chartOutline, fm, g, false );

        for( int b = 0; b < buffers.size(); b++ )
        {
            DataBuffer buffer = buffers.get( b );

            if( buffer.getSize() < 1 )
            {
                continue;
            }

            paintBuffer( buffer, chartOutline, g, xMets.range.max );
        }
    }

    /***************************************************************************
     * @param buffer the buffer to be painted.
     * @param chart the border dimensions of the chart.
     * @param g the graphics used for painting.
     * @param xmax the maximum x-value from all buffers.
     **************************************************************************/
    private void paintBuffer( DataBuffer buffer, Rectangle2D chart,
        Graphics2D g, double xmax )
    {
        double sx = chart.getWidth() / xAxis.getRange();
        double sy = chart.getHeight() / yAxis.getRange();
        DataPoint p1 = new DataPoint();
        DataPoint p2 = new DataPoint();

        // LogUtils.printDebug( "Chart Right is %f",
        // chartOutline.getX() + chartOutline.getWidth() );

        double chartright = chart.getX() + chart.getWidth();
        double chartbtm = chart.getY() + chart.getHeight();

        Stroke solid = new BasicStroke( 2, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER );

        Color pc = g.getColor();
        Stroke s = g.getStroke();

        g.setColor( buffer.getColor() );
        g.setStroke( solid );

        buffer.get( p1, buffer.getSize() - 1 );
        p1.x -= xmax;

        Line2D.Double line = new Line2D.Double();

        for( int i = buffer.getSize() - 1; i > 0; i-- )
        {
            buffer.get( p2, i - 1 );
            p2.x -= xmax;

            // int ri = buffer.getSize() - 1 - i;
            // int x1 = ( int )( chartright - ri );
            // int x2 = ( int )( chartright - ri - 1 );

            // int x1 = ( int )Math.round( chartright + p1.x * sx );
            // int x2 = ( int )Math.round( chartright + p2.x * sx );

            // x2 = x2 == x1 ? x2 - 1 : x2;

            // int y1 = ( int )Math.round(
            // chartbtm - ( p1.y - yAxis.minValue ) * sy );
            // int y2 = ( int )Math.round(
            // chartbtm - ( p2.y - yAxis.minValue ) * sy );

            line.x1 = chartright + p1.x * sx;
            line.y1 = chartbtm - ( p1.y - yAxis.ticks.minValue ) * sy;

            line.x2 = chartright + p2.x * sx;
            line.y2 = chartbtm - ( p2.y - yAxis.ticks.minValue ) * sy;

            if( line.x2 < chart.getX() )
            {
                break;
            }
            // else if( Math.abs( line.x2 - line.x1 ) > 0.01 )
            {
                // LogUtils.printDebug( "Drawing from %d,%d to %d,%d", x1, y1,
                // x2, y2 );
                // g.drawLine( x1, y1, x2, y2 );

                g.draw( line );

                p1.x = p2.x;
                p1.y = p2.y;
            }
        }

        g.setColor( pc );
        g.setStroke( s );
    }

    /***************************************************************************
     * @param i the index of the tick to be painted.
     * @param c the border dimensions of the chart.
     * @param fm the font metrics for the provided graphics.
     * @param g the graphics used for painting.
     * @param drawMajor {@code true} if major lines are to be drawn.
     **************************************************************************/
    private void paintyTick( int i, Rectangle2D c, FontMetrics fm, Graphics2D g,
        boolean drawMajor )
    {
        double tw = c.getHeight() / ( yAxis.ticks.sectionCount );
        int x1 = ( int )( c.getX() - 5 );
        int x2 = ( int )c.getX();
        int y = ( int )( c.getY() + c.getHeight() - i * tw );
        Stroke dashed = new BasicStroke( 1, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_BEVEL, 10.0f, new float[] { 5.0f }, 0.0f );

        // LogUtils.printDebug( "Drawing from %d,%d to %d,%d", x1, y, x2, y
        // );

        String txt = String.format( "%.1f",
            ( yAxis.ticks.minValue + i * yAxis.ticks.tickWidth ) );
        int txtw = fm.stringWidth( txt );
        int txth = fm.getAscent() / 2 - 1;

        g.drawLine( x1, y, x2, y );
        g.drawString( txt, x1 - txtw - 2, y + txth );

        if( drawMajor )
        {
            Color pc = g.getColor();
            Color clr = new Color( StripUtils.TICK_DEFAULT );

            Stroke s = g.getStroke();

            g.setStroke( dashed );
            g.setColor( clr );

            x1 = x2;
            x2 = ( int )( x1 + c.getWidth() );

            g.drawLine( x1, y, x2, y );

            g.setStroke( s );
            g.setColor( pc );
        }
    }

    /***************************************************************************
     * @param i the index of the tick to be painted.
     * @param c the border dimensions of the chart.
     * @param fm the font metrics for the provided graphics.
     * @param g the graphics used for painting.
     **************************************************************************/
    private void paintxTick( int i, Rectangle2D c, FontMetrics fm,
        Graphics2D g )
    {
        double tw = c.getWidth() / ( xAxis.ticks.sectionCount );
        int y1 = ( int )( c.getY() + c.getHeight() );
        int y2 = ( int )( c.getY() + c.getHeight() + 5 );
        int x = ( int )( c.getX() + i * tw );

        g.drawLine( x, y1, x, y2 );

        String txt = String.format( "%.1f",
            ( xAxis.ticks.minValue + i * xAxis.ticks.tickWidth ) );

        int txth = fm.getAscent();
        int txtw = fm.stringWidth( txt ) / 2;

        g.drawString( txt, x - txtw, y2 + txth + 2 );
    }
}
