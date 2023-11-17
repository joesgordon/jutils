package jutils.platform.data;

import jutils.core.INamedValue;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum RtsControl implements INamedValue
{
    /**  */
    DISABLE( 0, "Disable" ),
    /**  */
    ENABLE( 1, "Enable" ),
    /**  */
    HANDSHAKE( 2, "Handshake" ),
    /**  */
    TOGGLE( 3, "Toggle" );

    /**  */
    public final int value;
    /**  */
    public final String name;

    /***************************************************************************
     * @param value
     * @param name
     **************************************************************************/
    private RtsControl( int value, String name )
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

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getValue()
    {
        return value;
    }
}
