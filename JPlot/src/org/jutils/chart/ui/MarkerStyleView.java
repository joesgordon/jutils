package org.jutils.chart.ui;

import java.awt.Component;

import javax.swing.JPanel;

import org.jutils.chart.model.MarkerStyle;
import org.jutils.chart.model.MarkerType;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.fields.*;
import org.jutils.ui.model.IDataView;

public class MarkerStyleView implements IDataView<MarkerStyle>
{
    /**  */
    private final JPanel view;
    /**  */
    private final BooleanFormField visibleField;
    /**  */
    private final ComboFormField<MarkerType> shapeField;
    /**  */
    private final IntegerFormField weightField;
    /**  */
    private final ColorField colorField;

    /**  */
    private MarkerStyle marker;

    /***************************************************************************
     * 
     **************************************************************************/
    public MarkerStyleView()
    {
        this.visibleField = new BooleanFormField( "Visible" );
        this.shapeField = new ComboFormField<>( "Shape", MarkerType.values() );
        this.weightField = new IntegerFormField( "Size", 1, 20 );
        this.colorField = new ColorField( "Color" );

        this.view = createView();

        setData( new MarkerStyle() );

        visibleField.setUpdater( ( b ) -> marker.visible = b );
        shapeField.setUpdater( ( t ) -> marker.type = t );
        weightField.setUpdater( ( i ) -> marker.weight = i );
        colorField.setUpdater( ( c ) -> marker.color = c );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        StandardFormView form = new StandardFormView();

        form.addField( visibleField );
        form.addField( shapeField );
        form.addField( weightField );
        form.addField( colorField );

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
    public MarkerStyle getData()
    {
        return marker;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( MarkerStyle data )
    {
        this.marker = data;

        visibleField.setValue( data.visible );
        shapeField.setValue( data.type );
        weightField.setValue( data.weight );
        colorField.setValue( data.color );
    }
}
