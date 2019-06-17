package org.jutils.ui.fields;

import javax.swing.JComponent;

import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.validation.*;

/*******************************************************************************
 * Defines a validated form field for editing immutable objects or updating
 * mutable objects.
 ******************************************************************************/
public interface IDataFormField<D> extends IFormField, IValidationField
{
    /***************************************************************************
     * Returns the current edited value.
     **************************************************************************/
    public D getValue();

    /***************************************************************************
     * Sets the value to be edited. Setting the value shall not cause any
     * updaters to be invoked.
     * @param value the item to be edited.
     **************************************************************************/
    public void setValue( D value );

    /***************************************************************************
     * Sets the callback to be invoked when the value has be changed by the
     * user.
     * @param updater the callback to be invoked.
     **************************************************************************/
    public void setUpdater( IUpdater<D> updater );

    /***************************************************************************
     * Returns the updater previously set.
     **************************************************************************/
    public IUpdater<D> getUpdater();

    /***************************************************************************
     * Sets the field as editable according to the provided boolean.
     * @param editable {@code true} if the user can edit the control;
     * {@code false} otherwise.
     **************************************************************************/
    public void setEditable( boolean editable );

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName();

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView();

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l );

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l );

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Validity getValidity();
}
