package jutils.telemetry.ch10.ui;

import java.awt.Component;

import javax.swing.JComponent;

import jutils.core.ui.ClassedView.IClassedView;
import jutils.core.ui.StandardFormView;
import jutils.core.ui.fields.IntegerFormField;
import jutils.telemetry.ch10.MonthDayTime;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MonthDayTimeView implements IClassedView<MonthDayTime>
{
    /**  */
    public final JComponent view;
    /**  */
    public final IntegerFormField millisField;
    /**  */
    public final IntegerFormField secondsField;
    /**  */
    public final IntegerFormField minutesField;
    /**  */
    public final IntegerFormField hoursField;
    /**  */
    public final IntegerFormField dayOfMonthField;
    /**  */
    public final IntegerFormField monthField;
    /**  */
    public final IntegerFormField yearField;

    /**  */
    private MonthDayTime time;

    /***************************************************************************
     * 
     **************************************************************************/
    public MonthDayTimeView()
    {
        this.millisField = new IntegerFormField( "Milliseconds", 0, 999 );
        this.secondsField = new IntegerFormField( "Seconds", 0, 59 );
        this.minutesField = new IntegerFormField( "Minutes", 0, 59 );
        this.hoursField = new IntegerFormField( "Hours", 0, 23 );
        this.dayOfMonthField = new IntegerFormField( "Day of Month", 0, 30 );
        this.monthField = new IntegerFormField( "Month", 0, 11 );
        this.yearField = new IntegerFormField( "Year", 0, 9999 );

        this.view = this.createView();

        this.setEditable( false );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        StandardFormView form = new StandardFormView();

        form.addField( millisField );
        form.addField( secondsField );
        form.addField( minutesField );
        form.addField( hoursField );
        form.addField( dayOfMonthField );
        form.addField( monthField );
        form.addField( yearField );

        return form.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        millisField.setEditable( editable );
        secondsField.setEditable( editable );
        minutesField.setEditable( editable );
        hoursField.setEditable( editable );
        dayOfMonthField.setEditable( editable );
        monthField.setEditable( editable );
        yearField.setEditable( editable );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public MonthDayTime getData()
    {
        return this.time;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( MonthDayTime data )
    {
        this.time = data;

        millisField.setValue( time.milliseconds );
        secondsField.setValue( time.seconds );
        minutesField.setValue( time.minutes );
        hoursField.setValue( time.hours );
        dayOfMonthField.setValue( time.dayofMonth );
        monthField.setValue( time.month );
        yearField.setValue( time.year );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view;
    }
}
