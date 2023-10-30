package jutils.core.task;

import java.awt.event.ActionListener;

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
import jutils.core.ui.event.ActionAdapter;

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

            if( !Utils.sleep( 500 ) )
            {
                break;
            }
        }

        handler.signalPercent( 100 );
    }
}
