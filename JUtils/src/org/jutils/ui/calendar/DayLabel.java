package org.jutils.ui.calendar;

import java.awt.Color;
import java.time.LocalDate;

import javax.swing.JLabel;

import org.jutils.data.UIProperty;
import org.jutils.ui.model.IView;

/*******************************************************************************
 *
 ******************************************************************************/
class DayLabel implements IView<JLabel>
{
    /**  */
    private static final Color dayBG = new Color( 0xFF, 0xFF, 0xFF );
    /**  */
    private static final Color dayFG = new Color( 0x00, 0x00, 0x00 );
    /**  */
    private static final Color daySelectedBG = UIProperty.TEXTAREA_SELECTIONBACKGROUND.getColor();
    /**  */
    private static final Color daySelectedFG = new Color( 0xFF, 0xFF, 0xFF );
    /**  */
    private static final Color nonDayFG = Color.gray;

    /**  */
    private final JLabel label;

    /**  */
    private LocalDate date;
    /**  */
    private boolean isSelected = false;
    /**  */
    private boolean isNonDay = false;

    /***************************************************************************
     * 
     **************************************************************************/
    public DayLabel()
    {
        this.label = new JLabel();

        this.date = LocalDate.now();

        label.setForeground( dayFG );
        label.setBackground( dayBG );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JLabel getView()
    {
        return label;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getDay()
    {
        return date.getDayOfMonth();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public LocalDate getDate()
    {
        return date;
    }

    /***************************************************************************
     * @param selected
     **************************************************************************/
    public void setSelected( boolean selected )
    {
        isSelected = selected;

        label.setFocusable( isSelected );
        if( isSelected )
        {
            label.setForeground( daySelectedFG );
            label.setBackground( daySelectedBG );
        }
        else
        {
            if( isNonDay )
            {
                label.setForeground( nonDayFG );
            }
            else
            {
                label.setForeground( dayFG );
            }

            label.setBackground( dayBG );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isSelected()
    {
        return isSelected;
    }

    /***************************************************************************
     * @param nonDay
     **************************************************************************/
    public void setNonDay( boolean nonDay )
    {
        isNonDay = nonDay;

        if( !this.isSelected() )
        {
            if( isNonDay )
            {
                label.setForeground( nonDayFG );
            }
            else
            {
                label.setForeground( dayFG );
            }
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isNonDay()
    {
        return isNonDay;
    }

    /***************************************************************************
     * @param date
     **************************************************************************/
    public void setDate( LocalDate date )
    {
        this.date = date;

        label.setText( "" + date.getDayOfMonth() );
    }
}
