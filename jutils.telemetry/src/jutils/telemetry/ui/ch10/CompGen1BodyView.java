package jutils.telemetry.ui.ch10;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import jutils.core.io.FieldPrinter;
import jutils.core.ui.StandardFormView;
import jutils.core.ui.StringView;
import jutils.core.ui.fields.BooleanFormField;
import jutils.core.ui.fields.ComboFormField;
import jutils.core.ui.fields.IntegerFormField;
import jutils.core.ui.fields.NamedItemDescriptor;
import jutils.telemetry.data.ch10.CompGen1Body;
import jutils.telemetry.data.ch10.CompGen1Word;
import jutils.telemetry.data.ch10.Rcc106Version;
import jutils.telemetry.data.ch10.TmatsFormat;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CompGen1BodyView implements IPacketBodyView<CompGen1Body>
{
    /**  */
    private final JComponent view;

    /**  */
    private final ComboFormField<TmatsFormat> formatField;
    /**  */
    private final BooleanFormField srccField;
    /**  */
    private final ComboFormField<Rcc106Version> rccVerField;
    /**  */
    private final IntegerFormField reservedField;
    /**  */
    private final StringView setupField;
    /**  */
    private final StringView tmatsField;

    /**  */
    private CompGen1Body body;

    /***************************************************************************
     * 
     **************************************************************************/
    public CompGen1BodyView()
    {
        this.formatField = new ComboFormField<>( "Format", TmatsFormat.values(),
            new NamedItemDescriptor<>() );
        this.srccField = new BooleanFormField( "Setup Record Config Changed" );
        this.rccVerField = new ComboFormField<>( "RCC-106 Version",
            Rcc106Version.values(), new NamedItemDescriptor<>() );
        this.reservedField = new IntegerFormField( "Reserved", 0,
            ( int )CompGen1Word.RESERVED.getMax() );
        this.setupField = new StringView();
        this.tmatsField = new StringView();

        this.view = createView();

        reservedField.setEditable( false );

        this.setEditable( false );
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        formatField.setEditable( editable );
        srccField.setEditable( editable );
        rccVerField.setEditable( editable );
        reservedField.setEditable( editable );
        setupField.setEditable( editable );
        tmatsField.setEditable( editable );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( createForm(), BorderLayout.NORTH );
        panel.add( createTabs(), BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createForm()
    {
        StandardFormView form = new StandardFormView();

        form.addField( formatField );
        form.addField( srccField );
        form.addField( rccVerField );
        form.addField( reservedField );

        return form.getView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createTabs()
    {
        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab( "Setup", setupField.getView() );
        tabs.addTab( "TMATS", tmatsField.getView() );

        return tabs;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public CompGen1Body getData()
    {
        return this.body;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( CompGen1Body data )
    {
        this.body = data;

        formatField.setValue( body.format );
        srccField.setValue( body.setupRecordConfigChanged );
        rccVerField.setValue( body.rccVersion );
        reservedField.setValue( body.reserved );
        setupField.setData( body.setup );
        tmatsField.setData( FieldPrinter.toString( body.tmats ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view;
    }
}
