package jutils.kairosion.ui;

import javax.swing.JComponent;

import jutils.core.time.TimeUtils;
import jutils.core.timestamps.ui.DateAndTimeField;
import jutils.core.timestamps.ui.DateTimeField;
import jutils.core.timestamps.ui.UnixTimeField;
import jutils.core.timestamps.ui.YearNanosField;
import jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class KairosionView implements IView<JComponent>
{
    /**  */
    private final TimestampsView timesView;

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

        this.dateAndTimeField = new DateAndTimeField( "Date & Time" );
        this.dateTimeField = new DateTimeField( "Date/Time" );
        this.yearNanosField = new YearNanosField( "Year/Seconds" );
        this.linuxField = new UnixTimeField( "Unix Time" );

        timesView.addField( new LocalDateTimeField( dateAndTimeField ) );
        timesView.addField( new LocalDateTimeField( dateTimeField ) );
        timesView.addField( new TimestampField<>( yearNanosField ) );
        timesView.addField( new TimestampField<>( linuxField ) );

        timesView.setData( TimeUtils.getUtcNow() );
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
