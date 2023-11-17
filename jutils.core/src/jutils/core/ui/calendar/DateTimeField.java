package jutils.core.ui.calendar;

import java.awt.*;
import java.time.*;

import javax.swing.JPanel;

import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.fields.IDataFormField;
import jutils.core.ui.validation.IValidityChangedListener;
import jutils.core.ui.validation.Validity;

/*******************************************************************************
 * Shows a time field and a date field next to one another.
 ******************************************************************************/
public class DateTimeField implements IDataFormField<LocalDateTime>
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
    public DateTimeField( String name )
    {
        this.timeField = new TimeField( name );
        this.dateField = new DateField( "" );
        this.view = createView();

        setValue( LocalDateTime.now() );

        timeField.setUpdater( ( t ) -> fireUpdater( t ) );
        dateField.setUpdater( ( d ) -> fireUpdater( d ) );
    }

    /***************************************************************************
     * 
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
     * 
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    @Override
    public String getName()
    {
        return timeField.getName();
    }

    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        timeField.addValidityChanged( l );
        dateField.addValidityChanged( l );
    }

    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        timeField.removeValidityChanged( l );
        dateField.removeValidityChanged( l );
    }

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

    @Override
    public LocalDateTime getValue()
    {
        return LocalDateTime.of( dateField.getValue(), timeField.getValue() );
    }

    @Override
    public void setValue( LocalDateTime value )
    {
        timeField.setValue( value.toLocalTime() );
        dateField.setValue( value.toLocalDate() );
    }

    @Override
    public void setUpdater( IUpdater<LocalDateTime> updater )
    {
        this.updater = updater;
    }

    @Override
    public IUpdater<LocalDateTime> getUpdater()
    {
        return updater;
    }

    @Override
    public void setEditable( boolean editable )
    {
        timeField.setEditable( editable );
        dateField.setEditable( editable );
    }

    private void fireUpdater( LocalDate date )
    {
        if( updater != null )
        {
            LocalDateTime dt = LocalDateTime.of( date, timeField.getValue() );

            updater.update( dt );
        }
    }

    private void fireUpdater( LocalTime time )
    {
        if( updater != null )
        {
            LocalDateTime dt = LocalDateTime.of( dateField.getValue(), time );

            updater.update( dt );
        }
    }
}
