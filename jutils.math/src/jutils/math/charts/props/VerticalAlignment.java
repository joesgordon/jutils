package jutils.math.charts.props;

import jutils.core.INamedItem;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum VerticalAlignment implements INamedItem
{
    /**  */
    TOP( "Left" ),
    /**  */
    MIDDLE( "Center" ),
    /**  */
    BOTTOM( "Right" );

    /**  */
    public final String name;

    /***************************************************************************
     * @param name
     **************************************************************************/
    private VerticalAlignment( String name )
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
