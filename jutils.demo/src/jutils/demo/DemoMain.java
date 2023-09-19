package jutils.demo;

import jutils.core.laf.SimpleLookAndFeel;
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
        AppRunner.invokeLater( new DemoApp(), true,
            SimpleLookAndFeel.class.getName() );
    }
}
