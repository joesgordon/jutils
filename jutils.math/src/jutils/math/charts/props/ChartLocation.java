package jutils.math.charts.props;

import jutils.core.INamedItem;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum ChartLocation implements INamedItem
{
    /**  */
    TOP( "Top" ),
    /**  */
    LEFT( "Left" ),
    /**  */
    BOTTOM( "Bottom" ),
    /**  */
    RIGHT( "Right" ),
    /**  */
    FLOATING( "Floating" );

    /**  */
    public final String name;

    /***************************************************************************
     * @param name
     **************************************************************************/
    private ChartLocation( String name )
    {
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
