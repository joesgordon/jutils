package jutils.colorific;

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import jutils.core.laf.UIProperty;
import jutils.core.ui.ColorIcon;
import jutils.core.ui.ShadowBorder;
import jutils.core.ui.event.ActionAdapter;
import jutils.core.ui.model.IView;

/***************************************************************************
 * 
 **************************************************************************/
public class ColorificView implements IView<JComponent>
{
    /**  */
    private final JPanel view;
    /**  */
    private final ZoomView zoomArea;
    /**  */
    private final JColorChooser colorChooser;
    /**  */
    private final JLabel colorLabel;
    /**  */
    private final SwatchView [] swatches;
    /**  */
    private final Action grabAction;
    /**  */
    private final JToggleButton pickerButton;

    /**  */
    private int selectedIndex;
    /**  */
    private int currentIndex;

    /**  */
    private Timer timer;

    /***************************************************************************
     * 
     **************************************************************************/
    public ColorificView()
    {
        this.zoomArea = new ZoomView();
        this.colorLabel = new JLabel();
        this.swatches = new SwatchView[16];
        this.colorChooser = new JColorChooser();
        this.pickerButton = new JToggleButton();

        this.selectedIndex = 0;
        this.currentIndex = 0;

        this.timer = null;

        this.view = createView();

        KeyStroke key;

        grabAction = new ActionAdapter( ( e ) -> handleGrab(), "grabKeyStroke",
            null );
        key = KeyStroke.getKeyStroke( "control G" );

        grabAction.putValue( Action.ACCELERATOR_KEY, key );
        view.getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW ).put( key,
            "grabKeyStroke" );
        view.getActionMap().put( "grabKeyStroke", grabAction );

