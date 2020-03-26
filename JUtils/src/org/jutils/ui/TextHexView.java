package org.jutils.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.fields.HexAreaFormField;
import org.jutils.ui.fields.StringAreaFormField;
import org.jutils.ui.hex.HexUtils;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TextHexView implements IDataView<byte []>
{
    /**  */
    private final String CR = "" + ( char )0xD;
    /**  */
    private final String LF = "" + ( char )0xA;

    /**  */
    private final TitleView view;
    /**  */
    private final ComponentView msgView;
    /**  */
    private final StringAreaFormField textField;
    /**  */
    private final HexAreaFormField hexField;
    /**  */
    private final HexTextListener hextTextListener;
    /**  */
    private final ItemActionList<byte []> enterListeners;

    /***************************************************************************
     * 
     **************************************************************************/
    public TextHexView()
    {
        this.textField = new StringAreaFormField( "Message Text" );
        this.hexField = new HexAreaFormField( "Message Bytes" );
        this.msgView = new ComponentView();
        this.hextTextListener = new HexTextListener( this );
        this.view = new TitleView( "Hexadecimal", createMainView() );
        this.enterListeners = new ItemActionList<>();

        hextTextListener.showHex();

        setData( HexUtils.fromHexStringToArray( "EB91" ) );

        SwingUtils.addKeyListener( textField.getTextArea(), "shift ENTER",
            ( e ) -> insertText( LF ), "Shift+Enter Listener", false );
        SwingUtils.addKeyListener( textField.getTextArea(), "control ENTER",
            ( e ) -> insertText( CR ), "Control+Enter Listener", false );
    }

    /***************************************************************************
     * Inserts the provided text at the current cursor position in the
     * {@link #textField}.
     * @param text the text to be inserted.
     **************************************************************************/
    private void insertText( String text )
    {
        JTextArea area = textField.getTextArea();
        int offset = area.getCaretPosition();
        try
        {

            area.getDocument().insertString( offset, text, null );
        }
        catch( BadLocationException ex )
        {
            throw new RuntimeException( ex );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createMainView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( createToolbar(), BorderLayout.NORTH );
        panel.add( msgView.getView(), BorderLayout.CENTER );

        addEnterHook( textField.getTextArea() );
        addEnterHook( hexField.getTextArea() );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createToolbar()
    {
        JToolBar toolbar = new JToolBar();

        SwingUtils.setToolbarDefaults( toolbar );

        SwingUtils.addActionToToolbar( toolbar, createHexTextAction(),
            hextTextListener.button );

        return toolbar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createHexTextAction()
    {
        ActionListener listener = ( e ) -> hextTextListener.toggle();

        return new ActionAdapter( listener, "HexText", null );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class HexTextListener
    {
        /**  */
        private final TextHexView view;
        /**  */
        public final JButton button;

        /**  */
        private final Icon textIcon;
        /**  */
        private final String textText;
        /**  */
        private final String textTooltip;

        /**  */
        private final Icon hexIcon;
        /**  */
        private final String hexText;
        /**  */
        private final String hexTooltip;

        /**
         * @param view
         */
        public HexTextListener( TextHexView view )
        {
            this.view = view;
            this.button = new JButton();

            this.textIcon = IconConstants.getIcon( IconConstants.FONT_16 );
            this.hexIcon = IconConstants.getIcon( IconConstants.HEX_16 );

            this.textText = "Text";
            this.hexText = "Hex";

            this.textTooltip = "Show message as text";
            this.hexTooltip = "Show message as hex";
        }

        /**
         * 
         */
        public void toggle()
        {
            boolean showHex = button.getIcon() == hexIcon;

            if( showHex )
            {
                showHex();
            }
            else
            {
                showText();
            }
        }

        /**
         * 
         */
        private void showHex()
        {
            button.setIcon( textIcon );
            button.setText( textText );
            button.setToolTipText( textTooltip );
            byte [] bytes = view.textField.getValue().getBytes(
                HexAreaFormField.HEXSET );
            view.hexField.setValue( bytes );
            view.msgView.setComponent( view.hexField.getView() );
        }

        /**
         * 
         */
        private void showText()
        {
            button.setIcon( hexIcon );
            button.setText( hexText );
            button.setToolTipText( hexTooltip );
            String text = new String( view.hexField.getValue(),
                HexAreaFormField.HEXSET );
            view.textField.setValue( text );
            view.msgView.setComponent( view.textField.getView() );
        }
    }

    /***************************************************************************
     * @param textPane
     **************************************************************************/
    private void addEnterHook( JTextArea textPane )
    {
        KeyStroke ks;
        String aname;
        ActionListener l;
        Action action;
        ActionMap amap;
        InputMap imap;

        ks = KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0 );
        aname = "SEND_MESSAGE";
        l = ( e ) -> fireEnterListeners();
        action = new ActionAdapter( l, aname, null );
        amap = textPane.getActionMap();
        imap = textPane.getInputMap();
        imap.put( ks, aname );
        amap.put( aname, action );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void fireEnterListeners()
    {
        byte [] msgBuffer = getData();

        enterListeners.fireListeners( this, msgBuffer );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public byte [] getData()
    {
        if( msgView.getComponent() == hexField.getView() )
        {
            return hexField.getValue();
        }

        return textField.getValue().getBytes( HexAreaFormField.HEXSET );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( byte [] text )
    {
        textField.setValue( new String( text, HexAreaFormField.HEXSET ) );
        hexField.setValue( text );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addEnterListener( ItemActionListener<byte []> l )
    {
        enterListeners.addListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void selectAll()
    {
        hexField.getTextArea().selectAll();
        textField.getTextArea().selectAll();
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    public void setEditable( boolean editable )
    {
        hextTextListener.button.setEnabled( editable );
        textField.setEditable( editable );
        hexField.setEditable( editable );
    }

    /***************************************************************************
     * @param text
     **************************************************************************/
    public void setText( String text )
    {
        textField.setValue( text );
        hexField.setText( text );
    }
}
