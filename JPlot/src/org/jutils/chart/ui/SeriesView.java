package org.jutils.chart.ui;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.jutils.chart.data.DefaultSeries;
import org.jutils.chart.data.XYPoint;
import org.jutils.chart.model.Series;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.TitleView;
import org.jutils.ui.fields.BooleanFormField;
import org.jutils.ui.fields.StringFormField;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SeriesView implements IDataView<Series>
{
    /**  */
    private final JPanel view;
    /**  */
    private final StringFormField titleField;
    /**  */
    private final BooleanFormField visibleField;
    /**  */
    private final BooleanFormField primaryDomainField;
    /**  */
    private final BooleanFormField primaryRangeField;
    /**  */
    private final MarkerStyleView markerView;
    /**  */
    private final MarkerStyleView highlightView;
    /**  */
    private final LineStyleView lineView;

    /**  */
    private Series series;

    /***************************************************************************
     * 
     **************************************************************************/
    public SeriesView()
    {
        this.titleField = new StringFormField( "Title" );
        this.visibleField = new BooleanFormField( "Visible" );
        this.primaryDomainField = new BooleanFormField( "Is Primary Domain" );
        this.primaryRangeField = new BooleanFormField( "Is Primary Range" );
        this.markerView = new MarkerStyleView();
        this.highlightView = new MarkerStyleView();
        this.lineView = new LineStyleView();

        this.view = createView();

        setData( new Series( new DefaultSeries( new ArrayList<XYPoint>() ) ) );

        titleField.setUpdater( ( s ) -> series.name = s );
        visibleField.setUpdater( ( b ) -> series.visible = b );
        primaryDomainField.setUpdater( ( b ) -> series.isPrimaryDomain = b );
        primaryRangeField.setUpdater( ( b ) -> series.isPrimaryRange = b );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        StandardFormView form = new StandardFormView();

        form.addField( titleField );
        form.addField( visibleField );
        form.addField( primaryDomainField );
        form.addField( primaryRangeField );
        form.addComponent(
            new TitleView( "Marker", markerView.getView() ).getView() );
        form.addComponent(
            new TitleView( "Highlight", highlightView.getView() ).getView() );
        form.addComponent(
            new TitleView( "Line", lineView.getView() ).getView() );

        return form.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Series getData()
    {
        return series;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( Series data )
    {
        this.series = data;

        titleField.setValue( data.name );
        visibleField.setValue( data.visible );
        primaryDomainField.setValue( data.isPrimaryDomain );
        primaryRangeField.setValue( data.isPrimaryRange );
        markerView.setData( data.marker );
        highlightView.setData( data.highlight );
        lineView.setData( data.line );
    }
}
