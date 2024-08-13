package jutils.telemetry.ui.ch10;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import jutils.core.io.FieldPrinter;
import jutils.core.ui.fields.StringAreaFormField;
import jutils.core.ui.model.IDataView;
import jutils.telemetry.data.ch09.Tmats;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TmatsView implements IDataView<Tmats>
{
    /**  */
    private final JPanel view;
    /**  */
    private final StringAreaFormField tmatsField;

    /**  */
    private Tmats tmats;

    /***************************************************************************
     * 
     **************************************************************************/
    public TmatsView()
    {
        this.tmatsField = new StringAreaFormField( "TMATS" );
        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );
        JTextArea area = tmatsField.getTextArea();
        JScrollPane pane = new JScrollPane( area );

        area.setFont( area.getFont().deriveFont( 14.f ) );

        pane.getVerticalScrollBar().setUnitIncrement( 12 );

        panel.add( pane, BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Tmats getData()
    {
        return this.tmats;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( Tmats data )
    {
        this.tmats = data;

        tmatsField.setValue( FieldPrinter.toString( tmats ) );
    }
}
