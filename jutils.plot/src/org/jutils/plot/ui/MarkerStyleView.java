package org.jutils.plot.ui;

import java.awt.Component;

import javax.swing.JPanel;

import org.jutils.core.ui.StandardFormView;
import org.jutils.core.ui.fields.*;
import org.jutils.core.ui.model.IDataView;
import org.jutils.plot.model.MarkerStyle;
import org.jutils.plot.model.MarkerType;

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
