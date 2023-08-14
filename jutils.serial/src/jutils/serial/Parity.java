package jutils.serial;

import jutils.core.INamedItem;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum Parity implements INamedItem
{
    /**  */
    NONE( 0, "None" ),
    /**  */
    ODD( 1, "Odd" ),
    /**  */
    EVEN( 2, "Even" ),
    /**  */
    MARK( 3, "Mark" ),
    /**  */
    SPACE( 4, "Space" );

    /**  */
    public final int value;
    /**  */
    public final String name;

    /***************************************************************************
     * @param value
     * @param name
     **************************************************************************/
    private Parity( int value, String name )
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
