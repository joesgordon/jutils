package org.jutils.chart.ui.objects;

import java.awt.*;
import java.awt.geom.AffineTransform;

import org.jutils.chart.model.TextLabel;
import org.jutils.chart.ui.IChartWidget;
import org.jutils.chart.ui.Layer2d;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TextWidget implements IChartWidget
{
    /**  */
    public final TextLabel label;
    /**  */
    private final Layer2d layer;
    /**  */
    private final TextDirection direction;

    /**  */
    private FontMetrics metrics;

    /***************************************************************************
     * @param label
     **************************************************************************/
    public TextWidget( TextLabel label )
    {
        this( label, TextDirection.DOWN );
    }

    /***************************************************************************
     * @param label
     * @param direction
     **************************************************************************/
    public TextWidget( TextLabel label, TextDirection direction )
    {
        this.label = label;
        this.layer = new Layer2d();
        this.direction = direction;

        Graphics2D g = layer.getGraphics();

        g.setFont( label.font );
        metrics = g.getFontMetrics();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void draw( Graphics2D graphics, Point location, Dimension size )
    {
        boolean rotate = direction == TextDirection.LEFT ||
            direction == TextDirection.RIGHT;
        Dimension ts = calculateSize( size );
        ts = rotate ? new Dimension( ts.height, ts.width ) : ts;

        if( layer.repaint )
        {
            layer.clear();

            if( ts.width > 0 && ts.height > 0 )
            {
                layer.setSize( ts );
                Graphics2D g2d = layer.getGraphics();

                g2d.setColor( label.color );
                g2d.setFont( label.font );
                g2d.drawString( label.text, 0, label.font.getSize() );
                // g2d.drawRect( 0, 0, ts.width - 1, ts.height - 1 );

                layer.repaint = false;
            }
        }

        int tx = location.x;
        int ty = location.y + ( size.height - ts.height - 1 ) / 2;

        switch( label.alignment )
        {
            case CENTER:
                tx = location.x + ( size.width - ts.width + 1 ) / 2;
                break;

            case RIGHT:
                tx = tx + size.width - ts.width;
                break;

            default:
                break;
        }

        // LogUtils.printDebug( "text: x: " + x + ", y: " + y + ", w: " +
        // size.width + ", h: " + size.height + ", t: " + label.text );

        // LogUtils.printDebug( "text2: x: " + x + ", ts.width: " + ts.width +
        // ", ts.height: " + ts.height );

        if( rotate )
        {
            // final double px = location.x + size.width / 2.0;
            // final double py = location.y + size.height / 2.0;

            // graphics.setColor( Color.red );
            // graphics.fillRect( location.x - 5, location.y - 5, 10, 10 );
            // graphics.drawRect( location.x, location.y, size.width,
            // size.height );
            // drawCrosshairBox( graphics, new Point( tx, ty ), new Dimension(
            // ts.width, ts.height ) );
            // graphics.fillRect( ( int )( px - 2 ), ( int )( py - 2 ), 4, 4 );

            // layer.paint( graphics, tx, ty );

            final double rx = location.x + size.width / 2.0;
            final double ry = location.y + size.height / 2.0;

            AffineTransform t = graphics.getTransform();
            AffineTransform r;

            r = AffineTransform.getRotateInstance( direction.angle, rx, ry );

            // if( label.text.equals( "Y Values" ) )
            // {
            // LogUtils.printDebug( String.format(
            // "text: loc: (%d,%d), rp: (%f,%f), tsize: (%d,%d), size: (%d,%d),
            // a: %f",
            // tx, ty, rx, ry, ts.width, ts.height, size.width,
            // size.height, direction.angle ) );
            // }

            // LogUtils.printDebug( "y: " + y );

            graphics.setTransform( r );

            layer.paint( graphics, tx, ty );

            // graphics.setColor( label.color );
            // graphics.fillRect( ( int )( rx - 5 ), ( int )( ry - 5 ), 10, 10
            // );
            // graphics.drawRect( location.x, location.y, size.width,
            // size.height );
            // drawCrosshairBox( graphics, new Point( tx, ty ), new Dimension(
            // ts.width, ts.height ) );

            graphics.setTransform( t );
        }
        else
        {
            layer.paint( graphics, tx, ty );
        }
    }

    public Dimension layout()
    {
        Dimension dim = new Dimension();

        if( !label.visible )
        {
            return dim;
        }

        Graphics2D g = layer.getGraphics();

        g.setFont( label.font );
        metrics = g.getFontMetrics();

        dim.width = metrics.stringWidth( label.text );
        // dim.height = label.font.getSize() + 2;
        dim.height = metrics.getHeight();

        if( direction == TextDirection.RIGHT ||
            direction == TextDirection.LEFT )
        {
            int i = dim.width;
            dim.width = dim.height;
            dim.height = i;
        }

        return dim;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Dimension calculateSize( Dimension canvasSize )
    {
        return layout();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void repaint()
    {
        layer.repaint = true;

        layer.getGraphics().setFont( label.font );
        metrics = layer.getGraphics().getFontMetrics();
    }
}
