package org.jutils.ui.validation;

import java.awt.Color;
import java.awt.Font;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import org.jutils.ValidationException;
import org.jutils.data.UIProperty;
import org.jutils.ui.validators.ITextValidator;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ValidationTextComponentField<T extends JTextComponent>
    implements IValidationField
{
    /**  */
    private final T field;
    /**  */
    private final ValidityListenerList listenerList;

    // TODO add not-editable background color.

    /**  */
    private final Color nonEditableBackground;
    /**  */
    private Color validBackground;
    /**  */
    private Color invalidBackground;
    /**  */
    private ITextValidator validator;

    /***************************************************************************
     * @param comp
     **************************************************************************/
    public ValidationTextComponentField( T comp )
    {
        this.field = comp;
        this.listenerList = new ValidityListenerList();

        this.nonEditableBackground = UIProperty.TEXTFIELD_INACTIVEBACKGROUND.getColor();
        this.validBackground = field.getBackground();
        this.invalidBackground = Color.red;

        this.validator = null;

        field.getDocument().addDocumentListener(
            new ValidationDocumentListener( this ) );
        field.setBackground( validBackground );

        setComponentValid( listenerList.getValidity().isValid );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public T getView()
    {
        return field;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return listenerList.getValidity();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void validateText()
    {
        if( validator != null )
        {
            Validity v;

            try
            {
                validator.validateText( field.getText() );
                v = new Validity();
            }
            catch( ValidationException ex )
            {
                v = new Validity( ex.getMessage() );
            }

            setComponentValid( v.isValid );

            // LogUtils.printDebug(
            // ">>> Validating text \"%s\", old validity: %s, new validity: %s",
            // field.getText(), listenerList.getValidity(), v );
            // Utils.printStackTrace();

            listenerList.signalValidity( v );
        }
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    public void setEditable( boolean editable )
    {
        field.setBackground( null );
        field.setEditable( editable );

        if( editable )
        {
            setComponentValid( getValidity().isValid );
        }
        else
        {
            field.setBackground( nonEditableBackground );
        }

        // LogUtils.printDebug( "Editable: %b: %s", editable,
        // listenerList.getValidity().toString() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void setComponentValid( boolean valid )
    {
        if( field.isEditable() && field.isEnabled() )
        {
            if( valid )
            {
                field.setBackground( validBackground );
            }
            else
            {
                field.setBackground( invalidBackground );
            }
        }
        else
        {
            field.setBackground( null );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener vcl )
    {
        listenerList.addListener( vcl );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener vcl )
    {
        listenerList.removeListener( vcl );
    }

    /***************************************************************************
     * @param validator
     **************************************************************************/
    public final void setValidator( ITextValidator validator )
    {
        this.validator = validator;

        validateText();
    }

    /***************************************************************************
     * @param name
     **************************************************************************/
    public void setText( String text )
    {
        field.setText( text );

        if( text == null )
        {
            listenerList.signalValidity();
            setComponentValid( true );
        }
        else
        {
            validateText();
        }
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
     * 
     **************************************************************************/
    private static class ValidationDocumentListener implements DocumentListener
    {
        /**  */
        private ValidationTextComponentField<?> field;

        /**
         * @param field
         */
        public ValidationDocumentListener(
            ValidationTextComponentField<?> field )
        {
            this.field = field;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void removeUpdate( DocumentEvent e )
        {
            // LogUtils.printDebug( "Updating text" );
            field.validateText();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void insertUpdate( DocumentEvent e )
        {
            // LogUtils.printDebug( "Inserting text" );
            field.validateText();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void changedUpdate( DocumentEvent e )
        {
            // LogUtils.printDebug( "Changing text" );
            field.validateText();
        }
    }
}
