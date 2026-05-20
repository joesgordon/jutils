package jutils.telemetry.ch10.ui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JPanel;

import jutils.core.ui.StandardFormView;
import jutils.core.ui.fields.ComboFormField;
import jutils.core.ui.fields.IntegerFormField;
import jutils.core.ui.fields.NamedItemDescriptor;
import jutils.telemetry.ch10.DateFormat;
import jutils.telemetry.ch10.IrigTimeSource;
import jutils.telemetry.ch10.Time1Body;
import jutils.telemetry.ch10.Time1Word;
import jutils.telemetry.ch10.TimeSource;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Time1BodyView implements IPacketBodyView<Time1Body>
{
    /**  */
    private final JComponent view;
    /**  */
    private final ComboFormField<TimeSource> sourceField;
    /**  */
    private final ComboFormField<DateFormat> formatField;
    /**  */
    private final ComboFormField<IrigTimeSource> irigSourceField;
    /**  */
    private final IntegerFormField reservedField;
    /**  */
    private final Time1View timeView;

    /**  */
    private Time1Body body;

    /***************************************************************************
     * 
     **************************************************************************/
    public Time1BodyView()
    {
        this.sourceField = new ComboFormField<>( "Time Source",
            TimeSource.values(), new NamedItemDescriptor<>() );
        this.formatField = new ComboFormField<>( "Date Format",
            DateFormat.values(), new NamedItemDescriptor<>() );
        this.irigSourceField = new ComboFormField<>( "Irig Time Source",
            IrigTimeSource.values(), new NamedItemDescriptor<>() );
        this.reservedField = new IntegerFormField( "Reserved", 0,
            ( int )Time1Word.RESERVED.getMax() );
        this.timeView = new Time1View();

        this.view = createView();

        this.setEditable( false );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( createForm(), BorderLayout.NORTH );
        panel.add( timeView.getView(), BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createForm()
    {
        StandardFormView form = new StandardFormView();

        form.addField( sourceField );
        form.addField( formatField );
        form.addField( irigSourceField );
        form.addField( reservedField );

        return form.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Time1Body getData()
    {
        return this.body;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( Time1Body data )
    {
        this.body = data;

        sourceField.setValue( body.source.getValue() );
        formatField.setValue( body.format );
        irigSourceField.setValue( body.irigSource );
        reservedField.setValue( body.reserved );
        timeView.setData( body.time );
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
    public void setEditable( boolean editable )
    {
        sourceField.setEditable( editable );
        formatField.setEditable( editable );
        irigSourceField.setEditable( editable );
        reservedField.setEditable( editable );
        timeView.setEditable( editable );
    }
}
