package jutils.colorific;

import java.awt.Color;

/*******************************************************************************
 * Defines functions for transforming standard colors into values closer related
 * to human perseption.
 ******************************************************************************/
public class ColorUtils
{
    /**  */
    private static final double RGB_MAX = 255.0;

    /***************************************************************************
     * Calculates the luminance for the provided color.
     * @param c
     * @return
     **************************************************************************/
    public static double getLuminance( Color c )
    {
        double r = c.getRed() / RGB_MAX;
        double g = c.getGreen() / RGB_MAX;
        double b = c.getBlue() / RGB_MAX;

        // Apply sRGB gamma correction
        r = ( r <= 0.03928 ) ? r / 12.92
            : Math.pow( ( r + 0.055 ) / 1.055, 2.4 );
        g = ( g <= 0.03928 ) ? g / 12.92
            : Math.pow( ( g + 0.055 ) / 1.055, 2.4 );
        b = ( b <= 0.03928 ) ? b / 12.92
            : Math.pow( ( b + 0.055 ) / 1.055, 2.4 );

        return 0.2126 * r + 0.7152 * g + 0.0722 * b;
    }

    /***************************************************************************
     * @param targetHue
     * @param targetLum
     * @return
     **************************************************************************/
    public static Color matchLuminance( float targetHue, double targetLum )
    {
        float s = 0.8f; // Constant saturation for vibrancy
        float b = 0.5f; // Starting brightness
        Color result = Color.getHSBColor( targetHue, s, b );

        // Simple iterative refinement to match luminance
        for( int i = 0; i < 10; i++ )
        {
            double currentLum = getLuminance( result );
            b = ( float )( b * ( targetLum / currentLum ) );
            b = Math.max( 0, Math.min( 1, b ) ); // Clamp 0-1
            result = Color.getHSBColor( targetHue, s, b );
        }
        return result;
    }

    /***************************************************************************
     * Calculates contrast ratio between two colors (Target 4.5:1 or 7:1).
     * @param c1 color 1.
     * @param c2 color 2.
     * @return the ratio between the two colors provided.
     **************************************************************************/
    public static double getContrastRatio( Color c1, Color c2 )
    {
        double l1 = getLuminance( c1 );
        double l2 = getLuminance( c2 );

        return ( Math.max( l1, l2 ) + 0.05 ) / ( Math.min( l1, l2 ) + 0.05 );
    }
}
