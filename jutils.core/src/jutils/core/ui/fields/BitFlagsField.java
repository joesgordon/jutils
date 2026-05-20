package jutils.core.ui.fields;

import javax.swing.JComponent;

import jutils.core.data.INamedBitFlag;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.validation.IValidityChangedListener;
import jutils.core.ui.validation.Validity;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BitFlagsField implements IDataFormField<Long>
{
    /**  */
    private final HexLongFormField field;
    /**  */
    private final INamedBitFlag [] flags;

    /**  */
    private IUpdater<Long> updater;

    // TODO add a button to set the fields individually

    /***************************************************************************
     * @param name
     **************************************************************************/
    public BitFlagsField( String name, INamedBitFlag [] flags )
    {
        this.field = new HexLongFormField( name );
        this.flags = flags;
        this.updater = ( d ) -> {
        };

        field.setUpdater( ( d ) -> handleUpdate( d ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Long getValue()
    {
        return field.getValue();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( Long value )
    {
        field.setValue( value );

        setTooltip( value );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<Long> updater )
    {
        this.updater = updater != null ? updater : ( d ) -> {
        };
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<Long> getUpdater()
    {
        return field.getUpdater();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        field.setEditable( editable );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return field.getName();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return field.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        field.addValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        field.removeValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return field.getValidity();
    }

    /***************************************************************************
     * @param value
     **************************************************************************/
    private void handleUpdate( long value )
    {
        setTooltip( value );
        updater.update( value );
    }

    /***************************************************************************
     * @param value
     **************************************************************************/
    private void setTooltip( long value )
    {
        String tooltip = INamedBitFlag.getStatuses( value, flags );

        field.getTextField().setToolTipText( tooltip );
    }
}
