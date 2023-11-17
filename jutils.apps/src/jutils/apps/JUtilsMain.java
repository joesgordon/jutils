package jutils.apps;

import java.util.ArrayList;
import java.util.List;

import jutils.core.ui.IToolView;
import jutils.core.ui.app.AppRunner;
import jutils.duak.DuakTool;
import jutils.explorer.ExplorerTool;
import jutils.filespy.FileSpyTool;
import jutils.hexedit.HexeditTool;
import jutils.insomnia.InsomniaTool;
import jutils.mines.MinesTool;
import jutils.multicon.MulticonTool;
import jutils.plot.app.PlotTool;
import jutils.summer.SummerTool;

/*******************************************************************************
 * This class defines the application that will display the main applications
 * contained in JUtils.
 ******************************************************************************/
public class JUtilsMain
{
    /***************************************************************************
     * Application Gallery definition to display an AppGallery frame.
     * @param args Unused arguments to the Application Gallery application.
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.DEFAULT_LAF = AppRunner.SIMPLE_LAF;

        AppRunner.invokeLater( new JUtilsApp(), false );
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
