package jutils.telemetry.ui.ch10;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import jutils.core.io.FieldPrinter;
import jutils.core.ui.StandardFormView;
import jutils.core.ui.fields.BooleanFormField;
import jutils.core.ui.fields.ComboFormField;
import jutils.core.ui.fields.IntegerFormField;
import jutils.core.ui.fields.NamedItemDescriptor;
import jutils.core.ui.fields.StringAreaFormField;
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
    private final StringAreaFormField setupField;
    /**  */
    private final StringAreaFormField tmatsField;

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
        this.setupField = new StringAreaFormField( "Setup" );
        this.tmatsField = new StringAreaFormField( "TMATS" );

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

        addTab( tabs, setupField );
        addTab( tabs, tmatsField );

        return tabs;
    }

    /***************************************************************************
     * @param tabs
     * @param field
     * @return
     **************************************************************************/
    private void addTab( JTabbedPane tabs, StringAreaFormField field )
    {
        JTextArea area = field.getTextArea();
        JScrollPane pane = new JScrollPane( area );

        area.setFont( area.getFont().deriveFont( 14.f ) );

        pane.getVerticalScrollBar().setUnitIncrement( 12 );

        tabs.addTab( field.getName(), pane );
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
        setupField.setValue( body.setup );
        tmatsField.setValue( FieldPrinter.toString( body.tmats ) );
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
