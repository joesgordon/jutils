package jutils.core.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import jutils.core.IconConstants;
import jutils.core.SwingUtils;
import jutils.core.ui.event.ActionAdapter;
import jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public abstract class PaginatedView implements IView<JPanel>
{
    /** This class's main view. */
    private JPanel view;

    /** The toolbar for this view. */
    private final JToolBar toolbar;

    /**  */
    private final JButton navFirstButton;
    /**  */
    private final JButton navPreviousButton;
    /**  */
    private final JButton navNextButton;
    /**  */
    private final JButton navLastButton;
    /**  */
    private final JLabel pageLabel;

    /***************************************************************************
     * @param tableCfg the configuration of the table to be displayed.
     * @param itemsStream
     **************************************************************************/
    public PaginatedView()
    {
        this.navFirstButton = new JButton();
        this.navPreviousButton = new JButton();
        this.navNextButton = new JButton();
        this.navLastButton = new JButton();
        this.pageLabel = new JLabel( "Page 0 of 0 (0)" );
        this.toolbar = createToolbar();
    }

    /***************************************************************************
     * Creates this class's main view.
     * @param comp
     * @return this class's main view.
     **************************************************************************/
    protected void createView( Component comp )
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( toolbar, BorderLayout.NORTH );
        panel.add( comp, BorderLayout.CENTER );

        panel.setMinimumSize( new Dimension( 625, 200 ) );
        panel.setPreferredSize( new Dimension( 625, 200 ) );

        this.view = panel;

        updateControls();
    }

    /***************************************************************************
     * Creates the toolbar for this view.
     * @return the toolbar for this view.
     **************************************************************************/
    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JToolBar();

        SwingUtils.setToolbarDefaults( toolbar );

        SwingUtils.addActionToToolbar( toolbar, createNavAction( true, false ),
            navFirstButton );
        navFirstButton.setText( "" );

        SwingUtils.addActionToToolbar( toolbar, createNavAction( false, false ),
            navPreviousButton );
        navPreviousButton.setText( "" );

        SwingUtils.addActionToToolbar( toolbar, createNavAction( false, true ),
            navNextButton );
        navNextButton.setText( "" );

        SwingUtils.addActionToToolbar( toolbar, createNavAction( true, true ),
            navLastButton );
        navLastButton.setText( "" );

        toolbar.addSeparator();

        toolbar.add( pageLabel );

        toolbar.addSeparator();

        SwingUtils.addActionToToolbar( toolbar, createClearAction() );

        toolbar.addSeparator();

        return toolbar;
    }

    /***************************************************************************
     * @param absolute if this action navigates to an end of the pages.
     * @param forward if this action navigates forward.
     * @return the action that navigates.
     **************************************************************************/
    private Action createNavAction( boolean absolute, boolean forward )
    {
        ActionListener listener = ( e ) -> navigatePage( absolute, forward );
        String iconName;
        Icon icon;
        String actionName;

        if( absolute && !forward )
        {
            iconName = IconConstants.NAV_FIRST_16;
            actionName = "First Page";
        }
        else if( !absolute && !forward )
        {
            iconName = IconConstants.NAV_PREVIOUS_16;
            actionName = "Previous Page";
        }
        else if( !absolute && forward )
        {
            iconName = IconConstants.NAV_NEXT_16;
            actionName = "Next Page";
        }
        else
        {
            iconName = IconConstants.NAV_LAST_16;
            actionName = "Last Page";
        }

        icon = IconConstants.getIcon( iconName );

        return new ActionAdapter( listener, actionName, icon );
    }

    /***************************************************************************
     * @return an action that clears all items.
     **************************************************************************/
    private Action createClearAction()
    {
        ActionListener listener = ( e ) -> removeAllPages();
        Icon icon = IconConstants.getIcon( IconConstants.EDIT_CLEAR_16 );

        return new ActionAdapter( listener, "Clear", icon );
    }

    /***************************************************************************
     * @param absolute if the navigation is to an end of the pages.
     * @param forward if the navigation is forward one page (+1).
     **************************************************************************/
    private void navigatePage( boolean absolute, boolean forward )
    {
        int page = getCurrentPage();
        int count = getPageCount();

        if( absolute && !forward )
        {
            page = 0;
        }
        else if( !absolute && !forward )
        {
            page = page - 1;
        }
        else if( !absolute && forward )
        {
            page = page + 1;
        }
        else
        {
            page = count - 1;
        }

        if( page > -1 && page < count )
        {
            setCurrentPage( page );
        }
    }

    /***************************************************************************
     * @return {@code true} if there is a previous page.
     **************************************************************************/
    private boolean hasPrevious()
    {
        return getCurrentPage() > 0;
    }

    /***************************************************************************
     * @return {@code true} if there is a next page.
     **************************************************************************/
    private boolean hasNext()
    {
        return getCurrentPage() < ( getPageCount() - 1 );
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
     * @param pages
     **************************************************************************/
    protected void updateControls()
    {
        boolean hasPrev = hasPrevious();
        boolean hasNext = hasNext();

        navFirstButton.setEnabled( hasPrev );
        navPreviousButton.setEnabled( hasPrev );
        navNextButton.setEnabled( hasNext );
        navLastButton.setEnabled( hasNext );

        pageLabel.setText( getPageTitle( getCurrentPage() ) );
    }

    /***************************************************************************
     * @param a the action to be added.
     * @return the button created from adding the provided action.
     **************************************************************************/
    protected JButton addToToolbar( Action a )
    {
        return SwingUtils.addActionToToolbar( toolbar, a );
    }

    /***************************************************************************
     * Adds a separator to the toolbar.
     **************************************************************************/
    protected void addToToolbar()
    {
        toolbar.addSeparator();
    }

    /***************************************************************************
     * @param pageIndex
     * @return
     **************************************************************************/
    protected String getPageTitle( int pageIndex )
    {
        int pageNum = pageIndex < 0 ? 0 : pageIndex + 1;

        return String.format( "Page %d of %d", pageNum, getPageCount() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public abstract int getPageCount();

    /***************************************************************************
     * @return
     **************************************************************************/
    public abstract int getCurrentPage();

    /***************************************************************************
     * @param pageIndex
     **************************************************************************/
    public abstract void setCurrentPage( int pageIndex );

    /***************************************************************************
     * 
     **************************************************************************/
    public abstract void removeAllPages();
}
