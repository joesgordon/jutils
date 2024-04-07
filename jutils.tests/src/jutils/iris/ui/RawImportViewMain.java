package jutils.iris.ui;

import jutils.core.ui.app.AppRunner;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RawImportViewMain
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
        RawImportView view = new RawImportView();

        view.showDialog( null, null );
    }
}
