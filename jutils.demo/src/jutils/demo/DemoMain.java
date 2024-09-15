package jutils.demo;

import jutils.core.ui.app.AppRunner;

/*******************************************************************************
 * Defines the application entry point to demo all JUtils functionality.
 ******************************************************************************/
public class DemoMain
{
    /***************************************************************************
     * Main entry point for the demo application.
     * @param args ignored.
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.DEFAULT_LAF = AppRunner.JGOODIES_LAF;
        // AppRunner.DEFAULT_LAF = AppRunner.SIMPLE_LAF;

        AppRunner.invokeLater( new DemoApp(), true );
    }
}
