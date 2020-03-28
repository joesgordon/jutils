package org.jutils.chart;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PresetPalette implements IPalette
{
    /**  */
    private final CyclingPalette cycle;

    /***************************************************************************
     * 
     **************************************************************************/
    public PresetPalette()
    {
        List<Color> colors = new ArrayList<>();

        colors.add( Color.black );
        colors.add( new Color( 0x41B6C4 ) );
        colors.add( new Color( 0x2C7FB8 ) );
        colors.add( new Color( 0xFD8D3C ) );
        colors.add( new Color( 0xF03B20 ) );
        colors.add( new Color( 0x31A354 ) );
        colors.add( new Color( 0xF768A1 ) );
        colors.add( new Color( 0xD7B5D8 ) );
        colors.add( new Color( 0x78C679 ) );
        colors.add( new Color( 0xFECC5C ) );
        colors.add( new Color( 0xC51B8A ) );
        colors.add( new Color( 0x006837 ) );
        colors.add( new Color( 0x7A0177 ) );
        colors.add( new Color( 0xBD0026 ) );

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

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void reset()
    {
        cycle.reset();
    }
}
