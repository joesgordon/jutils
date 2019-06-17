package org.jutils.task;

import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.ui.event.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TaskView implements ITaskView
{
    /**  */
    private final JPanel view;

    /** The message field. */
    private final JTextArea messageField;
    /** The progress bar. */
    private final JProgressBar progressBar;
    /** The message field. */
    private final JButton cancelButton;

    /** List of listeners to be called when the user cancels the action. */
    private final ActionListenerList cancelListeners;

    /***************************************************************************
     * 
     **************************************************************************/
    public TaskView()
    {
        this( false );
    }

    /***************************************************************************
     * @param inlineCancel
     **************************************************************************/
    public TaskView( boolean inlineCancel )
    {
        this.messageField = new JTextArea();
        this.progressBar = new JProgressBar();
        this.cancelListeners = new ActionListenerList();
        this.cancelButton = new JButton();

        this.view = createView( inlineCancel );
    }

    /***************************************************************************
     * @param inlineCancel
     * @return
     **************************************************************************/
    private JPanel createView( boolean inlineCancel )
    {
        JPanel contentPanel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        messageField.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
        messageField.setEditable( false );
        messageField.setLineWrap( true );
        messageField.setWrapStyleWord( true );

        progressBar.setStringPainted( true );
        progressBar.setString( "" );
        progressBar.setMinimum( 0 );
        progressBar.setMaximum( 100 );

        cancelButton.setIcon( IconConstants.getIcon( IconConstants.STOP_16 ) );
        cancelButton.addActionListener( new TaskCancelListener( this ) );
        cancelButton.setFocusable( false );

        int fieldCols = 1;
        int cancelCol = 0;
        int cancelRow = 2;
        int grow = 20;
        int cancelTop = 10;

        if( inlineCancel )
        {
            fieldCols = 2;
            cancelCol = 1;
            cancelRow = 1;
            grow = 0;
            cancelTop = 0;
            cancelButton.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
            cancelButton.setOpaque( false );
        }
        else
        {
            cancelButton.setText( "Cancel" );
        }

        constraints = new GridBagConstraints( 0, 0, fieldCols, 1, 1.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 4, 4, 4 ), 0, 0 );
        contentPanel.add( messageField, constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 4, 2, 4 ), 0, 0 );
        contentPanel.add( progressBar, constraints );

        constraints = new GridBagConstraints( cancelCol, cancelRow, 1, 1, 0.0,
            0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( cancelTop, 0, 2, 4 ), grow, grow );
        contentPanel.add( cancelButton, constraints );

        return contentPanel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void signalMessage( String message )
    {
        messageField.setText( message );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean signalPercent( int percent )
    {
        if( percent < 0 )
        {
            progressBar.setIndeterminate( true );
            progressBar.setString( "" );
        }
        else
        {
            progressBar.setIndeterminate( false );
            progressBar.setValue( percent );
            progressBar.setString( percent + "%" );
        }

        return true;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void signalError( TaskError error )
    {
        TaskUtils.displayError( view, error );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    @Override
    public void addCancelListener( ActionListener l )
    {
        cancelListeners.addListener( l );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void removeCancelListener( TaskCancelledListener l )
    {
        cancelListeners.removeListener( l );
    }

    /***************************************************************************
     * @param background
     **************************************************************************/
    public void setBackground( Color background )
    {
        view.setBackground( background );
        cancelButton.setBackground( background );
    }

    /***************************************************************************
     * @param fg
     **************************************************************************/
    public void setForeground( Color fg )
    {
        messageField.setForeground( fg );
        cancelButton.setForeground( fg );
    }

    /***************************************************************************
     * @param view
     * @return
     **************************************************************************/
    public static ITaskView createEdtView( ITaskView view )
    {
        return new EdtTv( view );
    }

    /***************************************************************************
     * @param comp
     * @param task
     * @param title
     * @return
     **************************************************************************/
    public static TaskMetrics startAndShow( Component comp, IStatusTask task,
        String title )
    {
        return startAndShow( comp, task, title, false );
    }

    /***************************************************************************
     * @param comp
     * @param task
     * @param title
     * @param playNotify
     * @return
     **************************************************************************/
    public static TaskMetrics startAndShow( Component comp, IStatusTask task,
        String title, boolean playNotify )
    {
        Window parent = SwingUtils.getComponentsWindow( comp );
        TaskView view = new TaskView();
        TaskRunner runner = new TaskRunner( task,
            TaskView.createEdtView( view ) );
        JDialog dialog = new JDialog( parent, ModalityType.DOCUMENT_MODAL );
        TaskFinishedListener finishedListener = new TaskFinishedListener(
            dialog );

        runner.addFinishedListener( finishedListener );

        dialog.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
        dialog.setTitle( title );
        dialog.setContentPane( view.getView() );
        dialog.pack();
        dialog.setSize( 400, dialog.getHeight() + 20 );
        dialog.setLocationRelativeTo( parent );

        Thread thread = new Thread( runner, task.getName() );

        TaskCancelledListener cancelListener = new TaskCancelledListener(
            thread, runner );

        view.addCancelListener( cancelListener );

        thread.start();

        dialog.setVisible( true );

        if( playNotify )
        {
            IconConstants.playNotify();
        }

        // while( thread.getState() != State.TERMINATED )
        // {
        // try
        // {
        // thread.join( 250 );
        // }
        // catch( InterruptedException ex )
        // {
        // }
        // }

        view.removeAll();
        dialog.removeAll();

        return runner.getMetrics();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void removeAll()
    {
        view.removeAll();
        cancelListeners.removeAll();
        for( ActionListener al : cancelButton.getActionListeners() )
        {
            cancelButton.removeActionListener( al );
        }
    }

    /***************************************************************************
     * Listener to be added to the cancel button that, in turn, calls the cancel
     * listeners added to this view.
     **************************************************************************/
    public static class TaskCancelListener implements ActionListener
    {
        private final TaskView view;

        public TaskCancelListener( TaskView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            view.cancelButton.setEnabled( false );
            view.cancelListeners.fireListeners( this, 0, null );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class EdtTv implements ITaskView
    {
        private final ITaskView view;

        private int lastPercent = -1;

        public EdtTv( ITaskView view )
        {
            this.view = view;
        }

        @Override
        public boolean signalPercent( final int percent )
        {
            if( percent == lastPercent )
            {
                return false;
            }

            lastPercent = percent;

            SwingUtilities.invokeLater( () -> view.signalPercent( percent ) );

            return true;
        }

        @Override
        public void signalMessage( final String message )
        {
            Runnable r = new Runnable()
            {
                @Override
                public void run()
                {
                    view.signalMessage( message );
                }
            };
            SwingUtilities.invokeLater( r );
        }

        @Override
        public void signalError( final TaskError error )
        {
            Runnable r = new Runnable()
            {
                @Override
                public void run()
                {
                    view.signalError( error );
                }
            };
            invokeWaitIgnore( r );
        }

        @Override
        public Component getView()
        {
            return view.getView();
        }

        @Override
        public void addCancelListener( ActionListener listener )
        {
            view.addCancelListener( listener );
        }

        private static void invokeWaitIgnore( Runnable r )
        {
            try
            {
                SwingUtilities.invokeAndWait( r );
            }
            catch( InvocationTargetException ex )
            {
            }
            catch( InterruptedException ex )
            {
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class TaskCancelledListener implements ActionListener
    {
        private final Thread thread;
        private final TaskRunner runner;

        public TaskCancelledListener( Thread thread, TaskRunner runner )
        {
            this.thread = thread;
            this.runner = runner;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            runner.stop();
            thread.interrupt();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class TaskFinishedListener
        implements ItemActionListener<Boolean>, Runnable
    {
        private final JDialog dialog;

        public TaskFinishedListener( JDialog dialog )
        {
            this.dialog = dialog;
        }

        @Override
        public void actionPerformed( ItemActionEvent<Boolean> event )
        {
            try
            {
                SwingUtilities.invokeAndWait( this );
            }
            catch( InvocationTargetException ex )
            {
            }
            catch( InterruptedException ex )
            {
            }
        }

        @Override
        public void run()
        {
            dialog.setVisible( false );
        }
    }
}
