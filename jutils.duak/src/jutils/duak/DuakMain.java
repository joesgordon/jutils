package jutils.duak;

import jutils.core.ui.app.AppRunner;

/***************************************************************************
 * 
 **************************************************************************/
public class DuakMain
{
    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private DuakMain()
    {
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.invokeLater( new DuakApp() );
    }
}
