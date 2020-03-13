package org.jutils.chart.ui.objects;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

import org.jutils.chart.model.Chart.FillSet;
import org.jutils.chart.ui.IChartWidget;
import org.jutils.chart.ui.Layer2d;
import org.jutils.chart.ui.objects.PlotWidget.IPixelListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PlotsWidget implements IChartWidget
{
    /**  */
    private final Layer2d seriesLayer;
    /**  */
    public final Layer2d highlightLayer;
    /**  */
    public final SelectionWidget selection;

    /**  */
    public final List<PlotWidget> plots;
    /**  */
    public final List<FillSet> fills;

    /**  */
    public final PlotContext context;

    /***************************************************************************
     * @param context
     **************************************************************************/
    public PlotsWidget( PlotContext context )
    {
        this.context = context;

        this.seriesLayer = new Layer2d();
        this.highlightLayer = new Layer2d();
        this.selection = new SelectionWidget( context );
        this.plots = new ArrayList<>();
        this.fills = new ArrayList<>();

        highlightLayer.repaint = false;
    }

    /***************************************************************************
     * @param size
     * @return
     **************************************************************************/
    public Dimension layout( Dimension size )
    {
        seriesLayer.setSize( size );
        highlightLayer.setSize( size );

        return size;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void draw( Graphics2D graphics, Point unusedPoint,
        Dimension unusedSize )
    {
        Graphics2D g2d;

        if( context.width < 1 || context.height < 1 )
        {
            return;
        }

        // ---------------------------------------------------------------------
        // Draw series layer.
        // ---------------------------------------------------------------------

        List<FillListener> fillShapes = new ArrayList<>();

        g2d = seriesLayer.getGraphics();
        if( seriesLayer.repaint )
        {
            seriesLayer.clear();

            for( FillSet f : fills )
            {
                FillListener ipl = new FillListener();

                fillShapes.add( ipl );

                for( PlotWidget p : plots )
                {
                    if( p.series == f.s1 )
                    {
                        p.pixelListener = ipl;
                    }
                    else if( p.series == f.s2 )
                    {
                        p.pixelListener = ipl;
                    }
                }
            }

            for( PlotWidget s : plots )
            {
                s.draw( g2d, null, null );
            }

            for( FillListener f : fillShapes )
            {
                f.draw( g2d );
            }

            seriesLayer.repaint = false;
        }
        seriesLayer.paint( graphics, 0, 0 );

        // ---------------------------------------------------------------------
        // Draw highlight layer.
        // ---------------------------------------------------------------------
        g2d = highlightLayer.getGraphics();

        if( highlightLayer.repaint )
        {
            highlightLayer.clear();

            for( PlotWidget p : plots )
            {
                if( p.series.visible && p.trackPoint &&
                    p.highlightLocation != null )
                {
                    p.highlight.draw( g2d, p.highlightLocation );
                }
            }

            highlightLayer.repaint = false;

            selection.draw( g2d, null, null );
        }

        highlightLayer.paint( graphics, 0, 0 );
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
    public void repaint()
    {
        seriesLayer.repaint = true;
        highlightLayer.repaint = true;
    }

    private static final class FillListener implements IPixelListener
    {
        private final List<Point> s1;
        private final List<Point> s2;

        public FillListener()
        {
            this.s1 = new ArrayList<>();
            this.s2 = new ArrayList<>();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void pixelDrawn( Point point )
        {
            point = new Point( point );

            if( !s2.isEmpty() )
            {
                s2.add( point );
            }
            else if( s1.isEmpty() )
            {
                s1.add( point );
            }
            else
            {
                Point lp = s1.get( s1.size() - 1 );

                if( lp.x > point.x )
                {
                    s2.add( point );
                }
                else
                {
                    s1.add( point );
                }
            }
        }

        /**
         * @param g
         */
        public void draw( Graphics2D g )
        {
            Polygon fill = new Polygon();

            for( int i1 = 0; i1 < s1.size(); i1++ )
            {
                Point p1 = s1.get( i1 );
                fill.addPoint( p1.x, p1.y );
            }

            for( int i2 = s2.size() - 1; i2 > -1; i2-- )
            {
                Point p2 = s2.get( i2 );
                fill.addPoint( p2.x, p2.y );
            }

            g.setColor( Color.yellow );
            g.fill( fill );
        }
    }
}
