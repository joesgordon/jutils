package jutils.multicon.ui;

import java.awt.*;
import java.io.IOException;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import jutils.core.OptionUtils;
import jutils.core.ui.ItemsListView;
import jutils.core.ui.ListView.SelectionMode;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.event.updater.UpdaterList;
import jutils.core.ui.model.IView;
import jutils.core.ui.model.ItemsListModel;
import jutils.multicon.data.LinkType;
import jutils.multicon.links.ILink;
import jutils.multicon.ui.links.*;

/*******************************************************************************
 * Defines the a list view that displays {@link ILink}s.
 ******************************************************************************/
public class MultiConnectionList implements IView<JComponent>
{
    /**  */
    private final JScrollPane scroll;
    /**  */
    private final ItemsListView<ILinkView> list;
    /**  */
    private final UpdaterList<ILinkView> selectedListeners;

    /***************************************************************************
     * 
     **************************************************************************/
    public MultiConnectionList()
    {
        this.scroll = new JScrollPane();
        this.list = new ItemsListView<>( new MultiConnectionItemsListModel(),
            new MultiConnectionRenderer() );
        this.selectedListeners = new UpdaterList<>();

        list.addItemSelectedListener( ( d ) -> handleItemSelected( d ) );
        list.setSelectionMode( SelectionMode.SINGLE_ITEM );
        scroll.setViewportView( list.getView() );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return scroll;
    }

    /***************************************************************************
     * @param type
     **************************************************************************/
    public void addConnection( LinkType type )
    {
        ILinkView view = createView( type );

        // link.add

        list.addItem( view );
    }

