package org.jutils.chart.ui.objects;

import java.awt.*;

import org.jutils.chart.ui.IChartWidget;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SelectionWidget implements IChartWidget
{
    /**  */
    private PlotContext context;

    /**  */
    private Stroke borderStroke;
    /**  */
    public Color border;
    /**  */
    public Color color;

    /**  */
    public Point start;
    /**  */
    public Point end;

    /**  */
    public boolean visible;

    /***************************************************************************
     * @param context
     **************************************************************************/
    public SelectionWidget( PlotContext context )
    {
        this.context = context;
        this.borderStroke = new BasicStroke( 2 );
        this.border = new Color( 0x007AE9 );
        this.color = new Color( 0x70005EB0, true );
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
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void draw( Graphics2D graphics, Point unusedPoint,
        Dimension unusedSize )
    {
        if( visible && start != null )
        {
            Point s = new Point( start );
            Point e = new Point( end );

            s.x -= context.x;
            e.x -= context.x;

            s.y -= context.y;
            e.y -= context.y;

            int xp = Math.min( s.x, e.x );
            int yp = Math.min( s.y, e.y );
            int wp = Math.abs( e.x - s.x );
            int hp = Math.abs( e.y - s.y );

            graphics.setColor( color );
            graphics.fillRect( xp, yp, wp, hp );

            graphics.setStroke( borderStroke );
            graphics.setColor( border );
            graphics.drawRect( xp, yp, wp, hp );
        }
    }
}
