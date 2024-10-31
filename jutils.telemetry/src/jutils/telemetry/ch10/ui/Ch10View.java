package jutils.telemetry.ch10.ui;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import jutils.core.ui.TitleView;
import jutils.core.ui.model.IDataView;
import jutils.telemetry.ch10.Ch10File;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Ch10View implements IDataView<Ch10File>
{
    /**  */
    private final JComponent view;
    /**  */
    private final PacketsView packetsView;

    /***************************************************************************
     * 
     **************************************************************************/
    public Ch10View()
    {
        this.packetsView = new PacketsView();

        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        TitleView pView = new TitleView( "Packets", packetsView.getView() );

        // TODO Auto-generated method stub
        panel.add( pView.getView(), BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Ch10File getData()
    {
        return packetsView.getData();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( Ch10File data )
    {
        packetsView.setData( data );
    }
}
