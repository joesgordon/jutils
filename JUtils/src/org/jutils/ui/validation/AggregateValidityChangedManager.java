package org.jutils.ui.validation;

import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * This class will monitor the changes in validity to many
 * {@link IValidationField}s and will notify the provided listener only when the
 * aggregate changes validity. It is assumed valid upon construction and will be
 * reevaluated immediately upon each addition.
 ******************************************************************************/
public class AggregateValidityChangedManager
{
    /** The list of fields to be monitored. */
    private final List<IValidationField> fields;
    /** The single listener to be added to each field. */
    private final AggregateValidityChangedListener listener;
    /**
     * The list of listeners to be invoked when the aggregate validity changes.
     */
    private final ValidityListenerList validityListeners;
    /**  */
    private boolean enabled;

    /***************************************************************************
     * Creates a new manager and uses the provided listener when the aggregate
     * validity changes.
     * @param changedListener called when the aggregate validity changes.
     **************************************************************************/
    public AggregateValidityChangedManager()
    {
        this.fields = new ArrayList<IValidationField>();
        this.listener = new AggregateValidityChangedListener( this );
        this.validityListeners = new ValidityListenerList();
        this.enabled = true;
    }

    /***************************************************************************
     * @see IValidationField#addValidityChanged(IValidityChangedListener)
     **************************************************************************/
    public void addValidityChanged( IValidityChangedListener l )
    {
        validityListeners.addListener( l );

        l.signalValidity( validityListeners.getValidity() );
    }

    /***************************************************************************
     * @see IValidationField#removeValidityChanged(IValidityChangedListener)
     **************************************************************************/
    public void removeValidityChanged( IValidityChangedListener l )
    {
        validityListeners.removeListener( l );
    }

    /***************************************************************************
     * @see IValidationField#getValidity()
     **************************************************************************/
    public Validity getValidity()
    {
        return validityListeners.getValidity();
    }

    /***************************************************************************
     * Adds a field to the aggregate.
     * @param field the field to be added.
     **************************************************************************/
    public void addField( IValidationField field )
    {
        field.addValidityChanged( listener );

        fields.add( field );

        validityListeners.signalValidity( field.getValidity() );
    }

    /***************************************************************************
     * Test the validity of the fields and calls the client supplied listener if
     * the validity changes. We walk though the list testing all of them not for
     * the case when an edit causes a field to become invalid, but rather the
     * case when an edit causes a field to become valid. In this latter case, we
     * cannot assume the last know validity only reflects the field in question.
     * Another may still be invalid.
     **************************************************************************/
    private void testValidity()
    {
        boolean newValidity = true;

        if( !enabled )
        {
            return;
        }

        for( IValidationField field : fields )
        {
            // LogUtils.printDebug( "Field " + field.getView().getName() +
            // ", validity: " + field.isValid() );
            newValidity &= field.getValidity().isValid;
            if( !newValidity )
            {
                validityListeners.signalValidity( field.getValidity() );
                return;
            }
        }

        validityListeners.signalValidity( new Validity() );
    }

    /***************************************************************************
     * @param enabled
     **************************************************************************/
    public void setEnabled( boolean enabled )
    {
        this.enabled = enabled;

        testValidity();
    }

    /***************************************************************************
     * The aggregate listener to monitor every field for a change in validity.
     * @see AggregateValidityChangedManager#testValidity()
     **************************************************************************/
    private static class AggregateValidityChangedListener
        implements IValidityChangedListener
    {
        private final AggregateValidityChangedManager manager;

        public AggregateValidityChangedListener(
            AggregateValidityChangedManager manager )
        {
            this.manager = manager;
        }

        @Override
        public void signalValidity( Validity validity )
        {
            manager.testValidity();
        }
    }
}
