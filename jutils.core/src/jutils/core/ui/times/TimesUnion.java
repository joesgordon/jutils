package jutils.core.ui.times;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;

import jutils.core.time.YearNanos;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TimesUnion
{
    /**  */
    private final LocalDateTime LINUX_EPOCH = LocalDateTime.of( 1970,
        Month.JANUARY, 1, 0, 0, 0, 0 );

    /**  */
    public LocalDateTime dateTime;
    /**  */
    public YearNanos yearNanos;

    /***************************************************************************
     * 
     **************************************************************************/
    public TimesUnion()
    {
        this( LocalDateTime.MIN );
    }

    /***************************************************************************
     * @param dateTime
     **************************************************************************/
    public TimesUnion( LocalDateTime dateTime )
    {
        this.dateTime = dateTime;
        this.yearNanos = new YearNanos();

        this.setDateTime( dateTime );
    }

    /***************************************************************************
     * @param time
     **************************************************************************/
    public void setDateTime( LocalDateTime time )
    {
        this.dateTime = time;
        this.yearNanos.setDateTime( dateTime );
    }

    /***************************************************************************
     * @param date
     **************************************************************************/
    public void setDate( LocalDate date )
    {
        this.dateTime = LocalDateTime.of( date, dateTime.toLocalTime() );
        this.yearNanos.setDateTime( dateTime );
    }

    /***************************************************************************
     * @param yn
     **************************************************************************/
    public void setYearNanos( YearNanos yn )
    {
        this.yearNanos = yn;
        this.dateTime = yn.toDateTime();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getDayOfYear()
    {
        return dateTime.getDayOfYear();
    }

    /***************************************************************************
     * @param time
     **************************************************************************/
    public void set( TimesUnion time )
    {
        setDateTime( time.dateTime );
    }

    /***************************************************************************
     * @param seconds
     **************************************************************************/
    public void setLinuxTime( long seconds )
    {
        LocalDateTime time = ChronoUnit.SECONDS.addTo( LINUX_EPOCH, seconds );

        int nanos = this.dateTime.getNano();

        time.plusNanos( nanos );

        setDateTime( time );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public long getLinuxTime()
    {
        long secs = ChronoUnit.SECONDS.between( LINUX_EPOCH, dateTime );

        return secs;
    }
}
