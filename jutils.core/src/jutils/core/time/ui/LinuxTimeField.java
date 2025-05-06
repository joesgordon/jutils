package jutils.core.time.ui;

import java.time.LocalDateTime;

import javax.swing.JComponent;
import javax.swing.JTextField;

import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.fields.IDataFormField;
import jutils.core.ui.fields.LongFormField;
import jutils.core.ui.times.TimesUnion;
import jutils.core.ui.validation.IValidityChangedListener;
import jutils.core.ui.validation.Validity;

/*******************************************************************************
 * 
 ******************************************************************************/
public class LinuxTimeField implements IDataFormField<Long>
{
    /**  */
    private final LongFormField field;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public LinuxTimeField( String name )
    {
        this.field = new LongFormField( name );

        field.getTextField().setHorizontalAlignment( JTextField.RIGHT );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Long getValue()
    {
        return field.getValue();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( Long value )
    {
        field.setValue( value );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<Long> updater )
    {
        field.setUpdater( updater );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<Long> getUpdater()
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
     * @param dateTime
     **************************************************************************/
    public void setDateTime( LocalDateTime dateTime )
    {
        TimesUnion ts = new TimesUnion( dateTime );
        long secs = ts.getLinuxTime();

        setValue( secs );
    }
}
