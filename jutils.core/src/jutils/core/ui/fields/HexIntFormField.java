package jutils.core.ui.fields;

import javax.swing.JComponent;
import javax.swing.JTextField;

import jutils.core.io.parsers.HexIntegerParser;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.hex.HexUtils;
import jutils.core.ui.validation.IValidityChangedListener;
import jutils.core.ui.validation.Validity;

/*******************************************************************************
 * Defines an {@link IFormField} that contains a double validater.
 ******************************************************************************/
public class HexIntFormField implements IDataFormField<Integer>
{
    /**  */
    private final JTextField textField;
    /**  */
    private final ParserFormField<Integer> field;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public HexIntFormField( String name )
    {
        this( name, ( String )null );
    }

    /***************************************************************************
     * @param name
     * @param units
     **************************************************************************/
    public HexIntFormField( String name, String units )
    {
        this( name, units, 20 );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param columns
     **************************************************************************/
    public HexIntFormField( String name, String units, int columns )
    {
        this( name, units, columns, null, null );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param min
     * @param max
     **************************************************************************/
    public HexIntFormField( String name, String units, Integer min,
        Integer max )
    {
        this( name, units, 20, min, max );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param columns
     * @param min
     * @param max
     **************************************************************************/
    public HexIntFormField( String name, String units, int columns, Integer min,
        Integer max )
    {
        this.textField = new JTextField( columns );
        this.field = new ParserFormField<>( name,
            new HexIntegerParser( min, max ), textField, ( d ) -> toString( d ),
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
    public Integer getValue()
    {
        return field.getValue();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( Integer value )
    {
        field.setValue( value );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<Integer> updater )
    {
        field.setUpdater( updater );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<Integer> getUpdater()
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
    private static String toString( Integer value )
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
    public JTextField getTextField()
    {
        return textField;
    }
}
