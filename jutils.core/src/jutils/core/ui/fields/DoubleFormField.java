package jutils.core.ui.fields;

import javax.swing.JComponent;
import javax.swing.JTextField;

import jutils.core.io.parsers.DoubleParser;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.validation.IValidityChangedListener;
import jutils.core.ui.validation.Validity;

/*******************************************************************************
 * Defines an {@link IFormField} that validates text as {@link Double}s.
 ******************************************************************************/
public class DoubleFormField implements IDataFormField<Double>
{
    /**  */
    private final ParserFormField<Double> textField;

    /**  */
    private String format;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public DoubleFormField( String name )
    {
        this( name, null );
    }

    /***************************************************************************
     * @param name
     * @param min
     * @param max
     **************************************************************************/
    public DoubleFormField( String name, Double min, Double max )
    {
        this( name, null, min, max );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param min
     * @param max
     **************************************************************************/
    public DoubleFormField( String name, String units, Double min, Double max )
    {
        this( name, units, 20, min, max );
    }

    /***************************************************************************
     * @param name
     * @param units
     **************************************************************************/
    public DoubleFormField( String name, String units )
    {
        this( name, units, 20 );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param columns
     **************************************************************************/
    public DoubleFormField( String name, String units, int columns )
    {
        this( name, units, columns, null, null );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param columns
     * @param updater
     * @param min
     * @param max
     **************************************************************************/
    public DoubleFormField( String name, String units, int columns, Double min,
        Double max )
    {
        JTextField jtf = new JTextField( columns );

        this.textField = new ParserFormField<>( name,
            new DoubleParser( min, max ), jtf, ( d ) -> toString( d ), jtf,
            units );
        this.format = null;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return textField.getName();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return textField.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Double getValue()
    {
        return textField.getValue();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( Double value )
    {
        textField.setValue( value );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        textField.setEditable( editable );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<Double> updater )
    {
        textField.setUpdater( updater );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<Double> getUpdater()
    {
        return textField.getUpdater();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        textField.addValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        textField.removeValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return textField.getValidity();
    }

    /***************************************************************************
     * @param format
     **************************************************************************/
    public void setFormat( String format )
    {
        this.format = format;
    }

    /***************************************************************************
     * @param value
     * @return
     **************************************************************************/
    private String toString( Double value )
    {
        String text = "";

        if( value != null )
        {
            if( format != null )
            {
                text = String.format( format, value );
            }
            else
            {
                text = Double.toString( value );
            }
        }

        return text;
    }
}
