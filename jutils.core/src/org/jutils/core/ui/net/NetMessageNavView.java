package org.jutils.core.ui.net;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.jutils.core.IconConstants;
import org.jutils.core.SwingUtils;
import org.jutils.core.net.NetMessage;
import org.jutils.core.ui.event.ActionAdapter;
import org.jutils.core.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NetMessageNavView implements IDataView<NetMessage>
{
    /**  */
    private final NetMessagesView msgsView;

    /**  */
    private final JPanel view;
    /**  */
    private final NetMessageView msgView;

    /**  */
    private final JButton prevButton;
    /**  */
    private final JButton nextButton;

    /***************************************************************************
     * @param msgsView
     * @param msgView
     * @param addScrollPane
     **************************************************************************/
    public NetMessageNavView( NetMessagesView msgsView,
        IDataView<NetMessage> msgView, boolean addScrollPane )
    {
        this.msgsView = msgsView;
        this.msgView = new NetMessageView( msgView, addScrollPane );
        this.prevButton = new JButton();
        this.nextButton = new JButton();

        this.view = createView();

        setButtonsEnabled();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( createToolbar(), BorderLayout.NORTH );
        panel.add( msgView.getView(), BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JToolBar();

        SwingUtils.setToolbarDefaults( toolbar );

        SwingUtils.addActionToToolbar( toolbar, createNavAction( false ),
            prevButton );

        SwingUtils.addActionToToolbar( toolbar, createNavAction( true ),
            nextButton );

        return toolbar;
    }

    /***************************************************************************
     * @param forward
     * @return
     **************************************************************************/
    private Action createNavAction( boolean forward )
    {
        ActionListener listener = ( e ) -> navigate( forward );
        Icon icon = IconConstants.getIcon( forward ? IconConstants.NAV_NEXT_16
            : IconConstants.NAV_PREVIOUS_16 );
        String name = forward ? "Next Message" : "Previous Message";

        return new ActionAdapter( listener, name, icon );
    }

    /***************************************************************************
     * @param forward
     **************************************************************************/
    private void navigate( boolean forward )
    {
        int inc = forward ? 1 : -1;
        long index = msgsView.getSelectedIndex();
        long nextIndex = index + inc;

        msgsView.showMessage( nextIndex );

        setButtonsEnabled();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void setButtonsEnabled()
    {
        long index = msgsView.getSelectedIndex();
        long maxRow = msgsView.getMessageCount() - 1;

        prevButton.setEnabled( index > 0 );
        nextButton.setEnabled( index > -1 && index < maxRow );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public NetMessage getData()
    {
        return msgView.getData();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( NetMessage data )
    {
        msgView.setData( data );

        setButtonsEnabled();
    }
}
