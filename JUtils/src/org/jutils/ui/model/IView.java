package org.jutils.ui.model;

import java.awt.Component;

/*******************************************************************************
 * Represents a generic method of storing a view.
 * @param <T> the type of {@link Component} stored by this view.
 ******************************************************************************/
public interface IView<T extends Component>
{
    /***************************************************************************
     * Returns control or panel that represents this view. For performance
     * reasons, it is recommended that the view is created previously to calling
     * this function.
     * @return the previously built view.
     **************************************************************************/
    public T getView();
}
