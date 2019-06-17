package org.jutils.ui.model;

import java.awt.Component;

/*******************************************************************************
 * Defines a view that allows for display and optionally editing of data.
 * @param <D> the type of the data to be displayed/edited.
 ******************************************************************************/
public interface IDataView<D> extends IView<Component>
{
    /***************************************************************************
     * Returns the data as seen in the view. This function may optionally build
     * this data when called, or return the original data instance (meaning the
     * data was updated without creating a new instance).
     * @return the data as seen in the view.
     **************************************************************************/
    public D getData();

    /***************************************************************************
     * Sets the components in this view to the fields within the provided data.
     * @param data the data to be displayed.
     **************************************************************************/
    public void setData( D data );
}
