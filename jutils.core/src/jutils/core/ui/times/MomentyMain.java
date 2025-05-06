package jutils.core.ui.times;

import java.time.Duration;
import java.time.LocalDateTime;

import javax.swing.JFrame;

import jutils.core.io.LogUtils;
import jutils.core.ui.StandardFrameView;
import jutils.core.ui.app.AppRunner;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MomentyMain
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.DEFAULT_LAF = AppRunner.JGOODIES_LAF;

        LocalDateTime ft_epoch = LocalDateTime.of( 1601, 1, 1, 0, 0 );
        LocalDateTime linux_epoch = LocalDateTime.of( 1970, 1, 1, 0, 0 );

        Duration d = Duration.between( ft_epoch, linux_epoch );

        long millis = d.toMillis();

        LogUtils.printDebug( "ft delta is %d", millis );

        AppRunner.invokeLater( () -> createFrame() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static JFrame createFrame()
    {
        StandardFrameView view = new StandardFrameView();
        JFrame frame = view.getView();
        TimesView times = new TimesView();

        view.setTitle( "Momenty" );
        view.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        view.setSize( 500, 800 );
        view.setContent( times.getView() );

        return frame;
    }
}
