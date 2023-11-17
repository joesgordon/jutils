package jutils.core.ui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import jutils.core.IconConstants;
import jutils.core.OptionUtils;
import jutils.core.SwingUtils;
import jutils.core.Utils;
import jutils.core.io.parsers.IntegerParser;
import jutils.core.ui.event.RightClickListener;

/*******************************************************************************
 *
 ******************************************************************************/
public class StatusBarPanel
{
    // -------------------------------------------------------------------------
    // Main panel components
    // -------------------------------------------------------------------------
    /**  */
    private final JProgressBar statusProgressBar;
    /**  */
    private final JLabel memoryLabel;
    /**  */
    private final JPanel view;
    /**  */
    private final JLabel statusLabel;

    // -------------------------------------------------------------------------
    // Popup menu components.
    // -------------------------------------------------------------------------
    /**  */
    private final JPopupMenu popup;

    // -------------------------------------------------------------------------
    // Supporting members.
    // -------------------------------------------------------------------------
    /**  */
    private final Timer swingTimer;
    /**  */
    private final ComponentFlasher flasher;

    /***************************************************************************
     *
     **************************************************************************/
    public StatusBarPanel()
    {
        GridBagConstraints constraints;

        // ---------------------------------------------------------------------
        // Setup popup menu.
        // ---------------------------------------------------------------------
        popup = createPopupMenu();

        // ---------------------------------------------------------------------
        // Setup the memory panel. Need to put the label in its own panel to
        // make the spacing right.
        // ---------------------------------------------------------------------
        memoryLabel = new JLabel( "" );
        memoryLabel.addMouseListener(
            new RightClickListener( ( e ) -> handleMemoryRightClick( e ) ) );

        statusLabel = new JLabel();

        // ---------------------------------------------------------------------
        // Setup refresh toobar.
        // ---------------------------------------------------------------------
        JToolBar toolbar = createToolbar();

        // ---------------------------------------------------------------------
        // Setup the progress bar.
        // ---------------------------------------------------------------------
        statusProgressBar = new JProgressBar();

        statusProgressBar.setLayout( new GridBagLayout() );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 4, 0, 4 ), 0, 0 );
        statusProgressBar.add( statusLabel, constraints );

        // ---------------------------------------------------------------------
        // Setup the main panel.
        // ---------------------------------------------------------------------
        view = new JPanel( new GridBagLayout() );

        // statusProgress.setForeground( new Color( 50, 130, 180 ) );
        statusProgressBar.setBorder( new EmptyBorder( 4, 4, 4, 4 ) );
        statusProgressBar.setOpaque( false );
        statusProgressBar.setString( "" );
        statusProgressBar.setStringPainted( true );
        statusProgressBar.setBorderPainted( false );
        statusProgressBar.setAlignmentX( Component.LEFT_ALIGNMENT );

        view.add( statusProgressBar,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );
        view.add( memoryLabel,
            new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );
        view.add( toolbar,
            new GridBagConstraints( 2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 2 ) );

        flasher = new ComponentFlasher( memoryLabel );
        swingTimer = new Timer( 10000, ( e ) -> refreshStatus( false ) );

        swingTimer.setRepeats( true );
        swingTimer.start();

        refreshStatus( false );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Component getView()
    {
        return view;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JToolBar();

        toolbar.setMargin( new Insets( 0, 0, 0, 0 ) );
        JButton refreshButton = new JButton();
        // refreshButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        refreshButton.setText( "" );
        refreshButton.setIcon(
            IconConstants.getIcon( IconConstants.REFRESH_16 ) );
        refreshButton.setFocusable( false );
        refreshButton.addActionListener( ( e ) -> handleRefresh() );

        toolbar.add( refreshButton );

        SwingUtils.setToolbarDefaults( toolbar );

        return toolbar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPopupMenu createPopupMenu()
    {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem delayMenuItem = new JMenuItem( "Set Delay" );
        JMenuItem flashMenuItem = new JMenuItem( "Flash" );

        flashMenuItem.addActionListener( ( e ) -> flashProgress() );

        delayMenuItem.addActionListener( ( e ) -> handleSetDelay() );
        popup.add( delayMenuItem );
        popup.add( flashMenuItem );

        return popup;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void flashProgress()
    {
        Thread t = new Thread( () -> runFlasher(), "ProgressFlasher" );
        t.start();
    }

    /***************************************************************************
     * @param runGC boolean
     **************************************************************************/
    public void refreshStatus( boolean runGC )
    {
        Runtime rt = Runtime.getRuntime();

        long freeMemory = rt.freeMemory();
        long maxMemory = rt.maxMemory();
        long totalMemory = rt.totalMemory();
        long usedMemory = totalMemory - freeMemory;

        double percentUsed = ( double )usedMemory / ( double )maxMemory;
        int per = ( int )( 100 * percentUsed );
        int memMeg = ( int )( maxMemory / 1024 / 1024 );

        memoryLabel.setText( Integer.toString( per ) + "% of " +
            Integer.toString( memMeg ) + "MB" );
        evalMem( percentUsed );

        if( runGC )
        {
            Thread collector = new Thread( () -> runGc(), "GCThread" );
            collector.start();
        }
    }

    /***************************************************************************
     * @param millis int
     **************************************************************************/
    public void setDelay( int millis )
    {
        if( millis > 50 )
        {
            swingTimer.setDelay( millis );
        }
    }

    /***************************************************************************
     * @param percentUsed double
     **************************************************************************/
    private void evalMem( double percentUsed )
    {
        // LogUtils.printDebug( "% used = " + percentUsed );

        if( percentUsed > 0.95 )
        {
            flasher.startFlashing();
        }
        else
        {
            flasher.stopFlashing();
        }
    }

    /***************************************************************************
     * @param text String
     **************************************************************************/
    public void setText( String text )
    {
        statusLabel.setText( text );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void handleRefresh()
    {
        refreshStatus( true );
        flashProgress();
        IconConstants.playNotify();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void handleSetDelay()
    {
        Integer delay = OptionUtils.promptForValue( StatusBarPanel.this.view,
            "delay", new IntegerParser(), "New Delay in seconds" );

        if( delay != null )
        {
            setDelay( delay.intValue() * 1000 );
        }
    }

    /***************************************************************************
     * @param e
     **************************************************************************/
    private void handleMemoryRightClick( MouseEvent e )
    {
        // LogUtils.printDebug( "Right-click" );
        popup.show( e.getComponent(), e.getX(),
            e.getY() - popup.getPreferredSize().height );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void runFlasher()
    {
        for( int i = 1; i < 101; i += 5 )
        {
            int val = i;
            SwingUtilities.invokeLater(
                () -> statusProgressBar.setValue( val ) );

            if( !Utils.sleep( 30 ) )
            {
                break;
            }
        }

        Utils.sleep( 100 );

        SwingUtilities.invokeLater( () -> statusProgressBar.setValue( 0 ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void runGc()
    {
        Runtime.getRuntime().gc();
        refreshStatus( false );
    }
}
