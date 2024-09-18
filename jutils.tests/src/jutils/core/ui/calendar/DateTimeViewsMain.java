package jutils.core.ui.calendar;

import java.awt.Container;

import javax.swing.JFrame;

import jutils.core.IconConstants;
import jutils.core.SwingUtils;
import jutils.core.ui.StandardFormView;
import jutils.core.ui.StandardFrameView;
import jutils.core.ui.app.AppRunner;
import jutils.core.ui.app.IFrameApp;
import jutils.core.ui.time.DateAndTimeField;
import jutils.core.ui.time.TimeField;
import jutils.demo.ui.jutils.DateTimeViews;

/*******************************************************************************
 * Defines an application that simply displays a calendar.
 ******************************************************************************/
public class DateTimeViewsMain
{
    /***************************************************************************
     * Defines the main entry point for this application. Arguments are ignored.
     * @param args ignored.
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.DEFAULT_LAF = AppRunner.SIMPLE_LAF;

        AppRunner.invokeLater( () -> createFrame2() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static JFrame createFrame2()
    {
        StandardFrameView view = new StandardFrameView();
        JFrame frame = view.getView();

        // JPanel panel = new JPanel( new BorderLayout() );
        // panel.add( new DateTimeViews().getView(), BorderLayout.CENTER );

        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setTitle( "CalDis" );
        frame.setContentPane( new DateTimeViews().getView() );
        // frame.setContentPane( panel );

        frame.setSize( 500, 500 );
        // frame.pack();

        // SwingUtils.createTrayIcon(
        // IconConstants.getImage( IconConstants.CALENDAR_16 ), "CalDis",
        // frame, null );

        return frame;
    }

    /***************************************************************************
     * Defines the {@link IFrameApp} used to create and display this
     * applications UI.
     * @return
     **************************************************************************/
    public static JFrame createFrame()
    {
        StandardFrameView view = new StandardFrameView();
        JFrame frame = view.getView();

        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setTitle( "CalDis" );
        frame.setContentPane( createContent() );

        frame.setSize( 500, 500 );
        // frame.pack();

        SwingUtils.createTrayIcon(
            IconConstants.getImage( IconConstants.CALENDAR_16 ), "CalDis",
            frame, null );

        return frame;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static Container createContent()
    {
        StandardFormView form = new StandardFormView();

        form.addField( "Date View", new DateView().getView() );
        form.addField( new DateField( "Date Field" ) );
        form.addField( new TimeField( "Time Field" ) );
        form.addField( new DateAndTimeField( "Date/Time Field" ) );

        return form.getView();
    }
}
