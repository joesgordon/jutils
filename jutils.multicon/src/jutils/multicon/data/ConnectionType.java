package jutils.multicon.data;

import jutils.core.INamedValue;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum ConnectionType implements INamedValue
{
    /**  */
    UDP( 0, "UDP" ),
    /**  */
    TCP_LISTEN( 1, "TCP Listen" ),
    /**  */
    TCP_CONNECT( 2, "TCP Connect" ),
    /**  */
    SERIAL( 3, "Serial" ),
    /**  */
    BRIDGE( 4, "Bridge" );

    /**  */
    public final int value;
    /**  */
    public final String name;

    /***************************************************************************
     * @param value
     * @param name
     **************************************************************************/
    private ConnectionType( int value, String name )
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
