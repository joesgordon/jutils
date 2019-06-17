package org.jutils.data;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.*;

import org.jutils.io.StringPrintStream;

/*******************************************************************************
 * Defines a list of attributes of specified runs of styled text and methods of
 * generated them from and applying them to a {@link StyledDocument}.
 ******************************************************************************/
public class TextStyleList
{
    /** List of attributes of specified runs of styled text. */
    public final List<TextStyle> styles;

    /***************************************************************************
     * Creates a new list of styles.
     **************************************************************************/
    public TextStyleList()
    {
        this.styles = new ArrayList<>();
    }

    /***************************************************************************
     * Adds the provided description for the character at the provided location.
     * If the last style added is the same as the current, the
     * {@link TextStyle#count} of the previous will be incremented instead of
     * adding a duplicate style.
     * @param description the attributes of a styled character.
     * @param location the location of the styled character.
     **************************************************************************/
    private void addDescription( FontDescription description, int location )
    {
        if( styles.isEmpty() )
        {
            TextStyle ts = new TextStyle( location, 1, description );
            styles.add( ts );
        }
        else
        {
            TextStyle ts = styles.get( styles.size() - 1 );

            if( ts.description.equals( description ) )
            {
                ts.count++;
            }
            else
            {
                ts = new TextStyle( ts.location + ts.count, 1, description );
                styles.add( ts );
            }
        }
    }

    /***************************************************************************
     * Fills this list with the styles from the provided document.
     * @param doc the document containing styled text from which this list will
     * be populated.
     **************************************************************************/
    public void fromStyledDocument( StyledDocument doc )
    {
        styles.clear();

        for( int i = 0; i < doc.getLength(); i++ )
        {
            AttributeSet set = doc.getCharacterElement( i ).getAttributes();

            FontDescription fd = new FontDescription();

            fd.setAttributes( set );

            addDescription( fd, i );

            // LogUtils.printDebug( "************" );
            // LogUtils.printDebug( fd.toString() );
        }
    }

    /***************************************************************************
     * Applies this list of styles to the provided document.
     * @param doc the document for which the styles will be applied.
     **************************************************************************/
    public void toStyledDocument( StyledDocument doc )
    {
        for( TextStyle style : styles )
        {
            SimpleAttributeSet s = new SimpleAttributeSet();
            style.description.getAttributes( s );
            doc.setCharacterAttributes( style.location, style.count, s, true );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class TextStyle
    {
        /** The index in the text at which to start applying this style. */
        public int location;
        /** The number of characters for which this style is applied. */
        public int count;
        /** The attributes of the style. */
        public FontDescription description;

        /**
         * @param location the index for the style, {@link #location}.
         * @param count the number of characters to apply the style,
         * {@link #count}.
         * @param description the attributes to be set, {@link #description}.
         */
        public TextStyle( int location, int count, FontDescription description )
        {
            this.location = location;
            this.count = count;
            this.description = description;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString()
        {
            try( StringPrintStream str = new StringPrintStream() )
            {
                str.println( "Location: %d", location );
                str.println( "Count: %d", count );
                str.println( description.toString() );

                return str.toString();
            }
        }
    }
}
