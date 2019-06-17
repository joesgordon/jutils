package org.jutils.ui.calendar;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jutils.io.LogUtils;
import org.jutils.ui.SpinnerWheelListener;
import org.jutils.ui.event.updater.DataUpdaterList;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.model.CyclingSpinnerListModel;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * Displays a 5-week calendar page of the current month. If the month has only
 * four U-S weeks, the last week shown will be in the following month.
 ******************************************************************************/
public class DateView implements IDataView<LocalDate>
{
    /** Month strings to be used in the months' spinner box. */
    private static final String [] MONTHS = new String[] { "January",
        "February", "March", "April", "May", "June", "July", "August",
        "September", "October", "November", "December" };
    /**
     * Week day single letter abbreviations to be used as the header in the
     * calendar.
     */
    private static final String [] WEEK_DAYS = new String[] { "S", "M", "T",
        "W", "R", "F", "S" };
    /** The background of the header labels. */
    private static final Color HEADER_BACKGROUND = new Color( 0x8D, 0x8D,
        0x8D );
    /** The foreground of the header labels. */
    private static final Color HEADER_FOREGROUND = new Color( 0xFF, 0xFF,
        0xFF );

    /**  */
    private final JPanel view;
    /**  */
    private final JSpinner monthSpinner;
    /**  */
    private final JSpinner yearSpinner;
    /**  */
    private final JLabel [] weekdayLabels;
    /**  */
    private final DayLabel [] dayLabels;
    /**  */
    private final DataUpdaterList<LocalDate> dateChangedListeners;

    /**  */
    private DayLabel currentSelection = null;

    /***************************************************************************
     *
     **************************************************************************/
    public DateView()
    {
        this.monthSpinner = new JSpinner(
            new CyclingSpinnerListModel( MONTHS ) );
        this.yearSpinner = new JSpinner( new SpinnerNumberModel() );
        this.weekdayLabels = new JLabel[WEEK_DAYS.length];
        this.dayLabels = new DayLabel[42];

        this.dateChangedListeners = new DataUpdaterList<>();

        SpinnerWheelListener.install( monthSpinner );
        SpinnerWheelListener.install( yearSpinner );

        for( int i = 0; i < weekdayLabels.length; i++ )
        {
            weekdayLabels[i] = new JLabel( WEEK_DAYS[i] );
            initHeaderLabel( weekdayLabels[i] );
        }

        for( int i = 0; i < dayLabels.length; i++ )
        {
            dayLabels[i] = new DayLabel();
            initDayLabel( dayLabels[i] );
        }

        this.view = createView();

        setDate( LocalDate.now() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public LocalDate getData()
    {
        return getDate();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( LocalDate data )
    {
        setDate( data );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void setUpdater( IUpdater<LocalDate> l )
    {
        dateChangedListeners.addListener( l );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        // ---------------------------------------------------------------------
        //
        // ---------------------------------------------------------------------
        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 2, 0 ), 0, 0 );
        panel.add( createControlsPanel(), constraints );

        // ---------------------------------------------------------------------
        //
        // ---------------------------------------------------------------------
        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 2, 0, 0, 0 ), 0, 0 );
        panel.add( createMonthPanel(), constraints );

        panel.setMinimumSize( panel.getPreferredSize() );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createControlsPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );

        JSpinner.NumberEditor editor = new JSpinner.NumberEditor( yearSpinner,
            "#" );
        yearSpinner.setEditor( editor );
        yearSpinner.addChangeListener( new YearChangeListener( this ) );

        monthSpinner.addChangeListener( new MonthChangeListener( this ) );
        CyclingSpinnerListModel monthModel = ( CyclingSpinnerListModel )monthSpinner.getModel();
        monthModel.setLinkedModel( yearSpinner.getModel() );

