package jutils.core.ui.fields;

import java.awt.Font;
import java.nio.charset.Charset;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import jutils.core.io.IOUtils;
import jutils.core.io.parsers.HexBytesParser;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.hex.HexUtils;
import jutils.core.ui.validation.IValidityChangedListener;
import jutils.core.ui.validation.Validity;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HexAreaFormField implements IDataFormField<byte []>
{
    /**  */
    public static final Charset HEXSET = IOUtils.get8BitEncoding();

    /**  */
    private final JTextArea textField;
    /**  */
    private final ParserFormField<byte []> field;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public HexAreaFormField( String name )
    {
        this.textField = new JTextArea();
        JScrollPane pane = new JScrollPane( textField );
        this.field = new ParserFormField<>( name, new HexBytesParser(),
            textField, ( d ) -> toString( d ), pane, null, true );

        pane.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
        pane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );

        AbstractDocument ad = ( AbstractDocument )textField.getDocument();
        ad.setDocumentFilter( new InputFilter() );

        textField.setLineWrap( true );
        textField.setColumns( 20 );
        textField.setRows( 5 );
        textField.setFont( new Font( "Courier New", Font.PLAIN, 18 ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public byte [] getValue()
    {
        return field.getValue();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( byte [] value )
    {
        field.setValue( value );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<byte []> updater )
    {
        field.setUpdater( updater );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<byte []> getUpdater()
    {
        return field.getUpdater();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        field.setEditable( editable );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return field.getName();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return field.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        field.addValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        field.removeValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return field.getValidity();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public JTextArea getTextArea()
    {
        return textField;
    }

    /***************************************************************************
     * @param text
     **************************************************************************/
    public void setText( String text )
    {
        setValue( text.getBytes( HEXSET ) );
    }

    /***************************************************************************
     * @param value
     * @return
     **************************************************************************/
    private static String toString( byte [] value )
    {
        return value == null ? "" : HexUtils.toHexString( value );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class InputFilter extends DocumentFilter
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public void insertString( FilterBypass fb, int offset, String text,
            AttributeSet attr ) throws BadLocationException
        {
            text = text.replaceAll( "\\s", "" );
            super.insertString( fb, offset, text, attr );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void replace( FilterBypass fb, int offset, int length,
            String text, AttributeSet attrs ) throws BadLocationException
        {
            text = text.replaceAll( "[^a-fA-F0-9]", "" );
            super.replace( fb, offset, length, text, attrs );
        }
    }
}
