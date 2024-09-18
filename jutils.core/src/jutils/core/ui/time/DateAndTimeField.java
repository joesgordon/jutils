package jutils.core.ui.time;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.swing.JPanel;

import jutils.core.ui.calendar.DateField;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.fields.IDataFormField;
import jutils.core.ui.validation.IValidityChangedListener;
import jutils.core.ui.validation.Validity;

/*******************************************************************************
 * Shows a time field and a date field next to one another.
 ******************************************************************************/
public class DateAndTimeField implements IDataFormField<LocalDateTime>
{
    /**  */
    private final TimeField timeField;
    /**  */
    private final DateField dateField;
    /**  */
    private final JPanel view;

    /**  */
    private IUpdater<LocalDateTime> updater;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public DateAndTimeField( String name )
    {
        this.timeField = new TimeField( name );
        this.dateField = new DateField( "" );
        this.view = createView();

        setValue( LocalDateTime.now() );

        timeField.setUpdater( ( t ) -> fireUpdater( t ) );
        dateField.setUpdater( ( d ) -> fireUpdater( d ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 4, 4, 4, 4 ), 0, 0 );
        panel.add( timeField.getView(), constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 4, 0, 4, 4 ), 0, 0 );
        panel.add( dateField.getView(), constraints );

        return panel;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return timeField.getName();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        timeField.addValidityChanged( l );
        dateField.addValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        timeField.removeValidityChanged( l );
        dateField.removeValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        Validity tv = timeField.getValidity();
        Validity dv = dateField.getValidity();

        if( !tv.isValid )
        {
            return tv;
        }
        else if( !dv.isValid )
        {
            return dv;
        }

        return tv;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public LocalDateTime getValue()
    {
        return LocalDateTime.of( dateField.getValue(), timeField.getValue() );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( LocalDateTime value )
    {
        timeField.setValue( value.toLocalTime() );
        dateField.setValue( value.toLocalDate() );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<LocalDateTime> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<LocalDateTime> getUpdater()
    {
        return updater;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        timeField.setEditable( editable );
        dateField.setEditable( editable );
    }

    /***************************************************************************
     * @param date
     **************************************************************************/
    private void fireUpdater( LocalDate date )
    {
        if( updater != null )
        {
            LocalDateTime dt = LocalDateTime.of( date, timeField.getValue() );

            updater.update( dt );
        }
    }

    /***************************************************************************
     * @param time
     **************************************************************************/
    private void fireUpdater( LocalTime time )
    {
        if( updater != null )
        {
            LocalDateTime dt = LocalDateTime.of( dateField.getValue(), time );

            updater.update( dt );
        }
    }
}
