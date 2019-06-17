package org.jutils.chart.ui;

import java.awt.Component;

import javax.swing.JPanel;

import org.jutils.chart.model.LineStyle;
import org.jutils.chart.model.LineType;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.fields.*;
import org.jutils.ui.model.IDataView;

/***************************************************************************
 * 
 **************************************************************************/
public class LineStyleView implements IDataView<LineStyle>
{
    /**  */
    private final JPanel view;
    /**  */
    private final BooleanFormField visibleField;
    /**  */
    private final ComboFormField<LineType> shapeField;
    /**  */
    private final IntegerFormField weightField;
    /**  */
    private final ColorField colorField;

    /**  */
    private LineStyle line;

    /***************************************************************************
     * 
     **************************************************************************/
    public LineStyleView()
    {
        this.visibleField = new BooleanFormField( "Visible" );
        this.shapeField = new ComboFormField<>( "Shape", LineType.values() );
        this.weightField = new IntegerFormField( "Weight", 1, 10 );
        this.colorField = new ColorField( "Color" );

        this.view = createView();

        setData( new LineStyle() );

        visibleField.setUpdater( ( b ) -> line.visible = b );
        shapeField.setUpdater( ( t ) -> line.type = t );
        weightField.setUpdater( ( i ) -> line.weight = i );
        colorField.setUpdater( ( c ) -> line.color = c );
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
    public LineStyle getData()
    {
        return line;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( LineStyle data )
    {
        this.line = data;

        visibleField.setValue( data.visible );
        shapeField.setValue( data.type );
        weightField.setValue( data.weight );
        colorField.setValue( data.color );
    }
}
