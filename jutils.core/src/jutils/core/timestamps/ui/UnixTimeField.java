package jutils.core.timestamps.ui;

import javax.swing.JComponent;
import javax.swing.JTextField;

import jutils.core.timestamps.UnixTime;
import jutils.core.timestamps.UnixTime.UnixTimeDescriptor;
import jutils.core.timestamps.UnixTime.UnixTimeParser;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.fields.IDataFormField;
import jutils.core.ui.fields.ParserFormField;
import jutils.core.ui.validation.IValidityChangedListener;
import jutils.core.ui.validation.Validity;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UnixTimeField implements IDataFormField<UnixTime>
{
    /**  */
    private final ParserFormField<UnixTime> field;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public UnixTimeField( String name )
    {
        JTextField jtf = new JTextField();

        this.field = new ParserFormField<UnixTime>( "Time",
            new UnixTimeParser(), jtf, new UnixTimeDescriptor(), "seconds" );

        jtf.setHorizontalAlignment( JTextField.RIGHT );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public UnixTime getValue()
    {
        return field.getValue();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( UnixTime value )
    {
        field.setValue( value );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<UnixTime> updater )
    {
        field.setUpdater( updater );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<UnixTime> getUpdater()
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
}
