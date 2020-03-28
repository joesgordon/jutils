package org.jutils.chart.ui;

import java.awt.Component;

import org.jutils.chart.data.QuadSide;
import org.jutils.chart.model.Legend;
import org.jutils.core.ui.StandardFormView;
import org.jutils.core.ui.TitleView;
import org.jutils.core.ui.fields.*;
import org.jutils.core.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class LegendPropertiesView implements IDataView<Legend>
{
    /**  */
    private final StandardFormView form;

    /**  */
    private final BooleanFormField visibleField;
    /**  */
    private final BorderPropertiesView borderField;
    /**  */
    private final ComboFormField<QuadSide> sideField;
    /**  */
    private final ColorField colorField;

    /**  */
    private Legend legend;

    /***************************************************************************
     * 
     **************************************************************************/
    public LegendPropertiesView()
    {
        this.visibleField = new BooleanFormField( "Visible" );
        this.borderField = new BorderPropertiesView();
        this.sideField = new ComboFormField<>( "Side", QuadSide.values() );
        this.colorField = new ColorField( "Color" );

        this.form = createView();

        setData( new Legend() );

        visibleField.setUpdater( ( b ) -> legend.visible = b );
        sideField.setUpdater( ( t ) -> legend.side = t );
        colorField.setUpdater( ( c ) -> legend.fill = c );

    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private StandardFormView createView()
    {
        StandardFormView form = new StandardFormView();
        TitleView bordervView = new TitleView( "Border",
            borderField.getView() );

        form.addField( visibleField );
        form.addComponent( bordervView.getView() );
        form.addField( sideField );
        form.addField( colorField );

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
    public Legend getData()
    {
        return legend;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( Legend legend )
    {
        this.legend = legend;

        visibleField.setValue( legend.visible );
        borderField.setData( legend.border );
        sideField.setValue( legend.side );
        colorField.setValue( legend.fill );
    }
}
