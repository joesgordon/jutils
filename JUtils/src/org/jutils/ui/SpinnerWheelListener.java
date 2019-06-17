package org.jutils.ui;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SpinnerWheelListener implements MouseWheelListener
{
    /**  */
    private final JSpinner spinner;

    /***************************************************************************
     * @param spinner
     **************************************************************************/
    public SpinnerWheelListener( JSpinner spinner )
    {
        this.spinner = spinner;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void mouseWheelMoved( MouseWheelEvent e )
    {
        SpinnerModel model = spinner.getModel();

        if( e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL )
        {
            Object value = null;
            boolean up = e.getUnitsToScroll() < 0;

            if( up )
            {
                value = model.getNextValue();
            }
            else
            {
                value = model.getPreviousValue();
            }

            if( value != null )
            {
                spinner.setValue( value );
            }
        }
    }

    /***************************************************************************
     * @param spinner
     **************************************************************************/
    public static void install( JSpinner spinner )
    {
        spinner.addMouseWheelListener( new SpinnerWheelListener( spinner ) );
    }
}
