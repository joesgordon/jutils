package org.jutils.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.concurrent.GcThread;

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
        memoryLabel.addMouseListener( new RightClickMenuListener() );

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
        swingTimer = new Timer( 10000, new RefreshListener() );

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

    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JToolBar();

        toolbar.setMargin( new Insets( 0, 0, 0, 0 ) );
        JButton refreshButton = new JButton();
        refreshButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        refreshButton.setText( "" );
        refreshButton.setIcon(
            IconConstants.getIcon( IconConstants.REFRESH_16 ) );
        refreshButton.setFocusable( false );
        refreshButton.addActionListener( new RefreshButtonListener() );

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

        flashMenuItem.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                flashProgress();
            }
        } );

        delayMenuItem.addActionListener( new PromptForDelayListener() );
        popup.add( delayMenuItem );
        popup.add( flashMenuItem );

        return popup;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void flashProgress()
    {
        Thread t = new ProgressFlasher();
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
            Thread collector = new GarbageCollectionRunner();
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
    private class ProgressFlasher extends Thread
    {
        @Override
        public void run()
        {
            for( int i = 1; i < 101; i += 5 )
            {
                SwingUtilities.invokeLater( new StatusUpdater( i ) );
                try
                {
                    Thread.sleep( 30 );
                }
                catch( InterruptedException e )
                {
                    break;
                }
            }

            SwingUtilities.invokeLater( new StatusUpdater( 0 ) );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class StatusUpdater implements Runnable
    {
        private int value;

        public StatusUpdater( int val )
        {
            value = val;
        }

        @Override
        public void run()
        {
            statusProgressBar.setValue( value );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class GarbageCollectionRunner extends GcThread
    {
        @Override
        public void run()
        {
            super.run();
            refreshStatus( false );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class RefreshListener implements ActionListener
    {
        // private final Robot robot;

        public RefreshListener()
        {
            // try
            // {
            // this.robot = new Robot();
            // }
            // catch( AWTException ex )
            // {
            // throw new RuntimeException( ex );
            // }
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            // robot.keyPress( KeyEvent.VK_PAUSE );
            refreshStatus( false );
            // robot.keyRelease( KeyEvent.VK_PAUSE );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class RefreshButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            refreshStatus( true );
            flashProgress();
            IconConstants.playNotify();
            // try
            // {
            // byte [] b = new byte[Integer.MAX_VALUE];
            // }
            // catch( OutOfMemoryError err )
            // {
            // }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class PromptForDelayListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            Frame frame = SwingUtils.getComponentsFrame(
                StatusBarPanel.this.view );
            Integer delay = 10;
            Object obj = JOptionPane.showInputDialog( frame,
                "New Delay in seconds: ", delay );
            if( obj != null )
            {
                try
                {
                    delay = Integer.parseInt( obj.toString() );
                    setDelay( delay.intValue() * 1000 );
                }
                catch( NumberFormatException ex )
                {
                    JOptionPane.showMessageDialog( frame,
                        "Sorry, " + obj.toString() + "isn't a valid integer!",
                        "ERROR", JOptionPane.ERROR_MESSAGE );
                }
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class RightClickMenuListener extends MouseAdapter
    {
        @Override
        public void mouseClicked( MouseEvent e )
        {
            if( SwingUtilities.isRightMouseButton( e ) )
            {
                // LogUtils.printDebug( "Right-click" );
                popup.show( e.getComponent(), e.getX(),
                    e.getY() - popup.getPreferredSize().height );
            }
        }
    }
}
