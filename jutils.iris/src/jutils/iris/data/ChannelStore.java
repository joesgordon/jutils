package jutils.iris.data;

import jutils.core.INamedValue;

/***************************************************************************
 * 
 **************************************************************************/
public enum ChannelStore implements INamedValue
{
    /**  */
    INTERLEAVED( 0, "Interleaved" ),
    /**  */
    PLANAR( 1, "Planar" ),
    /**  */
    SEMI_PLANAR( 2, "Semi-Planar" ),
    /**  */
    BAYER( 3, "Bayer" );

    /**  */
    public final int value;
    /**  */
    public final String name;

    /***************************************************************************
     * @param value
     * @param name
     **************************************************************************/
    private ChannelStore( int value, String name )
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
