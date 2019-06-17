package org.jutils.ui.fields;

import java.awt.Font;
import java.nio.charset.Charset;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;

import org.jutils.io.parsers.HexBytesParser;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.hex.HexUtils;
import org.jutils.ui.validation.*;
import org.jutils.ui.validators.DataTextValidator;
import org.jutils.ui.validators.ITextValidator;

/***************************************************************************
 * 
 **************************************************************************/
public class HexAreaFormField implements IDataFormField<byte []>
{
    /**  */
    public static final Charset UTF8 = Charset.forName( "UTF-8" );

    /**  */
    private final String name;
    /**  */
    private final ValidationTextAreaField inputField;
    /**  */
    private final ValidationView view;

    /**  */
    private byte [] bytes;
    /**  */
    private IUpdater<byte []> updater;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public HexAreaFormField( String name )
    {
        this.name = name;
        this.inputField = new ValidationTextAreaField();

        JScrollPane pane = new JScrollPane( inputField.getView() );

        pane.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
        pane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );

        this.view = new ValidationView( inputField, null, pane, true );

        this.bytes = null;
        this.updater = null;

        ITextValidator validator;

        validator = new DataTextValidator<byte []>( new HexBytesParser(),
            ( d ) -> fireUpdaters( d ) );
        AbstractDocument ad = ( AbstractDocument )inputField.getView().getDocument();
        ad.setDocumentFilter( new InputFilter() );
        inputField.setValidator( validator );

        JTextArea field = inputField.getView();

        field.setLineWrap( true );
        field.setColumns( 20 );
        field.setRows( 5 );
        field.setFont( new Font( "Courier New", Font.PLAIN, 18 ) );
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    private void fireUpdaters( byte [] data )
    {
        this.bytes = data;

        if( updater != null )
        {
            updater.update( data );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public byte [] getValue()
    {
        return bytes;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( byte [] bytes )
    {
        this.bytes = bytes;

        inputField.setText( HexUtils.toHexString( bytes ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<byte []> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<byte []> getUpdater()
    {
        return updater;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        view.setEditable( editable );
        inputField.getView().setEditable( editable );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return name;
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
    public void addValidityChanged( IValidityChangedListener l )
    {
        inputField.addValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        inputField.removeValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return inputField.getValidity();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public JTextArea getTextArea()
    {
        return inputField.getView();
    }

    /***************************************************************************
     * @param text
     **************************************************************************/
    public void setText( String text )
    {
        setValue( text.getBytes( UTF8 ) );
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
