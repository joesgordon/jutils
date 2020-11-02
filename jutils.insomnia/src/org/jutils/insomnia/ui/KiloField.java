package org.jutils.insomnia.ui;

import javax.swing.JComponent;

import org.jutils.core.ValidationException;
import org.jutils.core.io.IParser;
import org.jutils.core.io.parsers.DoubleParser;
import org.jutils.core.ui.event.updater.IUpdater;
import org.jutils.core.ui.fields.IDataFormField;
import org.jutils.core.ui.fields.IDescriptor;
import org.jutils.core.ui.fields.ParserFormField;
import org.jutils.core.ui.validation.IValidityChangedListener;
import org.jutils.core.ui.validation.Validity;

/*******************************************************************************
 * 
 ******************************************************************************/
public class KiloField implements IDataFormField<Long>
{
    /**  */
    private final ParserFormField<Long> field;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public KiloField( String name )
    {
        this( name, null );
    }

    /***************************************************************************
     * @param name
     * @param units
     **************************************************************************/
    public KiloField( String name, String units )
    {
        IDescriptor<Long> desc = ( v ) -> String.format( "%.3f",
            ( v / 1000.0 ) );

        this.field = new ParserFormField<>( name, new KiloParser(), desc,
            units );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Long getValue()
    {
        return field.getValue();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( Long value )
    {
        field.setValue( value );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<Long> updater )
    {
        field.setUpdater( updater );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<Long> getUpdater()
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
     * 
     **************************************************************************/
    private static final class KiloParser implements IParser<Long>
    {
        /**  */
        private final DoubleParser parser;

        /**
         * 
         */
        public KiloParser()
        {
            this.parser = new DoubleParser();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Long parse( String text ) throws ValidationException
        {
            double dv = parser.parse( text );

            long value = ( long )( dv * 1000 + 0.5 );

            return value;
        }
    }
}
