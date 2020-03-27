package org.jutils.chart.ui;

import java.awt.Component;

import org.jutils.chart.model.Chart;
import org.jutils.chart.model.ChartOptions.PointRemovalMethod;
import org.jutils.core.ui.StandardFormView;
import org.jutils.core.ui.fields.BooleanFormField;
import org.jutils.core.ui.fields.ComboFormField;
import org.jutils.core.ui.fields.NamedItemDescriptor;
import org.jutils.core.ui.model.IDataView;

/*******************************************************************************
 * Defines the set of components that edit a {@link Chart}'s properties.
 ******************************************************************************/
public class ChartPropertiesView implements IDataView<Chart>
{
    /** The form that contains all the fields to edit properties. */
    private final StandardFormView form;
    /** The field that edits the chart's title. */
    private final TextLabelField titleField;
    /** The field that edits the chart's subtitle. */
    private final TextLabelField subtitleField;
    /** The field that edits the chart's top/bottom text. */
    private final TextLabelField topBottomField;
    /** The field enables/disables the chart's major grid lines. */
    private final BooleanFormField gridlinesVisibleField;
    /** The field enables/disables the shape anti-aliasing for the chart. */
    private final BooleanFormField antiAliasField;
    /** The field enables/disables the text anti-aliasing for the chart. */
    private final BooleanFormField textAntiAliasField;
    /** The field edits the chart's point removal method. */
    private final ComboFormField<PointRemovalMethod> removalMethodField;

    /** The chart being edited. */
    private Chart chart;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChartPropertiesView()
    {
        this.titleField = new TextLabelField( "Title" );
        this.subtitleField = new TextLabelField( "Subtitle" );
        this.topBottomField = new TextLabelField( "Top/Bottom" );
        this.gridlinesVisibleField = new BooleanFormField(
            "Gridlines Visible" );
        this.antiAliasField = new BooleanFormField( "Anti-Alias" );
        this.textAntiAliasField = new BooleanFormField( "Text Anti-Alias" );
        this.removalMethodField = new ComboFormField<>( "Point Removal",
            PointRemovalMethod.values(), new NamedItemDescriptor<>() );

        this.form = createView();

        setData( new Chart() );

        gridlinesVisibleField.setUpdater(
            ( d ) -> chart.options.gridlinesVisible = d );
        antiAliasField.setUpdater( ( d ) -> chart.options.antialias = d );
        textAntiAliasField.setUpdater(
            ( d ) -> chart.options.textAntiAlias = d );
        removalMethodField.setUpdater(
            ( d ) -> chart.options.removalMethod = d );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private StandardFormView createView()
    {
        StandardFormView form = new StandardFormView();

        form.addField( titleField );
        form.addField( subtitleField );
        form.addField( topBottomField );
        form.addField( gridlinesVisibleField );
        form.addField( antiAliasField );
        form.addField( textAntiAliasField );
        form.addField( removalMethodField );

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
    public Chart getData()
    {
        return chart;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( Chart data )
    {
        this.chart = data;

        titleField.setValue( data.title );
        subtitleField.setValue( data.subtitle );
        topBottomField.setValue( data.topBottomLabel );
        gridlinesVisibleField.setValue( data.options.gridlinesVisible );
        antiAliasField.setValue( data.options.antialias );
        textAntiAliasField.setValue( data.options.textAntiAlias );
        removalMethodField.setValue( data.options.removalMethod );
    }
}
