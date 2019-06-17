package org.jutils.ui.fields;

import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.jutils.io.IParser;
import org.jutils.io.parsers.StringLengthParser;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.validation.*;
import org.jutils.ui.validators.DataTextValidator;
import org.jutils.ui.validators.ITextValidator;

/*******************************************************************************
 * Defines an {@link IFormField} that contains a double validator.
 ******************************************************************************/
public class StringFormField implements IDataFormField<String>
{
    /**  */
    private final String name;
    /**  */
    private final ValidationTextView textField;

    /**  */
    private IUpdater<String> updater;
    /**  */
    private String value;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public StringFormField( String name )
    {
        this( name, 20, 1, null );
    }

    /***************************************************************************
     * @param name
     * @param minLen
     * @param maxLen
     **************************************************************************/
    public StringFormField( String name, Integer minLen, Integer maxLen )
    {
        this( name, 20, minLen, maxLen );
    }

    /***************************************************************************
     * @param name
     * @param columns
     **************************************************************************/
    public StringFormField( String name, int columns, Integer minLen,
        Integer maxLen )
    {
        this.name = name;
        this.textField = new ValidationTextView( null, columns );

        this.updater = null;

        ITextValidator textValidator;
        IParser<String> dataValidator;
        IUpdater<String> updater = new ValueUpdater( this );

        dataValidator = new StringLengthParser( minLen, maxLen );
        textValidator = new DataTextValidator<>( dataValidator, updater );
        textField.getField().setValidator( textValidator );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        return name;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return textField.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getValue()
    {
        return value;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( String value )
    {
        this.value = value;

        String text = value == null ? "" : "" + value;

        IUpdater<String> u = this.updater;
        this.updater = null;
        textField.setText( text );
        this.updater = u;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<String> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IUpdater<String> getUpdater()
    {
        return updater;
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        textField.setEditable( editable );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        textField.getField().addValidityChanged( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        textField.getField().removeValidityChanged( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return textField.getField().getValidity();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public JTextField getTextField()
    {
        return textField.getField().getView();
    }

    /***************************************************************************
     * @param ml
     **************************************************************************/
    public void addMouseListener( MouseListener ml )
    {
        textField.getField().getView().addMouseListener( ml );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ValueUpdater implements IUpdater<String>
    {
        private final StringFormField view;

        public ValueUpdater( StringFormField view )
        {
            this.view = view;
        }

        @Override
        public void update( String data )
        {
            view.value = data;
            if( view.updater != null )
            {
                view.updater.update( data );
            }
        }
    }
}
