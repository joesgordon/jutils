package jutils.core.ui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import jutils.core.IconConstants;
import jutils.core.OptionUtils;
import jutils.core.SwingUtils;
import jutils.core.Utils;
import jutils.core.io.parsers.IntegerParser;
import jutils.core.ui.event.RightClickListener;
import jutils.core.ui.model.IView;

/*******************************************************************************
 *
 ******************************************************************************/
public class StatusBarView implements IView<JComponent>
{
    /**  */
    private static final int TIMER_PERIOD = 10000;

    // -------------------------------------------------------------------------
    // Main panel components
    // -------------------------------------------------------------------------
    /**  */
    private final JPanel view;

    /**  */
    private final JLabel statusLabel;
    /**  */
    private final JProgressBar statusBar;
    /**  */
    protected final JPanel additionalStatus;
    /**  */
    private final JLabel memoryLabel;
    /**  */
    protected final JToolBar toolbar;

    // -------------------------------------------------------------------------
    // Popup menu components.
    // -------------------------------------------------------------------------
    /**  */
    protected final JPopupMenu popup;

    // -------------------------------------------------------------------------
    // Supporting members.
    // -------------------------------------------------------------------------
    /**  */
    private final ComponentFlasher flasher;
    /**  */
    private final Timer swingTimer;
    /**  */
    private final MemoryStatus memory;

    /***************************************************************************
     *
     **************************************************************************/
    public StatusBarView()
    {
        this.statusLabel = new JLabel( "" );
        this.statusBar = createProgressBar();
        this.additionalStatus = new JPanel();
        this.memoryLabel = new JLabel( "" );
        this.toolbar = new JToolBar();
        this.view = createView();
        this.popup = createPopupMenu();
        this.flasher = new ComponentFlasher( memoryLabel );
        this.swingTimer = createTimer();
        this.memory = new MemoryStatus();

        refreshStatus( false );

        swingTimer.start();
    }

    /***************************************************************************
     * @return the newly created progress bar.
     **************************************************************************/
    private JProgressBar createProgressBar()
    {
        JProgressBar bar = new JProgressBar();
        GridBagConstraints constraints;

        bar.setLayout( new GridBagLayout() );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 4, 0, 4 ), 0, 0 );
        bar.add( statusLabel, constraints );

        // bar.setForeground( new Color( 50, 130, 180 ) );
        bar.setOpaque( false );
        bar.setString( "" );
        bar.setStringPainted( true );
        bar.setBorderPainted( false );
        bar.setAlignmentX( Component.LEFT_ALIGNMENT );

        return bar;
    }

    /***************************************************************************
     * @return the newly created main view.
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        memoryLabel.addMouseListener(
            new RightClickListener( ( e ) -> handleMemoryRightClick( e ) ) );

        int c = 0;

        constraints = new GridBagConstraints( c++, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 4, 4, 4, 4 ), 0, 0 );
        panel.add( statusBar, constraints );

        constraints = new GridBagConstraints( c++, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( additionalStatus, constraints );

        constraints = new GridBagConstraints( c++, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( memoryLabel, constraints );

        createToolbar();

        constraints = new GridBagConstraints( c++, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 2 );
        panel.add( toolbar, constraints );

        return panel;
    }

    /***************************************************************************
     * Creates a timer that calls {@link #refreshStatus(boolean)} at
     * {@link #TIMER_PERIOD}.
     * @return the newly created timer.
     **************************************************************************/
    private Timer createTimer()
    {
        Timer t = new Timer( TIMER_PERIOD, ( e ) -> refreshStatus( false ) );

        t.setRepeats( true );

        return t;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void createToolbar()
    {
        SwingUtils.setToolbarDefaults( toolbar );

        toolbar.setMargin( new Insets( 0, 0, 0, 0 ) );
        JButton refreshButton = new JButton();
        // refreshButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        refreshButton.setText( "" );
        refreshButton.setIcon(
            IconConstants.getIcon( IconConstants.REFRESH_16 ) );
        refreshButton.setFocusable( false );
        refreshButton.addActionListener( ( e ) -> handleRefresh() );

        toolbar.add( refreshButton );
    }

    /***************************************************************************
     * @return the newly created popup menu.
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
    public void flashProgress()
    {
        Thread t = new Thread( () -> executeFlasher(), "ProgressFlasher" );
        t.start();
    }

    /***************************************************************************
     * @param runGc boolean
     **************************************************************************/
    protected void refreshStatus( boolean runGc )
    {
        memory.refreshStatus();

        int memMib = ( int )( memory.maxMemory / 1024 / 1024 );

        memoryLabel.setText( memory.percentUsed + "% of " + memMib + "MB" );

        evalMem( memory.percentUsed );

        if( runGc )
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
     * @param text the text to show on the status bar.
     **************************************************************************/
    public void setStatusText( String text )
    {
        statusLabel.setText( text );
    }

    /***************************************************************************
     * @param value percent to fill the status bar between 0 and 100, inclusive.
     **************************************************************************/
    public void setStatusValue( int value )
    {
        statusBar.setValue( value );
    }

    /***************************************************************************
     * @param percentUsed double
     **************************************************************************/
    private void evalMem( int percentUsed )
    {
        // LogUtils.printDebug( "% used = " + percentUsed );

        if( percentUsed > 95 )
        {
            flasher.startFlashing();
        }
        else
        {
            flasher.stopFlashing();
        }
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
        Integer delay = OptionUtils.promptForValue( StatusBarView.this.view,
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
    private void executeFlasher()
    {
        for( int i = 1; i < 101; i += 10 )
        {
            int val = i;

            SwingUtilities.invokeLater( () -> statusBar.setValue( val ) );

            if( Utils.sleep( 20 ) )
            {
                break;
            }
        }

        Utils.sleep( 100 );

        SwingUtilities.invokeLater( () -> statusBar.setValue( 0 ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void runGc()
    {
        Runtime.getRuntime().gc();
        refreshStatus( false );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    protected static class MemoryStatus
    {
        /**  */
        public long freeMemory;
        /**  */
        public long maxMemory;
        /**  */
        public long totalMemory;
        /**  */
        public long usedMemory;

        /**  */
        public int percentUsed;

        /**
         * 
         */
        public MemoryStatus()
        {
            this.freeMemory = 0L;
            this.maxMemory = 0L;
            this.totalMemory = 0L;
            this.usedMemory = 0L;
            this.percentUsed = 0;
        }

        /**
         * 
         */
        public void refreshStatus()
        {
            Runtime rt = Runtime.getRuntime();

            this.freeMemory = rt.freeMemory();
            this.maxMemory = rt.maxMemory();
            this.totalMemory = rt.totalMemory();
            this.usedMemory = totalMemory - freeMemory;

            double fractionUsed = ( double )usedMemory / ( double )maxMemory;
            this.percentUsed = ( int )( 100 * fractionUsed );
        }
    }
}
