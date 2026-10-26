package jutils.multicon.ui;

import java.awt.*;

import javax.swing.JComponent;
import javax.swing.JPanel;

import jutils.core.ui.ComponentView;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.event.updater.UpdaterList;
import jutils.core.ui.model.IView;
import jutils.multicon.data.LinkType;

/*******************************************************************************
 * Defines the main view for Multicon.
 ******************************************************************************/
public class MulticonView implements IView<JComponent>
{
    /**  */
    private final JPanel view;
    /**  */
    private final MultiConnectionList list;
    /**  */
    private final ComponentView connectionView;
    /**  */
    private final UpdaterList<ILinkView> selectedListeners;

    /***************************************************************************
     * 
     **************************************************************************/
    public MulticonView()
    {
        this.list = new MultiConnectionList();
        this.connectionView = new ComponentView();
        this.selectedListeners = new UpdaterList<>();
        this.view = createView();

        list.getView().setMinimumSize( new Dimension( 200, 200 ) );
        list.getView().setPreferredSize( new Dimension( 200, 200 ) );
        list.addItemSelectedListener( ( d ) -> handleLinkSelected( d ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 6, 6, 6, 6 ), 0, 0 );
        panel.add( list.getView(), constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 6, 0, 6, 6 ), 0, 0 );
        panel.add( connectionView.getView(), constraints );

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
     * @param onSelected
     **************************************************************************/
    public void addItemSelectedListener( IUpdater<ILinkView> onSelected )
    {
        selectedListeners.add( onSelected );
    }

    /***************************************************************************
     * @param type
     **************************************************************************/
    public void addConnection( LinkType type )
    {
        list.addConnection( type );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void closeAll()
    {
        list.closeAll();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean bind()
    {
        return list.bind();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean unbind()
    {
        return list.unbind();
    }

    /***************************************************************************
     * @param view
     **************************************************************************/
    private void handleLinkSelected( ILinkView view )
    {
        if( view != null )
        {
            connectionView.setComponent( view.getView() );
        }

        selectedListeners.fire( view );
    }
}
