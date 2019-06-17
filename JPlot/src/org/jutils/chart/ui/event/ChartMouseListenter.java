package org.jutils.chart.ui.event;

import java.awt.Point;
import java.awt.event.*;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.jutils.chart.ChartUtils;
import org.jutils.chart.data.XYPoint;
import org.jutils.chart.model.Interval;
import org.jutils.chart.ui.ChartView;
import org.jutils.chart.ui.ZoomDirection;
import org.jutils.chart.ui.objects.*;
import org.jutils.chart.ui.objects.PlotContext.IAxisCoords;

/*******************************************************************************
 * 
 *****************************************************************************/
public class ChartMouseListenter extends MouseAdapter
{
    /**  */
    private final ChartView view;
    /**  */
    private final ChartWidget chartWidget;
    /**  */
    private final JComponent panel;

    /***************************************************************************
     * @param view
     * @param chartWidget
     * @param panel
     **************************************************************************/
    public ChartMouseListenter( ChartView view, ChartWidget chartWidget,
        JComponent panel )
    {
        this.view = view;
        this.chartWidget = chartWidget;
        this.panel = panel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void mouseDragged( MouseEvent e )
    {
        if( SwingUtilities.isMiddleMouseButton( e ) )
        {
            ;
        }
        else
        {
            chartWidget.plots.selection.visible = true;
            chartWidget.plots.selection.end = e.getPoint();

            chartWidget.plots.highlightLayer.repaint = true;
            panel.repaint();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void mouseWheelMoved( MouseWheelEvent e )
    {
        if( e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL )
        {
            int zoomAmount = e.getWheelRotation();
            int exmod = e.getModifiersEx();

            boolean shift = ( MouseWheelEvent.SHIFT_DOWN_MASK &
                exmod ) == MouseWheelEvent.SHIFT_DOWN_MASK;

            boolean ctrl = ( MouseWheelEvent.CTRL_DOWN_MASK &
                exmod ) == MouseWheelEvent.CTRL_DOWN_MASK;

            if( zoomAmount < 0 )
            {
                view.zoomIn( ZoomDirection.get( shift, ctrl ) );
            }
            else
            {
                view.zoomOut( ZoomDirection.get( shift, ctrl ) );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void mouseClicked( MouseEvent e )
    {
        if( SwingUtilities.isLeftMouseButton( e ) && e.getClickCount() == 2 )
        {
            view.zoomRestore();
        }
        else if( SwingUtilities.isRightMouseButton( e ) &&
            e.getClickCount() == 2 )
        {
            for( PlotWidget sw : chartWidget.plots.plots )
            {
                sw.clearSelected();
                chartWidget.plots.repaint();
                panel.repaint();
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void mousePressed( MouseEvent e )
    {
        panel.requestFocus();

        chartWidget.plots.selection.start = e.getPoint();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void mouseReleased( MouseEvent evt )
    {
        if( !chartWidget.plots.selection.visible )
        {
            return;
        }

        chartWidget.plots.selection.visible = false;

        PlotContext context = chartWidget.context;

        Point start = chartWidget.plots.selection.start;
        Point end = evt.getPoint();

        if( start == null )
        {
            return;
        }

        start = context.ensurePoint( start );
        end = context.ensurePoint( end );

        validateDragArea( start, end );

        Interval pds = getBounds( context.domainCoords, start.x, end.x );
        Interval prs = getBounds( context.rangeCoords, end.y, start.y );
        Interval sds = null;
        Interval srs = null;

        // LogUtils.printDebug( "primary domain from " + ymin + " to " +
        // ymax );
        // LogUtils.printDebug( "primary domain from " + dmin + " to " +
        // dmax );

        if( view.chart.secDomainAxis.isUsed )
        {
            sds = getBounds( context.secDomainCoords, start.x, end.x );
        }

        if( view.chart.secRangeAxis.isUsed )
        {
            srs = getBounds( context.secRangeCoords, end.y, start.y );
        }

        if( pds.range == 0.0 || prs.range == 0.0 ||
            ( sds != null && sds.range == 0.0 ) ||
            ( srs != null && srs.range == 0.0 ) )
        {
            chartWidget.plots.highlightLayer.repaint = true;
            panel.repaint();
            return;
        }

        if( SwingUtilities.isLeftMouseButton( evt ) )
        {
            view.chart.domainAxis.setBounds( pds );
            view.chart.rangeAxis.setBounds( prs );
            view.chart.secDomainAxis.setBounds( sds );
            view.chart.secRangeAxis.setBounds( srs );

            chartWidget.latchBounds();
        }
        else if( SwingUtilities.isMiddleMouseButton( evt ) )
        {
            ;
        }
        else
        {
            Interval ds;
            Interval rs;

            for( PlotWidget plot : chartWidget.plots.plots )
            {
                if( plot.series.visible )
                {
                    if( plot.series.isPrimaryDomain )
                    {
                        ds = pds;
                    }
                    else
                    {
                        ds = sds;
                    }

                    if( plot.series.isPrimaryRange )
                    {
                        rs = prs;
                    }
                    else
                    {
                        rs = srs;
                    }

                    plot.setSelected( ds, rs );
                }
            }
        }

        chartWidget.plots.repaint();
        chartWidget.plots.highlightLayer.repaint = true;
        panel.repaint();
    }

    /***************************************************************************
     * @param coords
     * @param min
     * @param max
     * @return
     **************************************************************************/
    private static Interval getBounds( IAxisCoords coords, int min, int max )
    {
        double dmin = coords.fromScreen( min );
        double dmax = coords.fromScreen( max );

        return new Interval( dmin, dmax );
    }

    /***************************************************************************
     * @param start
     * @param end
     **************************************************************************/
    private void validateDragArea( Point start, Point end )
    {
        start.x -= chartWidget.context.x;
        end.x -= chartWidget.context.x;

        start.y -= chartWidget.context.y;
        end.y -= chartWidget.context.y;

        int xmin = Math.min( start.x, end.x );
        int xmax = Math.max( start.x, end.x );

        int ymin = Math.min( start.y, end.y );
        int ymax = Math.max( start.y, end.y );

        start.x = xmin;
        start.y = ymin;

        end.x = xmax;
        end.y = ymax;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void mouseMoved( MouseEvent e )
    {
        Point p = new Point( e.getX() - chartWidget.context.x, e.getY() - 20 );
        XYPoint xy = new XYPoint();
        int idx;

        PlotContext context = chartWidget.context;

        // if( p.x < 0 || p.y < 0 || p.x > context.width ||
        // p.y > context.height )
        // {
        // for( SeriesWidget s : chartWidget.plot.serieses )
        // {
        // s.highlight.setLocation( new Point( -5, -5 ) );
        // }
        // }

        // LogUtils.printDebug( "hover: " + mx );

        int seriesIdx = 0;
        for( PlotWidget plot : chartWidget.plots.plots )
        {
            Point sp = new Point( p );
            IAxisCoords domainCoords;
            IAxisCoords rangeCoords;

            if( !plot.series.visible )
            {
                continue;
            }

            if( plot.series.isPrimaryDomain )
            {
                domainCoords = context.domainCoords;
            }
            else
            {
                domainCoords = context.secDomainCoords;
            }

            if( domainCoords.getBounds() != null )
            {
                xy.x = domainCoords.fromScreen( sp.x );

                idx = ChartUtils.findNearest( plot.series.data, xy.x );

                if( idx > -1 )
                {
                    if( plot.series.isPrimaryRange )
                    {
                        rangeCoords = context.rangeCoords;
                    }
                    else
                    {
                        rangeCoords = context.secRangeCoords;
                    }

                    if( domainCoords.getBounds() == null ||
                        rangeCoords.getBounds() == null )
                    {
                        continue;
                    }

                    xy = new XYPoint( plot.series.data.get( idx ) );
                    sp.x = domainCoords.fromCoord( xy.x );
                    sp.y = rangeCoords.fromCoord( xy.y );

                    // LogUtils.printDebug( "hover [" + s.series.name +
                    // "]: " +
                    // p.x + xy.x );

                    plot.setHighlightLocation( new Point( sp ) );

                    view.setSelectedSeries( seriesIdx, idx );
                }
            }
            seriesIdx++;
        }

        chartWidget.plots.highlightLayer.repaint = true;
        panel.repaint();
    }
}
