package jutils.plot.model;

import jutils.core.INamedItem;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum HorizontalAlignment implements INamedItem
{
    /**  */
    LEFT( "Left" ),
    /**  */
    CENTER( "Center" ),
    /**  */
    RIGHT( "Right" );

    /**  */
    public final String name;

    /***************************************************************************
     * @param name
     **************************************************************************/
    private HorizontalAlignment( String name )
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
