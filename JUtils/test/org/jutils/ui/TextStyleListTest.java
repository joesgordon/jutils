package org.jutils.ui;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.SimpleAttributeSet;

import org.jutils.data.FontDescription;
import org.jutils.data.TextStyleList;
import org.jutils.data.TextStyleList.TextStyle;
import org.jutils.io.LogUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TextStyleListTest
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        try
        {
            SwingUtilities.invokeAndWait( () -> runTest() );
        }
        catch( InvocationTargetException ex )
        {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
        catch( InterruptedException ex )
        {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static void runTest()
    {
        JTextPane pane = new JTextPane();
        String quick = "quick";
        String message = "The " + quick + " brown fox jumped over the lazy dog";
        int start = message.indexOf( "quick" );
        int cnt = quick.length();

        pane.setText( message );
        pane.setSelectionStart( start );
        pane.setSelectionEnd( start + cnt );

        FontDescription desc = new FontDescription();

        desc.bold = true;

        SimpleAttributeSet attrset = new SimpleAttributeSet();

        desc.getAttributes( attrset );

        pane.setCharacterAttributes( attrset, true );

        message = pane.getText();

        TextStyleList tsl = new TextStyleList();

        tsl.fromStyledDocument( pane.getStyledDocument() );

        // LogUtils.printDebug( "" );
        // LogUtils.printDebug( "" );
        LogUtils.printDebug( "fd cnt %d", tsl.styles.size() );

        for( TextStyle ts : tsl.styles )
        {
            LogUtils.printDebug( ts.toString() );
        }
    }
}
