package jutils.telemetry.ui.ch10;

import java.awt.Component;

import jutils.core.ui.ClassedView;
import jutils.core.ui.model.IDataView;
import jutils.telemetry.data.ch10.ITime1;
import jutils.telemetry.data.ch10.IrigDayTime;
import jutils.telemetry.data.ch10.MonthDayTime;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Time1View implements IDataView<ITime1>
{
    /**  */
    private final ClassedView<ITime1> view;

    /***************************************************************************
     * 
     **************************************************************************/
    public Time1View()
    {
        this.view = new ClassedView<>();

        view.put( MonthDayTime.class, new MonthDayTimeView(), false );
        view.put( IrigDayTime.class, new IrigDayTimeView(), false );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public ITime1 getData()
    {
        return view.getData();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( ITime1 data )
    {
        view.setData( data );
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    public void setEditable( boolean editable )
    {
        view.setEditable( editable );
    }
}
