package org.jutils.core.task;

import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JToolBar;

import org.jutils.core.IconConstants;
import org.jutils.core.SwingUtils;
import org.jutils.core.io.LogUtils;
import org.jutils.core.ui.JGoodiesToolBar;
import org.jutils.core.ui.StandardFrameView;
import org.jutils.core.ui.app.AppRunner;
import org.jutils.core.ui.event.ActionAdapter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TaskTestMain
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.invokeLater( () -> createFrame() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static JFrame createFrame()
    {
        StandardFrameView frameView = new StandardFrameView();
        JFrame frame = frameView.getView();

        frameView.setToolbar( createToolbar( frame ) );

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
        Icon icon;
        ActionListener listener;
        IStatusRunnable runnable;
        IStatusTask task;

        icon = IconConstants.getIcon( IconConstants.NAV_NEXT_16 );
        runnable = ( h ) -> runSampleTask( h );
        task = new NamedStatusTask( "Sample Test Task", runnable );
        listener = ( e ) -> TaskView.startAndShow( frame, task,
            "Testing 1... 2... 3.." );

        return new ActionAdapter( listener, "Go", icon );
    }

    /***************************************************************************
     * @param handler
     **************************************************************************/
    private static void runSampleTask( ITaskStatusHandler handler )
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
}
