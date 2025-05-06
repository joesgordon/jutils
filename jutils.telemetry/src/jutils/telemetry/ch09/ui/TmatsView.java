package jutils.telemetry.ch09.ui;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JPanel;

import jutils.core.ui.model.IDataView;
import jutils.telemetry.ch09.Tmats;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TmatsView implements IDataView<Tmats>
{
    /**  */
    private final JComponent view;

    /**  */
    private Tmats tmats;

    /***************************************************************************
     * 
     **************************************************************************/
    public TmatsView()
    {
        this.view = createView();

        setData( new Tmats() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JPanel panel = new JPanel();
        // TODO Auto-generated method stub
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
        return tmats;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( Tmats data )
    {
        this.tmats = data;

        // TODO Auto-generated method stub
    }
}
