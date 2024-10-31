package jutils.core.ui.fields;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import jutils.core.io.parsers.HexShortParser;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.hex.HexUtils;
import jutils.core.ui.validation.IValidityChangedListener;
import jutils.core.ui.validation.Validity;

/*******************************************************************************
 * Defines an {@link IFormField} that contains a double validater.
 ******************************************************************************/
public class HexShortFormField implements IDataFormField<Short>
{
    /**  */
    private final ParserFormField<Short> field;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public HexShortFormField( String name )
    {
        this( name, ( String )null );
    }

    /***************************************************************************
     * @param name
     * @param units
     **************************************************************************/
    public HexShortFormField( String name, String units )
    {
        this( name, units, 20 );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param columns
     **************************************************************************/
    public HexShortFormField( String name, String units, int columns )
    {
        this( name, units, columns, null, null );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param min
     * @param max
     **************************************************************************/
    public HexShortFormField( String name, String units, Short min, Short max )
    {
        this( name, units, 20, min, max );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param columns
     * @param updater
     **************************************************************************/
    public HexShortFormField( String name, String units, int columns, Short min,
        Short max )
    {
        JTextField textField = new JTextField( columns );

        this.field = new ParserFormField<>( name,
            new HexShortParser( min, max ), textField, ( d ) -> toString( d ),
            textField, units );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return field.getName();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return field.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Short getValue()
    {
        return field.getValue();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( Short value )
    {
        field.setValue( value );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<Short> updater )
    {
        field.setUpdater( updater );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<Short> getUpdater()
    {
        return field.getUpdater();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        field.setEditable( editable );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        field.addValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        field.removeValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return field.getValidity();
    }

    /***************************************************************************
     * @param value
     * @return
     **************************************************************************/
    private static String toString( Short value )
    {
        String text = "";

        if( value != null )
        {
            text = HexUtils.getHexString( value );
        }

        return text;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public JTextComponent getTextField()
    {
        return field.getTextField();
    }
}
