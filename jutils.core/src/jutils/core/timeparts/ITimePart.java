package jutils.core.timeparts;

import java.time.LocalDateTime;

import jutils.core.io.FieldPrinter;
import jutils.core.io.FieldPrinter.ITierPrinter;

/*******************************************************************************
 * 
 ******************************************************************************/
public interface ITimePart extends ITierPrinter
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer );

    /***************************************************************************
     * Updates the provided time with the this section of time.
     * @param time the date/time to be updated with this section.
     * @return the date/time of this instant.
     **************************************************************************/
    public LocalDateTime updateDateTime( LocalDateTime time );

    /***************************************************************************
     * Sets this section of time to that in the provided date/time.
     * @param time the date/time to set this instant to.
     **************************************************************************/
    public void fromDateTime( LocalDateTime time );
}
