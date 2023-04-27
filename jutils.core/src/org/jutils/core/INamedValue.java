package org.jutils.core;

/*******************************************************************************
 * Defines an item that has a name.
 ******************************************************************************/
public interface INamedValue extends INamedItem
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName();

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getValue();

    /***************************************************************************
     * @return
     **************************************************************************/
    public default String getDescription()
    {
        return String.format( "%s (0x%04X)", getName(), getValue() );
    }

    /***************************************************************************
     * @param <T>
     * @param value
     * @param values
     * @param invalid
     * @return
     **************************************************************************/
    public static <T extends INamedValue> T fromValue( int value, T [] values,
        T invalid )
    {
        T item = invalid;

        for( T v : values )
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
