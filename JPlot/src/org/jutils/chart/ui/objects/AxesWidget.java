package org.jutils.chart.ui.objects;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import org.jutils.Utils;
import org.jutils.chart.data.Tick;
import org.jutils.chart.data.TickGen;
import org.jutils.chart.model.*;
import org.jutils.chart.ui.IChartWidget;
import org.jutils.chart.ui.Layer2d;
import org.jutils.chart.ui.objects.PlotContext.IAxisCoords;

/*******************************************************************************
 * 
 ******************************************************************************/
public class AxesWidget implements IChartWidget
{
    /**  */
    private static final int AXIS_WEIGHT = 2;

    /**  */
    public final Chart chart;
    /**  */
    public final PlotContext context;

    /**  */
    public final Layer2d axesLayer;

    /**  */
    private final BasicStroke majorStroke;
    /**  */
    private final BasicStroke gridStroke;
    /**  */
    private final TextWidget domainText;
    /**  */
    private final TextWidget rangeText;

    /**  */
    private final TextWidget domainTitle;
    /**  */
    private final TextWidget rangeTitle;
    /**  */
    private final TextWidget secDomainTitle;
    /**  */
    private final TextWidget secRangeTitle;

    /**  */
    private static final int MAJOR_TICK_LEN = 12;

    /***************************************************************************
     * @param context
     **************************************************************************/
    public AxesWidget( PlotContext context, Chart chart )
    {
        this.chart = chart;
        this.context = context;
        this.axesLayer = new Layer2d();
        this.majorStroke = new BasicStroke( AXIS_WEIGHT, BasicStroke.CAP_ROUND,
            BasicStroke.JOIN_ROUND );
        this.gridStroke = new BasicStroke( 1.0f, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER, 10.0f, new float[] { 2.0f }, 0.0f );

        this.domainText = new TextWidget(
            new TextLabel( new Font( "Helvetica", Font.PLAIN, 12 ) ) );

        this.rangeText = new TextWidget(
            new TextLabel( new Font( "Helvetica", Font.PLAIN, 12 ) ) );

        this.domainTitle = new TextWidget( chart.domainAxis.title );
        this.secDomainTitle = new TextWidget( chart.secDomainAxis.title );

        this.rangeTitle = new TextWidget( chart.rangeAxis.title,
            TextDirection.RIGHT );
        this.secRangeTitle = new TextWidget( chart.secRangeAxis.title,
            TextDirection.LEFT );

        rangeText.label.alignment = HorizontalAlignment.CENTER;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Dimension calculateSize( Dimension canvasSize )
    {
        return null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void draw( Graphics2D graphics, Point location, Dimension size )
    {
        Point p = new Point( location );
        Dimension d = new Dimension( size );

        // ---------------------------------------------------------------------
        // Determine the space needed to draw the axis titles.
        // ---------------------------------------------------------------------
        Insets titleSpace = caculateTitleSpace( size );

        // graphics.setColor( Color.green );
        // graphics.drawRect( location.x, location.y, size.width, size.height );

        // ---------------------------------------------------------------------
        // Determine rough tick sizes.
        // ---------------------------------------------------------------------
        context.x = location.x + titleSpace.left;
        context.y = location.y + titleSpace.top;
        context.width = size.width - titleSpace.left - titleSpace.right;
        context.height = size.height - titleSpace.top - titleSpace.bottom;

        context.width = Math.max( 0, context.width );
        context.height = Math.max( 0, context.height );

        p.x = context.x;
        p.y = context.y;

        d.width = context.width;
        d.height = context.height;

        // graphics.setColor( Color.black );
        // graphics.drawRect( p.x, p.y, d.width, d.height );

        context.latchCoords();

        // Graphics2D g = ( Graphics2D )graphics.create( context.x, context.y,
        // context.width, context.height );

        // ---------------------------------------------------------------------
        // Determine rough ticks.
        // ---------------------------------------------------------------------
        Ticks ticks;
        Insets textSpace;

        // ---------------------------------------------------------------------
        // Determine tick sizes.
        // ---------------------------------------------------------------------s
        ticks = calculateTicks();
        textSpace = calculateLabelInsets( ticks );

        // ---------------------------------------------------------------------
        // Update plot size.
        // ---------------------------------------------------------------------
        context.x = p.x + textSpace.left;
        context.y = p.y + textSpace.top;
        context.width = d.width - ( textSpace.left + textSpace.right ) -
            AXIS_WEIGHT / 2;
        context.height = d.height - ( textSpace.top + textSpace.bottom ) -
            AXIS_WEIGHT / 2;

        context.width = Math.max( 0, context.width );
        context.height = Math.max( 0, context.height );

        context.latchCoords();

        // ---------------------------------------------------------------------
        // Determine tick sizes.
        // ---------------------------------------------------------------------
        ticks = calculateTicks();
        textSpace = calculateLabelInsets( ticks );

        // ---------------------------------------------------------------------
        // Update plot size.
        // ---------------------------------------------------------------------
        context.x = p.x + textSpace.left;
        context.y = p.y + textSpace.top;
        context.width = d.width - ( textSpace.left + textSpace.right ) -
            AXIS_WEIGHT / 2;
        context.height = d.height - ( textSpace.top + textSpace.bottom ) -
            AXIS_WEIGHT / 2;

        context.width = Math.max( 0, context.width );
        context.height = Math.max( 0, context.height );

        // graphics.setColor( Color.red );
        // graphics.drawRect( context.x, context.y, context.width,
        // context.height );

        // if( "".isEmpty() )
        // {
        // return;
        // }

        context.latchCoords();

        positionTicks( ticks, textSpace );

        // ---------------------------------------------------------------------
        // Draw the axis titles.
        // ---------------------------------------------------------------------
        drawTitles( graphics, location, size, textSpace );

        // ---------------------------------------------------------------------
        // Draw the ticks.
        // ---------------------------------------------------------------------
        drawTicksAndAxes( graphics, location, size, textSpace, ticks );

        // graphics.setColor( Color.blue );
        // graphics.drawRect( context.x, context.y, context.width,
        // context.height );

        // g.dispose();
    }

    /***************************************************************************
     * @param location
     * @param size
     **************************************************************************/
    private void drawTitles( Graphics2D graphics, Point location,
        Dimension size, Insets textSpace )
    {
        Point p = new Point( location );
        Dimension d;

        // ---------------------------------------------------------------------
        // Draw secondary domain title
        // ---------------------------------------------------------------------
        if( chart.secDomainAxis.title.visible && chart.secDomainAxis.isUsed )
        {
            d = secDomainTitle.calculateSize( size );

            d.width = size.width - ( textSpace.left + textSpace.right );

            p.x = textSpace.left + location.x;
            p.y = location.y;

            secDomainTitle.draw( graphics, p, d );

            location.y += d.height;
            size.height -= d.height;
        }

        // ---------------------------------------------------------------------
        // Draw primary domain title
        // ---------------------------------------------------------------------
        if( chart.domainAxis.title.visible && chart.domainAxis.isUsed )
        {
            d = domainTitle.calculateSize( size );

            d.width = size.width - ( textSpace.left + textSpace.right );

            p.x = textSpace.left + location.x;
            p.y = location.y + size.height - d.height;

            domainTitle.draw( graphics, p, d );

            // LogUtils.printDebug( "Label is: " + domainTitle.label.text );

            size.height -= d.height;
        }

        // ---------------------------------------------------------------------
        // Draw secondary range title
        // ---------------------------------------------------------------------
        if( chart.secRangeAxis.title.visible && chart.secRangeAxis.isUsed )
        {
            d = secRangeTitle.calculateSize( size );

            int h = size.height - ( textSpace.top + textSpace.bottom );

            p.x = location.x + size.width - d.width;
            p.y = location.y + textSpace.top;

            d.height = h;

            secRangeTitle.draw( graphics, p, d );

            size.width -= d.width;
        }

        // ---------------------------------------------------------------------
        // Draw primary range title
        // ---------------------------------------------------------------------
        if( chart.rangeAxis.title.visible && chart.rangeAxis.isUsed )
        {
            d = rangeTitle.calculateSize( size );

            int h = size.height - ( textSpace.top + textSpace.bottom );

            p.x = location.x;
            p.y = location.y + textSpace.top;

            d.height = h;

            rangeTitle.draw( graphics, p, d );

            location.x += d.width;
            size.width -= d.width;
        }
    }

    /***************************************************************************
     * @param graphics
     * @param location
     * @param size
     * @param textSpace
     **************************************************************************/
    private void drawTicksAndAxes( Graphics2D graphics, Point location,
        Dimension size, Insets textSpace, Ticks ts )
    {
        int x = location.x;
        int y = location.y;
        int width = size.width;

        // LogUtils.printDebug( "axes: w: " + width + ", h: " + height );

        // ---------------------------------------------------------------------
        // Draw axes layer.
        // ---------------------------------------------------------------------
        if( axesLayer.repaint )
        {
            axesLayer.setSize( size );
            Graphics2D g2d = axesLayer.getGraphics();

            axesLayer.clear();

            // g2d.setColor( Color.red );
            // g2d.setStroke( new BasicStroke( 1.0f ) );
            // g2d.drawRect( 0, 0, size.width - 1, size.height - 1 );

            // LogUtils.printDebug( "axes: w: " + w + ", h: " + h );
            // LogUtils.printDebug( "axes: xr: " + context.getXRange() );
            // LogUtils.printDebug( "axes: xmin: " + context.xMin );
            // LogUtils.printDebug( "axes: xmax: " + context.xMax );
            // LogUtils.printDebug( "axes: width: " + context.width );

            Tick t;

            // -----------------------------------------------------------------
            // Draw grid lines.
            // -----------------------------------------------------------------
            if( chart.options.gridlinesVisible )
            {
                g2d.setColor( Color.lightGray );
                g2d.setStroke( gridStroke );

                for( int i = 0; i < ts.domainTicks.size(); i++ )
                {
                    t = ts.domainTicks.get( i );

                    g2d.drawLine( t.offset, textSpace.top, t.offset,
                        textSpace.top + context.height );
                }

                for( int i = 0; i < ts.rangeTicks.size(); i++ )
                {
                    t = ts.rangeTicks.get( i );

                    g2d.drawLine( textSpace.left, t.offset,
                        textSpace.left + context.width, t.offset );
                }
            }

            // -----------------------------------------------------------------
            // Draw axes.
            // -----------------------------------------------------------------
            g2d.setColor( Color.black );
            g2d.setStroke( majorStroke );

            g2d.drawRect( textSpace.left, textSpace.top, context.width,
                context.height );

            // -----------------------------------------------------------------
            // Draw major ticks.
            // -----------------------------------------------------------------
            drawMajorTicks( g2d, ts, textSpace );

            // -----------------------------------------------------------------
            // Draw domain labels.
            // -----------------------------------------------------------------
            drawDomainLabels( g2d, ts.domainTicks,
                textSpace.top + context.height + 2, textSpace.bottom, size );

            // -----------------------------------------------------------------
            // Draw secondary domain ticks and labels.
            // -----------------------------------------------------------------
            drawDomainLabels( g2d, ts.secDomainTicks, 0, textSpace.top, size );

            // -----------------------------------------------------------------
            // Draw range ticks and labels.
            // -----------------------------------------------------------------
            drawRangeLabels( g2d, ts.rangeTicks, -2, textSpace.left, false,
                size );

            // -----------------------------------------------------------------
            // Draw secondary range ticks and labels.
            // -----------------------------------------------------------------
            drawRangeLabels( g2d, ts.secRangeTicks, width - textSpace.right + 2,
                textSpace.right, true, size );

            axesLayer.repaint = false;
        }

        axesLayer.paint( graphics, x, y );
    }

    /***************************************************************************
     * @param size
     * @return
     **************************************************************************/
    private Insets caculateTitleSpace( Dimension size )
    {
        Insets space = new Insets( 0, 0, 0, 0 );
        Dimension d;

        if( domainTitle.label.visible && chart.domainAxis.isUsed )
        {
            d = domainTitle.calculateSize( size );
            space.bottom = d.height;
        }

        if( rangeTitle.label.visible && chart.rangeAxis.isUsed )
        {
            d = rangeTitle.calculateSize( size );
            space.left = d.width;
        }

        if( secDomainTitle.label.visible && chart.secDomainAxis.isUsed )
        {
            d = secDomainTitle.calculateSize( size );
            space.top = d.height;
        }

        if( secRangeTitle.label.visible && chart.secRangeAxis.isUsed )
        {
            d = secRangeTitle.calculateSize( size );
            space.right = d.width;
        }

        return space;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Ticks calculateTicks()
    {
        Ticks ts = new Ticks();
        TickGen gen = new TickGen();

        gen.genTicks( chart.domainAxis, context.width, ts.domainTicks );
        gen.genTicks( chart.rangeAxis, context.height, ts.rangeTicks );
        gen.genTicks( chart.secDomainAxis, context.width, ts.secDomainTicks );
        gen.genTicks( chart.secRangeAxis, context.height, ts.secRangeTicks );

        return ts;
    }

    /***************************************************************************
     * @param ts
     * @param textSpace
     **************************************************************************/
    private void positionTicks( Ticks ts, Insets textSpace )
    {
        positionTicks( ts.domainTicks, context.domainCoords, textSpace.left );
        positionTicks( ts.rangeTicks, context.rangeCoords, textSpace.top );
        positionTicks( ts.secDomainTicks, context.secDomainCoords,
            textSpace.left );
        positionTicks( ts.secRangeTicks, context.secRangeCoords,
            textSpace.top );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static void positionTicks( List<Tick> ticks, IAxisCoords coords,
        int offset )
    {
        for( Tick t : ticks )
        {
            t.offset = offset + coords.fromCoord( t.value );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void repaint()
    {
        axesLayer.repaint = true;

        domainTitle.repaint();
        secDomainTitle.repaint();
        rangeTitle.repaint();
        secRangeTitle.repaint();
    }

    /***************************************************************************
     * @param g2d
     * @param domainTicks
     * @param secRangeTicks
     * @param secDomainTicks
     * @param textSpace
     **************************************************************************/
    private void drawMajorTicks( Graphics2D g2d, Ticks ts, Insets textSpace )
    {
        List<Tick> secDomainTicks = ts.secDomainTicks.isEmpty() ? ts.domainTicks
            : ts.secDomainTicks;
        List<Tick> secRangeTicks = ts.secRangeTicks.isEmpty() ? ts.rangeTicks
            : ts.secRangeTicks;

        for( Tick t : ts.domainTicks )
        {
            g2d.drawLine( t.offset,
                textSpace.top + context.height - MAJOR_TICK_LEN, t.offset,
                textSpace.top + context.height );
        }

        for( Tick t : ts.rangeTicks )
        {
            g2d.drawLine( textSpace.left, t.offset,
                textSpace.left + MAJOR_TICK_LEN, t.offset );
        }

        for( Tick t : secDomainTicks )
        {
            g2d.drawLine( t.offset, textSpace.top, t.offset,
                textSpace.top + MAJOR_TICK_LEN );
        }

        for( Tick t : secRangeTicks )
        {
            g2d.drawLine( textSpace.left + context.width, t.offset,
                textSpace.left + context.width - MAJOR_TICK_LEN, t.offset );
        }
    }

    /***************************************************************************
     * @param g2d
     * @param ticks
     * @param y
     * @param h
     **************************************************************************/
    private void drawDomainLabels( Graphics2D g2d, List<Tick> ticks, int y,
        int h, Dimension canvasSize )
    {
        for( Tick t : ticks )
        {
            drawDomainLabel( g2d, t, y, h, canvasSize );
        }
    }

    /***************************************************************************
     * @param g2d
     * @param t
     * @param y
     * @param h
     * @param canvasSize
     **************************************************************************/
    private void drawDomainLabel( Graphics2D g2d, Tick t, int y, int h,
        Dimension canvasSize )
    {
        domainText.label.text = t.label;
        int tw = domainText.calculateSize( canvasSize ).width;
        Point p = new Point( t.offset - tw / 2, y );
        Dimension d = new Dimension( tw, h );
        domainText.repaint();
        domainText.draw( g2d, p, d );
    }

    /***************************************************************************
     * @param g2d
     * @param ticks
     * @param x
     * @param w
     * @param leftAlign
     **************************************************************************/
    private void drawRangeLabels( Graphics2D g2d, List<Tick> ticks, int x,
        int w, boolean leftAlign, Dimension canvasSize )
    {
        for( Tick t : ticks )
        {
            drawRangeLabel( g2d, t, x, w, leftAlign, canvasSize );
        }
    }

    /***************************************************************************
     * @param g2d
     * @param t
     * @param x
     * @param w
     * @param leftAlign
     * @param canvasSize
     **************************************************************************/
    private void drawRangeLabel( Graphics2D g2d, Tick t, int x, int w,
        boolean leftAlign, Dimension canvasSize )
    {
        rangeText.label.text = t.label;
        Dimension d = rangeText.calculateSize( canvasSize );
        int tw = d.width;
        int h = d.height;
        int tx = leftAlign ? x : x + w - tw;
        Point p = new Point( tx, t.offset - h / 2 );

        rangeText.repaint();
        rangeText.draw( g2d, p, d );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Insets calculateLabelInsets( Ticks ticks )
    {
        Insets textSpace = new Insets( 0, 0, 0, 0 );

        AxisSizes domainSizes = new AxisSizes( ticks.domainTicks, domainText );
        AxisSizes rangeSizes = new AxisSizes( ticks.rangeTicks, rangeText );
        AxisSizes secDomainSizes = new AxisSizes( ticks.secDomainTicks,
            domainText );
        AxisSizes secRangeSizes = new AxisSizes( ticks.secRangeTicks,
            rangeText );

        // ---------------------------------------------------------------------
        // The top offset is the maximum of the largest secondary domain height,
        // half the maximum range height, and half the maximum secondary range
        // height.
        // ---------------------------------------------------------------------
        textSpace.top = Utils.max( secDomainSizes.size.height,
            Math.round( rangeSizes.maxSize.height / 2.0f ),
            Math.round( secRangeSizes.maxSize.height / 2.0f ) );

        // ---------------------------------------------------------------------
        // The left offset is the maximum of the largest range width, half the
        // minimum domain width, and half the minimum secondary domain width.
        // ---------------------------------------------------------------------
        textSpace.left = Utils.max( rangeSizes.size.width,
            Math.round( domainSizes.minSize.width / 2.0f ),
            Math.round( secDomainSizes.minSize.width / 2.0f ) );

        // ---------------------------------------------------------------------
        // The bottom offset is the maximum of the largest domain height, half
        // the minimum range height, and half the minimum secondary range
        // height.
        // ---------------------------------------------------------------------
        textSpace.bottom = Utils.max( domainSizes.size.height,
            Math.round( rangeSizes.minSize.height / 2.0f ),
            Math.round( secRangeSizes.minSize.height / 2.0f ) );

        // ---------------------------------------------------------------------
        // The right offset is the maximum of the largest secondary range width,
        // half the maximum domain width, and half the maximum secondary domain
        // width.
        // ---------------------------------------------------------------------
        textSpace.right = Utils.max( secRangeSizes.size.width,
            Math.round( domainSizes.maxSize.width / 2.0f ),
            Math.round( secDomainSizes.maxSize.width / 2.0f ) );

        textSpace.left += 5;
        textSpace.bottom += 5;

        if( !ticks.secDomainTicks.isEmpty() )
        {
            textSpace.top += 5;
        }

        if( !ticks.secRangeTicks.isEmpty() )
        {
            textSpace.right += 5;
        }

        return textSpace;
    }

    /***************************************************************************
     * @param t
     * @param w
     * @param size
     **************************************************************************/
    private static void getTickSize( Tick t, TextWidget w, Dimension size )
    {
        w.label.text = t.label;
        Dimension d = w.calculateSize( null );

        size.width = d.width;
        size.height = d.height;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class AxisSizes
    {
        public final Dimension minSize;
        public final Dimension maxSize;
        public final Dimension size;

        public AxisSizes( List<Tick> ticks, TextWidget w )
        {
            this.minSize = new Dimension( 0, 0 );
            this.maxSize = new Dimension( 0, 0 );
            this.size = new Dimension( 0, 0 );

            if( !ticks.isEmpty() )
            {
                getTickSize( ticks.get( 0 ), w, minSize );
                getTickSize( ticks.get( ticks.size() - 1 ), w, maxSize );

                Utils.getMaxSize( size, minSize, maxSize );

                Dimension d = new Dimension();

                for( int i = 1; i < ticks.size(); i++ )
                {
                    getTickSize( ticks.get( i ), w, d );
                    Utils.getMaxSize( size, size, d );
                }
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class Ticks
    {

        public final List<Tick> domainTicks;
        public final List<Tick> rangeTicks;
        public final List<Tick> secDomainTicks;
        public final List<Tick> secRangeTicks;

        public Ticks()
        {
            this.domainTicks = new ArrayList<>();
            this.rangeTicks = new ArrayList<>();
            this.secDomainTicks = new ArrayList<>();
            this.secRangeTicks = new ArrayList<>();
        }
    }
}
