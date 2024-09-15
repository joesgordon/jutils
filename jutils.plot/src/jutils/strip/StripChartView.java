package jutils.strip;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import jutils.core.ui.IPaintable;
import jutils.core.ui.PaintingComponent;
import jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class StripChartView implements IView<JComponent>
{
    /**  */
    public static final int INNER = 6;

    /**  */
    private final PaintingComponent view;
    /**  */
    private final AxisConfig xAxis;
    /**  */
    private final AxisConfig yAxis;
    /**  */
    private final List<DataBuffer> buffers;

    /***************************************************************************
     * 
     **************************************************************************/
    public StripChartView()
    {
        this.view = new PaintingComponent( ( c, g ) -> paintChart( c, g ) );
        this.xAxis = new AxisConfig();
        this.yAxis = new AxisConfig();
        this.buffers = new ArrayList<>( 5 );

        view.setBackground( new Color( ChartUtils.BG_DEFAULT ) );
        view.setForeground( new Color( ChartUtils.FG_DEFAULT ) );

        view.setMinimumSize( new Dimension( 20, 20 ) );

        xAxis.autoBounds = false;
        xAxis.minValue = -30;
        xAxis.tickCount = 6;
        xAxis.tickWidth = 5;
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
     * @param index the index of the buffer (series).
     * @param x the x-value.
     * @param y the y-value.
     **************************************************************************/
    public void add( int index, double x, double y )
    {
        buffers.get( index ).add( x, y );
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
     * @param c the component to be painted.
     * @param g the graphics used for painting.
     * @see IPaintable#paint(JComponent, Graphics2D)
     **************************************************************************/
    private void paintChart( JComponent c, Graphics2D g )
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
        Insets margin = new Insets( INNER, INNER, INNER, INNER );

        int w = c.getWidth();
        int h = c.getHeight();

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

            m.max = 0;
            m.min = -xMets.getRange();
            m.count = xMets.count;

            calcTicks( xAxis, m, ch );
        }

        if( yAxis.autoBounds )
        {
            calcTicks( yAxis, yMets, ch );
        }

        // LogUtils.printDebug( "Metrics: %s", metrics );

        margin.left = ( int )( fm.stringWidth( "" + yAxis.minValue ) * 1.15 ) +
            2 * INNER;

        int cw = w - margin.left - margin.right;
        cw = cw < 50 ? 50 : cw;

        Rectangle2D chartOutline = new Rectangle2D.Float( margin.left,
            margin.top, cw, ch );

        g.setColor( c.getBackground() );
        g.fillRect( 0, 0, w, h );

        g.setColor( c.getForeground() );
        g.draw( chartOutline );

        // g.drawLine( margin.left - 5, margin.top, margin.left, margin.top );

        // LogUtils.printDebug( "Tick count %d", tm.count );
        for( int i = 0; i < xAxis.tickCount; i++ )
        {
            paintxTick( i, chartOutline, fm, g );
        }

        paintxTick( xAxis.tickCount, chartOutline, fm, g );

        for( int i = 0; i < yAxis.tickCount; i++ )
        {
            boolean drawMajor = i != 0;
            paintyTick( i, chartOutline, fm, g, drawMajor );
        }

        paintyTick( yAxis.tickCount, chartOutline, fm, g, false );

        for( int b = 0; b < buffers.size(); b++ )
        {
            DataBuffer buffer = buffers.get( b );

            if( buffer.getSize() < 1 )
            {
                continue;
            }

            paintBuffer( buffer, chartOutline, g, xMets.max );
        }
    }

    /***************************************************************************
     * @param buffer the buffer to be painted.
     * @param c the border dimensions of the chart.
     * @param g the graphics used for painting.
     * @param xmax the maximum x-value from all buffers.
     **************************************************************************/
    private void paintBuffer( DataBuffer buffer, Rectangle2D c, Graphics2D g,
        double xmax )
    {
        double sx = c.getWidth() / xAxis.getRange();
        double sy = c.getHeight() / yAxis.getRange();
        DataPoint p1 = new DataPoint();
        DataPoint p2 = new DataPoint();

        // LogUtils.printDebug( "Chart Right is %f",
        // chartOutline.getX() + chartOutline.getWidth() );

        double chartright = c.getX() + c.getWidth();
        double chartbtm = c.getY() + c.getHeight();

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
            line.y1 = chartbtm - ( p1.y - yAxis.minValue ) * sy;

            line.x2 = chartright + p2.x * sx;
            line.y2 = chartbtm - ( p2.y - yAxis.minValue ) * sy;

            if( line.x2 < c.getX() )
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
        double tw = c.getHeight() / ( yAxis.tickCount );
        int x1 = ( int )( c.getX() - 5 );
        int x2 = ( int )c.getX();
        int y = ( int )( c.getY() + c.getHeight() - i * tw );
        Stroke dashed = new BasicStroke( 1, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_BEVEL, 10.0f, new float[] { 5.0f }, 0.0f );

        // LogUtils.printDebug( "Drawing from %d,%d to %d,%d", x1, y, x2, y
        // );

        String txt = String.format( "%.1f",
            ( yAxis.minValue + i * yAxis.tickWidth ) );
        int txtw = fm.stringWidth( txt );
        int txth = fm.getAscent() / 2 - 1;

        g.drawLine( x1, y, x2, y );
        g.drawString( txt, x1 - txtw, y + txth );

        if( drawMajor )
        {
            Color pc = g.getColor();
            Color clr = new Color( ChartUtils.TICK_DEFAULT );

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
        double tw = c.getWidth() / ( xAxis.tickCount );
        int txth = fm.getAscent();
        int y1 = ( int )( c.getY() + c.getHeight() );
        int y2 = ( int )( c.getY() + c.getHeight() + 5 );
        int x = ( int )( c.getX() + i * tw );
        String txt = String.format( "%.1f",
            ( xAxis.minValue + i * xAxis.tickWidth ) );
        int txtw = fm.stringWidth( txt ) / 2;

        g.drawLine( x, y1, x, y2 );
        g.drawString( txt, x - txtw, y2 + txth + 2 );
    }

    /***************************************************************************
     * @param axis the axis for which ticks and bounds will be calculated.
     * @param metrics the metrics of the data for this axis.
     * @param length the size of this axis in pixels.
     **************************************************************************/
    public static void calcTicks( AxisConfig axis, DataMetrics metrics,
        int length )
    {
        int maxTicks = ( int )Math.ceil(
            length / ( double )ChartUtils.TICK_MIN );
        int minTicks = ( int )Math.floor(
            length / ( double )ChartUtils.TICK_MAX );
        double range = metrics.getRange();

        double rlog = Math.log10( range );
        int order = ( int )Math.floor( rlog );

        // LogUtils.printDebug( "%s, %d", metrics, length, rlog, order );
        // LogUtils.printDebug(
        // "=> minTicks: %d, maxTicks: %d rlog: %f, order: %d", minTicks,
        // maxTicks, rlog, order );

        double div = calcTicksDivisor( range, order, minTicks, maxTicks );

        if( div < 0 )
        {
            order += 1;
            div = calcTicksDivisor( range, order, minTicks, maxTicks );

            if( div < 0 )
            {
                String msg = String.format(
                    "The maths have failed me!!: %s, over %d pixels", metrics,
                    length );
                throw new IllegalStateException( msg );
            }
        }

        int startTc = ( int )Math.floor( metrics.min * div );
        int endTc = ( int )Math.ceil( metrics.max * div );

        // LogUtils.printDebug( "=> startTc: %d, endTc: %d", startTc, endTc
        // );

        double start = startTc / div;
        double end = endTc / div;
        int count = endTc - startTc;
        double width = ( end - start ) / ( count );

        axis.minValue = start;
        axis.tickWidth = width;
        axis.tickCount = count;

        // LogUtils.printDebug(
        // "=> start: %f, end: %f, range: %f, count: %d, width: %f", start,
        // end, end - start, count, width );
        //
        // double twpx = length / ( double )( count - 1 );
        //
        // LogUtils.printDebug( "=> twpx: %f px", twpx );
    }

    /***************************************************************************
     * @param range the range of this axis.
     * @param order the order of the range of this axis.
     * @param minTicks the minimum number of ticks for this axis.
     * @param maxTicks the maximum number of ticks for this axis.
     * @return the divisor calculated or {@code -1} if none valid.
     **************************************************************************/
    private static double calcTicksDivisor( double range, int order,
        int minTicks, int maxTicks )
    {
        double powder = range / Math.pow( 10, order );

        int tc01 = ( int )Math.ceil( powder );
        int tc02 = ( int )Math.ceil( 2.0 * powder );
        int tc05 = ( int )Math.ceil( 5.0 * powder );
        int tc10 = ( int )Math.ceil( 10.0 * powder );

        // LogUtils.printDebug( "=> tc01: %d, tc02: %d, tc05: %d, tc10: %d",
        // tc01, tc02, tc05, tc10 );

        double div = Math.pow( 10, -order );
        if( minTicks <= tc10 && tc10 <= maxTicks )
        {
            div *= 10.0;
        }
        else if( minTicks <= tc05 && tc05 <= maxTicks )
        {
            div *= 5.0;
        }
        else if( minTicks <= tc02 && tc02 <= maxTicks )
        {
            div *= 2.0;
        }
        else if( minTicks <= tc01 && tc01 <= maxTicks )
        {
            div *= 1.0;
        }
        else
        {
            div = -1;
        }

        return div;
    }
}
