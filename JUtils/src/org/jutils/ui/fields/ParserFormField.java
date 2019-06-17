package org.jutils.ui.fields;

import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import org.jutils.io.IParser;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.validation.*;
import org.jutils.ui.validators.DataTextValidator;
import org.jutils.ui.validators.ITextValidator;

/*******************************************************************************
 * Defines an {@link IFormField} that allows the user to define an object that
 * has the provided parser.
 * @param <T>
 ******************************************************************************/
public class ParserFormField<T> implements IDataFormField<T>
{
    /**  */
    private final String name;
    /**  */
    private final ValidationTextComponentField<JTextComponent> textField;
    /**  */
    private final ValidationView view;
    /**  */
    private final IDescriptor<T> itemDescriptor;

    /**  */
    private IUpdater<T> updater;
    /**  */
    private T value;

    /***************************************************************************
     * @param name
     * @param parser
     **************************************************************************/
    public ParserFormField( String name, IParser<T> parser )
    {
        this( name, parser, new DefaultItemDescriptor<>() );
    }

    /***************************************************************************
     * @param name
     * @param parser
     * @param itemDescriptor
     **************************************************************************/
    public ParserFormField( String name, IParser<T> parser,
        IDescriptor<T> itemDescriptor )
    {
        this( name, parser, new JTextField( 20 ), itemDescriptor );
    }

    /***************************************************************************
     * @param name
     * @param parser
     * @param comp
     **************************************************************************/
    public ParserFormField( String name, IParser<T> parser,
        JTextComponent comp )
    {
        this( name, parser, comp, new DefaultItemDescriptor<>( true ) );
    }

    /***************************************************************************
     * @param name
     * @param parser
     * @param comp
     * @param itemDescriptor
     **************************************************************************/
    public ParserFormField( String name, IParser<T> parser, JTextComponent comp,
        IDescriptor<T> itemDescriptor )
    {
        this.name = name;
        this.textField = new ValidationTextComponentField<>( comp );
        this.view = new ValidationView( textField );
        this.itemDescriptor = itemDescriptor == null
            ? new DefaultItemDescriptor<T>() : itemDescriptor;

        ITextValidator textValidator;
        IUpdater<T> updater = ( d ) -> update( d );

        textValidator = new DataTextValidator<>( parser, updater );
        textField.setValidator( textValidator );
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
        return view.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public T getValue()
    {
        return value;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( T value )
    {
        this.value = value;

        String text = itemDescriptor.getDescription( value );

        IUpdater<T> updater = this.updater;
        this.updater = null;
        textField.setText( text );
        this.updater = updater;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<T> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IUpdater<T> getUpdater()
    {
        return updater;
    }

    /***************************************************************************
     * 
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
        textField.addValidityChanged( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        textField.removeValidityChanged( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return textField.getValidity();
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    private void update( T data )
    {
        value = data;

        if( updater != null )
        {
            updater.update( data );
        }
    }

    /***************************************************************************
     * @param listener
     **************************************************************************/
    public void addMouseListener( MouseListener listener )
    {
        textField.getView().addMouseListener( listener );
    }
}
