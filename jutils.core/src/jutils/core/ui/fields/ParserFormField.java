package jutils.core.ui.fields;

import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import jutils.core.io.IParser;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.validation.IValidityChangedListener;
import jutils.core.ui.validation.ValidationTextComponentField;
import jutils.core.ui.validation.ValidationView;
import jutils.core.ui.validation.Validity;
import jutils.core.ui.validators.DataTextValidator;
import jutils.core.ui.validators.ITextValidator;

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
    /**  */
    private boolean isSetting;

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
     * @param itemDescriptor
     * @param units
     **************************************************************************/
    public ParserFormField( String name, IParser<T> parser,
        IDescriptor<T> itemDescriptor, String units )
    {
        this( name, parser, new JTextField( 20 ), itemDescriptor, units );
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
        this( name, parser, comp, itemDescriptor, comp );
    }

    /***************************************************************************
     * @param name
     * @param parser
     * @param comp
     * @param itemDescriptor
     * @param units
     **************************************************************************/
    public ParserFormField( String name, IParser<T> parser, JTextComponent comp,
        IDescriptor<T> itemDescriptor, String units )
    {
        this( name, parser, comp, itemDescriptor, comp, units );
    }

    /***************************************************************************
     * @param name
     * @param parser
     * @param comp
     * @param itemDescriptor
     * @param fieldView
     **************************************************************************/
    public ParserFormField( String name, IParser<T> parser, JTextComponent comp,
        IDescriptor<T> itemDescriptor, JComponent fieldView )
    {
        this( name, parser, comp, itemDescriptor, fieldView, null );
    }

    /***************************************************************************
     * @param name
     * @param parser
     * @param comp
     * @param itemDescriptor
     * @param fieldView
     * @param units
     **************************************************************************/
    public ParserFormField( String name, IParser<T> parser, JTextComponent comp,
        IDescriptor<T> itemDescriptor, JComponent fieldView, String units )
    {
        this( name, parser, comp, itemDescriptor, fieldView, units, false );
    }

    /***************************************************************************
     * @param name
     * @param parser
     * @param comp
     * @param itemDescriptor
     * @param fieldView
     * @param units
     * @param fillBoth
     **************************************************************************/
    public ParserFormField( String name, IParser<T> parser, JTextComponent comp,
        IDescriptor<T> itemDescriptor, JComponent fieldView, String units,
        boolean fillBoth )
    {
        this.name = name;
        this.textField = new ValidationTextComponentField<>( comp );
        this.view = new ValidationView( textField, units, fieldView, fillBoth );
        this.itemDescriptor = itemDescriptor == null
            ? new DefaultItemDescriptor<T>()
            : itemDescriptor;

        this.updater = null;
        this.value = null;
        this.isSetting = false;

        ITextValidator textValidator;
        IUpdater<T> updater = ( d ) -> handleDataUpdated( d );

        textValidator = new DataTextValidator<>( parser, updater );
        textField.setValidator( textValidator );
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
    public T getValue()
    {
        return value;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( T value )
    {
        this.value = value;

        String text = itemDescriptor.getDescription( value );

        this.isSetting = true;
        textField.setText( text );
        this.isSetting = false;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<T> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<T> getUpdater()
    {
        return updater;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        // LogUtils.printDebug( "ParserFormField: " + name + " field is " +
        // ( editable ? "" : "not " ) + "editable" );
        textField.setEditable( editable );
        view.setEditable( editable );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        textField.addValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        textField.removeValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return textField.getValidity();
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    private void handleDataUpdated( T data )
    {
        value = data;

        if( updater != null && !isSetting )
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

    /***************************************************************************
     * @return
     **************************************************************************/
    public JTextComponent getTextField()
    {
        return textField.getView();
    }
}
