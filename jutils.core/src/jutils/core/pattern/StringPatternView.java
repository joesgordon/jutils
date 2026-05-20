package jutils.core.pattern;

import java.awt.Component;

import javax.swing.JPanel;

import jutils.core.ui.StandardFormView;
import jutils.core.ui.fields.BooleanFormField;
import jutils.core.ui.fields.ComboFormField;
import jutils.core.ui.fields.NamedItemDescriptor;
import jutils.core.ui.fields.StringFormField;
import jutils.core.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class StringPatternView implements IDataView<StringPattern>
{
    /**  */
    private final JPanel view;
    /**  */
    private final ComboFormField<StringPatternType> typeField;
    /**  */
    private final BooleanFormField caseField;
    /**  */
    private final StringFormField patternField;
    /**  */
    private final StringFormField nameField;

    /**  */
    private StringPattern pattern;

    /***************************************************************************
     * 
     **************************************************************************/
    public StringPatternView()
    {
        this.typeField = new ComboFormField<>( "Type",
            StringPatternType.values(), new NamedItemDescriptor<>() );
        this.caseField = new BooleanFormField( "Is Case Sensitive" );
        this.patternField = new StringFormField( "Pattern", 0, null );
        this.nameField = new StringFormField( "Name", 0, null );
        this.view = createView();

        setData( new StringPattern() );

        typeField.setUpdater( ( t ) -> pattern.type = t );
        caseField.setUpdater( ( b ) -> pattern.isCaseSensitive = b );
        patternField.setUpdater( ( s ) -> pattern.patternText = s );
        nameField.setUpdater( ( s ) -> pattern.name = s );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        StandardFormView form = new StandardFormView();

        form.addField( nameField );
        form.addField( typeField );
        form.addField( patternField );
        form.addField( caseField );

        return form.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public StringPattern getData()
    {
        return pattern;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( StringPattern data )
    {
        this.pattern = data;

        typeField.setValue( pattern.type );
        caseField.setValue( pattern.isCaseSensitive );
        patternField.setValue( pattern.patternText );
        nameField.setValue( pattern.name );
    }
}
