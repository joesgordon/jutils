package jutils.core.time.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;

import javax.swing.JPanel;

import jutils.core.time.NamedMonth;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.fields.ComboFormField;
import jutils.core.ui.fields.DefaultItemDescriptor;
import jutils.core.ui.fields.IDataFormField;
import jutils.core.ui.fields.IntegerFormField;
import jutils.core.ui.validation.IValidityChangedListener;
import jutils.core.ui.validation.Validity;

/*******************************************************************************
 * Shows a year, a month, and a day field next to one another.
 ******************************************************************************/
public class YmdField implements IDataFormField<LocalDate>
{
    /**  */
    private final String name;
    /**  */
    private final IntegerFormField yearField;
    /**  */
    private final ComboFormField<NamedMonth> monthField;
    /**  */
    private final ComboFormField<Integer> dayField;
    /**  */
    private final JPanel view;

    /**  */
    private IUpdater<LocalDate> updater;
    /**  */
    private LocalDate date;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public YmdField( String name )
    {
        this.name = name;
        this.yearField = new IntegerFormField( "Year" );
        this.monthField = new ComboFormField<>( "Month", NamedMonth.values(),
            new DefaultItemDescriptor<>() );
        this.dayField = new ComboFormField<>( "Day", getDays( 0 ) );
        this.view = createView();

        setValue( LocalDate.now() );

        yearField.setUpdater( ( d ) -> handleYearUpdated( d ) );
        monthField.setUpdater( ( d ) -> handleMonthUpdated( d ) );
        dayField.setUpdater( ( d ) -> handleDayUpdated( d ) );

        date = LocalDate.MIN;

        setValue( date );
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
            new Insets( 0, 0, 0, 4 ), 0, 0 );
        panel.add( yearField.getView(), constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 4 ), 0, 0 );
        panel.add( monthField.getView(), constraints );

        constraints = new GridBagConstraints( 2, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( dayField.getView(), constraints );

        return panel;
    }

    /***************************************************************************
     * @param year
     * @param month
     * @return
     **************************************************************************/
    private static Integer [] getDays( int year, NamedMonth month )
    {
        YearMonth ym = YearMonth.of( year, month.value );

        return getDays( ym.lengthOfMonth() );
    }

    /***************************************************************************
     * @param lengthOfMonth
     * @return
     **************************************************************************/
    private static Integer [] getDays( int lengthOfMonth )
    {
        Integer [] days = new Integer[lengthOfMonth];

        for( int i = 0; i < lengthOfMonth; i++ )
        {
            days[i] = i + 1;
        }

        return days;
    }

    /***************************************************************************
     * @param year
     **************************************************************************/
    private void handleYearUpdated( Integer year )
    {
        Month m = date.getMonth();
        int d = date.getDayOfMonth();

        this.date = LocalDate.of( year, m, d );

        fireUpdater();
    }

    /***************************************************************************
     * @param month
     **************************************************************************/
    private void handleMonthUpdated( NamedMonth month )
    {
        Integer day = dayField.getValue();
        Integer [] days = getDays( yearField.getValue(), month );

        dayField.setValues( days );
        dayField.setValue( day );

        this.date = date.withMonth( month.value );

        fireUpdater();
    }

    /***************************************************************************
     * @param day
     **************************************************************************/
    private void handleDayUpdated( Integer day )
    {
        this.date = date.withDayOfMonth( day );

        fireUpdater();
    }

    /***************************************************************************
     * @param date
     **************************************************************************/
    private void fireUpdater()
    {
        if( updater != null )
        {
            updater.update( date );
        }
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
        return name;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        yearField.addValidityChanged( l );
        monthField.addValidityChanged( l );
        dayField.addValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        yearField.removeValidityChanged( l );
        monthField.removeValidityChanged( l );
        dayField.removeValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        Validity yv = yearField.getValidity();
        Validity mv = monthField.getValidity();
        Validity dv = dayField.getValidity();

        if( !yv.isValid )
        {
            return yv;
        }
        else if( !mv.isValid )
        {
            return mv;
        }
        else if( !dv.isValid )
        {
            return dv;
        }

        return yv;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public LocalDate getValue()
    {
        return LocalDate.of( yearField.getValue(), monthField.getValue().month,
            dayField.getValue() );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( LocalDate value )
    {
        yearField.setValue( value.getYear() );
        monthField.setValue( NamedMonth.fromMonth( value.getMonth() ) );
        dayField.setValue( value.getDayOfMonth() );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<LocalDate> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<LocalDate> getUpdater()
    {
        return updater;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        yearField.setEditable( editable );
        monthField.setEditable( editable );
        dayField.setEditable( editable );
    }
}
