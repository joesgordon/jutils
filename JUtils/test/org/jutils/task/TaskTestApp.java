package org.jutils.task;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.io.LogUtils;
import org.jutils.ui.JGoodiesToolBar;
import org.jutils.ui.StandardFrameView;
import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;
import org.jutils.ui.event.ActionAdapter;

public class TaskTestApp implements IFrameApp
{
    @Override
    public JFrame createFrame()
    {
        StandardFrameView frameView = new StandardFrameView();
        JFrame frame = frameView.getView();

        frameView.setToolbar( createToolbar( frame ) );

        frame.setIconImages( IconConstants.getPageMagImages() );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize( 400, 400 );

        return frame;
    }

    private static JToolBar createToolbar( JFrame frame )
    {
        JToolBar toolbar = new JGoodiesToolBar();

        SwingUtils.addActionToToolbar( toolbar, createGoAction( frame ) );

        return toolbar;
    }

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

    @Override
    public void finalizeGui()
    {
    }

    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new TaskTestApp() );
    }

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
            TaskView.startAndShow( frame, new SampleTask(),
                "Testing 1... 2... 3.." );
        }
    }

    private static class SampleTask implements IStatusTask
    {
        @Override
        public void run( ITaskStatusHandler handler )
        {
            handler.signalMessage( "Executing" );

            for( int i = 0; i < 10 && handler.canContinue(); i++ )
            {
                int percent = i * 100 / 10;
                handler.signalPercent( percent );

                LogUtils.printDebug( "Percent : " + percent );

                if( percent >= 70 )
                {
                    handler.signalMessage( "Almost there..." );
                }

                try
                {
                    Thread.sleep( 500 );
                }
                catch( InterruptedException e )
                {
                    break;
                }
            }

            handler.signalPercent( 100 );
        }

        @Override
        public String getName()
        {
            return "Sample Test Task";
        }
    }
}
