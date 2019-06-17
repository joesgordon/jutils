package org.jutils.task;

import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MultiTaskView implements IMultiTaskView
{
    /**  */
    private final JPanel view;

    /** The title field. */
    private final JLabel titleField;
    /** The progress bar. */
    private final JProgressBar progressBar;
    /**  */
    private final ViewList progressList;
    /**  */
    private final JButton cancelButton;

    /**  */
    private String title;

    /***************************************************************************
     * 
     **************************************************************************/
    public MultiTaskView()
    {
        this.titleField = new JLabel();
        this.progressBar = new JProgressBar();
        this.progressList = new ViewList();
        this.cancelButton = new JButton();

        this.view = createView();

        this.title = "";
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        progressBar.setStringPainted( true );

        cancelButton.setText( "Cancel" );
        cancelButton.setIcon( IconConstants.getIcon( IconConstants.STOP_16 ) );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
            new Insets( 4, 4, 2, 4 ), 0, 0 );
        panel.add( titleField, constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
            new Insets( 4, 4, 2, 4 ), 0, 0 );
        panel.add( progressBar, constraints );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 1.0, 1.0,
            GridBagConstraints.EAST, GridBagConstraints.BOTH,
            new Insets( 4, 4, 2, 4 ), 0, 0 );
        panel.add( progressList.getView(), constraints );

        constraints = new GridBagConstraints( 0, 3, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 4, 4, 4, 4 ), 20, 20 );
        panel.add( cancelButton, constraints );

        return panel;
    }

    private void addCancelListener( ActionListener l )
    {
        cancelButton.addActionListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public ITaskView addTaskView( String message )
    {
        TaskView statusView = new TaskView( true );

        statusView.signalMessage( message );
        statusView.signalPercent( -1 );

        progressList.addView( statusView );
        setTitle( this.title );

        statusView.addCancelListener(
            new TaskCancelledListener( this, statusView ) );

        return statusView;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeTask( ITaskView view )
    {
        progressList.removeView( view );
        setTitle( this.title );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setTitle( String title )
    {
        this.title = title == null ? "" : title;

        String space = this.title.isEmpty() ? "" : " ";
        String txt = this.title + space + "(" + progressList.getCount() +
            " Concurrent)";

        titleField.setText( txt );
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
     * 
     **************************************************************************/
    @Override
    public void setPercent( int percent )
    {
        if( percent > -1 )
        {
            progressBar.setIndeterminate( false );
            progressBar.setValue( percent );
            progressBar.setString( percent + "%" );
        }
        else
        {
            progressBar.setIndeterminate( true );
            progressBar.setString( "" );
        }
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
     * @return
     **************************************************************************/
    public static IMultiTaskView createEdtView( IMultiTaskView view )
    {
        return new EdtMpv( view );
    }

    /***************************************************************************
     * @param comp
     * @param tasker
     * @param title
     * @param numThreads
     * @return
     **************************************************************************/
    public static TaskMetrics startAndShow( Component comp, IMultiTask tasker,
        String title, int numThreads )
    {
        Window parent = SwingUtils.getComponentsWindow( comp );
        MultiTaskView mtv = new MultiTaskView();
        IMultiTaskView view = MultiTaskView.createEdtView( mtv );
        JDialog dialog = new JDialog( parent, ModalityType.DOCUMENT_MODAL );

        MultiTaskRunner runner = new MultiTaskRunner( tasker, view,
            numThreads );
        CancelListener cl = new CancelListener( mtv, runner );

        mtv.addCancelListener( cl );

        runner.addFinishedListener( new FinishedListener( dialog, runner ) );

        dialog.setDefaultCloseOperation( JDialog.DO_NOTHING_ON_CLOSE );
        dialog.addWindowListener( new CancelListener( mtv, runner ) );
        dialog.setTitle( title );
        dialog.setContentPane( view.getView() );
        dialog.pack();
        dialog.setSize( 600, 400 );
        dialog.setLocationRelativeTo( parent );

        Thread thread = new Thread( runner, title );

        thread.start();

        dialog.setVisible( true );

        return runner.getMetrics();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class EdtMpv implements IMultiTaskView
    {
        private IMultiTaskView view;

        private int lastPercent = -1;

        public EdtMpv( IMultiTaskView view )
        {
            this.view = view;
        }

        @Override
        public ITaskView addTaskView( final String message )
        {
            EdtAddTask r = new EdtAddTask( view, message );

            try
            {
                SwingUtilities.invokeAndWait( r );
            }
            catch( InvocationTargetException e )
            {
            }
            catch( InterruptedException e )
            {
            }

            return r.taskView;
        }

        @Override
        public void setTitle( final String title )
        {
            Runnable r = new Runnable()
            {
                @Override
                public void run()
                {
                    view.setTitle( title );
                }
            };
            SwingUtilities.invokeLater( r );
        }

        @Override
        public void setPercent( final int percent )
        {
            if( percent == lastPercent )
            {
                return;
            }

            lastPercent = percent;

            Runnable r = new Runnable()
            {
                @Override
                public void run()
                {
                    view.setPercent( percent );
                }
            };
            SwingUtilities.invokeLater( r );
        }

        @Override
        public void removeTask( final ITaskView itv )
        {
            Runnable r = new Runnable()
            {
                @Override
                public void run()
                {
                    view.removeTask( itv );
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
            SwingUtilities.invokeLater( r );
        }

        @Override
        public JPanel getView()
        {
            return view.getView();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class EdtAddTask implements Runnable
    {
        private final IMultiTaskView view;
        private final String message;

        public ITaskView taskView = null;

        public EdtAddTask( IMultiTaskView view, String message )
        {
            this.view = view;
            this.message = message;
        }

        @Override
        public void run()
        {
            taskView = TaskView.createEdtView( view.addTaskView( message ) );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class TaskCancelledListener implements ActionListener
    {
        private final MultiTaskView view;
        private final ITaskView taskView;

        public TaskCancelledListener( MultiTaskView view, ITaskView taskView )
        {
            this.view = view;
            this.taskView = taskView;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            view.removeTask( taskView );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class CancelListener extends WindowAdapter
        implements ActionListener
    {
        private final MultiTaskView view;
        private final MultiTaskRunner runner;

        public CancelListener( MultiTaskView view, MultiTaskRunner runner )
        {
            this.view = view;
            this.runner = runner;
        }

        @Override
        public void windowClosing( WindowEvent e )
        {
            stop();
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            stop();
        }

        private void stop()
        {
            view.cancelButton.setEnabled( false );
            runner.stop();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FinishedListener implements ActionListener
    {
        /**  */
        private final JDialog dialog;
        /**  */
        private final MultiTaskRunner runner;

        /**
         * @param dialog
         * @param runner
         */
        public FinishedListener( JDialog dialog, MultiTaskRunner runner )
        {
            this.dialog = dialog;
            this.runner = runner;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            TaskError error = runner.getError();

            if( error != null )
            {
                TaskUtils.displayError( dialog, error );
            }

            dialog.dispose();
        }
    }
}
