package jutils.math.rand;

import jutils.core.INamedValue;

/*******************************************************************************
 * Distribution type enumerations.
 ******************************************************************************/
public enum DistributionType implements INamedValue
{
    /**  */
    UNIFORM( 0, "Uniform" ),
    /**  */
    NORMAL( 1, "Normal" ),
    /**  */
    LOG_NORMAL( 2, "Log-Normal" ),
    /**  */
    TANGENT_LOG_NORMAL( 3, "Tagent Log-Normal" ),
    /**  */
    RAYLEIGH( 4, "Rayleigh" ),;

    /**  */
    public final int value;
    /**  */
    public final String name;

    /***************************************************************************
     * @param value
     * @param name
     **************************************************************************/
    private DistributionType( int value, String name )
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
