package org.jutils.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.regex.Pattern;

import javax.swing.JTextArea;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import org.jutils.Utils;
import org.jutils.io.LogUtils;
import org.jutils.ui.model.IView;

/*******************************************************************************
 *
 ******************************************************************************/
public class SearchableTextArea implements IView<JTextComponent>
{
    /**  */
    private final JTextArea textArea;
    /**  */
    private final FindDialog findDialog;

    /**  */
    private FindOptions lastOptions = null;

    /***************************************************************************
     *
     **************************************************************************/
    public SearchableTextArea()
    {
        this( null, null, 0, 0 );
    }

    /***************************************************************************
     * @param doc Document
     * @param text String
     * @param rows int
     * @param columns int
     **************************************************************************/
    public SearchableTextArea( Document doc, String text, int rows,
        int columns )
    {
        this.textArea = new JTextArea( doc, text, rows, columns );
        this.findDialog = new FindDialog();
        this.findDialog.addFindListener( new TextAreaFindListener( this ) );

        textArea.setToolTipText( "Press CTRL+F to find text" );
        textArea.addKeyListener( new TextAreaKeyListener( this ) );

        findDialog.getView().setTitle( "Find" );
        findDialog.getView().pack();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JTextComponent getView()
    {
        return textArea;
    }

    /***************************************************************************
     * @param text
     **************************************************************************/
    public void setText( String text )
    {
        textArea.setText( text );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String getText()
    {
        return textArea.getText();
    }

    /***************************************************************************
     * @param selectionStart
     * @param selectionEnd
     **************************************************************************/
    public void select( int selectionStart, int selectionEnd )
    {
        textArea.select( selectionStart, selectionEnd );
    }

    /***************************************************************************
     *
     **************************************************************************/
    public void reFind()
    {
        if( lastOptions != null )
        {
            searchAndHighlight( lastOptions );
        }
    }

    /***************************************************************************
     * @param options FindOptions
     * @return boolean
     **************************************************************************/
    public boolean searchAndHighlight( FindOptions options )
    {
        boolean found = false;
        CharSequence seq = getSequence();
        int position = textArea.getCaretPosition();

        if( options.pattern == null )
        {
            buildOptions( options );
        }

        if( options.matcher == null )
        {
            options.matcher = options.pattern.matcher( seq );
        }
        if( options.matcher.find( position ) )
        {
            found = true;
            // LogUtils.printDebug( "m.start: " + m.start( 1 ) );
            // LogUtils.printDebug( "m.end: " + m.end( 1 ) );
        }
        else if( options.wrapAround )
        {
            options.matcher.reset();
            if( options.matcher.find() )
            {
                found = true;
            }
        }

        if( found )
        {
            textArea.select( options.matcher.start(), options.matcher.end() );
        }

        // LogUtils.printDebug( "****************************************" );

        // LogUtils.printDebug( "" );

        lastOptions = options;

        return found;
    }

    /***************************************************************************
     * @param options FindOptions
     * @return boolean
     **************************************************************************/
    private static boolean buildOptions( FindOptions options )
    {
        boolean found = false;
        int flags = Pattern.MULTILINE | Pattern.DOTALL;
        String expression = options.textToFind;

        flags |= options.matchCase ? flags : Pattern.CASE_INSENSITIVE;

        if( !options.useRegex )
        {
            expression = Utils.escapeRegexMetaChar( expression );
        }

        options.pattern = Pattern.compile( expression, flags );

        LogUtils.printDebug( "Building options..." );
        // LogUtils.printDebug( "expression: " + expression );
        // LogUtils.printDebug( "flags: " + Integer.toHexString( flags ) );
        // LogUtils.printDebug( "startPos: " + startPos );
        // LogUtils.printDebug( "endPos: " + content.length() );
        // LogUtils.printDebug( "content: " );
        // LogUtils.printDebug( sequence.toString() );

        return found;
    }

    /***************************************************************************
     * @return ContentSequence
     **************************************************************************/
    public CharSequence getSequence()
    {
        return textArea.getText();
    }

    /***************************************************************************
     *
     **************************************************************************/
    public void showFind()
    {
        String str = textArea.getSelectedText();
        if( str != null && lastOptions != null )
        {
            lastOptions.textToFind = str;
        }

        findDialog.setOptions( lastOptions );
        findDialog.getView().setLocationRelativeTo( null );
        findDialog.getView().setVisible( true );
        findDialog.getView().requestFocus();
    }

    /***************************************************************************
     * @param options FindOptions
     **************************************************************************/
    public void setOptions( FindOptions options )
    {
        this.lastOptions = options;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class TextAreaFindListener implements FindListener
    {
        SearchableTextArea textArea = null;

        public TextAreaFindListener( SearchableTextArea ta )
        {
            textArea = ta;
        }

        @Override
        public void findText( FindOptions findData )
        {
            textArea.searchAndHighlight( findData );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class TextAreaKeyListener implements KeyListener
    {
        SearchableTextArea textArea = null;

        public TextAreaKeyListener( SearchableTextArea ta )
        {
            textArea = ta;
        }

        @Override
        public void keyPressed( KeyEvent e )
        {
            // LogUtils.printDebug( "keyPressed" );
        }

        @Override
        public void keyReleased( KeyEvent e )
        {
            if( e.getKeyCode() == KeyEvent.VK_F3 )
            {
                // LogUtils.printDebug( "F3 Released" );
                textArea.reFind();
            }
        }

        @Override
        public void keyTyped( KeyEvent e )
        {
            char keyTyped = e.getKeyChar();
            int modifiers = e.getModifiersEx();

            if( ( modifiers & KeyEvent.CTRL_DOWN_MASK ) != 0 )
            {
                if( keyTyped == 'f' - 'a' + 1 )
                {
                    // LogUtils.printDebug( "CTRL+F Typed" );
                    textArea.showFind();
                }
            }
        }
    }
}
