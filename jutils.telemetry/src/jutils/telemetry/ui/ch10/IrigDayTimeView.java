package jutils.telemetry.ui.ch10;

import java.awt.Component;

import javax.swing.JComponent;

import jutils.core.ui.ClassedView.IClassedView;
import jutils.core.ui.StandardFormView;
import jutils.core.ui.fields.IntegerFormField;
import jutils.telemetry.data.ch10.IrigDayTime;

/*******************************************************************************
 * 
 ******************************************************************************/
public class IrigDayTimeView implements IClassedView<IrigDayTime>
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
    public final IntegerFormField daysField;

    /**  */
    private IrigDayTime time;

    /***************************************************************************
     * 
     **************************************************************************/
    public IrigDayTimeView()
    {
        this.millisField = new IntegerFormField( "Milliseconds", 0, 999 );
        this.secondsField = new IntegerFormField( "Seconds", 0, 59 );
        this.minutesField = new IntegerFormField( "Minutes", 0, 59 );
        this.hoursField = new IntegerFormField( "Hours", 0, 23 );
        this.daysField = new IntegerFormField( "Days", 0, 365 );

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
        form.addField( daysField );

        return form.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IrigDayTime getData()
    {
        return this.time;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( IrigDayTime data )
    {
        this.time = data;

        millisField.setValue( time.milliseconds );
        secondsField.setValue( time.seconds );
        minutesField.setValue( time.minutes );
        hoursField.setValue( time.hours );
        daysField.setValue( time.days );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view;
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
        daysField.setEditable( editable );
    }
}
