package jutils.core.ui.calendar;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import jutils.core.IconConstants;
import jutils.core.SwingUtils;
import jutils.core.ui.StandardFormView;
import jutils.core.ui.StandardFrameView;
import jutils.core.ui.app.AppRunner;
import jutils.core.ui.app.IFrameApp;

/*******************************************************************************
 * Defines an application that simply displays a calendar.
 ******************************************************************************/
public class CalDisMain
{
    /***************************************************************************
     * Defines the main entry point for this application. Arguments are ignored.
     * @param args ignored.
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.invokeLater( new CalDisApp() );
    }

    /***************************************************************************
     * Defines the {@link IFrameApp} used to create and display this
     * applications UI.
     **************************************************************************/
    public static class CalDisApp implements IFrameApp
    {
        @Override
        public JFrame createFrame()
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

        private static Container createContent()
        {
            JTabbedPane tabs = new JTabbedPane();

            tabs.addTab( "Date View", createDateView() );

            tabs.addTab( "Date Field", createDateField() );

            tabs.addTab( "Time Field", createTimeField() );

            tabs.addTab( "Date/Time Field", createDateTimeField() );

            return tabs;
        }

        private static Component createDateView()
        {
            JPanel panel = new JPanel();

            panel.add( new DateView().getView() );

            return panel;
        }

        private static Component createDateField()
        {
            StandardFormView form = new StandardFormView();

            form.addField( new DateField( "Date" ) );

            return form.getView();
        }

        private static Component createTimeField()
        {
            StandardFormView form = new StandardFormView();

            form.addField( new TimeField( "Time" ) );

            return form.getView();
        }

        private static Component createDateTimeField()
        {
            StandardFormView form = new StandardFormView();

            form.addField( new DateTimeField( "Date/Time" ) );

            return form.getView();
        }

        @Override
        public void finalizeGui()
        {
        }
    }
}