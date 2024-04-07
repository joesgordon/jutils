package jutils.iris.colors;

import jutils.core.INamedValue;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum BayerDemosaicAlgorithm implements INamedValue
{
    /**  */
    NEAREST( 0, "Nearest Neighbor" ),
    /**  */
    BILINEAR( 1, "Bilinear" ),
    /**  */
    BICUBIC( 2, "Bicubic" ),
    /**  */
    SPLINE( 3, "Spline" ),
    /**  */
    LANCZOS( 3, "Lanczos" ),;

    /**  */
    public final int value;
    /**  */
    public final String name;

    /***************************************************************************
     * @param value
     * @param name
     **************************************************************************/
    private BayerDemosaicAlgorithm( int value, String name )
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
