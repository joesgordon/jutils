package jutils.demo.ui.jutils;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import jutils.core.time.ui.DateAndTimeField;
import jutils.core.time.ui.DateField;
import jutils.core.time.ui.DateView;
import jutils.core.time.ui.TimeField;
import jutils.core.ui.StandardFormView;
import jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DateTimeViews implements IView<JComponent>
{
    /**  */
    private final JTabbedPane tabs;

    /***************************************************************************
     * 
     **************************************************************************/
    public DateTimeViews()
    {
        this.tabs = new JTabbedPane();

        tabs.addTab( "Date View", createDateView() );

        tabs.addTab( "Date Field", createDateField() );

        tabs.addTab( "Time Field", createTimeField() );

        tabs.addTab( "Date/Time Field", createDateTimeField() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static Component createDateView()
    {
        JPanel panel = new JPanel();

        panel.add( new DateView().getView() );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static Component createDateField()
    {
        StandardFormView form = new StandardFormView();

        form.addField( new DateField( "Date" ) );

        return form.getView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static Component createTimeField()
    {
        StandardFormView form = new StandardFormView();

        form.addField( new TimeField( "Time" ) );

        return form.getView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static Component createDateTimeField()
    {
        StandardFormView form = new StandardFormView();

        form.addField( new DateAndTimeField( "Date/Time" ) );

        return form.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return tabs;
    }
}
