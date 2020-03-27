package org.jutils.core.ui.calendar;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.List;

import javax.swing.*;

import org.jutils.core.Utils;
import org.jutils.core.ValidationException;
import org.jutils.core.io.IParser;
import org.jutils.core.io.parsers.IntegerParser;
import org.jutils.core.ui.event.updater.IUpdater;
import org.jutils.core.ui.fields.IDataFormField;
import org.jutils.core.ui.fields.ParserFormField;
import org.jutils.core.ui.validation.IValidityChangedListener;
import org.jutils.core.ui.validation.Validity;

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

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class TimeParser implements IParser<LocalTime>
    {
        private final IntegerParser hourParser;
        private final IntegerParser minuteParser;
        private final IntegerParser secondParser;
        private final IntegerParser fractionParser;

        public TimeParser()
        {
            this.hourParser = new IntegerParser( 0, 23 );
            this.minuteParser = new IntegerParser( 0, 59 );
            this.secondParser = new IntegerParser( 0, 59 );
            this.fractionParser = new IntegerParser( 0, 999999999 );
        }

        @Override
        public LocalTime parse( String str ) throws ValidationException
        {
            if( str.isEmpty() )
            {
                throw new ValidationException(
                    "Cannot parse time from empty string" );
            }

            String text = str.replace( " ", "" );
            text = text.replace( '.', ':' );

            List<String> fields = Utils.split( text, ':' );

            if( fields.size() < 3 || fields.size() > 4 )
            {
                throw new ValidationException(
                    "Incorrect number of fields in time: " + fields.size() +
                        "; expected 3 or 4" );
            }

            int hour = hourParser.parse( fields.get( 0 ) );
            int min = minuteParser.parse( fields.get( 1 ) );
            int sec = secondParser.parse( fields.get( 2 ) );
            int fracs = 0;

            if( fields.size() == 4 && !fields.get( 3 ).isEmpty() )
            {
                fractionParser.parse( fields.get( 3 ) );
            }

            try
            {
                return LocalTime.of( hour, min, sec, fracs );
            }
            catch( DateTimeException ex )
            {
                throw new ValidationException(
                    "Cannot parse time from string: " + text + " : " +
                        ex.getMessage(),
                    ex );
            }
        }
    }
}
