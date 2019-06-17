package org.jutils.ui.app;

/*******************************************************************************
 * Utility class for starting {@link IFrameApp}s.
 ******************************************************************************/
public class FrameRunner
{
    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private FrameRunner()
    {
    }

    /***************************************************************************
     * Validates the frame from the provided app and starts it on the swing
     * event queue using the default look-n-feel set by
     * {@link AppRunner#setDefaultLaf()}.
     * @param app the application to be started.
     **************************************************************************/
    public static void invokeLater( IFrameApp app )
    {
        invokeLater( app, true, null );
    }

    /***************************************************************************
     * @param app the application to be started.
     * @param validate signals to validate the application's frame if
     * {@code true}; packs the frame otherwise.
     **************************************************************************/
    public static void invokeLater( IFrameApp app, boolean validate )
    {
        invokeLater( app, validate, null );
    }

    /***************************************************************************
     * @param app the application to be started.
     * @param validate signals to validate the application's frame if
     * {@code true}; packs the frame otherwise.
     * @param lookAndFeel the name of the look-n-feel to use.
     **************************************************************************/
    public static void invokeLater( IFrameApp app, boolean validate,
        String lookAndFeel )
    {
        IApplication fApp = new FrameApplication( app, validate, lookAndFeel );

        AppRunner.invokeLater( fApp );
    }
}