        panel.add( monthSpinner,
            new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 2 ), 0, 0 ) );
        panel.add( yearSpinner,
            new GridBagConstraints( 1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 2, 0, 0 ), 15, 0 ) );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createMonthPanel()
    {
        JPanel monthPanel = new JPanel( new GridBagLayout() );

        monthPanel.setBorder( BorderFactory.createLoweredBevelBorder() );

        monthPanel.setBackground( Color.white );

        for( int i = 0; i < weekdayLabels.length; i++ )
        {
            monthPanel.add( weekdayLabels[i], new GridBagConstraints( i, 0, 1,
                1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 5, i == 0 ? 5 : 0, 0, i == 6 ? 5 : 0 ), 0, 0 ) );
        }

        for( int i = 0; i < dayLabels.length; i++ )
        {
            int row = 1 + ( i / 7 );
            int col = i % 7;

            int top = 0;
            int lft = col == 0 ? 5 : 0;
            int btm = row == 6 ? 5 : 0;
            int rht = col == 6 ? 5 : 0;

            monthPanel.add( dayLabels[i].getView(),
                new GridBagConstraints( col, row, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE,
                    new Insets( top, lft, btm, rht ), 0, 0 ) );
        }

        return monthPanel;
    }

    /***************************************************************************
     * @param label JLabel
     **************************************************************************/
    private static void initHeaderLabel( JLabel label )
    {
        label.setForeground( HEADER_FOREGROUND );
        label.setBackground( HEADER_BACKGROUND );
        label.setHorizontalAlignment( JLabel.CENTER );
        // label.setBorder( BorderFactory.createLineBorder( Color.red ) );

        initLabel( label );
    }

    /***************************************************************************
     * @param labelView JLabel
     **************************************************************************/
    private void initDayLabel( DayLabel labelView )
    {
        JLabel label = labelView.getView();
        label.setHorizontalAlignment( JLabel.CENTER );
        label.addMouseListener( new DayLabelMouseListener( this, labelView ) );
        label.addKeyListener( new DayLabelKeyListener( this ) );

        initLabel( label );
    }

    /***************************************************************************
     * @param label JLabel
     **************************************************************************/
    private static void initLabel( JLabel label )
    {
        label.setOpaque( true );
        label.setPreferredSize( new Dimension( 20, 20 ) );
        label.setMinimumSize( new Dimension( 20, 20 ) );
    }

    /***************************************************************************
     * @param cal Calendar
     **************************************************************************/
    public void setDate( LocalDate date )
    {
        if( this.currentSelection != null )
        {
            this.currentSelection.setSelected( false );
        }

        if( date == null )
        {
            date = LocalDate.now();
        }

        LocalDate ld = date;
        ld = ld.minusDays( date.getDayOfMonth() - 1 );

        DayOfWeek dowFirst = ld.getDayOfWeek();
        int dow = dowFirst.getValue();

        ld = ld.minusDays( dow == 7 ? 0 : dow );

        for( int i = 0; i < dayLabels.length; i++ )
        {
            dayLabels[i].setDate( ld );
            dayLabels[i].setNonDay( date.getMonth() != ld.getMonth() );

            if( date.equals( ld ) )
            {
                dayLabels[i].setSelected( true );
                currentSelection = dayLabels[i];
            }

            ld = ld.plusDays( 1 );
        }

        monthSpinner.setValue( MONTHS[date.getMonthValue() - 1] );

        yearSpinner.setValue(  date.getYear() );
    }

    /***************************************************************************
     * @return GregorianCalendar
     **************************************************************************/
    public LocalDate getDate()
    {
        LocalDate date = null;

        if( this.currentSelection != null )
        {
            return this.currentSelection.getDate();
        }

        return date;
    }

    /***************************************************************************
     * @param mth String
     * @return int
     **************************************************************************/
    private static int getMonthIndex( String mth )
    {
        for( int i = 0; i < MONTHS.length; i++ )
        {
            if( MONTHS[i].compareToIgnoreCase( mth ) == 0 )
            {
                return i;
            }
        }
        return -1;
    }

    /***************************************************************************
     * @param cal Calendar
     **************************************************************************/
    public static void debugPrintCal( Calendar cal )
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
            "MM/dd/yyyy hh:mm:ss a" );

        LogUtils.printDebug( dateFormat.format( cal.getTime() ) );
    }

    private void updateFromSpinners()
    {
        String mthStr = monthSpinner.getValue().toString();

        int month = getMonthIndex( mthStr ) + 1;
        int day = currentSelection == null ? 1 : currentSelection.getDay();
        int year = ( ( Number )yearSpinner.getValue() ).intValue();

        LocalDate date = LocalDate.of( year, month, day );

        setDate( date );

        dateChangedListeners.fireListeners( date );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class DayLabelKeyListener extends KeyAdapter
    {
        private final DateView view;

        public DayLabelKeyListener( DateView view )
        {
            this.view = view;
        }

        @Override
        public void keyPressed( KeyEvent e )
        {
            if( view.currentSelection == null )
            {
                return;
            }

            LocalDate newDate = view.currentSelection.getDate();

            if( e.getKeyCode() == KeyEvent.VK_UP )
            {
                newDate = newDate.minusDays( 7 );
            }
            else if( e.getKeyCode() == KeyEvent.VK_DOWN )
            {
                newDate = newDate.plusDays( 7 );
            }
            else if( e.getKeyCode() == KeyEvent.VK_LEFT )
            {
                newDate = newDate.minusDays( 1 );
            }
            else if( e.getKeyCode() == KeyEvent.VK_RIGHT )
            {
                newDate = newDate.plusDays( 1 );
            }

            view.setData( newDate );
            view.currentSelection.getView().requestFocus();
            view.dateChangedListeners.fireListeners( newDate );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class YearChangeListener implements ChangeListener
    {
        private final DateView calPanel;

        public YearChangeListener( DateView adaptee )
        {
            this.calPanel = adaptee;
        }

        @Override
        public void stateChanged( ChangeEvent e )
        {
            calPanel.updateFromSpinners();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class MonthChangeListener implements ChangeListener
    {
        private final DateView calPanel;

        public MonthChangeListener( DateView adaptee )
        {
            this.calPanel = adaptee;
        }

        @Override
        public void stateChanged( ChangeEvent e )
        {
            calPanel.updateFromSpinners();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class DayLabelMouseListener extends MouseAdapter
    {
        private final DateView view;
        private final DayLabel labelClicked;

        public DayLabelMouseListener( DateView view, DayLabel label )
        {
            this.view = view;
            this.labelClicked = label;
        }

        @Override
        public void mouseClicked( MouseEvent e )
        {
            if( labelClicked != view.currentSelection )
            {
                view.currentSelection.setSelected( false );

                if( labelClicked.isNonDay() )
                {
                    LocalDate date = labelClicked.getDate();

                    view.setDate( date );
                }
                else
                {
                    labelClicked.setSelected( true );

                    view.currentSelection = labelClicked;
                }

                view.dateChangedListeners.fireListeners( view.getDate() );
            }

            labelClicked.getView().requestFocus();
        }
    }
}
