package org.jutils.minesweeper.ui;

import java.awt.Dialog.ModalityType;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import org.jutils.core.IconConstants;
import org.jutils.core.ui.OkDialogView;
import org.jutils.core.ui.StandardFrameView;
import org.jutils.core.ui.OkDialogView.OkDialogButtons;
import org.jutils.core.ui.event.ActionAdapter;
import org.jutils.core.ui.model.IView;
import org.jutils.minesweeper.MsIcons;
import org.jutils.minesweeper.data.GameOptions;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MsFrameView implements IView<JFrame>
{
    /**  */
    private final StandardFrameView view;
    /**  */
    private final MsView gameView;

    /***************************************************************************
     * 
     **************************************************************************/
    public MsFrameView()
    {
        this.view = new StandardFrameView();
        this.gameView = new MsView();

        view.setTitle( "Minesweeper" );
        view.setContent( gameView.getView() );
        view.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        view.setSize( 800, 800 );
        view.getView().setIconImages( MsIcons.getAppImages() );
        view.getView().setResizable( false );

        createMenus( view.getMenuBar() );
    }

    /***************************************************************************
     * @param menuBar
     **************************************************************************/
    private void createMenus( JMenuBar menuBar )
    {
        menuBar.add( createToolsMenu() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JMenu createToolsMenu()
    {
        JMenu menu = new JMenu( "Tools" );

        menu.add( createOptionsAction() );

        return menu;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createOptionsAction()
    {
        ActionListener listener = ( e ) -> showOptions();
        Icon icon = IconConstants.getIcon( IconConstants.CONFIG_16 );

        return new ActionAdapter( listener, "Options", icon );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private void showOptions()
    {
        GameOptions options = gameView.getOptions();
        GameOptionsView optionsView = new GameOptionsView();

        optionsView.setData( options );

        OkDialogView okView = new OkDialogView( getView(),
            optionsView.getView(), ModalityType.DOCUMENT_MODAL,
            OkDialogButtons.OK_ONLY );

        okView.setTitle( "Options" );
        okView.show( 400, 300 );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return view.getView();
    }
}
