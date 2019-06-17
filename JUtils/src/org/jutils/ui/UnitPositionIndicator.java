package org.jutils.ui;

import javax.swing.JComponent;

import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.event.updater.UpdaterList;
import org.jutils.ui.fields.IDescriptor;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UnitPositionIndicator implements IView<JComponent>
{
    /**  */
    private final PositionIndicator indicator;
    /**  */
    private final IDescriptor<Long> descriptor;
    /** The list of listers to be notified when the position changes. */
    private final UpdaterList<Long> posititonListeners;

    /**  */
    private long unitLength;
    /**  */
    private long itemPosition;
    /**  */
    private long itemLength;

    /***************************************************************************
     * 
     **************************************************************************/
    public UnitPositionIndicator()
    {
        this( PositionIndicator.createDefaultPositionDescriptor() );
    }

    /***************************************************************************
     * @param descriptor converts a {@link Long} into a {@link String}.
     **************************************************************************/
    public UnitPositionIndicator( IDescriptor<Long> descriptor )
    {
        IDescriptor<Long> unitDescriptor = ( d ) -> describeUnitIndex( d );
        this.indicator = new PositionIndicator( unitDescriptor );
        this.descriptor = descriptor;
        this.posititonListeners = new UpdaterList<>();

        this.unitLength = 10L;
        this.itemPosition = 0L;
        this.itemLength = 1000L;

        indicator.addPositionListener( ( d ) -> handleUnitIndexChanged( d ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return indicator.getView();
    }

    /***************************************************************************
     * Adds the provided listener to be called when the position is updated.
     * @param listener the listener to be added.
     **************************************************************************/
    public void addPositionListener( IUpdater<Long> listener )
    {
        posititonListeners.add( listener );
    }

    /***************************************************************************
     * @param position the position of the bookmark to add.
     **************************************************************************/
    public void addBookmark( long position )
    {
        indicator.addBookmark( position );
    }

    /***************************************************************************
     * Returns the previously set indicated position.
     * @return the position being displayed.
     **************************************************************************/
    public long getPosition()
    {
        return this.itemPosition;
    }

    /***************************************************************************
     * @param position the position to be displayed.
     **************************************************************************/
    public void setPosition( long position )
    {
        this.itemPosition = position;

        long unitIndex = calculateUnitIndex( position );

        indicator.setPosition( unitIndex );
    }

    /***************************************************************************
     * Returns the previously set length of the item containing the indicated
     * position.
     * @return the length of the item.
     **************************************************************************/
    public long getLength()
    {
        return this.itemLength;
    }

    /***************************************************************************
     * Sets the length of the item containing the indicated position.
     * @param length the length of the item.
     **************************************************************************/
    public void setLength( long length )
    {
        this.itemLength = length;

        indicator.setLength( calculateUnitCount() );
    }

    /***************************************************************************
     * Returns the previously set length of the units that break up the length
     * of the item.
     * @return the length of a unit.
     **************************************************************************/
    public long getUnitLength()
    {
        return this.unitLength;
    }

    /***************************************************************************
     * Sets the length of the units that break up the length of the item.
     * @param length the length of a unit.
     * @throws IllegalArgumentException if the provided length is less than 1.
     **************************************************************************/
    public void setUnitLength( long length ) throws IllegalArgumentException
    {
        if( length > 0 )
        {
            this.unitLength = length;
            indicator.setLength( calculateUnitCount() );
        }
        else
        {
            throw new IllegalArgumentException(
                "The unit increment length must be greater than 0" );
        }
    }

    /***************************************************************************
     * @param unitIndex the position of the unit
     **************************************************************************/
    private void handleUnitIndexChanged( long unitIndex )
    {
        this.itemPosition = calculateItemPosition( unitIndex );

        posititonListeners.fire( itemPosition );
    }

    /***************************************************************************
     * @param unitIndex the position of the unit.
     * @return the position within the {@code #itemLength} represented by the
     * provided unit index.
     **************************************************************************/
    private long calculateItemPosition( long unitIndex )
    {
        long position = unitIndex * unitLength;

        // LogUtils.printDebug( );

        return position;
    }

    /***************************************************************************
     * @param position the indicated position of the item.
     * @return the unit index that represents the indicated position.
     **************************************************************************/
    private long calculateUnitIndex( long position )
    {
        long unitCount = calculateUnitCount();

        return Math.round( position / ( this.itemLength - 1.0 ) * unitCount );
    }

    /***************************************************************************
     * @return the number of units needed to represent the entire item length.
     **************************************************************************/
    private long calculateUnitCount()
    {
        return ( itemLength + unitLength - 1 ) / unitLength;
    }

    /***************************************************************************
     * @param unitIndex the position of the unit.
     * @return a string that represents the item position.
     **************************************************************************/
    private String describeUnitIndex( long unitIndex )
    {
        long pos = calculateItemPosition( unitIndex );

        return this.descriptor.getDescription( pos );
    }
}
