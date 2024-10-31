package jutils.telemetry.ui.ch10;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;

import jutils.core.io.FieldPrinter;
import jutils.core.ui.StringView;
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
    private final StringView tmatsField;

    /**  */
    private Tmats tmats;

    /***************************************************************************
     * 
     **************************************************************************/
    public TmatsView()
    {
        this.tmatsField = new StringView();
        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( tmatsField.getView(), BorderLayout.CENTER );

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

        tmatsField.setData( FieldPrinter.toString( tmats ) );
    }
}
