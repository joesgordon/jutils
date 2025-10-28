package jutils.kairosion.ui;

import java.time.LocalDateTime;

import javax.swing.JComponent;

import jutils.core.timestamps.YearNanos;
import jutils.core.timestamps.ui.DateAndTimeField;
import jutils.core.timestamps.ui.DateTimeField;
import jutils.core.timestamps.ui.UnixTimeField;
import jutils.core.timestamps.ui.YearNanosField;
import jutils.core.timestamps.ui.YmdField;
import jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class KairosionView implements IView<JComponent>
{
    /**  */
    private final TimestampsView timesView;

    /**  */
    private final YmdField yearMonthDayField;
    /** A field that displays a date field and a time field. */
    private final DateAndTimeField dateAndTimeField;
    /** A date/time field. */
    private final DateTimeField dateTimeField;
    /** A year/nanoseconds into the year field. */
    private final YearNanosField yearNanosField;
    /**  */
    private final UnixTimeField linuxField;

    // TODO Add Microsoft Filetime

    // TODO Add GPS Week

    // TODO Add GPS time

    // TODO Add Day of Week

    // TODO Add Week of Year

    // TODO Add Year/Day of Year/Seconds into Day

    // TODO Add Julian time

    // TODO Add Sidereal time

    /***************************************************************************
     * 
     **************************************************************************/
    public KairosionView()
    {
        this.timesView = new TimestampsView();

        this.yearMonthDayField = new YmdField( "Year/Month/Day" );
        this.dateAndTimeField = new DateAndTimeField( "Date & Time" );
        this.dateTimeField = new DateTimeField( "Date/Time" );
        this.yearNanosField = new YearNanosField( "Year/Seconds" );
        this.linuxField = new UnixTimeField( "Linux Seconds" );

        timesView.addField( new TimestampField<>( yearMonthDayField,
            ( ldt ) -> ldt.toLocalDate(), ( d ) -> LocalDateTime.of( d,
                timesView.getData().toLocalTime() ) ) );
        timesView.addField(
            new TimestampField<>( dateAndTimeField, ( d ) -> d, ( d ) -> d ) );
        timesView.addField(
            new TimestampField<>( dateTimeField, ( d ) -> d, ( d ) -> d ) );
        timesView.addField( new TimestampField<YearNanos>( yearNanosField,
            ( ldt ) -> new YearNanos( ldt ), ( d ) -> d.toDateTime() ) );
        // fields.add( new TimesField<>( linuxField, time,
        // ( t ) -> t.getLinuxSeconds(), ( t, d ) -> t.setLinuxSeconds( d ) ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return timesView.getView();
    }
}
