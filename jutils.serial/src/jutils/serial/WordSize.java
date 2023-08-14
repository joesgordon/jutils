package jutils.serial;

import jutils.core.INamedItem;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum WordSize implements INamedItem
{
    /**  */
    FOUR( 4, "4 Bits" ),
    /**  */
    FIVE( 5, "5 Bits" ),
    /**  */
    SIX( 6, "6 Bits" ),
    /**  */
    SEVEN( 7, "7 Bits" ),
    /**  */
    EIGHT( 8, "8 Bits" );

    /**  */
    public final int value;
    /**  */
    public final String name;

    /***************************************************************************
     * @param value
     * @param name
     **************************************************************************/
    private WordSize( int value, String name )
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