        grabAction.setEnabled( false );
    }

    /***************************************************************************
     * @return the panel for this view.
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( createPickerPanel(), BorderLayout.WEST );
        panel.add( colorChooser, BorderLayout.CENTER );

        colorChooser.getChooserPanels();

        return panel;
    }

    /***************************************************************************
     * @return the panel containing the picker, zoom area, and swatches.
     **************************************************************************/
    private Component createPickerPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );

        Action action;
        Icon icon;

        icon = ColorificIcons.loader.getIcon( ColorificIcons.COLOR_PICKER_024 );
        action = new ActionAdapter( ( e ) -> handlePickerButton(), "Pick Color",
            icon );
        pickerButton.setAction( action );
        pickerButton.setText( null );
        pickerButton.setToolTipText( "Pick Color from screen" );
        pickerButton.setFocusable( false );
        pickerButton.setMargin( new Insets( 2, 2, 2, 2 ) );

        colorLabel.setText( "#------" );
        colorLabel.setFont( new Font( "Monospaced", Font.PLAIN, 12 ) );

        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 8, 8, 8, 8 ), 0, 0 );
        panel.add( pickerButton, constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 8, 8, 0, 8 ), 0, 0 );
        panel.add( zoomArea.getView(), constraints );

        constraints = new GridBagConstraints( 1, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 2, 8, 8, 8 ), 0, 0 );
        panel.add( colorLabel, constraints );

        constraints = new GridBagConstraints( 0, 2, 2, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 8, 8, 8, 8 ), 0, 0 );
        panel.add( new JLabel( "Control+G to save current color" ),
            constraints );

        constraints = new GridBagConstraints( 0, 3, 2, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 8, 8, 8, 8 ), 0, 0 );
        panel.add( createSwatchPanel(), constraints );

        constraints = new GridBagConstraints( 0, 4, 2, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 8, 8, 8, 8 ), 0, 0 );
        panel.add( Box.createHorizontalGlue(), constraints );

        return panel;
    }

    /***************************************************************************
     * @return the panel containing the swatches.
     **************************************************************************/
    private Component createSwatchPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        for( int i = 0; i < swatches.length; i++ )
        {
            SwatchView s = new SwatchView();
            int sidx = i;

            swatches[i] = s;

            int x = i % 4;
            int y = i / 4;
            JButton comp = swatches[i].getView();
            int t = 4;
            int l = 4;
            int b = 4;
            int r = 4;

            comp.setToolTipText( "Swatch " + ( i + 1 ) );
            comp.addActionListener( ( e ) -> handleSwatchSelected( s, sidx ) );

            constraints = new GridBagConstraints( x, y, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets( t, l, b, r ), 0, 0 );
            panel.add( comp, constraints );
        }

        swatches[0].setSelected( true );

        return panel;
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
    private void handlePickerButton()
    {
        if( pickerButton.isSelected() )
        {
            startPicking();
        }
        else
        {
            stopPicking();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void handleGrab()
    {
        int nextSelected = currentIndex;

        if( selectedIndex == currentIndex )
        {
            nextSelected++;

            if( nextSelected < 0 )
            {
                nextSelected = swatches.length - 1;
            }

            currentIndex = nextSelected;
        }

        // LogUtils.printDebug( "Selected = %d, Current = %d, Next = %d",
        // selectedIndex, currentIndex, nextSelected );

        swatches[selectedIndex].setSelected( false );
        selectedIndex = nextSelected;
        swatches[selectedIndex].setSelected( true );
    }

    /***************************************************************************
     * @param bot
     **************************************************************************/
    private void handlePickerTimer( Robot bot )
    {
        PointerInfo pi = MouseInfo.getPointerInfo();

        if( pi == null )
        {
            return;
        }

        Point p = pi.getLocation();

        zoomArea.copyArea( p, bot, swatches[selectedIndex], colorLabel );
    }

    /***************************************************************************
     * @param swatch the swatch selected.
     * @param index the index of the swatch selected.
     **************************************************************************/
    private void handleSwatchSelected( SwatchView swatch, int index )
    {
        stopPicking();

        colorChooser.setColor( swatch.getColor() );

        swatches[selectedIndex].setSelected( false );
        swatch.setSelected( true );

        selectedIndex = index;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void startPicking()
    {
        try
        {
            Robot bot = new Robot();

            timer = new Timer( 10, ( e ) -> handlePickerTimer( bot ) );

            timer.start();

            grabAction.setEnabled( true );
        }
        catch( AWTException ex )
        {
            ex.printStackTrace();
            timer.stop();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void stopPicking()
    {
        if( timer != null )
        {
            timer.stop();
            timer = null;
            zoomArea.reset();
            grabAction.setEnabled( false );
            pickerButton.setSelected( false );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ZoomView implements IView<JLabel>
    {
        /**  */
        private static final int CAP_ZOOM = 16 + 1;
        /**  */
        private static final int CAP_SIZE = 9;
        /**  */
        private static final int CAP_HALF = CAP_SIZE / 2;
        /**  */
        public static final int IMG_SIZE = CAP_SIZE * CAP_ZOOM;
        /**  */
        private static final int LINE_W = 3;
        /**  */
        private static final int CH_MIN = CAP_ZOOM * CAP_HALF - LINE_W / 2;
        /**  */
        private static final int CH_MAX = CAP_ZOOM * CAP_HALF - LINE_W / 2 +
            CAP_ZOOM;

        /**  */
        private final Stroke solidStroke;
        /**  */
        private final JLabel zoomLabel;

        /**
         * 
         */
        public ZoomView()
        {
            this.solidStroke = new BasicStroke( LINE_W, BasicStroke.CAP_SQUARE,
                BasicStroke.JOIN_ROUND );
            this.zoomLabel = new JLabel();

            zoomLabel.setPreferredSize( new Dimension( IMG_SIZE, IMG_SIZE ) );
            zoomLabel.setBorder( new ShadowBorder() );
        }

        /**
         * @param p
         * @param bot
         * @param swatch
         * @param colorLabel
         */
        public void copyArea( Point p, Robot bot, SwatchView swatch,
            JLabel colorLabel )
        {
            Color c = bot.getPixelColor( p.x, p.y );

            swatch.setColor( c );
            colorLabel.setText(
                String.format( "#%06X", c.getRGB() & 0x00FFFFFF ) );

            Rectangle sr = new Rectangle( p.x - CAP_HALF, p.y - CAP_HALF,
                CAP_SIZE, CAP_SIZE );
            BufferedImage image = bot.createScreenCapture( sr );
            BufferedImage resizedImage = new BufferedImage( IMG_SIZE, IMG_SIZE,
                BufferedImage.TYPE_INT_RGB );
            Graphics2D g2 = resizedImage.createGraphics();

            g2.drawImage( image, 0, 0, IMG_SIZE, IMG_SIZE, null );

            g2.setStroke( solidStroke );
            g2.setColor( new Color( 255 - c.getRed(), 255 - c.getGreen(),
                255 - c.getBlue() ) );

            g2.drawLine( CH_MIN, CH_MIN, CH_MIN, CH_MAX );
            g2.drawLine( CH_MIN, CH_MIN, CH_MAX, CH_MIN );
            g2.drawLine( CH_MAX, CH_MIN, CH_MAX, CH_MAX );
            g2.drawLine( CH_MIN, CH_MAX, CH_MAX, CH_MAX );

            g2.dispose();

            ImageIcon icon = new ImageIcon( resizedImage );
            zoomLabel.setIcon( icon );
        }

        /**
         * 
         */
        public void reset()
        {
            zoomLabel.setIcon( null );
        }

        /**
         * @{@inheritDoc}
         */
        @Override
        public JLabel getView()
        {
            return zoomLabel;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class SwatchView implements IView<JComponent>
    {
        /**  */
        private final JButton component;
        /**  */
        private final ColorIcon icon;

        /**  */
        private final EmptyBorder invisibleBorder;
        /**  */
        private final LineBorder selectedBorder;

        /**
         * 
         */
        public SwatchView()
        {
            int bdrSize = 2;

            this.icon = new ColorIcon( UIProperty.PANEL_BACKGROUND.getColor(),
                32 );
            this.component = new JButton( icon );
            this.invisibleBorder = new EmptyBorder(
                new Insets( bdrSize, bdrSize, bdrSize, bdrSize ) );
            this.selectedBorder = new LineBorder( Color.blue, bdrSize );

            component.setOpaque( false );
            component.setMargin( new Insets( 2, 2, 2, 2 ) );
            component.setEnabled( false );

            setSelected( false );
        }

        /**
         * @param selected
         */
        public void setSelected( boolean selected )
        {
            if( selected )
            {
                component.setBorder( selectedBorder );
            }
            else
            {
                component.setBorder( invisibleBorder );
            }
        }

        /**
         * @param c
         */
        public void setColor( Color c )
        {
            if( c != null )
            {
                component.setEnabled( true );
            }
            else
            {
                c = UIProperty.PANEL_BACKGROUND.getColor();
                component.setEnabled( false );
            }

            icon.setColor( c );

            component.repaint();
        }

        /**
         * @return
         */
        public Color getColor()
        {
            return icon.getColor();
        }

        /**
         * @{@inheritDoc}
         */
        @Override
        public JButton getView()
        {
            return component;
        }
    }
}
