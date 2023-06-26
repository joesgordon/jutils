package org.jutils.serial;

import org.jutils.core.INamedItem;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum SerialMode implements INamedItem
{
    /**  */
    TEXT( 0, "Text" ),
    /**  */
    RAW( 1, "Raw" );

    /**  */
    public final int value;
    /**  */
    public final String name;

    /***************************************************************************
     * @param value
     * @param name
     **************************************************************************/
    private SerialMode( int value, String name )
    {
        this.value = value;
        this.name = name;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return name;
    }
}
