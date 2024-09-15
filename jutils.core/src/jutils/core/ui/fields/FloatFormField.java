package jutils.core.ui.fields;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import jutils.core.io.parsers.FloatParser;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.validation.IValidityChangedListener;
import jutils.core.ui.validation.Validity;

/*******************************************************************************
 * Defines an {@link IFormField} that validates text as {@link Double}s.
 ******************************************************************************/
public class FloatFormField implements IDataFormField<Float>
{
    /**  */
    private final ParserFormField<Float> textField;

    /**  */
    private String format;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public FloatFormField( String name )
    {
        this( name, null );
    }

    /***************************************************************************
     * @param name
     * @param min
     * @param max
     **************************************************************************/
    public FloatFormField( String name, Float min, Float max )
    {
        this( name, null, min, max );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param min
     * @param max
     **************************************************************************/
    public FloatFormField( String name, String units, Float min, Float max )
    {
        this( name, units, 20, min, max );
    }

    /***************************************************************************
     * @param name
     * @param units
     **************************************************************************/
    public FloatFormField( String name, String units )
    {
        this( name, units, 20 );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param columns
     **************************************************************************/
    public FloatFormField( String name, String units, int columns )
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
    public FloatFormField( String name, String units, int columns, Float min,
        Float max )
    {
        JTextField jtf = new JTextField( columns );

        this.textField = new ParserFormField<>( name,
            new FloatParser( min, max ), jtf, ( d ) -> toString( d ), jtf,
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
    public Float getValue()
    {
        return textField.getValue();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( Float value )
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
    public void setUpdater( IUpdater<Float> updater )
    {
        textField.setUpdater( updater );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<Float> getUpdater()
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
     * @return
     **************************************************************************/
    public JTextComponent getTextField()
    {
        return this.textField.getTextField();
    }

    /***************************************************************************
     * @param value
     * @return
     **************************************************************************/
    private String toString( Float value )
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
                text = Float.toString( value );
            }
        }

        return text;
    }
}
