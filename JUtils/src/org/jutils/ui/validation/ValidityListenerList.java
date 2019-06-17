package org.jutils.ui.validation;

import java.util.LinkedList;
import java.util.List;

/*******************************************************************************
 * Provides a notification list for {@link IValidityChangedListener}s.
 ******************************************************************************/
public class ValidityListenerList
{
    /** The notification list to be signalled. */
    private final List<IValidityChangedListener> validityChangedListeners;

    /** The last evaluated validity; {@code true} by default. */
    private Validity validity;

    /***************************************************************************
     * Creates a new empty notification list and an invalid validity state.
     **************************************************************************/
    public ValidityListenerList()
    {
        this.validityChangedListeners = new LinkedList<IValidityChangedListener>();

        this.validity = new Validity( "Uninitialized validity" );
    }

    /***************************************************************************
     * Adds the provided listener.
     * @param vcl the listener to be added.
     **************************************************************************/
    public void addListener( IValidityChangedListener vcl )
    {
        validityChangedListeners.add( 0, vcl );
    }

    /***************************************************************************
     * Removes the provided listener
     * @param vcl the listener to be removed.
     **************************************************************************/
    public void removeListener( IValidityChangedListener vcl )
    {
        validityChangedListeners.remove( vcl );
    }

    /***************************************************************************
     * Signals all listeners that the item is valid.
     **************************************************************************/
    public void signalValidity()
    {
        signalValidity( new Validity() );
    }

    /***************************************************************************
     * Signals all listeners that the items is invalid with the provided reason.
     * @param reason the explanation as to why the item is invalid.
     **************************************************************************/
    public void signalValidity( String reason )
    {
        signalValidity( new Validity( reason ) );
    }

    /***************************************************************************
     * Signals all listeners with the provided validity.
     * @param validity defines whether the item is valid or not.
     **************************************************************************/
    public void signalValidity( Validity validity )
    {
        this.validity = validity;

        for( IValidityChangedListener vcl : validityChangedListeners )
        {
            vcl.signalValidity( validity );
        }
    }

    /***************************************************************************
     * Returns the current validity state.
     **************************************************************************/
    public Validity getValidity()
    {
        return validity;
    }
}
