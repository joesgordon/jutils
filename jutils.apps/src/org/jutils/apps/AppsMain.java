package org.jutils.apps;

import java.util.ArrayList;
import java.util.List;

import org.jutils.core.ui.IToolView;
import org.jutils.core.ui.app.FrameRunner;
import org.jutils.duak.DuakTool;
import org.jutils.explorer.ExplorerTool;
import org.jutils.filespy.FileSpyTool;
import org.jutils.hexedit.HexeditTool;
import org.jutils.insomnia.InsomniaTool;
import org.jutils.mines.MinesTool;
import org.jutils.multicon.MulticonTool;
import org.jutils.plot.app.PlotTool;
import org.jutils.summer.SummerTool;

/*******************************************************************************
 * This class defines the application that will display the main applications
 * contained in JUtils.
 ******************************************************************************/
public class AppsMain
{
    /***************************************************************************
     * Application Gallery definition to display an AppGallery frame.
     * @param args Unused arguments to the Application Gallery application.
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new AppsApp(), false );
    }

    /***************************************************************************
     * Defines the tools displayed by this application.
     * @return the list of tools in this application.
     **************************************************************************/
    public static List<IToolView> getTools()
    {
        List<IToolView> apps = new ArrayList<IToolView>();

        apps.add( new HexeditTool() );
        apps.add( new MulticonTool() );
        apps.add( new FileSpyTool() );
        apps.add( new PlotTool() );
        apps.add( new SummerTool() );
        apps.add( new MinesTool() );
        apps.add( new ExplorerTool() );
        apps.add( new DuakTool() );
        apps.add( new InsomniaTool() );

        return apps;
    }
}
