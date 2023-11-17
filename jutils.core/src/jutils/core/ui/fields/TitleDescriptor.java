package jutils.core.ui.fields;

import jutils.core.Utils;

public class TitleDescriptor<T> implements IDescriptor<T>
{
    @Override
    public String getDescription( T item )
    {
        String str = item == null ? "" : Utils.toTitleCase( item.toString() );
        return str;
    }
}
