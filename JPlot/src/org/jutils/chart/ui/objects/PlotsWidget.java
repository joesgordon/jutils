package org.jutils.chart.ui.objects;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import org.jutils.chart.ui.IChartWidget;
import org.jutils.chart.ui.Layer2d;

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

        highlightLayer.repaint = false;
    }

    public Dimension layout( Dimension size )
    {
        seriesLayer.setSize( size );
        highlightLayer.setSize( size );

        return size;
    }

    /***************************************************************************
     * 
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
        g2d = seriesLayer.getGraphics();
        if( seriesLayer.repaint )
        {
            seriesLayer.clear();

            for( PlotWidget s : plots )
            {
                s.draw( g2d, null, null );
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
    public void repaint()
    {
        seriesLayer.repaint = true;
        highlightLayer.repaint = true;
    }
}
