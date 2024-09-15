package jutils.core.ui.fields;

import jutils.core.INamedItem;

/*******************************************************************************
 * Defines an {@link IDescriptor} for {@link INamedItem}s.
 ******************************************************************************/
public class NamedItemDescriptor<T extends INamedItem> implements IDescriptor<T>
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getDescription( T item )
    {
        return item == null ? "" : item.getName();
    }

    /***************************************************************************
     * @param items
     * @return
     **************************************************************************/
    public String [] getDescriptions( T [] items )
    {
        String [] strs = new String[items.length];

        int i = 0;
        for( T item : items )
        {
            strs[i++] = item == null ? "" : item.getName();
        }

        return strs;
    }
}
