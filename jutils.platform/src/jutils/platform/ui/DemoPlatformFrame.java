package jutils.platform.ui;

import javax.swing.JFrame;
import javax.swing.JToolBar;

import jutils.core.IconConstants;
import jutils.core.SwingUtils;
import jutils.core.ui.ComponentView;
import jutils.core.ui.StandardFrameView;
import jutils.core.ui.event.ActionAdapter;
import jutils.core.ui.model.IView;
import jutils.platform.IPlatform;
import jutils.platform.PlatformUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DemoPlatformFrame implements IView<JFrame>
{
    /**  */
    private final StandardFrameView view;
    /**  */
    private final ComponentView contentView;
    /**  */
    private final DemoPlatformView platformView;

    /***************************************************************************
     * 
     **************************************************************************/
    public DemoPlatformFrame()
    {
        this.view = new StandardFrameView();
        this.contentView = new ComponentView();
        this.platformView = new DemoPlatformView();

        view.setTitle( "Demo Platform Frame" );
        view.setSize( 800, 800 );
        view.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        view.setToolbar( createToolbar() );
        view.setContent( contentView.getView() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JToolBar();

        SwingUtils.setToolbarDefaults( toolbar );

        SwingUtils.addActionToToolbar( toolbar,
            new ActionAdapter( ( e ) -> handleGenPlatform(), "Get Platform",
                IconConstants.getIcon( IconConstants.LAUNCH_16 ) ) );

        return toolbar;
    }

    /**
     * 
     */
    private void handleGenPlatform()
    {
        IPlatform platform = PlatformUtils.getPlatform();

        if( platform != null )
        {
            contentView.setComponent( platformView.getView() );
        }
        else
        {
            contentView.setComponent( null );
        }
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
