package jutils.core.ui.fields;

import java.awt.Component;

import javax.swing.JComponent;

import jutils.core.INamedItem;
import jutils.core.ui.model.IView;

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
