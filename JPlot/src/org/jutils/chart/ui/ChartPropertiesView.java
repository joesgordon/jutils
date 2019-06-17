package org.jutils.chart.ui;

import java.awt.Component;

import org.jutils.chart.model.Chart;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.fields.BooleanFormField;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChartPropertiesView implements IDataView<Chart>
{
    /**  */
    private final StandardFormView form;
    /**  */
    private final TextLabelField titleField;
    /**  */
    private final TextLabelField subtitleField;
    /**  */
    private final TextLabelField topBottomField;
    /**  */
    private final BooleanFormField gridlinesVisibleField;
    /**  */
    private final BooleanFormField antiAliasField;
    /**  */
    private final BooleanFormField textAntiAliasField;

    /**  */
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

        this.form = createView();

        setData( new Chart() );

        gridlinesVisibleField.setUpdater(
            ( b ) -> chart.options.gridlinesVisible = b );
        antiAliasField.setUpdater( ( b ) -> chart.options.antialias = b );
        textAntiAliasField.setUpdater(
            ( b ) -> chart.options.textAntiAlias = b );

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
    }
}
