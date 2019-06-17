package org.jutils.chart.ui;

import java.awt.Component;

import org.jutils.chart.model.Axis;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.fields.BooleanFormField;
import org.jutils.ui.fields.DoubleFormField;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class AxisPropertiesView implements IDataView<Axis>
{
    /**  */
    private final StandardFormView form;

    /**  */
    private final TextLabelField titleField;
    /**  */
    private final TextLabelField subtitleField;
    /**  */
    private final BooleanFormField autoTicksField;
    /**  */
    private final DoubleFormField tickStartField;
    /**  */
    private final DoubleFormField tickEndField;
    /**  */
    private final DoubleFormField tickWidthField;
    /**  */
    private final BooleanFormField dockZeroField;

    /**  */
    private Axis axis;

    /***************************************************************************
     * 
     **************************************************************************/
    public AxisPropertiesView()
    {
        this.titleField = new TextLabelField( "Title" );
        this.subtitleField = new TextLabelField( "Subtitle" );
        this.autoTicksField = new BooleanFormField( "Auto Ticks" );
        this.tickStartField = new DoubleFormField( "Tick Start" );
        this.tickEndField = new DoubleFormField( "Tick End" );
        this.tickWidthField = new DoubleFormField( "Tick Width" );
        this.dockZeroField = new BooleanFormField( "Dock Zero" );

        this.form = createView();

        setData( new Axis() );

        autoTicksField.setUpdater( ( d ) -> {
            setAutoTicksEnabled( d );
            axis.autoTicks = d;
        } );
        tickStartField.setUpdater( ( d ) -> axis.tickStart = d );
        tickEndField.setUpdater( ( d ) -> axis.tickEnd = d );
        tickWidthField.setUpdater( ( d ) -> axis.tickWidth = d );
        dockZeroField.setUpdater( ( d ) -> axis.dockZero = d );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private StandardFormView createView()
    {
        StandardFormView form = new StandardFormView();

        form.addField( titleField );
        form.addField( subtitleField );
        form.addField( autoTicksField );
        form.addField( tickStartField );
        form.addField( tickEndField );
        form.addField( tickWidthField );
        form.addField( dockZeroField );

        return form;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getView()
    {
        return form.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Axis getData()
    {
        return axis;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( Axis data )
    {
        this.axis = data;

        titleField.setValue( data.title );
        subtitleField.setValue( data.subtitle );
        autoTicksField.setValue( data.autoTicks );
        tickStartField.setValue( data.tickStart );
        tickEndField.setValue( data.tickEnd );
        tickWidthField.setValue( data.tickWidth );
        dockZeroField.setValue( data.dockZero );

        setAutoTicksEnabled( data.autoTicks );
    }

    /***************************************************************************
     * @param autoEnabled
     **************************************************************************/
    public void setAutoTicksEnabled( Boolean autoEnabled )
    {
        tickStartField.setEditable( !autoEnabled );
        tickEndField.setEditable( !autoEnabled );
        tickWidthField.setEditable( !autoEnabled );
        dockZeroField.setEditable( autoEnabled );
        dockZeroField.setValue( autoEnabled && dockZeroField.getValue() );
    }
}
