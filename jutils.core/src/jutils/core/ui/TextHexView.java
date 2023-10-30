package jutils.core.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;

import jutils.core.IconConstants;
import jutils.core.SwingUtils;
import jutils.core.ui.ABButton.IABCallback;
import jutils.core.ui.event.ActionAdapter;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.event.updater.UpdaterList;
import jutils.core.ui.fields.HexAreaFormField;
import jutils.core.ui.fields.StringAreaFormField;
import jutils.core.ui.hex.HexUtils;
import jutils.core.ui.model.IDataView;

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
    private final ABButton hexTextButton;
    /**  */
    private final ComponentView msgView;
    /**  */
    private final StringAreaFormField textField;
    /**  */
    private final HexAreaFormField hexField;
    /**  */
    private final UpdaterList<byte []> enterListeners;

    /***************************************************************************
     * 
     **************************************************************************/
    public TextHexView()
    {
        this.hexTextButton = createHexTextButton();
        this.msgView = new ComponentView();
        this.textField = new StringAreaFormField( "Message Text", true );
        this.hexField = new HexAreaFormField( "Message Bytes" );
        this.view = new TitleView( "Hexadecimal", createMainView() );
        this.enterListeners = new UpdaterList<>();

        setData( HexUtils.fromHexStringToArray( "EB91" ) );

        handleHex();
        hexTextButton.setState( false );

        SwingUtils.addKeyListener( textField.getTextArea(), "shift ENTER",
            ( e ) -> insertText( LF ), "Shift+Enter Listener", false );
        SwingUtils.addKeyListener( textField.getTextArea(), "control ENTER",
            ( e ) -> insertText( CR ), "Control+Enter Listener", false );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private ABButton createHexTextButton()
    {
        Icon hexIcon = IconConstants.getIcon( IconConstants.HEX_16 );
        Icon textIcon = IconConstants.getIcon( IconConstants.FONT_16 );

        IABCallback hexCallback = () -> handleHex();
        IABCallback textCallback = () -> handleText();

        ABButton button = new ABButton( "Hex", hexIcon, hexCallback, "Text",
            textIcon, textCallback );

        button.setATooltip( "Show message as hex" );
        button.setBTooltip( "Show message as text" );

        return button;
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

        toolbar.add( hexTextButton.button );

        return toolbar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private boolean handleHex()
    {
        byte [] bytes = textField.getValue().getBytes(
            HexAreaFormField.HEXSET );
        hexField.setValue( bytes );
        msgView.setComponent( hexField.getView() );

        return true;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private boolean handleText()
    {
        String text = new String( hexField.getValue(),
            HexAreaFormField.HEXSET );
        textField.setValue( text );
        msgView.setComponent( textField.getView() );

        return true;
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

        enterListeners.fire( msgBuffer );
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
    public void addEnterListener( IUpdater<byte []> l )
    {
        enterListeners.add( l );
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
        hexTextButton.button.setEnabled( editable );
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
