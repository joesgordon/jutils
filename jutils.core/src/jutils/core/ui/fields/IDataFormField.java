package jutils.core.ui.fields;

import javax.swing.JComponent;

import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.validation.IValidationField;
import jutils.core.ui.validation.IValidityChangedListener;
import jutils.core.ui.validation.Validity;

/*******************************************************************************
 * Defines a validated form field for editing immutable objects or updating
 * mutable objects.
 * @param <D> the type of the value to be edited.
 ******************************************************************************/
public interface IDataFormField<D> extends IFormField, IValidationField
{
    /***************************************************************************
     * Returns the current edited value.
     * @return the value of the field as of the last valid edit.
     **************************************************************************/
    public D getValue();

    /***************************************************************************
     * Sets the value to be edited. Setting the value shall not cause any
     * {@link IUpdater}s to be invoked.
     * @param value the item to be edited.
     **************************************************************************/
    public void setValue( D value );

    /***************************************************************************
     * Sets the callback to be invoked when the value has been changed by user
     * interaction.
     * @param updater the callback to be invoked.
     **************************************************************************/
    public void setUpdater( IUpdater<D> updater );

    /***************************************************************************
     * Returns the callback invoked when the value has been changed by user
     * interaction.
     * @return the updater previously set.
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
