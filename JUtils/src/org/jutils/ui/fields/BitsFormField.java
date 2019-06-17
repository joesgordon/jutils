package org.jutils.ui.fields;

import javax.swing.JComponent;

import org.jutils.io.parsers.BinaryParser;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.validation.*;
import org.jutils.ui.validators.DataTextValidator;
import org.jutils.ui.validators.ITextValidator;
import org.jutils.utils.BitArray;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BitsFormField implements IDataFormField<BitArray>
{
    /**  */
    private final String name;
    /**  */
    private final ValidationTextView textField;

    /**  */
    public BitArray value;
    /**  */
    private IUpdater<BitArray> updater;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public BitsFormField( String name )
    {
        this.name = name;
        this.textField = new ValidationTextView( null, 20 );
        this.value = null;
        this.updater = null;

        ITextValidator textValidator;

        textValidator = new DataTextValidator<>( new BinaryParser(),
            new ValueUpdater( this ) );
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
    public BitArray getValue()
    {
        return value;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( BitArray value )
    {
        this.value = value;

        String text = value == null ? "" : value.toString();

        textField.setText( text );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<BitArray> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IUpdater<BitArray> getUpdater()
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
     * 
     **************************************************************************/
    private static class ValueUpdater implements IUpdater<BitArray>
    {
        private final BitsFormField view;

        public ValueUpdater( BitsFormField view )
        {
            this.view = view;
        }

        @Override
        public void update( BitArray data )
        {
            view.value = data;

            if( view.updater != null )
            {
                view.updater.update( data );
            }
        }
    }
}
