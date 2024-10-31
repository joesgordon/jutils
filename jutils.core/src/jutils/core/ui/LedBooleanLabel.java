package jutils.core.ui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JComponent;

import jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class LedBooleanLabel implements IView<JComponent>
{
    /** The color full green, {@code 0x00FF00}. */
    public static final Color DEFAULT_TRUE_COLOR = Color.GREEN;
    /** The color light gray, {@code 0xC0C0C0} */
    public static final Color DEFAULT_FALSE_COLOR = Color.LIGHT_GRAY;

    /**  */
    public final LedLabel label;

    /**  */
    private final Color trueColor;
    /**  */
    private final String trueText;

    /**  */
    private final Color falseColor;
    /**  */
    private final String falseText;

    /***************************************************************************
     * 
     **************************************************************************/
    public LedBooleanLabel()
    {
        this( DEFAULT_TRUE_COLOR, LedLabel.DEFAULT_TEXT, DEFAULT_FALSE_COLOR,
            LedLabel.DEFAULT_TEXT, LedLabel.DEFAULT_SIZE );
    }

    /***************************************************************************
     * @param trueColor
     * @param trueText
     * @param falseColor
     * @param falseText
     * @param size
     **************************************************************************/
    public LedBooleanLabel( Color trueColor, String trueText, Color falseColor,
        String falseText, int size )
    {
        this.label = new LedLabel( falseColor, falseText, size );
        this.trueColor = trueColor;
        this.trueText = trueText;
        this.falseColor = falseColor;
        this.falseText = falseText;

        if( size > LedLabel.DEFAULT_SIZE )
        {
            float fontSize = size;
            Font f = label.label.getFont();

            f = f.deriveFont( fontSize );
            label.label.setFont( f );
        }
    }

    /***************************************************************************
     * @param value
     **************************************************************************/
    public void setValue( boolean value )
    {
        label.setColor( value ? trueColor : falseColor );
        label.setText( value ? trueText : falseText );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return label.getView();
    }
}
