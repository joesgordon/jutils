package jutils.iris.ui;

import jutils.core.ui.app.AppRunner;
import jutils.iris.albums.IRasterAlbum;
import jutils.iris.albums.RasterListAlbum;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SaveOptionsViewMain
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.invokeLater( () -> createUI() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static void createUI()
    {
        SaveOptionsView view = new SaveOptionsView();
        IRasterAlbum album = new RasterListAlbum();

        view.showDialog( null, album );
    }
}
