package jutils.platform.ui;

import java.util.List;

import javax.swing.JFrame;

import jutils.core.io.LogUtils;
import jutils.core.ui.StandardFrameView;
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
    private final SerialParamsView configView;

    /***************************************************************************
     * 
     **************************************************************************/
    public DemoPlatformFrame()
    {
        this.view = new StandardFrameView();
        this.configView = new SerialParamsView();

        view.setTitle( "Demo Platform Frame" );
        view.setSize( 800, 800 );
        view.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        view.setContent( configView.getView() );

        IPlatform platform = PlatformUtils.getPlatform();

        platform.initialize();

        List<String> ports = platform.listSerialPorts();

        for( String port : ports )
        {
            LogUtils.printDebug( "> %s", port );
        }

        // platform.destroy();
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
