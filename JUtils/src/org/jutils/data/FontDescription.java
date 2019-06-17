package org.jutils.data;

import java.awt.Color;
import java.util.Objects;

import javax.swing.text.*;

import org.jutils.Utils;
import org.jutils.io.StringPrintStream;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FontDescription
{
    /**  */
    public String name;
    /**  */
    public int size;
    /**  */
    public boolean bold;
    /**  */
    public boolean italic;
    /**  */
    public boolean underline;
    /**  */
    public boolean strikeThrough;
    /**  */
    public boolean subscript;
    /**  */
    public boolean superscript;
    /**  */
    public Color color;

    /***************************************************************************
     * 
     **************************************************************************/
    public FontDescription()
    {
        this.name = "Arial";
        this.size = 12;
        this.bold = false;
        this.italic = false;
        this.underline = false;
        this.strikeThrough = false;
        this.subscript = false;
        this.superscript = false;
        this.color = Color.black;
    }

    /***************************************************************************
     * Sets the provided attributes according to this font description.
     * @param attributes the attributes to set this description to.
     **************************************************************************/
    public void getAttributes( MutableAttributeSet attributes )
    {
        StyleConstants.setFontFamily( attributes, name );
        StyleConstants.setFontSize( attributes, size );
        StyleConstants.setBold( attributes, bold );
        StyleConstants.setItalic( attributes, italic );
        StyleConstants.setUnderline( attributes, underline );
        StyleConstants.setStrikeThrough( attributes, strikeThrough );
        StyleConstants.setSubscript( attributes, subscript );
        StyleConstants.setSuperscript( attributes, superscript );
        StyleConstants.setForeground( attributes, color );
    }

    /***************************************************************************
     * Sets this font description according to the provided attributes.
     * @param attributes the attributes to be set using this description.
     **************************************************************************/
    public void setAttributes( AttributeSet attributes )
    {
        name = StyleConstants.getFontFamily( attributes );
        size = StyleConstants.getFontSize( attributes );
        bold = StyleConstants.isBold( attributes );
        italic = StyleConstants.isItalic( attributes );
        underline = StyleConstants.isUnderline( attributes );
        strikeThrough = StyleConstants.isStrikeThrough( attributes );
        subscript = StyleConstants.isSubscript( attributes );
        superscript = StyleConstants.isSuperscript( attributes );
        color = StyleConstants.getForeground( attributes );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean equals( Object obj )
    {
        FontDescription fd = Utils.isAssignable( this, obj );
        if( fd != null )
        {
            if( name.equals( fd.name ) && size == fd.size && bold == fd.bold &&
                italic == fd.italic && underline == fd.underline &&
                strikeThrough == fd.strikeThrough &&
                subscript == fd.subscript && superscript == fd.superscript &&
                color.equals( fd.color ) )
            {
                return true;
            }
        }

        return false;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int hashCode()
    {
        return Objects.hash( name, size, bold, italic, underline, strikeThrough,
            subscript, color );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String toString()
    {
        try( StringPrintStream str = new StringPrintStream() )
        {
            str.println( "Name: %s", name );
            str.println( "Size: %d", size );
            str.println( "Bold: %b", bold );
            str.println( "Italic: %b", italic );
            str.println( "Underline: %b", underline );
            str.println( "Strikethrough: %b", strikeThrough );
            str.println( "Subscript: %b", subscript );
            str.println( "Superscript: %b", superscript );
            str.println( "Color: %s", color );

            return str.toString();
        }
    }
}
