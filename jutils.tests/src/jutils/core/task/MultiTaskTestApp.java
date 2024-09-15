package jutils.core.task;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JToolBar;

import jutils.core.IconConstants;
import jutils.core.SwingUtils;
import jutils.core.Utils;
import jutils.core.io.LogUtils;
import jutils.core.ui.JGoodiesToolBar;
import jutils.core.ui.StandardFrameView;
import jutils.core.ui.app.AppRunner;
import jutils.core.ui.app.IFrameApp;
import jutils.core.ui.event.ActionAdapter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MultiTaskTestApp implements IFrameApp
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        StandardFrameView frameView = new StandardFrameView();
        JFrame frame = frameView.getView();

        frameView.setToolbar( createToolbar( frame ) );

        frame.setTitle( "Testing Multi Tasking" );
        frame.setIconImages( IconConstants.getPageMagImages() );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize( 400, 400 );

        return frame;
    }

    /***************************************************************************
     * @param frame
     * @return
     **************************************************************************/
    private static JToolBar createToolbar( JFrame frame )
    {
        JToolBar toolbar = new JGoodiesToolBar();

        SwingUtils.addActionToToolbar( toolbar, createGoAction( frame ) );

        return toolbar;
    }

    /***************************************************************************
     * @param frame
     * @return
     **************************************************************************/
    private static Action createGoAction( JFrame frame )
    {
        Action action;
        ActionListener listener;
        Icon icon;
        String name;

        name = "Go";
        icon = IconConstants.getIcon( IconConstants.NAV_NEXT_16 );
        listener = new GoListener( frame );
        action = new ActionAdapter( listener, name, icon );

        return action;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void finalizeGui()
    {
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.invokeLater( new MultiTaskTestApp() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class GoListener implements ActionListener
    {
        private final JFrame frame;

        public GoListener( JFrame frame )
        {
            this.frame = frame;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            TaskMetrics metrics = MultiTaskView.startAndShow( frame,
                new SampleTasker( 120 ), "Testing 1... 2... 3..", 4 );

            LogUtils.printDebug( "Interrupted: %b", metrics.interrupted );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class SampleTasker implements IMultiTask
    {
        private final List<SampleTask> tasks;

        private int i;

        public SampleTasker( int numTasks )
        {
            this.tasks = new ArrayList<>();

            for( int i = 0; i < numTasks; i++ )
            {
                tasks.add( new SampleTask( ( i + 1 ) + " of " + numTasks ) );
            }

            this.i = 0;
        }

        @Override
        public IStatusTask nextTask()
        {
            return i < tasks.size() ? tasks.get( i++ ) : null;
        }

        @Override
        public int getTaskCount()
        {
            return tasks.size();
        }

        @Override
        public String getTaskAction()
        {
            return "Testing task";
        }

        @Override
        public void startup()
        {
        }

        @Override
        public void shutdown()
        {
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class SampleTask implements IStatusTask
    {
        private final String name;
        private final long millis;

        public SampleTask( String name )
        {
            this.name = name;
            this.millis = 500 + new Random().nextInt( 500 );
        }

        @Override
        public void run( ITaskStatusHandler handler )
        {
            for( int i = 0; i < 10 && handler.canContinue(); i++ )
            {
                int percent = i * 100 / 10;

                handler.signalPercent( percent );

                LogUtils.printDebug( "Percent : " + percent );

                if( Utils.sleep( millis ) )
                {
                    break;
                }

                // if( percent > 80 )
                // {
                // handler.signalError( new TaskError( "Test Error",
                // "Testing the error capabilities" ) );
                // // throw new RuntimeException( "jdlfjlkfsdjlkfsdlkjdfsdlkj"
                // // );
                // }
            }

            handler.signalPercent( 100 );
        }

        @Override
        public String getName()
        {
            return name;
        }
    }
}