    /***************************************************************************
     * @param onSelected
     **************************************************************************/
    public void addItemSelectedListener( IUpdater<ILinkView> onSelected )
    {
        selectedListeners.add( onSelected );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean bind()
    {
        ILinkView view = list.getSelectedItem();

        if( view != null )
        {
            try
            {
                view.bind();
                return true;
            }
            catch( IOException ex )
            {
                OptionUtils.showErrorMessage( getView(), ex.getMessage(),
                    "Bind Error" );
            }
        }

        return false;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean unbind()
    {
        ILinkView view = list.getSelectedItem();

        if( view != null )
        {
            try
            {
                view.unbind();
                return true;
            }
            catch( IOException ex )
            {
                OptionUtils.showErrorMessage( getView(), ex.getMessage(),
                    "Bind Error" );
            }
        }

        return false;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void closeAll()
    {
        List<ILinkView> linkViews = list.getItems();

        for( ILinkView view : linkViews )
        {
            try
            {
                view.unbind();
            }
            catch( IOException ex )
            {
                // TODO Trap exceptions for now. Fix later.
                ex.printStackTrace();
            }
        }
    }

    /***************************************************************************
     * @param link
     **************************************************************************/
    private void handleItemSelected( ILinkView link )
    {
        selectedListeners.fire( link );
    }

    /***************************************************************************
     * @param type
     * @return
     **************************************************************************/
    private ILinkView createView( LinkType type )
    {
        switch( type )
        {
            case BRIDGE:
                return new BridgeLinkView();

            case NTP:
                return new NtpLinkView();

            case SERIAL:
                return new SerialLinkView();

            case TCP_CONNECT:
                return new TcpLinkView();

            case TCP_SERVER:
                return new TcpServerLinkView();

            case TRANSFER:
                return new TransferLinkView();

            case UDP:
                return new UdpLinkView();
        }

        throw new IllegalArgumentException(
            "Unknown link type: " + type.getDescription() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class MultiConnectionItemsListModel
        implements ItemsListModel<ILinkView>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getName( ILinkView mc )
        {
            return mc.getLink().getDescription();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getTooltip( ILinkView mc )
        {
            return mc.getLink().getType().getDescription();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Icon getIcon( ILinkView mc )
        {
            // TODO Auto-generated method stub
            return null;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class MultiConnectionRenderer
        implements ListCellRenderer<ILinkView>
    {
        /**  */
        private static final Border DEFAULT_NO_FOCUS_BORDER = new EmptyBorder(
            1, 1, 1, 1 );

        /**  */
        private final JPanel panel;
        /**  */
        private final JLabel nameLabel;
        /**  */
        private final JLabel msgsLabel;
        /**  */
        private final JLabel bytesLabel;

        /**
         * 
         */
        public MultiConnectionRenderer()
        {
            this.panel = new JPanel( new GridBagLayout() );
            this.nameLabel = new JLabel();
            this.msgsLabel = new JLabel();
            this.bytesLabel = new JLabel();

            nameLabel.setFont( nameLabel.getFont().deriveFont( 14f ) );
            msgsLabel.setFont( msgsLabel.getFont().deriveFont( Font.ITALIC ) );
            bytesLabel.setFont( msgsLabel.getFont() );

            GridBagConstraints constraints;
            int r = 0;

            constraints = new GridBagConstraints( 0, r++, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 0, 4, 0, 4 ), 0, 0 );
            panel.add( nameLabel, constraints );

            constraints = new GridBagConstraints( 0, r++, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 0, 4, 0, 4 ), 0, 0 );
            panel.add( msgsLabel, constraints );

            constraints = new GridBagConstraints( 0, r++, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 0, 4, 0, 4 ), 0, 0 );
            panel.add( bytesLabel, constraints );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Component getListCellRendererComponent(
            JList<? extends ILinkView> list, ILinkView value, int index,
            boolean isSelected, boolean cellHasFocus )
        {
            setValue( value );
            setBackground( list, isSelected );
            setBorder( isSelected, cellHasFocus );

            return panel;
        }

        /**
         * @param view
         */
        private void setValue( ILinkView view )
        {
            ILink link = view.getLink();
            int connectedStyle = link.isConnected() ? Font.PLAIN : Font.ITALIC;
            // TODO add unread style
            // int unreadStyle = connection.isUnread() ? Font.BOLD : Font.PLAIN;
            int fStyle = connectedStyle; // | unreadStyle;

            nameLabel.setFont( nameLabel.getFont().deriveFont( fStyle ) );
            msgsLabel.setFont( msgsLabel.getFont().deriveFont( fStyle ) );
            bytesLabel.setFont( bytesLabel.getFont() );

            String nameTxt = String.format( "%s %s", link.getType().name,
                link.getDescription() );
            String msgsTxt = String.format( "%d / %d msgs tx/rx",
                link.getTxMsgCount(), link.getRxMsgCount() );
            String bytesTxt = String.format( "%d / %d bytes tx/rx",
                link.getTxByteCount(), link.getRxByteCount() );

            nameLabel.setText( nameTxt );
            msgsLabel.setText( msgsTxt );
            bytesLabel.setText( bytesTxt );
        }

        /**
         * @param list
         * @param isSelected
         */
        private void setBackground( JList<? extends ILinkView> list,
            boolean isSelected )
        {
            Color bg = list.getBackground();
            Color fg = list.getForeground();

            if( isSelected )
            {
                bg = list.getSelectionBackground();
                fg = list.getSelectionForeground();
            }

            panel.setBackground( bg );
            nameLabel.setForeground( fg );
            msgsLabel.setForeground( fg );
            bytesLabel.setForeground( fg );
        }

        /**
         * @param isSelected
         * @param cellHasFocus
         */
        private void setBorder( boolean isSelected, boolean cellHasFocus )
        {
            Border border = null;
            if( cellHasFocus )
            {
                if( isSelected )
                {
                    border = UIManager.getBorder(
                        "List.focusSelectedCellHighlightBorder" );
                }
                if( border == null )
                {
                    border = UIManager.getBorder(
                        "List.focusCellHighlightBorder" );
                }
            }
            else
            {
                border = DEFAULT_NO_FOCUS_BORDER;
            }

            panel.setBorder( border );
        }
    }
}
