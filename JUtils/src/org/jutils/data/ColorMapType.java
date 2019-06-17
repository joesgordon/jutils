package org.jutils.data;

import org.jutils.INamedItem;

/*******************************************************************************
 * Defines types of color maps.
 ******************************************************************************/
public enum ColorMapType implements INamedItem
{
    /** Signifies the gray scale color map. */
    GRAYSCALE( "Grayscale" ),
    /** Signifies the matlab default color map which is blue to green to red. */
    MATLAB_DEFAULT( "Matlab Default" ),
    /** Signifies the matlab hot color map which is black to red to white. */
    MATLAB_HOT( "Matlab Hot" ),
    /** Signifies a night vision color map which is black to green to white. */
    NIGHT_VISION( "Night Vision" ),
    /**
     * Signifies a color map using an inverted quadratic centered at 1/4, 1/2,
     * and 3/4 the index space for blue, green, and red respectively.
     */
    QUADRATIC( "RGB Quadratic" ),
    /**
     * Signifies a color map that is linearly separated from 0x000000 to
     * 0xFFFFFF.
     */
    LINEAR_STEP( "Linear Step" );

    /** A display name for the color map. */
    public final String name;

    /***************************************************************************
     * Creates a new color map type with the provided name.
     * @param name the display name of the color map.
     **************************************************************************/
    private ColorMapType( String name )
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
