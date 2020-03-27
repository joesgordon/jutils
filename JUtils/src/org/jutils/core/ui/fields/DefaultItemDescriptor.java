package org.jutils.core.ui.fields;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class DefaultItemDescriptor<T> implements IDescriptor<T>
{
    /**  */
    private final String nullStr;

    /***************************************************************************
     * 
     **************************************************************************/
    public DefaultItemDescriptor()
    {
        this( false );
    }

    /***************************************************************************
     * @param allowNull
     **************************************************************************/
    public DefaultItemDescriptor( boolean allowNull )
    {
        this.nullStr = allowNull ? null : "";
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getDescription( T item )
    {
        return item == null ? nullStr : item.toString();
    }
}
