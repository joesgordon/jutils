package org.jutils.ui.validation;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextField;

import org.jutils.ui.validators.ITextValidator;

/*******************************************************************************
 * A validation field that validates user input according to an
 * {@link ITextValidator}.
 ******************************************************************************/
public final class ValidationTextField implements IValidationField
{
    /**  */
    private final JTextField textField;
    /**  */
    private final ValidationTextComponentField<JTextField> field;

    /***************************************************************************
     * 
     **************************************************************************/
    public ValidationTextField()
    {
        this( "" );
    }

    /***************************************************************************
     * @param columns
     **************************************************************************/
    public ValidationTextField( int columns )
    {
        this( "", columns );
    }

    /***************************************************************************
     * @param str
     **************************************************************************/
    public ValidationTextField( String str )
    {
        this( str, 20 );
    }

    /***************************************************************************
     * @param str
     * @param columns
     **************************************************************************/
    private ValidationTextField( String str, int columns )
    {
        this.textField = new JTextField();
        this.field = new ValidationTextComponentField<>( textField );

        textField.setText( str );
        setColumns( columns );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JTextField getView()
    {
        return textField;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return field.getValidity();
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    public void setEditable( boolean editable )
    {
        field.setEditable( editable );
    }

    /***************************************************************************
     * @param vcl
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener vcl )
    {
        field.addValidityChanged( vcl );
    }

    /***************************************************************************
     * @param vcl
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener vcl )
    {
        field.removeValidityChanged( vcl );
    }

    /***************************************************************************
     * @param validator
     **************************************************************************/
    public final void setValidator( ITextValidator validator )
    {
        field.setValidator( validator );
    }

    /***************************************************************************
     * @param name
     **************************************************************************/
    public void setText( String text )
    {
        field.setText( text );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String getText()
    {
        return field.getText();
    }

    /***************************************************************************
     * @param font
     **************************************************************************/
    public void setFont( Font font )
    {
        field.setFont( font );
    }

    /***************************************************************************
     * @param columns
     **************************************************************************/
    public void setColumns( int columns )
    {
        textField.setColumns( columns );
    }

    /***************************************************************************
     * @param bg
     **************************************************************************/
    public void setValidBackground( Color bg )
    {
        field.setValidBackground( bg );
    }
}
