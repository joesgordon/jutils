package jutils.core.ui.fields;

import javax.swing.JComponent;
import javax.swing.JTextField;

import jutils.core.io.parsers.IntegerParser;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.validation.IValidityChangedListener;
import jutils.core.ui.validation.Validity;

/*******************************************************************************
 * Defines an {@link IFormField} that contains a integer validator.
 ******************************************************************************/
public class IntegerFormField implements IDataFormField<Integer>
{
    /**  */
    private final JTextField textField;
    /**  */
    private final ParserFormField<Integer> field;
    /**  */
    private final IntegerParser parser;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public IntegerFormField( String name )
    {
        this( name, null );
    }

    /***************************************************************************
     * @param name
     * @param units
     **************************************************************************/
    public IntegerFormField( String name, String units )
    {
        this( name, units, 20 );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param columns
     **************************************************************************/
    public IntegerFormField( String name, String units, int columns )
    {
        this( name, units, columns, null, null );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param min
     * @param max
     **************************************************************************/
    public IntegerFormField( String name, Integer min, Integer max )
    {
        this( name, null, min, max );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param min
     * @param max
     **************************************************************************/
    public IntegerFormField( String name, String units, Integer min,
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
     * @param updater
     **************************************************************************/
    public IntegerFormField( String name, String units, int columns,
        Integer min, Integer max )
    {
        IDescriptor<Integer> descriptor = ( d ) -> toString( d );
        this.parser = new IntegerParser( min, max );

        this.textField = new JTextField( columns );
        this.field = new ParserFormField<>( name, parser, textField, descriptor,
            textField, units );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        return field.getName();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return field.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Integer getValue()
    {
        return field.getValue();
    }

    /***************************************************************************
     * 
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
     * 
     **************************************************************************/
    @Override
    public IUpdater<Integer> getUpdater()
    {
        return field.getUpdater();
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        // LogUtils.printDebug( "IntegerFormField: " + getName() + " field is "
        // +
        // ( editable ? "" : "not " ) + "editable" );
        field.setEditable( editable );
    }

    /***************************************************************************
     * 
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
     * Sets the minimum value allowed to the provided values.
     * @param min The minimum bound, inclusive; not checked if {@code null}.
     **************************************************************************/
    public void setMinimum( Integer min )
    {
        parser.setMinimum( min );
    }

    /***************************************************************************
     * Sets the maximum value allowed to the provided values.
     * @param min The maximum bound, inclusive; not checked if {@code null}.
     **************************************************************************/
    public void setMaximum( Integer max )
    {
        parser.setMaximum( max );
    }

    /***************************************************************************
     * Sets the minimum and maximum values allowed to the provided values.
     * @param min The minimum bound, inclusive; not checked if {@code null}.
     * @param max The maximum bound, inclusive; not checked if {@code null}.
     **************************************************************************/
    public void setThresholds( Integer min, Integer max )
    {
        parser.setThresholds( min, max );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public JTextField getTextField()
    {
        return textField;
    }

    /***************************************************************************
     * @param value
     * @return
     **************************************************************************/
    private static String toString( Integer value )
    {
        return value == null ? "" : Integer.toString( value );
    }
}
