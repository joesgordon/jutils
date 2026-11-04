package jutils.core.laf;

import java.awt.Color;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SimpleColors
{
    /**  */
    public Color background;
    /**  */
    public Color controlBackground;
    /**  */
    public Color controlHighlight;
    /**  */
    public Color foreground;
    /**  */
    public Color control;
    /**  */
    public Color shadow;
    /**  */
    public Color highlight;
    /**  */
    public Color bright;

    /***************************************************************************
     * 
     **************************************************************************/
    public SimpleColors()
    {
        this.background = new Color( 0x484848 ); // 0x444444
        this.controlBackground = new Color( 0x2F2F2F );
        this.controlHighlight = new Color( 0x46A5DC );
        this.foreground = new Color( 0xF0F0F0 );
        this.control = new Color( 0x204060 );
        this.shadow = new Color( 0x111A1F );
        this.highlight = new Color( 0x7A7A7A );
        this.bright = new Color( 0x0579C8 );
    }
}
