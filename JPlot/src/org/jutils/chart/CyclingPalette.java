package org.jutils.chart;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CyclingPalette implements IPalette
{
    /**  */
    private final List<Color> colors;
    /**  */
    private int idx = 0;

    /***************************************************************************
     * @param colors
     **************************************************************************/
    public CyclingPalette( List<Color> colors )
    {
        this.colors = new ArrayList<>( colors );
        this.idx = 0;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Color next()
    {
        if( idx == colors.size() )
        {
            idx = 0;
        }

        return colors.get( idx++ );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void reset()
    {
        idx = 0;
    }
}
