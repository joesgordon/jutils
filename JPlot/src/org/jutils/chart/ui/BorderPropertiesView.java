package org.jutils.chart.ui;

import java.awt.Component;

import org.jutils.chart.model.WidgetBorder;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.fields.*;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BorderPropertiesView implements IDataView<WidgetBorder>
{
    /**  */
    private final StandardFormView form;

    /**  */
    private final BooleanFormField visibleField;
    /**  */
    private final ColorField colorField;
    /**  */
    private final IntegerFormField thicknessField;

    /**  */
    private WidgetBorder border;

    /***************************************************************************
     * 
     **************************************************************************/
    public BorderPropertiesView()
    {
        this.visibleField = new BooleanFormField( "Visible" );
        this.colorField = new ColorField( "Color" );
        this.thicknessField = new IntegerFormField( "Thickness" );

        this.form = createView();

        setData( new WidgetBorder() );

        visibleField.setUpdater( ( b ) -> border.visible = b );
        colorField.setUpdater( ( c ) -> border.color = c );
        thicknessField.setUpdater( ( i ) -> border.thickness = i );

    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private StandardFormView createView()
    {
        StandardFormView form = new StandardFormView();

        form.addField( visibleField );
        form.addField( colorField );
        form.addField( thicknessField );

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
    public WidgetBorder getData()
    {
        return border;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( WidgetBorder border )
    {
        this.border = border;

        visibleField.setValue( border.visible );
        colorField.setValue( border.color );
        thicknessField.setValue( border.thickness );
    }
}
