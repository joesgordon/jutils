package jutils.plot.ui.objects;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

import jutils.plot.model.Series;
import jutils.plot.model.Chart.FillSet;
import jutils.plot.ui.IChartWidget;
import jutils.plot.ui.IPlotDrawer;
import jutils.plot.ui.Layer2d;
import jutils.plot.ui.objects.PlotWidget.IPixelListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PlotsWidget implements IChartWidget
{
    /**  */
    private final Layer2d fillLayer;
    /**  */
    private final Layer2d seriesLayer;
    /**  */
    public final Layer2d highlightLayer;
    /**  */
    public final SelectionWidget selection;

    /**  */
    private final List<IPlotDrawer> drawers;
    /**  */
    public final List<FillSet> fills;
    /**  */
    public final List<PlotWidget> plots;

    /**  */
    public final PlotContext context;

    /***************************************************************************
     * @param context
     **************************************************************************/
    public PlotsWidget( PlotContext context )
    {
        this.context = context;

        this.fillLayer = new Layer2d();
        this.seriesLayer = new Layer2d();
        this.highlightLayer = new Layer2d();
        this.selection = new SelectionWidget( context );
        this.drawers = new ArrayList<>();
        this.fills = new ArrayList<>();
        this.plots = new ArrayList<>();

        highlightLayer.repaint = false;
    }

    /***************************************************************************
     * @param size
     * @return
     **************************************************************************/
    public Dimension layout( Dimension size )
    {
        fillLayer.setSize( size );
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
            fillLayer.clear();
            seriesLayer.clear();

            for( FillSet f : fills )
            {
                FillListener ipl = new FillListener();

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

                if( f.s1.visible && f.s2.visible )
                {
                    fillShapes.add( ipl );
                }
            }

            for( PlotWidget s : plots )
            {
                s.draw( g2d, null, null );
            }

            for( IPlotDrawer drawer : drawers )
            {
                drawer.draw( context, fillLayer.getGraphics() );
            }

            for( FillListener f : fillShapes )
            {
                f.draw( fillLayer.getGraphics() );
            }

            seriesLayer.repaint = false;
        }

        fillLayer.paint( graphics, 0, 0 );
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
                if( p.series.visible && p.series.highlight.visible &&
                    p.trackPoint && p.highlightLocation != null )
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
        fillLayer.repaint = true;
        seriesLayer.repaint = true;
        highlightLayer.repaint = true;
    }

    /***************************************************************************
     * Adds a method to draw on top of the chart.
     * @param drawer the callback invoked after the chart is drawn.
     **************************************************************************/
    public void addDrawCallback( IPlotDrawer drawer )
    {
        drawers.add( drawer );
    }

    /**
     * 
     */
    private static final class FillListener implements IPixelListener
    {
        /**  */
        private final Polygon fill;

        /**  */
        private Series s1;
        /**  */
        private Series s2;
        /**  */
        private int s2Idx;

        /**  */
        private Color fillColor;

        /**
         * 
         */
        public FillListener()
        {
            this.fill = new Polygon();

            this.s1 = null;
            this.s2 = null;
            this.s2Idx = -1;

            Color c = Color.yellow;

            this.fillColor = new Color( c.getRed(), c.getGreen(), c.getBlue(),
                100 );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void pixelDrawn( Series s, Point point )
        {
            // LogUtils.printDebug( "Adding point %d %d,%d of series %s",
            // fill.npoints - 1, point.x, point.y, s.name );

            if( s1 == null )
            {
                s1 = s;
            }
            else if( s2 == null && s1 != s )
            {
                s2 = s;
                s2Idx = fill.npoints;
            }

            fill.addPoint( point.x, point.y );
        }

        /**
         * @param g
         */
        public void draw( Graphics2D g )
        {
            // LogUtils.printDebug( "s2Idx is %d of %d", s2Idx, fill.npoints );

            for( int i = s2Idx, j = fill.npoints - 1; i < j; i++, j-- )
            {
                int d;

                d = fill.xpoints[i];
                fill.xpoints[i] = fill.xpoints[j];
                fill.xpoints[j] = d;

                d = fill.ypoints[i];
                fill.ypoints[i] = fill.ypoints[j];
                fill.ypoints[j] = d;
            }

            fill.addPoint( fill.xpoints[0], fill.ypoints[0] );

            g.setColor( fillColor );
            g.fill( fill );
        }
    }
}
