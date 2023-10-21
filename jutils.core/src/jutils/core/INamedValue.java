package jutils.core;

/*******************************************************************************
 * Defines an item that has value and a name.
 ******************************************************************************/
public interface INamedValue extends INamedItem
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName();

    /***************************************************************************
     * Gets the value of this item.
     * @return the value of this item.
     **************************************************************************/
    public int getValue();

    /***************************************************************************
     * Builds a description of this item in the form:
     * {@code "NAME (0xHEX_VALUE)"}.
     * @return the description of this item
     **************************************************************************/
    public default String getDescription()
    {
        return String.format( "%s (0x%04X)", getName(), getValue() );
    }

    /***************************************************************************
     * Returns the {@link INamedValue} from the provided list whose value
     * matches the provided value.
     * @param <T> a type that is a {@link INamedValue}.
     * @param value the value to search for.
     * @param items the list of items to be searched.
     * @param invalid the value to be returned if none are found.
     * @return the item found or the provided invalid value.
     **************************************************************************/
    public static <T extends INamedValue> T fromValue( int value, T [] items,
        T invalid )
    {
        T item = invalid;

        for( T v : items )
        {
            if( v.getValue() == value )
            {
                item = v;
                break;
            }
        }

        return item;
    }
}
