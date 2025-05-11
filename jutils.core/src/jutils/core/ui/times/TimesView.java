package jutils.core.ui.times;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import jutils.core.IconConstants;
import jutils.core.SwingUtils;
import jutils.core.time.TimeUtils;
import jutils.core.time.ui.DateAndTimeField;
import jutils.core.time.ui.DateTimeField;
import jutils.core.time.ui.LinuxTimeField;
import jutils.core.time.ui.YearNanosField;
import jutils.core.time.ui.YmdField;
import jutils.core.ui.StandardFormView;
import jutils.core.ui.event.ActionAdapter;
import jutils.core.ui.model.IDataView;

/*******************************************************************************
 * Defines a view that allows a user to define times in a variety of ways.
 ******************************************************************************/
public class TimesView implements IDataView<TimesUnion>
{
    /** The view that contains all the fields in this view. */
    private final JPanel view;

    /**  */
    private final YmdField yearMonthDayField;
    /** A field that displays a date field and a time field. */
    private final DateAndTimeField dateAndTimeField;
    /** A date/time field. */
    private final DateTimeField dateTimeField;
    /** A year/nanoseconds into the year field. */
    private final YearNanosField yearNanosField;
    /**  */
    private final LinuxTimeField linuxField;

    // TODO Add Microsoft Filetime

    // TODO Add GPS Week

    // TODO Add GPS time

    // TODO Add Day of Week

    // TODO Add Week of Year

    // TODO Add Year/Day of Year/Seconds into Day

    // TODO Add Julian time

    // TODO Add Sidereal time

    /**  */
    private final List<TimesField<?>> fields;

    /**  */
    private TimesUnion time;

    /***************************************************************************
     * Creates a new times view.
     **************************************************************************/
    public TimesView()
    {
        this.yearMonthDayField = new YmdField( "Year/Month/Day" );
        this.dateAndTimeField = new DateAndTimeField( "Date & Time" );
        this.dateTimeField = new DateTimeField( "Date/Time" );
        this.yearNanosField = new YearNanosField( "Year/Seconds" );
        this.linuxField = new LinuxTimeField( "Linux Seconds" );

        this.fields = new ArrayList<>();

        this.time = new TimesUnion();

        fields.add( new TimesField<>( yearMonthDayField, time,
            ( d ) -> d.dateTime.toLocalDate(), ( t, d ) -> t.setDate( d ) ) );
        fields.add( new TimesField<>( dateAndTimeField, time,
            ( t ) -> t.dateTime, ( t, d ) -> t.setDateTime( d ) ) );
        fields.add( new TimesField<>( dateTimeField, time, ( t ) -> t.dateTime,
            ( t, d ) -> t.setDateTime( d ) ) );
        fields.add( new TimesField<>( yearNanosField, time,
            ( t ) -> t.yearNanos, ( t, d ) -> t.setYearNanos( d ) ) );
        fields.add( new TimesField<>( linuxField, time,
            ( t ) -> t.getLinuxTime(), ( t, d ) -> t.setLinuxTime( d ) ) );

        this.view = createView();

        setData( time.dateTime );

        for( TimesField<?> f : fields )
        {
            f.setUpdater( ( d ) -> handleFieldUpdated( f, d ) );
        }
    }

    /***************************************************************************
     * Creates the main panel for this view.
     * @return main panel.
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( createToolbar(), BorderLayout.NORTH );
        panel.add( createForm(), BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * Creates the toolbar for this view.
     * @return the toolbar for this view.
     **************************************************************************/
    private Component createToolbar()
    {
        JToolBar toolbar = new JToolBar();

        SwingUtils.setToolbarDefaults( toolbar );

        SwingUtils.addActionToToolbar( toolbar, createNowAction() );

        return toolbar;
    }

    /***************************************************************************
     * Creates the action for setting fields to now.
     * @return the action for setting fields to now.
     **************************************************************************/
    private Action createNowAction()
    {
        Icon icon = IconConstants.getIcon( IconConstants.TODAY_16 );
        ActionListener listener = ( e ) -> setData( TimeUtils.utcNow() );
        return new ActionAdapter( listener, "Now", icon );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createForm()
    {
        StandardFormView form = new StandardFormView();

        for( TimesField<?> f : fields )
        {
            form.addField( f );
        }

        return form.getView();
    }

    /***************************************************************************
     * @param field
     * @param times
     **************************************************************************/
    private void handleFieldUpdated( TimesField<?> field, TimesUnion times )
    {
        for( TimesField<?> f : fields )
        {
            if( f != field )
            {
                f.setValue( times );
            }
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public TimesUnion getData()
    {
        return time;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( TimesUnion data )
    {
        this.time = data;

        for( TimesField<?> f : fields )
        {
            f.setValue( time );
        }
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    public void setData( LocalDateTime data )
    {
        this.time.setDateTime( data );

        setData( time );
    }
}
