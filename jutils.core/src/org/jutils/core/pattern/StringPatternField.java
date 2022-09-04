package org.jutils.core.pattern;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jutils.core.ValidationException;
import org.jutils.core.io.IParser;
import org.jutils.core.ui.event.updater.IUpdater;
import org.jutils.core.ui.fields.BooleanFormField;
import org.jutils.core.ui.fields.ComboFormField;
import org.jutils.core.ui.fields.IDataFormField;
import org.jutils.core.ui.fields.IDescriptor;
import org.jutils.core.ui.fields.NamedItemDescriptor;
import org.jutils.core.ui.fields.ParserFormField;
import org.jutils.core.ui.validation.IValidityChangedListener;
import org.jutils.core.ui.validation.Validity;

/*******************************************************************************
 * 
 ******************************************************************************/
public class StringPatternField implements IDataFormField<StringPattern>
{
    /**  */
    private final JPanel view;
    /**  */
    private final ParserFormField<String> patternField;
    /**  */
    private final ComboFormField<StringPatternType> typeField;
    /**  */
    private final BooleanFormField caseField;

    /**  */
    private StringPattern pattern;
    /**  */
    private IUpdater<StringPattern> updater;

    // TODO Re-validate pattern when type changes.

    /***************************************************************************
     * 
     **************************************************************************/
    public StringPatternField( String name )
    {
        PatternTextParser parser = new PatternTextParser( this );
        PatternTextDescriptor descriptor = new PatternTextDescriptor();

        this.pattern = new StringPattern();

        this.patternField = new ParserFormField<>( name, parser, descriptor );
        this.typeField = new ComboFormField<>( "Type",
            StringPatternType.values(), new NamedItemDescriptor<>() );
        this.caseField = new BooleanFormField( "Case" );
        this.view = createView();

        setValue( this.pattern );

        caseField.getView().setText( caseField.getName() );

        patternField.setUpdater( ( t ) -> {
            if( pattern != null )
            {
                pattern.patternText = t;
            }
            invokeUpdater();
        } );

        typeField.setUpdater( ( d ) -> {
            if( pattern != null )
            {
                pattern.type = d;
            }
            invokeUpdater();
        } );

        caseField.setUpdater( ( d ) -> {
            if( pattern != null )
            {
                pattern.isCaseSensitive = d;
            }
            invokeUpdater();
        } );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void invokeUpdater()
    {
        if( updater != null )
        {
            updater.update( pattern );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 4 ), 0, 0 );
        panel.add( patternField.getView(), constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( typeField.getView(), constraints );

        constraints = new GridBagConstraints( 2, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 6, 0, 0 ), 0, 0 );
        panel.add( caseField.getView(), constraints );

        return panel;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public StringPattern getValue()
    {
        return pattern;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( StringPattern value )
    {
        this.pattern = value;

        if( value != null )
        {
            patternField.setValue( pattern.patternText );
            typeField.setValue( pattern.type );
            caseField.setValue( pattern.isCaseSensitive );
        }
        else
        {
            patternField.setValue( null );
            typeField.setValue( StringPatternType.CONTAINS );
            caseField.setValue( false );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<StringPattern> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<StringPattern> getUpdater()
    {
        return updater;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        patternField.setEditable( editable );
        typeField.setEditable( editable );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return patternField.getName();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        patternField.addValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        patternField.removeValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return patternField.getValidity();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class PatternTextParser implements IParser<String>
    {
        private final StringPatternField field;

        public PatternTextParser( StringPatternField field )
        {
            this.field = field;
        }

        @Override
        public String parse( String str ) throws ValidationException
        {
            StringPattern pattern = new StringPattern( field.pattern );

            pattern.patternText = str;

            // LogUtils.printDebug( "Testing %s", pattern );

            pattern.createMatcher();

            return str;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class PatternTextDescriptor
        implements IDescriptor<String>
    {
        @Override
        public String getDescription( String item )
        {
            return item;
        }
    }
}
