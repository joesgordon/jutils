package org.jutils.ui.model;

import java.awt.Component;

/*******************************************************************************
 * Defines a view that returns the least specific possible object type.
 ******************************************************************************/
public interface IComponentView extends IView<Component>
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getView();
}
