package org.jutils.chart;

import java.awt.Color;
import java.util.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RandomPalette implements IPalette
{
    /**  */
    private final CyclingPalette cycle;

    /***************************************************************************
     * 
     **************************************************************************/
    public RandomPalette()
    {
        List<Color> colors = new ArrayList<>();

        Color c;
        Random r = new Random();
        for( int i = 0; i < 20; i++ )
        {
            c = new Color( r.nextInt( 255 ), r.nextInt( 255 ),
                r.nextInt( 255 ) );
            colors.add( c );
        }

        this.cycle = new CyclingPalette( colors );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Color next()
    {
        return cycle.next();
    }

    @Override
    public void reset()
    {
        cycle.reset();
    }
}
