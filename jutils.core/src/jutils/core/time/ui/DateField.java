package jutils.core.time.ui;

import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Window;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import jutils.core.IconConstants;
import jutils.core.SwingUtils;
import jutils.core.io.parsers.DateParser;
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
}
