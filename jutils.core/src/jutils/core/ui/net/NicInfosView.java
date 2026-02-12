package jutils.core.ui.net;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import jutils.core.IconConstants;
import jutils.core.SwingUtils;
import jutils.core.io.FieldPrinter;
import jutils.core.net.NetUtils;
import jutils.core.net.NicInfo;
import jutils.core.ui.IToolView;
import jutils.core.ui.ItemListView;
import jutils.core.ui.ListView;
import jutils.core.ui.ListView.IItemListModel;
import jutils.core.ui.StandardFrameView;
import jutils.core.ui.StringView;
import jutils.core.ui.app.AppRunner;
import jutils.core.ui.event.ActionAdapter;
import jutils.core.ui.model.IDataView;
import jutils.core.ui.model.IView;

/*******************************************************************************
 *
 ******************************************************************************/
public class NicInfosView implements IView<JComponent>
{
    /**  */
    private final JComponent view;
    /**  */
    private final ItemListView<NicInfo> nicsView;

    /***************************************************************************
     * 
     **************************************************************************/
    public NicInfosView()
    {
        this.nicsView = new ItemListView<>( new NicInfoView(),
            new NicInfoModel(), false, false );
        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( createToolbar(), BorderLayout.NORTH );
        panel.add( nicsView.getView(), BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createToolbar()
    {
        JToolBar toolbar = new JToolBar();

        SwingUtils.setToolbarDefaults( toolbar );

        SwingUtils.addActionToToolbar( toolbar, createRefreshAction() );

        return toolbar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createRefreshAction()
    {
        ActionListener listener = ( e ) -> handleRefresh();
        Icon icon = IconConstants.getIcon( IconConstants.NIC_24 );

        return new ActionAdapter( listener, "Refresh", icon );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void handleRefresh()
    {
        List<NicInfo> nics = NetUtils.listNics();

        nicsView.setData( nics );
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
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.DEFAULT_LAF = AppRunner.JGOODIES_LAF;

        AppRunner.invokeLater( () -> createFrame() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static JFrame createFrame()
    {
        NicsTool tool = new NicsTool();
        StandardFrameView frameView = new StandardFrameView();
        JFrame frame = frameView.getView();

        frameView.setTitle( "NICs" );
        frameView.setSize( 500, 500 );
        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frameView.setContent( tool.getView() );

        frame.setIconImages( tool.getImages() );

        return frame;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class NicInfoModel implements IItemListModel<NicInfo>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getTitle( NicInfo item )
        {
            return item.name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public NicInfo promptForNew( ListView<NicInfo> view )
        {
            return null;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class NicInfoView implements IDataView<NicInfo>
    {
        /**  */
        private final StringView view;

        /**  */
        private NicInfo nic;

        /**
         * 
         */
        public NicInfoView()
        {
            this.view = new StringView();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Component getView()
        {
            return view.getView();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public NicInfo getData()
        {
            return this.nic;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setData( NicInfo data )
        {
            this.nic = data;

            String text = FieldPrinter.toString( data );

            view.setData( text );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class NicsTool implements IToolView
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public Container getView()
        {
            return new NicInfosView().view;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getDescription()
        {
            return "Displays informations about adapters on this system";
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getName()
        {
            return "NICs";
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public List<Image> getImages()
        {
            return IconConstants.getAllImages( "nic" );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Icon getIcon24()
        {
            return IconConstants.getIcon( IconConstants.NIC_24 );
        }
    }
}
