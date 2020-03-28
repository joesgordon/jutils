package org.jutils.core.ui.validation;

import java.awt.Component;

import javax.swing.JComponent;

import org.jutils.core.ui.model.IComponentView;
import org.jutils.core.ui.model.IView;

/*******************************************************************************
 * Defines an {@link IComponentView} that supports validation.
 ******************************************************************************/
public interface IValidationField extends IView<Component>
{
    /***************************************************************************
     * Adds a listener that will be called when the user has entered data that
     * changed the validity of this field.
     * @param l the listener to be added.
     **************************************************************************/
    public void addValidityChanged( IValidityChangedListener l );

    /***************************************************************************
     * Removes the provided listener. Does nothing if the listener is not found.
     * @param l the listener to be removed.
     **************************************************************************/
    public void removeValidityChanged( IValidityChangedListener l );

    /***************************************************************************
     * Returns the current validity state for the field
     * @return the reason for invalidation.
     **************************************************************************/
    public Validity getValidity();

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView();
}
