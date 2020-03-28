package org.jutils.core.ui.fields;

import java.awt.Component;

import javax.swing.JComponent;

import org.jutils.core.INamedItem;
import org.jutils.core.ui.model.IView;

/*******************************************************************************
 * Defines a named view.
 ******************************************************************************/
public interface IFormField extends INamedItem, IView<Component>
{
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
}
