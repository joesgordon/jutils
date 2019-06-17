package org.jutils.chart.ui;

import java.awt.*;

import javax.swing.JComponent;

import org.jutils.ui.IPaintable;
import org.jutils.ui.PaintingComponent;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class WidgetPanel implements IView<JComponent>
{
    /**  */
    private final PaintingComponent comp;
    /**  */
    private IChartWidget object;
    /**  */
    private final Object lock;
    /**  */
    private final Layer2d layer;

    /***************************************************************************
     * 
     **************************************************************************/
    public WidgetPanel()
    {
        this( null );
    }

    /***************************************************************************
     * @param object
     **************************************************************************/
    public WidgetPanel( IChartWidget object )
    {
        this.lock = new Object();
        this.object = object;
        this.layer = new Layer2d();
        this.comp = new PaintingComponent( new WidgetPainter( this ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return comp;
    }

    /***************************************************************************
     * @param obj
     **************************************************************************/
    public void setObject( IChartWidget obj )
    {
        this.object = obj;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class WidgetPainter implements IPaintable
    {
        private final WidgetPanel panel;

        public WidgetPainter( WidgetPanel panel )
        {
            this.panel = panel;
        }

        @Override
        public void paint( JComponent c, Graphics2D g )
        {
            IChartWidget obj = null;

            synchronized( panel.lock )
            {
                obj = panel.object;
            }

            if( obj == null )
            {
                return;
            }

            Insets borderSize = panel.comp.getInsets();
            int width = panel.comp.getWidth() - borderSize.left -
                borderSize.right - 1;
            int height = panel.comp.getHeight() - borderSize.top -
                borderSize.bottom - 1;
            int x = borderSize.left;
            int y = borderSize.top;
            Dimension min = panel.comp.getMinimumSize();

            width = Math.max( width, min.width );
            height = Math.max( height, min.height );

            Dimension size = new Dimension( width, height );

            // graphics.setColor( Color.red );
            // graphics.drawRect( x + 1, y + 1, size.width - 2, size.height - 2
            // );
            // graphics.setColor( Color.cyan );
            // graphics.drawLine( x + 1, y + 5, x + size.width - 1, y + 5 );

            panel.layer.setSize(
                new Dimension( size.width + 1, size.height + 1 ) );
            obj.calculateSize( size );
            obj.draw( panel.layer.getGraphics(), new Point(), size );

            // Graphics2D g2 = layer.getGraphics();
            // g2.setColor( Color.black );
            // g2.drawRect( 0, 0, size.width, size.height );
            // g2.setColor( Color.green );
            // g2.drawLine( 1, 6, size.width - 1, 6 );

            panel.layer.paint( g, x, y );

            // if( "".isEmpty() )
            // {
            // return;
            // }
        }
    }

    public void repaint()
    {
        comp.repaint();
    }
}
