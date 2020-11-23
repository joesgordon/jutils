package org.jutils.multicon.ui;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.jutils.core.*;
import org.jutils.core.io.options.OptionsSerializer;
import org.jutils.core.io.xs.XsUtils;
import org.jutils.core.net.TcpInputs;
import org.jutils.core.net.UdpInputs;
import org.jutils.core.ui.StandardFrameView;
import org.jutils.core.ui.event.ActionAdapter;
import org.jutils.core.ui.event.WindowCloseListener;
import org.jutils.core.ui.event.updater.IUpdater;
import org.jutils.core.ui.model.IDataView;
import org.jutils.core.ui.model.IView;
import org.jutils.core.ui.net.TcpInputsView;
import org.jutils.core.ui.net.UdpInputsView;
import org.jutils.core.utils.IGetter;
import org.jutils.multicon.*;
import org.jutils.multicon.MulticonOptions.FavStorage;
import org.jutils.multicon.ui.BindingFrameView.IBindableView;
import org.jutils.multicon.ui.net.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MulticonFrame implements IView<JFrame>
{
    /**  */
    private final StandardFrameView frameView;
    /**  */
    private final List<IBindableView<?>> views;

    /***************************************************************************
     * 
     **************************************************************************/
    public MulticonFrame()
    {
        this.frameView = new StandardFrameView();
        this.views = new ArrayList<>();

        frameView.getView().setIconImages( MulticonIcons.getMulticonImages() );

        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frameView.setSize( 400, 400 );
        frameView.setTitle( "Multicon" );
        frameView.setContent( createContent() );

        frameView.getView().addWindowListener( new WindowCloseListener( () -> {
            for( IBindableView<?> view : views )
            {
                closeView( view );
            }
        } ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Container createContent()
    {
        JTabbedPane tabs = new JTabbedPane();
        IUpdater<IBindableView<?>> u = null;
        BindableFavView<?> favView;

        UdpInputsView udpView = new UdpInputsView();
        TcpInputsView tcpClientView = new TcpInputsView( false );
        TcpInputsView tcpServerView = new TcpInputsView( true );

        OptionsSerializer<MulticonOptions> userio = MulticonMain.getUserData();
        MulticonOptions options = userio.getOptions();

        udpView.setData( new UdpInputs( options.udpInputs ) );
        tcpClientView.setData( new TcpInputs( options.tcpClientInputs ) );
        tcpServerView.setData( new TcpInputs( options.tcpServerInputs ) );

        u = ( v ) -> {
            options.udpInputs = new UdpInputs( udpView.getData() );
            userio.write( options );
            showView( v );
        };
        favView = new BindableFavView<>( UdpView.NAME, udpView,
            () -> new ConnectionBindableView<>( new UdpView() ), u );
        tabs.addTab( favView.name, favView.getView() );

        u = ( v ) -> {
            options.tcpClientInputs = new TcpInputs( tcpClientView.getData() );
            userio.write( options );
            showView( v );
        };
        favView = new BindableFavView<>( TcpClientView.NAME, tcpClientView,
            () -> new ConnectionBindableView<>( new TcpClientView() ), u );
        tabs.addTab( favView.name, favView.getView() );

        u = ( v ) -> {
            options.tcpServerInputs = new TcpInputs( tcpServerView.getData() );
            userio.write( options );
            showView( v );
        };
        favView = new BindableFavView<>( TcpServerView.NAME, tcpServerView,
            () -> new TcpServerView(), u );
        tabs.addTab( favView.name, favView.getView() );

        return tabs;
    }

    /***************************************************************************
     * @param view
     **************************************************************************/
    private void showView( IBindableView<?> view )
    {
        views.add( view );

        showBindingFrame( view, getView() );
    }

    /***************************************************************************
     * @param view
     * @param parent
     * @return
     **************************************************************************/
    public static BindingFrameView showBindingFrame( IBindableView<?> view,
        Component parent )
    {
        Window window = SwingUtils.getComponentsWindow( parent );
        BindingFrameView frame = new BindingFrameView( view, parent );

        frame.getView().addWindowListener( new WindowCloseListener( () -> {
            try
            {
                view.unbind();
            }
            catch( IOException ex )
            {
                OptionUtils.showErrorMessage( parent, ex.getMessage(),
                    "Socket Close Error" );
            }
        } ) );

        frame.getView().setIconImages( window.getIconImages() );
        frame.getView().pack();
        frame.getView().setLocationRelativeTo( parent );
        frame.getView().setVisible( true );

        frame.bind();

        return frame;
    }

    /***************************************************************************
     * @param view
     **************************************************************************/
    private void closeView( IBindableView<?> view )
    {
        try
        {
            view.unbind();
        }
        catch( IOException ex )
        {
            OptionUtils.showErrorMessage( getView(), ex.getMessage(),
                "Socket Close Error" );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return frameView.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class BindableFavView<T> implements IView<JComponent>
    {
        /**  */
        public final String name;
        /**  */
        public final IDataView<T> inputsView;

        /**  */
        private final JComponent view;

        /**  */
        private String loadedName;

        /**
         * @param name
         * @param inputsView
         * @param getter
         * @param updater
         */
        public BindableFavView( String name, IDataView<T> inputsView,
            IGetter<IBindableView<T>> getter,
            IUpdater<IBindableView<?>> updater )
        {
            ActionListener bindListener = ( e ) -> bind( getter, updater );

            this.name = name;
            this.inputsView = inputsView;
            this.view = createView( bindListener );
            this.loadedName = null;

            OptionsSerializer<MulticonOptions> userio = MulticonMain.getUserData();
            MulticonOptions options = userio.getOptions();
            FavStorage<T> favs = options.getFavs( name );

            if( favs == null )
            {
                options.setFavs( name, new FavStorage<>() );
            }
        }

        /**
         * @param getter
         * @param updater
         */
        private void bind( IGetter<IBindableView<T>> getter,
            IUpdater<IBindableView<?>> updater )
        {
            IBindableView<T> view = getter.get();

            T data = XsUtils.cloneObject( inputsView.getData() );

            view.setData( data );

            updater.update( view );
        }

        /**
         * @param bindListener
         * @return
         */
        private JComponent createView( ActionListener bindListener )
        {
            JPanel panel = new JPanel( new GridBagLayout() );
            GridBagConstraints constraints;

            constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 );
            panel.add( createToolbar( bindListener ), constraints );

            constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 );
            panel.add( new JSeparator(), constraints );

            constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 );
            panel.add( inputsView.getView(), constraints );

            return panel;
        }

        /**
         * @param bindListener
         * @return
         */
        private Component createToolbar( ActionListener bindListener )
        {
            JToolBar toolbar = new JToolBar();

            SwingUtils.setToolbarDefaults( toolbar );

            ImageIcon bindIcon = MulticonIcons.getIcon(
                MulticonIcons.MULTICON_016 );

            SwingUtils.addActionToToolbar( toolbar,
                new ActionAdapter( bindListener, "Bind", bindIcon ) ).setText(
                    "Bind" );

            toolbar.addSeparator();

            SwingUtils.addActionToToolbar( toolbar, createLoadAction() );

            SwingUtils.addActionToToolbar( toolbar, createSaveAction() );

            return toolbar;
        }

        /**
         * @return
         */
        private Action createLoadAction()
        {
            ActionListener listener = ( e ) -> loadFav();
            Icon icon = IconConstants.getIcon( IconConstants.OPEN_FILE_16 );

            return new ActionAdapter( listener, "Load", icon );
        }

        /**
         * @return
         */
        private Action createSaveAction()
        {
            ActionListener listener = ( e ) -> saveFav();
            Icon icon = IconConstants.getIcon( IconConstants.SAVE_16 );

            return new ActionAdapter( listener, "Save", icon );
        }

        /**
         * 
         */
        private void loadFav()
        {
            OptionsSerializer<MulticonOptions> userio = MulticonMain.getUserData();
            MulticonOptions options = userio.getOptions();
            FavStorage<T> favs = options.getFavs( name );
            List<String> names = favs.getNames();

            if( !names.isEmpty() )
            {
                String[] choices = names.toArray( new String[names.size()] );

                String choice = OptionUtils.showComboDialog( getView(),
                    "Which favorite would you like to load?", "Choose Favorite",
                    "", choices, choices[0] );

                if( choice != null )
                {
                    inputsView.setData( favs.getFav( choice ) );
                    loadedName = choice;
                }
                // else user cancelled
            }
            // TODO else show info saying nothing to load
        }

        /**
         * 
         */
        private void saveFav()
        {
            T data = inputsView.getData();
            OptionsSerializer<MulticonOptions> userio = MulticonMain.getUserData();
            MulticonOptions options = userio.getOptions();
            FavStorage<T> favs = options.getFavs( name );

            if( data != null )
            {
                String[] list = favs.getNames().toArray( new String[0] );

                String lastName = loadedName == null ? "" : loadedName;

                String name = OptionUtils.showEditableMessage( view,
                    "Enter name:", "Enter Name", list, lastName );

                if( name != null )
                {
                    if( name.isEmpty() )
                    {
                        OptionUtils.showErrorMessage( view,
                            "Unable to save with zero length name.",
                            "Save Failed" );
                    }
                    else
                    {
                        favs.setFav( name, data );
                        userio.write( options );
                    }
                }
                // else user cancelled.
            }
            else
            {
                // TODO show error
            }
        }

        /**
         * @{@inheritDoc}
         */
        @Override
        public JComponent getView()
        {
            return view;
        }
    }
}
