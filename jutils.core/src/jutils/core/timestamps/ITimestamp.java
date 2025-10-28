package jutils.core.timestamps;

import java.time.LocalDateTime;

import jutils.core.io.FieldPrinter;
import jutils.core.io.FieldPrinter.ITierPrinter;
import jutils.core.time.TimeUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public interface ITimestamp extends ITierPrinter
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer );

    /***************************************************************************
     * Sets this instant to now (system time in UTC).
     **************************************************************************/
    public default void setNow()
    {
        fromDateTime( TimeUtils.getUtcNow() );
    }

    /***************************************************************************
     * Converts this instant to a date/time.
     * @return the date/time of this instant.
     **************************************************************************/
    public LocalDateTime toDateTime();

    /***************************************************************************
     * Sets this instant to the provided date/time.
     * @param time the date/time to set this instant to.
     **************************************************************************/
    public void fromDateTime( LocalDateTime time );
}
