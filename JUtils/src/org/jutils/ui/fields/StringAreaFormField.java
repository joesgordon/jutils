package org.jutils.ui.fields;

import java.awt.Font;
import java.nio.charset.Charset;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.jutils.io.parsers.StringLengthParser;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.validation.*;
import org.jutils.ui.validators.DataTextValidator;
import org.jutils.ui.validators.ITextValidator;

/***************************************************************************
 * 
 **************************************************************************/
public class StringAreaFormField implements IDataFormField<String>
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
    private IUpdater<String> updater;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public StringAreaFormField( String name )
    {
        this.name = name;
        this.inputField = new ValidationTextAreaField();

        JScrollPane pane = new JScrollPane( inputField.getView() );

        pane.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
        pane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );

        this.view = new ValidationView( inputField, null, pane, true );

        this.updater = null;

        ITextValidator validator;

        validator = new DataTextValidator<>( new StringLengthParser( 0, null ),
            ( d ) -> fireUpdaters( d ) );
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
    private void fireUpdaters( String data )
    {
        if( updater != null )
        {
            updater.update( data );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getValue()
    {
        return inputField.getText();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( String text )
    {
        inputField.setText( text );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<String> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<String> getUpdater()
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
        inputField.setText( text );
    }
}
