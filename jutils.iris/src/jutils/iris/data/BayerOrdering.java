package jutils.iris.data;

import jutils.core.INamedValue;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum BayerOrdering implements INamedValue
{
    /**  */
    GRBG( 0, "GRBG" ),
    /**  */
    GBRG( 1, "GBRG" ),
    /**  */
    RGGB( 2, "RGGB" ),
    /**  */
    BGGR( 3, "BGGR" ),;

    /**  */
    public final int value;
    /**  */
    public final String name;

    /***************************************************************************
     * @param value
     * @param name
     **************************************************************************/
    private BayerOrdering( int value, String name )
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
