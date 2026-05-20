package jutils.multicon.ui;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;

import jutils.core.*;
import jutils.core.io.options.OptionsSerializer;
import jutils.core.io.xs.XsUtils;
import jutils.core.ui.event.ActionAdapter;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.model.IDataView;
import jutils.core.ui.model.IView;
import jutils.core.utils.IGetter;
import jutils.multicon.*;
import jutils.multicon.MulticonOptions.FavStorage;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class BindableFavView<T> implements IView<JComponent>
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
        IGetter<IBindableView<T>> getter, IUpdater<IBindableView<?>> updater )
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

        ActionAdapter action = new ActionAdapter( bindListener, "Bind",
            bindIcon );

        JButton button = SwingUtils.addActionToToolbar( toolbar, action );
        button.setText( "Bind" );

        toolbar.addSeparator();

        SwingUtils.addActionToToolbar( toolbar, createOpenAction() );

        SwingUtils.addActionToToolbar( toolbar, createSaveAction() );

        return toolbar;
    }

    /**
     * @return
     */
    private Action createOpenAction()
    {
        ActionListener listener = ( e ) -> openFavorite();
        Icon icon = IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 );

        return new ActionAdapter( listener, "Open", icon );
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
    private void openFavorite()
    {
        OptionsSerializer<MulticonOptions> userio = MulticonMain.getUserData();
        MulticonOptions options = userio.getOptions();
        FavStorage<T> favs = options.getFavs( name );
        List<String> names = favs.getNames();

        if( !names.isEmpty() )
        {
            String[] choices = names.toArray( new String[names.size()] );

            String choice = OptionUtils.showComboDialog( getView(),
                "Which favorite would you like to open?", "Choose Favorite", "",
                choices, choices[0] );

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

            String name = OptionUtils.showEditableMessage( view, "Enter name:",
                "Enter Name", list, lastName );

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
