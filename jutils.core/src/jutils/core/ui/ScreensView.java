package jutils.core.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import jutils.core.INamedItem;
import jutils.core.laf.UIProperty;
import jutils.core.ui.event.MouseClickListener;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ScreensView implements IView<JComponent>
{
    /**  */
    private final PaintingComponent view;
    /**  */
    private final JLabel idLabel;
    /**  */
    private final Color screenBgColor;
    /**  */
    private final Color screenFgColor;
    /**  */
    private final Color selectedBgColor;
    /**  */
    private final Color selectedFgColor;

    /**  */
    private final List<ScreenInfo> screens;

    /**  */
    private String screenId;
    /**  */
    private boolean editable;
    /**  */
    private IUpdater<String> updater;

    /***************************************************************************
     * 
     **************************************************************************/
    public ScreensView()
    {
        this.view = new PaintingComponent( ( c, g ) -> paintScreens( c, g ) );
        this.idLabel = new JLabel();
        this.screenBgColor = UIProperty.LIST_BACKGROUND.getColor();
        this.screenFgColor = idLabel.getForeground();
        this.selectedBgColor = UIProperty.LIST_SELECTIONBACKGROUND.getColor();
        this.selectedFgColor = UIProperty.LIST_SELECTIONFOREGROUND.getColor();

        this.screens = new ArrayList<>();

        this.screenId = "";
        this.editable = true;
        this.updater = null;

        view.setPreferredSize( new Dimension( 200, 200 ) );
        view.addMouseListener( new MouseClickListener(
            ( pc, c, p, m ) -> handleClick( pc, c, p, m ) ) );

        idLabel.setFont(
            view.getFont().deriveFont( Font.BOLD ).deriveFont( 16f ) );
        idLabel.setHorizontalAlignment( SwingConstants.CENTER );
        idLabel.setVerticalAlignment( SwingConstants.CENTER );

        screens.addAll( detectScreens() );
    }

    /***************************************************************************
     * @param c
     * @param g
     **************************************************************************/
    private void paintScreens( JComponent c, Graphics2D g )
    {
        int w = c.getWidth();
        int h = c.getHeight();

        g.setColor( c.getBackground() );
        g.fillRect( 0, 0, w, h );

        g.setColor( c.getForeground() );

        int minSx = 0;
        int maxSx = 0;

        int minSy = 0;
        int maxSy = 0;

        int maxSw = 0; // Max screen width
        int maxSh = 0; // Max screen height

        for( ScreenInfo screen : screens )
        {
            minSx = Math.min( screen.bounds.x, minSx );
            maxSx = Math.max( screen.bounds.x + screen.bounds.width, maxSx );

            minSy = Math.min( screen.bounds.y, minSy );
            maxSy = Math.max( screen.bounds.y + screen.bounds.height, maxSy );

            maxSw = Math.max( screen.bounds.width, maxSw );
            maxSh = Math.max( screen.bounds.height, maxSh );
        }

        int totW = maxSx - minSx;
        int totH = maxSy - minSy;

        int dx = 8;
        int dy = 8;
        int dw = w - dx * 2;
        int dh = h - dy * 2;

        double rw = dw / ( double )totW;
        double rh = dh / ( double )totH;

        double r = Math.min( rw, rh );

        // LogUtils.printDebug( "r = %f : %d/%d or %d/%d", r, dw, totW, dh,
        // totH
        // );
        // LogUtils.printDebug( "minSx = %d, minSy = %d", minSx, minSy );

        for( int i = 0; i < screens.size(); i++ )
        {
            ScreenInfo screen = screens.get( i );
            boolean selected = screen.id.equals( screenId );

            screen.screen.x = dx + ( screen.bounds.x - minSx ) * r;
            screen.screen.y = dy + ( screen.bounds.y - minSy ) * r;
            screen.screen.width = screen.bounds.width * r;
            screen.screen.height = screen.bounds.height * r;
            screen.screen.archeight = 10;
            screen.screen.arcwidth = 10;

            // LogUtils.printDebug( "screen %s,%s,%s,%s", screen.screen.x,
            // screen.screen.y, screen.screen.width, screen.screen.height );

            RoundRectangle2D.Double outer = screen.screen;

            g.setColor( c.getForeground() );
            g.draw( outer );

            RoundRectangle2D.Double inner = new RoundRectangle2D.Double();

            inner.x = outer.x + 2;
            inner.y = outer.y + 2;
            inner.width = outer.width - 4;
            inner.height = outer.height - 4;
            inner.archeight = outer.archeight;
            inner.arcwidth = outer.arcwidth;

            g.setColor( selected ? selectedBgColor : screenBgColor );
            g.fill( inner );

            idLabel.setText( screen.id );
            idLabel.repaint();
            idLabel.validate();

            Dimension td = idLabel.getPreferredSize();

            int tx = ( int )Math.round(
                screen.screen.x + screen.screen.width / 2. - td.width / 2. );
            int ty = ( int )Math.round(
                screen.screen.y + screen.screen.height / 2. - td.height / 2. );

            idLabel.setSize( td );
            idLabel.setForeground( selected ? selectedFgColor : screenFgColor );

            g.translate( tx, ty );
            idLabel.paint( g );
            g.translate( -tx, -ty );
        }
    }

    /***************************************************************************
     * @param primaryClicked
     * @param count
     * @param p
     * @param modifiers
     **************************************************************************/
    private void handleClick( boolean primaryClicked, int count, Point p,
        int modifiers )
    {
        if( !editable || !primaryClicked || count != 1 )
        {
            return;
        }

        boolean controlPressed = MouseEvent.CTRL_DOWN_MASK == ( modifiers &
            MouseEvent.CTRL_DOWN_MASK );

        String newSelection = null;

        for( int i = 0; i < screens.size(); i++ )
        {
            ScreenInfo screen = screens.get( i );

            if( screen.screen.contains( p ) )
            {
                if( controlPressed && screen.id.equals( this.screenId ) )
                {
                    newSelection = "";
                }
                else
                {
                    newSelection = screen.id;
                }
                break;
            }
        }

        if( newSelection != null && !newSelection.equals( this.screenId ) )
        {
            this.screenId = newSelection;

            if( updater != null )
            {
                updater.update( screenId );
            }
        }

        view.repaint();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void refresh()
    {
        boolean generate = false;

        if( generate )
        {
            generateScreens();
        }
        else
        {
            refreshScreens();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void refreshScreens()
    {
        screens.clear();

        List<ScreenInfo> screens = detectScreens();

        String defaultId = null;
        boolean selectedFound = false;

        for( int i = 0; i < screens.size(); i++ )
        {
            ScreenInfo info = screens.get( i );

            screens.add( info );

            defaultId = info.isDefault ? info.id : defaultId;
            selectedFound = selectedFound ? selectedFound
                : info.id.equals( screenId );

            // LogUtils.printDebug( "screen %s: %d, %d, %d x %d", id, r.x, r.y,
            // r.width, r.height );
        }

        if( !selectedFound )
        {
            screenId = defaultId;
        }
    }

    /***************************************************************************
     * @param updater
     **************************************************************************/
    public void setUpdater( IUpdater<String> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static List<ScreenInfo> generateScreens()
    {
        List<ScreenInfo> screens = new ArrayList<>();

        for( int i = 0; i < 4; i++ )
        {
            String id = "\\Display" + i;
            Rectangle r = new Rectangle();
            r.x = ( i - 1 ) * 1920;
            r.y = 0;
            r.width = 1920;
            r.height = i == 2 ? 1200 : 1080;
            ScreenInfo info = new ScreenInfo( id, r, i == 0 );
            screens.add( info );
        }

        return screens;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static List<ScreenInfo> detectScreens()
    {
        List<ScreenInfo> screens = new ArrayList<>();

        GraphicsEnvironment ge;
        ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        GraphicsDevice [] devs = ge.getScreenDevices();
        GraphicsDevice defaultDev = ge.getDefaultScreenDevice();

        for( int i = 0; i < devs.length; i++ )
        {
            GraphicsDevice dev = devs[i];
            String id = dev.getIDstring();
            boolean isDefault = defaultDev == dev;
            GraphicsConfiguration gc = dev.getDefaultConfiguration();
            Rectangle r = gc.getBounds();
            ScreenInfo info = new ScreenInfo( id, r, isDefault );

            screens.add( info );
        }

        return screens;
    }

    /***************************************************************************
     * @param screenId
     **************************************************************************/
    public void setSelected( String screenId )
    {
        this.screenId = screenId;
        view.repaint();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String getSelected()
    {
        return this.screenId;
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
     * 
     **************************************************************************/
    public static final class ScreenInfo implements INamedItem
    {
        /**  */
        public final String id;
        /**  */
        public final Rectangle bounds;
        /**  */
        public final boolean isDefault;
        /**  */
        public final RoundRectangle2D.Double screen;

        /**
         * @param id
         * @param bounds
         * @param isDefault
         */
        public ScreenInfo( String id, Rectangle bounds, boolean isDefault )
        {
            this.id = id;
            this.bounds = bounds;
            this.isDefault = isDefault;
            this.screen = new RoundRectangle2D.Double( 0, 0, 0, 0, 0, 0 );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getName()
        {
            return id;
        }
    }
}
