package jutils.core.ui.fields;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import jutils.core.IconConstants;
import jutils.core.SwingUtils;
import jutils.core.io.ScreensView;
import jutils.core.io.ScreensView.ScreenInfo;
import jutils.core.ui.OkDialogView;
import jutils.core.ui.event.ActionAdapter;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.validation.IValidityChangedListener;
import jutils.core.ui.validation.Validity;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ScreenFormField implements IDataFormField<String>
{
    /**  */
    private final JPanel view;
    /**  */
    private final ComboFormField<ScreenInfo> field;

    /**  */
    private IUpdater<String> updater;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public ScreenFormField( String name )
    {
        this.field = new ComboFormField<ScreenInfo>( name,
            ScreensView.detectScreens(), new NamedItemDescriptor<>() );

        this.view = createView();

        this.updater = null;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 6 ), 0, 0 );
        panel.add( field.getView(), constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( createButtons(), constraints );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createButtons()
    {
        JToolBar toolbar = new JToolBar();

        SwingUtils.setToolbarDefaults( toolbar );

        SwingUtils.addActionToToolbar( toolbar, createSelectAction() );
        SwingUtils.addActionToToolbar( toolbar, createRefreshAction() );

        toolbar.validate();

        return toolbar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createSelectAction()
    {
        ActionListener listener = ( e ) -> handleSelectScreen();
        Icon icon = IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 );
        return new ActionAdapter( listener, "Select Screen", icon );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createRefreshAction()
    {
        ActionListener listener = ( e ) -> handleRefreshScreens();
        Icon icon = IconConstants.getIcon( IconConstants.REFRESH_16 );
        return new ActionAdapter( listener, "Refresh Screens", icon );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void handleSelectScreen()
    {
        ScreensView screensView = new ScreensView();
        OkDialogView dialogView = new OkDialogView( getView(),
            screensView.getView() );

        screensView.setSelected( getValue() );

        dialogView.setTitle( "Select Screen" );

        if( dialogView.show( new Dimension( 400, 400 ) ) )
        {
            setValue( screensView.getSelected() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void handleRefreshScreens()
    {
        this.field.setValues( ScreensView.generateScreens() );
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
    public String getName()
    {
        return field.getName();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getValue()
    {
        ScreenInfo screen = field.getValue();

        return screen == null ? null : screen.id;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( String value )
    {
        field.setValue( getScreen( value ) );

        view.invalidate();
        view.repaint();
    }

    /***************************************************************************
     * @param id
     * @return
     **************************************************************************/
    private ScreenInfo getScreen( String id )
    {
        for( ScreenInfo screen : field.getValues() )
        {
            if( screen.id.equals( id ) )
            {
                return screen;
            }
        }

        return new ScreenInfo( id, new Rectangle(), false );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<String> updater )
    {
        this.updater = updater;

        IUpdater<ScreenInfo> screenUpdater = updater == null ? null
            : ( d ) -> updater.update( d.id );

        field.setUpdater( screenUpdater );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<String> getUpdater()
    {
        return updater;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        field.setEditable( editable );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        field.addValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        field.removeValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return field.getValidity();
    }
}
