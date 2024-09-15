package jutils.iris.colors;

import jutils.core.INamedValue;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum MonoColorModel implements INamedValue
{
    /** Indexing proceeds a row of data at a time. */
    GRAYSCALE( 0, "Grayscale" ),
    /** Indexing proceeds a column of data at a time. */
    BGR( 1, "Blue-Green-Red" ),
    /**  */
    HOT( 2, "Hot" ),
    /**  */
    NIGHT_VISION( 3, "Night Vision" ),
    /**  */
    COOL_WARM( 4, "Cool-Warm" ),;

    /**  */
    public final int value;
    /**  */
    public final String name;

    /***************************************************************************
     * @param value
     * @param name
     **************************************************************************/
    private MonoColorModel( int value, String name )
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
        return this.name;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getValue()
    {
        return this.value;
    }
}
