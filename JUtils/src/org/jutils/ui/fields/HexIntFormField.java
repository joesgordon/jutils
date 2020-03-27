package org.jutils.ui.fields;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import org.jutils.io.parsers.HexIntegerParser;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.hex.HexUtils;
import org.jutils.ui.validation.IValidityChangedListener;
import org.jutils.ui.validation.Validity;

/*******************************************************************************
 * Defines an {@link IFormField} that contains a double validater.
 ******************************************************************************/
public class HexIntFormField implements IDataFormField<Integer>
{
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
        this( name, units, 20, null );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param columns
     **************************************************************************/
    public HexIntFormField( String name, String units, int columns )
    {
        this( name, units, columns, null );
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
     * @param updater
     **************************************************************************/
    public HexIntFormField( String name, String units, int columns, Integer min,
        Integer max )
    {
        JTextField textField = new JTextField( columns );

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
    private String toString( Integer value )
    {
        String text = "";

        if( value != null )
        {
            int i = value;

            byte b3 = ( byte )( ( i >> 24 ) & 0xFF );
            byte b2 = ( byte )( ( i >> 16 ) & 0xFF );
            byte b1 = ( byte )( ( i >> 8 ) & 0xFF );
            byte b0 = ( byte )( ( i >> 0 ) & 0xFF );

            text = HexUtils.toHexString( b3 ) + HexUtils.toHexString( b2 ) +
                HexUtils.toHexString( b1 ) + HexUtils.toHexString( b0 );
        }

        return text;
    }

    public JTextComponent getTextField()
    {
        return field.getTextField();
    }
}
