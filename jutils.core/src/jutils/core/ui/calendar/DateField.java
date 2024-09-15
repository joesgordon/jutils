package jutils.core.ui.calendar;

import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

import javax.swing.*;

import jutils.core.*;
import jutils.core.io.IParser;
import jutils.core.io.parsers.IntegerParser;
import jutils.core.ui.OkDialogView;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.fields.IDataFormField;
import jutils.core.ui.fields.ParserFormField;
import jutils.core.ui.validation.IValidityChangedListener;
import jutils.core.ui.validation.Validity;

/*******************************************************************************
 *
 ******************************************************************************/
public class DateField implements IDataFormField<LocalDate>
{
    /**  */
    private final String name;

    /**  */
    private final JPanel view;
    /**  */
    private final ParserFormField<LocalDate> dateField;
    /**  */
    private final JButton calendarButton;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public DateField( String name )
    {
        this.name = name;

        JTextField dateTextField = new JTextField();

        this.dateField = new ParserFormField<>( name, new DateParser(),
            dateTextField );
        this.calendarButton = new JButton();
        this.view = new JPanel( new GridBagLayout() );

        dateTextField.setColumns( 10 );
        dateTextField.setHorizontalAlignment( SwingConstants.RIGHT );
        dateTextField.setMinimumSize( dateTextField.getPreferredSize() );

        calendarButton.setText( "" );
        calendarButton.addActionListener(
            ( e ) -> displayDialog( "Select Date" ) );
        calendarButton.setIcon(
            IconConstants.getIcon( IconConstants.CALENDAR_16 ) );
        calendarButton.setMargin( new Insets( 0, 0, 0, 0 ) );

        view.add( dateField.getView(),
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );
        view.add( calendarButton,
            new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                new Insets( 0, 4, 0, 0 ), 0, 0 ) );

        setValue( LocalDate.now() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * @param text String
     **************************************************************************/
    public void setToolTipText( String text )
    {
        dateField.getView().setToolTipText( text );
        calendarButton.setToolTipText( text );
    }

    /***************************************************************************
     * @param text String
     **************************************************************************/
    public void setTextFieldToolTipText( String text )
    {
        dateField.getView().setToolTipText( text );
    }

    /***************************************************************************
     * @param text String
     **************************************************************************/
    public void setButtonToolTipText( String text )
    {
        calendarButton.setToolTipText( text );
    }

    /***************************************************************************
     * @param e ActionEvent
     **************************************************************************/
    private void displayDialog( String title )
    {
        Frame parent = SwingUtils.getComponentsFrame( view );
        JPanel panel = new JPanel();
        DateView calendarPanel = new DateView();

        OkDialogView dialogView = new OkDialogView( parent, panel,
            ModalityType.DOCUMENT_MODAL );

        panel.add( calendarPanel.getView() );

        dialogView.setTitle( "Select Date" );

        calendarPanel.setDate( dateField.getValue() );

        Window w = SwingUtils.getComponentsWindow( view );
        List<Image> icons = w.getIconImages();
        Dimension size = new Dimension( 200, 280 );

        if( dialogView.show( title, icons, size ) )
        {
            setValue( calendarPanel.getDate() );
            fireNewDate();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void fireNewDate()
    {
        IUpdater<LocalDate> updater = dateField.getUpdater();

        if( updater != null )
        {
            updater.update( dateField.getValue() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        return name;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        dateField.addValidityChanged( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        dateField.removeValidityChanged( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return dateField.getValidity();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public LocalDate getValue()
    {
        return dateField.getValue();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( LocalDate value )
    {
        dateField.setValue( value );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<LocalDate> updater )
    {
        dateField.setUpdater( updater );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IUpdater<LocalDate> getUpdater()
    {
        return dateField.getUpdater();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        dateField.setEditable( editable );
        calendarButton.setEnabled( editable );
        view.setEnabled( editable );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class DateParser implements IParser<LocalDate>
    {
        private final IntegerParser yearParser;
        private final IntegerParser monthParser;
        private final IntegerParser dayParser;

        public DateParser()
        {
            this.yearParser = new IntegerParser();
            this.monthParser = new IntegerParser( 1, 12 );
            this.dayParser = new IntegerParser( 1, 31 );
        }

        @Override
        public LocalDate parse( String str ) throws ValidationException
        {
            if( str.isEmpty() )
            {
                throw new ValidationException(
                    "Cannot parse date from empty string" );
            }

            String text = str.replace( " ", "" );
            text = text.replace( '/', '-' );

            List<String> fields = Utils.split( text, '-' );

            if( fields.size() != 3 )
            {
                throw new ValidationException(
                    "Incorrect number of fields in date: " + fields.size() +
                        "; expected 3" );
            }

            int [] ymdIdxs = new int[] { 0, 1, 2 };
            boolean add2k = false;

            if( fields.get( 2 ).length() == 4 )
            {
                ymdIdxs = new int[] { 2, 0, 1 };
            }
            else if( fields.get( 0 ).length() == 2 &&
                fields.get( 1 ).length() == 2 && fields.get( 2 ).length() == 2 )
            {
                ymdIdxs = new int[] { 2, 0, 1 };
                add2k = true;
            }

            int year = yearParser.parse( fields.get( ymdIdxs[0] ) );
            int month = monthParser.parse( fields.get( ymdIdxs[1] ) );
            int day = dayParser.parse( fields.get( ymdIdxs[2] ) );

            if( add2k )
            {
                year += 2000;
            }

            try
            {
                return LocalDate.of( year, month, day );
            }
            catch( DateTimeException ex )
            {
                throw new ValidationException(
                    "Cannot parse date from string: " + text + " : " +
                        ex.getMessage(),
                    ex );
            }
        }
    }
}
