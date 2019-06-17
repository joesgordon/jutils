package org.jutils.ui.fields;

/*******************************************************************************
 * Defines a callback method for marshalling data to a string representation.
 * @param <T> the type of data to be marshalled.
 ******************************************************************************/
public interface IDescriptor<T>
{
    /***************************************************************************
     * Returns the description of the provided item.
     * @param item the item to be described.
     * @return the item's description.
     **************************************************************************/
    public String getDescription( T item );
}
