package org.jutils.ui;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

import javax.swing.JTextPane;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.*;

import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ScrollableEditorPaneView implements IView<JTextPane>
{
    private final JTextPane pane;

    /***************************************************************************
     * 
     **************************************************************************/
    public ScrollableEditorPaneView()
    {
        this.pane = new ScrollableTextPane();
    }

    /***************************************************************************
     * @param text
     * @param attributes
     **************************************************************************/
    public void appendText( String text, AttributeSet attributes )
    {
        try
        {
            Document doc = pane.getDocument();

            doc.insertString( doc.getLength(), text, attributes );
        }
        catch( BadLocationException ex )
        {
            throw new RuntimeException( ex );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JTextPane getView()
    {
        return pane;
    }

    public String getText()
    {
        return pane.getText();
    }

    public void setContentType( String type )
    {
        pane.setContentType( type );
    }

    public void setPage( URL page ) throws IOException
    {
        pane.setPage( page );
    }

    public void setText( String text )
    {
        pane.setText( text );
    }

    public void setFont( Font font )
    {
        pane.setFont( font );
    }

    public void setEditable( boolean editable )
    {
        pane.setEditable( editable );
    }

    public void setCaretPosition( int i )
    {
        pane.setCaretPosition( i );
    }

    public Style addStyle( String styleName, Style parent )
    {
        return pane.addStyle( styleName, parent );
    }

    public void setBackground( Color bg )
    {
        pane.setBackground( bg );
    }

    public void setDocument( Document doc )
    {
        pane.setDocument( doc );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class ScrollableTextPane extends JTextPane
    {
        /**  */
        private static final long serialVersionUID = 4654921174781677788L;

        @Override
        public boolean getScrollableTracksViewportWidth()
        {
            Component parent = getParent();
            ComponentUI ui = getUI();

            return parent != null ? ( ui.getPreferredSize(
                this ).width <= parent.getSize().width ) : true;
        }

        @Override
        public void setBounds( int x, int y, int width, int height )
        {
            Dimension size = this.getPreferredSize();
            super.setBounds( x, y, Math.max( size.width, width ), height );
        }
    }
}
