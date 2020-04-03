package org.jutils.plot.ui;

import java.awt.Component;

import org.jutils.core.ui.StandardFormView;
import org.jutils.core.ui.fields.DoubleFormField;
import org.jutils.core.ui.fields.UsableFormField;
import org.jutils.core.ui.model.IDataView;
import org.jutils.plot.model.Axis;

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
    private final UsableFormField<Double> tickStartField;
    /**  */
    private final UsableFormField<Double> tickEndField;
    /**  */
    private final UsableFormField<Double> tickIntervalField;

    /**  */
    private Axis axis;

    /***************************************************************************
     * 
     **************************************************************************/
    public AxisPropertiesView()
    {
        this.titleField = new TextLabelField( "Title" );
        this.subtitleField = new TextLabelField( "Subtitle" );
        this.tickStartField = new UsableFormField<>(
            new DoubleFormField( "Tick Start" ) );
        this.tickEndField = new UsableFormField<>(
            new DoubleFormField( "Tick End" ) );
        this.tickIntervalField = new UsableFormField<>(
            new DoubleFormField( "Tick Width" ) );

        this.form = createView();

        setData( new Axis() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private StandardFormView createView()
    {
        StandardFormView form = new StandardFormView();

        form.addField( titleField );
        form.addField( subtitleField );
        form.addField( tickStartField );
        form.addField( tickEndField );
        form.addField( tickIntervalField );

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
        tickStartField.setValue( data.tickStart );
        tickEndField.setValue( data.tickEnd );
        tickIntervalField.setValue( data.tickInterval );
    }
}
