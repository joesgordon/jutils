package jutils.iris.ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.function.Consumer;

import javax.swing.JComponent;

import jutils.core.io.LogUtils;
import jutils.core.ui.IPaintable;
import jutils.core.ui.PaintingComponent;
import jutils.core.ui.event.MouseEventsListener;
import jutils.core.ui.event.MouseEventsListener.MouseEventType;
import jutils.core.ui.model.IView;
import jutils.iris.IrisUtils;
import jutils.iris.colors.IColorizer;
import jutils.iris.data.DirectImage;
import jutils.iris.data.DirectImage.ZoomLevel;
import jutils.iris.data.RasterImage;
import jutils.iris.rasters.IRaster;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RasterView implements IView<JComponent>
{
    /**  */
    private final PaintingComponent view;
    /**  */
    private final RasterImage image;

    /**  */
    private boolean hoverEnabled;
    /**  */
    private Consumer<Point> imageHoverCallback;
    /**  */
    private IPaintable overlay;

    /***************************************************************************
     * 
     **************************************************************************/
    public RasterView()
    {
        this.image = new RasterImage( 256, 256 );
        this.view = new PaintingComponent( ( c, g ) -> paint( c, g ), false );
        this.hoverEnabled = true;
        this.imageHoverCallback = ( p ) -> {
        };
        this.overlay = ( c, g ) -> {
        };

        MouseEventsListener mouseListener = new MouseEventsListener(
            ( t, e ) -> handleMouseEvent( t, e ) );

        view.addMouseListener( mouseListener );
        view.addMouseMotionListener( mouseListener );
        view.addMouseWheelListener( mouseListener );
        view.setFocusable( true );
        view.requestFocus();
    }

    /***************************************************************************
     * @param t
     * @param e
     **************************************************************************/
    private void handleMouseEvent( MouseEventType t, MouseEvent e )
    {
        switch( t )
        {
            case WHEEL_MOVED:
                handleMouseWheel( ( MouseWheelEvent )e );
                break;

            case MOVED:
                handleMouseMoved( e );
                break;

            case RELEASED:
                handleMouseReleased( e );

            default:
                break;
        }
    }

    /***************************************************************************
     * @param e
     **************************************************************************/
    private void handleMouseReleased( MouseEvent e )
    {
        view.requestFocus();
        LogUtils.printDebug( "Mouse released" );
    }

    /***************************************************************************
     * @param e
     **************************************************************************/
    private void handleMouseMoved( MouseEvent e )
    {
        Point p = new Point( e.getPoint() );
        Point px = new Point( p );

        ZoomLevel zoom = image.getImage().getZoom();

        if( zoom == ZoomLevel.FIT )
        {
            double zoomScale = image.getImage().getZoomScale();

            px.x = ( int )( p.x / zoomScale );
            px.y = ( int )( p.y / zoomScale );
        }
        else
        {
            px.x = p.x / zoom.value;
            px.y = p.y / zoom.value;
        }

        if( hoverEnabled )
        {
            if( px.x < 0 )
            {
                px.x = 0;
            }
            else if( px.x >= image.getWidth() )
            {
                px.x = image.getWidth() - 1;
            }

            if( px.y < 0 )
            {
                px.y = 0;
            }
            else if( px.y >= image.getHeight() )
            {
                px.y = image.getHeight() - 1;
            }

            imageHoverCallback.accept( px );
        }
    }

    /***************************************************************************
     * @param e
     **************************************************************************/
    private void handleMouseWheel( MouseWheelEvent e )
    {
        int zoomAmount = e.getWheelRotation();
        int exmod = e.getModifiersEx();

        boolean ctrl = ( MouseWheelEvent.CTRL_DOWN_MASK &
            exmod ) == MouseWheelEvent.CTRL_DOWN_MASK;

        if( ctrl )
        {
            ZoomLevel zoom = image.getImage().getZoom();

            if( zoomAmount < 0 )
            {
                zoom = ZoomLevel.nextLevel( zoom );
            }
            else
            {
                zoom = ZoomLevel.previousLevel( zoom );
            }

            setZoom( zoom );
        }
    }

    /***************************************************************************
     * @param c
     * @param g
     **************************************************************************/
    private void paint( JComponent c, Graphics2D g )
    {
        drawCheckerboard( c, g );

        image.paint( c, g );

        // AbstractBorder bdr = new LineBorder( Color.BLACK, 2 );
        // bdr.paintBorder( c, g, 5, 5, image.width, image.height );

        overlay.paint( c, g );
    }

    /***************************************************************************
     * @param c
     * @param g
     **************************************************************************/
    private static void drawCheckerboard( JComponent c, Graphics2D g )
    {
        int s = 32;
        Color lcbbg = IrisUtils.LIGHT_CHECKER;
        Color dcbbg = IrisUtils.DARK_CHECKER;

        for( int x = 0; x < c.getWidth(); x += s )
        {
            for( int y = 0; y < c.getHeight(); y += s )
            {
                int sum = x + y;
                boolean isLight = ( sum % ( 2 * s ) ) == 0;
                Color bg = isLight ? lcbbg : dcbbg;

                g.setColor( bg );
                g.fillRect( x, y, s, s );
            }
        }
    }

    /***************************************************************************
     * @param zoom
     **************************************************************************/
    private void setZoom( ZoomLevel zoom )
    {
        image.getImage().setZoom( zoom );
        resetSize();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void resetSize()
    {
        Dimension ps = image.getImage().getDisplaySize();

        view.setPreferredSize( ps );
        view.setMinimumSize( ps );
        view.setMaximumSize( ps );

        Container p = view.getParent();
        p.invalidate();
        p.validate();
        p.repaint();
        // view.repaint();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * @param overlay
     **************************************************************************/
    public void setOverlay( IPaintable overlay )
    {
        this.overlay = overlay;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void reset()
    {
        image.reset();
        view.repaint();
    }

    /***************************************************************************
     * @param raster
     **************************************************************************/
    public void set( IRaster raster )
    {
        image.set( raster );
        view.repaint();
    }

    /***************************************************************************
     * @param raster
     * @param colorModel
     **************************************************************************/
    public void set( IRaster raster, IColorizer colorModel )
    {
        if( this.image.set( raster, colorModel ) )
        {
            resetSize();
        }

        view.invalidate();
        view.validate();
        view.repaint();
        view.requestFocus();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public IRaster getRaster()
    {
        return image.getRaster();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public DirectImage getImage()
    {
        return image.getImage();
    }

    /***************************************************************************
     * @param callback
     **************************************************************************/
    public void setHoverCallback( Consumer<Point> callback )
    {
        this.imageHoverCallback = callback != null ? callback : ( p ) -> {
        };
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public RasterImage getRasterImage()
    {
        return image;
    }
}
