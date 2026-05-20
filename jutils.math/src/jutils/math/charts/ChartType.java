package jutils.math.charts;

import jutils.core.INamedValue;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum ChartType implements INamedValue
{
    /**  */
    UNKNOWN( 0, "Unknown" ),
    /**  */
    PLOT( 1, "Plot" ),
    /**  */
    STRIP( 2, "Strip" ),
    /**  */
    BAR( 3, "Bar" ),
    /**  */
    PIE( 4, "Pie" );

    /**  */
    private int value;
    /**  */
    private String name;

    /***************************************************************************
     * @param value
     * @param name
     **************************************************************************/
    private ChartType( int value, String name )
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
