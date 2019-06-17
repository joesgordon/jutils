package org.jutils.ui.model;

import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CyclingSpinnerListModel implements SpinnerModel
{
    /**  */
    private final SpinnerListModel baseModel;
    /**  */
    private final Object firstValue, lastValue;

    /**  */
    private SpinnerModel linkedModel = null;

    /***************************************************************************
     * @param values
     **************************************************************************/
    public CyclingSpinnerListModel( Object [] values )
    {
        this.baseModel = new SpinnerListModel( values );
        this.firstValue = values[0];
        this.lastValue = values[values.length - 1];
    }

    /***************************************************************************
     * @param linkedModel
     **************************************************************************/
    public void setLinkedModel( SpinnerModel linkedModel )
    {
        this.linkedModel = linkedModel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Object getNextValue()
    {
        Object value = baseModel.getNextValue();
        if( value == null )
        {
            value = firstValue;
            if( linkedModel != null )
            {
                linkedModel.setValue( linkedModel.getNextValue() );
            }
        }
        return value;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Object getPreviousValue()
    {
        Object value = baseModel.getPreviousValue();
        if( value == null )
        {
            value = lastValue;
            if( linkedModel != null )
            {
                linkedModel.setValue( linkedModel.getPreviousValue() );
            }
        }
        return value;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Object getValue()
    {
        return baseModel.getValue();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( Object value )
    {
        baseModel.setValue( value );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addChangeListener( ChangeListener l )
    {
        baseModel.addChangeListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeChangeListener( ChangeListener l )
    {
        baseModel.removeChangeListener( l );
    }
}
