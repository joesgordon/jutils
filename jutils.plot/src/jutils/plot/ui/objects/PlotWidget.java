package jutils.plot.ui.objects;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;

import jutils.core.SwingUtils;
import jutils.plot.ChartUtils;
import jutils.plot.model.IDataPoint;
import jutils.plot.model.Interval;
import jutils.plot.model.LineType;
import jutils.plot.model.Series;
import jutils.plot.ui.IChartWidget;
import jutils.plot.ui.Layer2d;
import jutils.plot.ui.lines.SimpleLine;
import jutils.plot.ui.markers.CircleBorderMarker;
import jutils.plot.ui.markers.CircleMarker;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PlotWidget implements IChartWidget
{
    /**  */
    public final Series series;
    /**  */
    private final PlotContext context;

    /**  */
    public final CircleMarker marker;
    /**  */
    private final CircleMarker selectedMarker;
    /**  */
    public final CircleBorderMarker highlight;
    /**  */
    private SimpleLine line;

    /**  */
    public boolean trackPoint;
    /**  */
    public Point highlightLocation;
    /**  */
    public IPixelListener pixelListener;

    /***************************************************************************
     * @param series
     * @param context
     **************************************************************************/
    public PlotWidget( Series series, PlotContext context )
    {
        this.series = series;
        this.context = context;

        this.marker = new CircleMarker();
        this.selectedMarker = new CircleMarker();
        this.highlight = new CircleBorderMarker();
        this.line = new SimpleLine();

        this.trackPoint = true;
        this.highlightLocation = null;
        this.pixelListener = null;

        marker.color = series.marker.color;
        highlight.color = series.highlight.color;
        line.color = series.line.color;
        selectedMarker.color = SwingUtils.inverseColor( series.marker.color );

        highlight.setSize( 10 );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void draw( Graphics2D graphics, Point unusedPoint,
        Dimension unusedSize )
    {
        if( !series.visible )
        {
            return;
        }

        marker.color = series.marker.color;
        marker.setSize( series.marker.weight );
        highlight.color = series.highlight.color;
        line.color = series.line.color;
        selectedMarker.color = SwingUtils.inverseColor( series.marker.color );

        Point p = new Point();
        Point last = new Point( -100, -100 );
        IDataPoint dp;

        // LogUtils.printDebug( "series: " + series.name + " weight: " +
        // series.marker.weight );

        IAxisCoords domain = series.isPrimaryDomain ? context.domainCoords
            : context.secDomainCoords;
        IAxisCoords range = series.isPrimaryRange ? context.rangeCoords
            : context.secRangeCoords;

        Interval spanx = domain.getBounds();
        // Span spany = series.isPrimaryRange ? context.primaryRangeSpan
        // : context.secondaryRangeSpan;

        if( spanx == null )
        {
            return;
        }

        int start = ChartUtils.findNearest( series.data, spanx.min ) - 2;
        int end = ChartUtils.findNearest( series.data, spanx.max ) + 2;

        start = Math.max( start, 0 );
        end = Math.min( end, series.data.getCount() );

        // LogUtils.printDebug( "series: start: " + start + ", end: " + end );

        Layer2d markerLayer = new Layer2d();
        Layer2d selectedLayer = new Layer2d();
        int d = series.marker.weight + 2;
        int r = d / 2 + 1;

        if( series.marker.visible )
        {
            Graphics2D g2d;
            Point mp = new Point( r, r );
            Dimension dim = new Dimension( d, d );

            markerLayer.setSize( dim );
            g2d = markerLayer.getGraphics();
            marker.draw( g2d, mp );

            selectedLayer.setSize( dim );
            g2d = selectedLayer.getGraphics();
            selectedMarker.draw( g2d, mp );
        }

        // int count = 0;

        Stroke dashed = new BasicStroke( series.line.weight,
            BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f,
            new float[] { 5.0f }, 0.0f );
        Stroke solid = new BasicStroke( series.line.weight,
            BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL );
        Stroke lineStroke = series.line.type == LineType.DASHED ? dashed
            : solid;

        boolean lastHidden = true;

        for( int i = start; i < end; i++ )
        {
            dp = series.data.get( i );

            if( dp.isHidden() )
            {
                lastHidden = true;
                continue;
            }

            p.x = domain.fromCoord( dp.getX() );
            p.y = range.fromCoord( dp.getY() );

            if( p.x != last.x || p.y != last.y )
            {
                if( series.line.visible && last.x != -100 && !lastHidden )
                {
                    Stroke s = graphics.getStroke();

                    graphics.setStroke( lineStroke );

                    line.draw( graphics, last, p );

                    graphics.setStroke( s );
                }

                if( series.marker.visible )
                {
                    Layer2d l2d = dp.isSelected() ? selectedLayer : markerLayer;

                    // m.draw( graphics, p, size );
                    l2d.paint( graphics, p.x - r, p.y - r );
                }

                if( pixelListener != null )
                {
                    pixelListener.pixelDrawn( series, p );
                }

                last.x = p.x;
                last.y = p.y;

                // count++;
            }

            lastHidden = false;
        }

        // LogUtils.printDebug( "Drew %d points for series with %d points",
        // count,
        // series.data.getCount() );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Dimension calculateSize( Dimension canvasSize )
    {
        return null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void clearSelected()
    {
        for( IDataPoint xy : series.data )
        {
            xy.setSelected( false );
        }
    }

    /***************************************************************************
     * @param domain
     * @param range
     **************************************************************************/
    public void setSelected( Interval domain, Interval range )
    {
        for( IDataPoint xy : series.data )
        {
            if( domain.min <= xy.getX() && xy.getX() <= domain.max &&
                range.min <= xy.getY() && xy.getY() <= range.max )
            {
                xy.setSelected( true );
            }
        }
    }

    /***************************************************************************
     * @param point
     **************************************************************************/
    public void setHighlightLocation( Point point )
    {
        this.highlightLocation = point;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static interface IPixelListener
    {
        /**
         * @param point
         */
        public void pixelDrawn( Series s, Point point );
    }
}
