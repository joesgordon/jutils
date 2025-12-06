package jutils.strip.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Line2D;

import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import jutils.core.ui.IPaintable;
import jutils.strip.data.ILegend;

/*******************************************************************************
 * 
 ******************************************************************************/
public class LegendPaintable implements IPaintable
{
    /**  */
    private static final int LEGEND_PADDING = 0;
    /**  */
    // private static final int MARKER_SIZE = 10;

    /**  */
    private Border border;

    /**  */
    private ILegend legend;

    /***************************************************************************
     * 
     **************************************************************************/
    public LegendPaintable()
    {
        this.legend = new ILegend()
        {
            @Override
            public int getSeriesCount()
            {
                return 1;
            }

            @Override
            public Color getSeriesColor( int seriesIndex )
            {
                return Color.YELLOW;
            }

            @Override
            public String getName( int seriesIndex )
            {
                return "Filler";
            }
        };
        this.border = new LineBorder( Color.GRAY, 1 );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void paint( JComponent c, Graphics2D graphics )
    {
        Point location = new Point( LEGEND_PADDING, LEGEND_PADDING );

        Rectangle bounds = graphics.getClipBounds();
        Dimension size = bounds.getSize();

        size.width -= 2 * LEGEND_PADDING;
        size.height -= 2 * LEGEND_PADDING;

        // ---------------------------------------------------------------------
        // Fill
        // ---------------------------------------------------------------------
        graphics.setColor( c.getBackground() );
        graphics.fillRect( location.x, location.y, size.width, size.height );

        // ---------------------------------------------------------------------
        // Draw border
        // ---------------------------------------------------------------------
        if( border != null )
        {
            border.paintBorder( c, graphics, location.x, location.y, size.width,
                size.height );
        }

        // ---------------------------------------------------------------------
        // Draw keys
        // ---------------------------------------------------------------------
        graphics.setStroke( new BasicStroke( 2 ) );
        graphics.setColor( Color.black );

        FontMetrics fm = graphics.getFontMetrics();
        int txth = fm.getAscent();
        Point p = new Point( location );

        p.x += 4;
        p.y += 4;

        Stroke stroke = graphics.getStroke();
        Stroke solid = new BasicStroke( 2, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER );
        Line2D.Double line = new Line2D.Double();

        for( int s = 0; s < legend.getSeriesCount(); s++ )
        {
            String name = legend.getName( s );
            Color clr = legend.getSeriesColor( s );
            // int txtw = fm.stringWidth( name );

            // LogUtils.printDebug( "drawing series keys at %d", s );

            line.x1 = p.x;
            line.x2 = line.x1 + 20;
            line.y1 = p.y + txth / 2;
            line.y2 = line.y1;

            graphics.setColor( clr );
            graphics.setStroke( solid );
            graphics.draw( line );

            p.y = p.y + txth;
            graphics.setStroke( stroke );
            graphics.drawString( name, p.x + 22, p.y );

            // graphics.setStroke( new BasicStroke() );
            //
            // graphics.setColor( Color.green );
            // graphics.drawRect( key.loc.x, key.loc.y, key.size.width,
            // key.size.height );
            //
            // graphics.setColor( Color.blue );
            // graphics.drawRect( p.x, p.y, key.size.width, key.size.height
            // );
        }
    }

}
