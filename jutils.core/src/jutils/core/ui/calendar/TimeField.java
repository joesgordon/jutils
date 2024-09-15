package jutils.core.ui.calendar;

import java.time.LocalTime;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import jutils.core.io.parsers.TimeParser;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.fields.IDataFormField;
import jutils.core.ui.fields.ParserFormField;
import jutils.core.ui.validation.IValidityChangedListener;
import jutils.core.ui.validation.Validity;

/*******************************************************************************
 *
 ******************************************************************************/
public class TimeField implements IDataFormField<LocalTime>
{
    /**  */
    private final String name;

    /**  */
    private final ParserFormField<LocalTime> timeField;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public TimeField( String name )
    {
        this.name = name;

        JTextField textField = new JTextField();

        this.timeField = new ParserFormField<>( name, new TimeParser(),
            textField );

        textField.setColumns( 10 );
        textField.setHorizontalAlignment( SwingConstants.RIGHT );
        textField.setMinimumSize( textField.getPreferredSize() );

        setValue( LocalTime.now() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return timeField.getView();
    }

    /***************************************************************************
     * @param text String
     **************************************************************************/
    public void setToolTipText( String text )
    {
        timeField.getView().setToolTipText( text );
    }

    /***************************************************************************
     * @param text String
     **************************************************************************/
    public void setTextFieldToolTipText( String text )
    {
        timeField.getView().setToolTipText( text );
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
        timeField.addValidityChanged( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        timeField.removeValidityChanged( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return timeField.getValidity();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public LocalTime getValue()
    {
        return timeField.getValue();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( LocalTime value )
    {
        timeField.setValue( value );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<LocalTime> updater )
    {
        timeField.setUpdater( updater );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IUpdater<LocalTime> getUpdater()
    {
        return timeField.getUpdater();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        timeField.setEditable( editable );
    }
}
