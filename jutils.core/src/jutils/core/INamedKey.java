package jutils.core;

/*******************************************************************************
 * Defines an key that has a name.
 ******************************************************************************/
public interface INamedKey extends INamedItem
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName();

    /***************************************************************************
     * Returns the key of this item.
     * @return the key of this item.
     **************************************************************************/
    public String getKey();

    /***************************************************************************
     * Builds a description of this item in the form:
     * {@code "NAME (0xHEX_VALUE)"}.
     * @return the description of this item
     **************************************************************************/
    public default String getDescription()
    {
        return String.format( "%s (%s)", getName(), getKey() );
    }

    /***************************************************************************
     * Returns the {@link INamedValue} from the provided list whose value
     * matches the provided value.
     * @param <T> a type that is a {@link INamedValue}.
     * @param key the key to search for.
     * @param items the list of items to be searched.
     * @param invalid the value to be returned if none are found.
     * @return the item found or the provided invalid value.
     **************************************************************************/
    public static <T extends INamedKey> T fromValue( String key, T [] items,
        T invalid )
    {
        T item = invalid;

        for( T v : items )
        {
            if( v.getKey().equals( key ) )
            {
                item = v;
                break;
            }
        }

        return item;
    }
}
