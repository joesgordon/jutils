package org.jutils.core.ui.app;

import javax.swing.JFrame;

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
     * Validates the frame from the provided creator and starts it on the swing
     * event queue using the default look-n-feel set by
     * {@link AppRunner#setDefaultLaf()}.
     * @param frameCreator the callback to create the frame.
     **************************************************************************/
    public static void invokeLater( IFrameCreator frameCreator )
    {
        invokeLater( new BasicApp( frameCreator ) );
    }

    /***************************************************************************
     * Validates the frame from the provided app and starts it on the swing
     * event queue using the default look-n-feel set by
     * {@link AppRunner#setDefaultLaf()}.
     * @param app the application to be started.
     **************************************************************************/
    public static void invokeLater( IFrameApp app )
    {
        invokeLater( app, true );
    }

    /***************************************************************************
     * Starts the frame from the provided creator on the swing event queue using
     * the default look-n-feel set by {@link AppRunner#setDefaultLaf()}.
     * @param frameCreator the callback to create the frame.
     * @param validate indicates the application's frame should be validated if
     * {@code true}; packs the frame if {@code false}.
     **************************************************************************/
    public static void invokeLater( IFrameCreator frameCreator,
        boolean validate )
    {
        invokeLater( new BasicApp( frameCreator ), validate, null );
    }

    /***************************************************************************
     * Starts the frame from the provided app on the swing event queue using the
     * default look-n-feel set by {@link AppRunner#setDefaultLaf()}.
     * @param app the application to be started.
     * @param validate indicates the application's frame should be validated if
     * {@code true}; packs the frame if {@code false}.
     **************************************************************************/
    public static void invokeLater( IFrameApp app, boolean validate )
    {
        invokeLater( app, validate, null );
    }

    /***************************************************************************
     * Starts the frame from the provided creator on the swing event queue using
     * the provided look-n-feel.
     * @param frameCreator the callback to create the frame.
     * @param validate indicates the application's frame should be validated if
     * {@code true}; packs the frame if {@code false}.
     * @param lookAndFeel the name of the look-n-feel to use.
     **************************************************************************/
    public static void invokeLater( IFrameCreator frameCreator,
        boolean validate, String lookAndFeel )
    {
        invokeLater( new BasicApp( frameCreator ), validate, lookAndFeel );
    }

    /***************************************************************************
     * Starts the frame from the provided app on the swing event queue using the
     * provided look-n-feel.
     * @param app the application to be started.
     * @param validate indicates the application's frame should be validated if
     * {@code true}; packs the frame if {@code false}.
     * @param lookAndFeel the name of the look-n-feel to use.
     **************************************************************************/
    public static void invokeLater( IFrameApp app, boolean validate,
        String lookAndFeel )
    {
        IApplication fApp = new FrameApplication( app, validate, lookAndFeel );

        AppRunner.invokeLater( fApp );
    }

    /***************************************************************************
     * Creates a JFrame to be displayed.
     **************************************************************************/
    public static interface IFrameCreator
    {
        /**
         * Creates the frame and all other UI (tray icons, etc.)
         * @return the frame for this application.
         */
        public JFrame createFrame();
    }

    /***************************************************************************
     * Defines an {@link IFrameApp} that uses an {@code IFrameCreator} to create
     * the frame and does nothing to finalize the GUI.
     **************************************************************************/
    private final static class BasicApp implements IFrameApp
    {
        /** The base creator */
        private IFrameCreator frameCreator;

        /**
         * Creates a new basic app with the provided creator.
         * @param frameCreator the base creator.
         */
        public BasicApp( IFrameCreator frameCreator )
        {
            this.frameCreator = frameCreator;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public JFrame createFrame()
        {
            return frameCreator.createFrame();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void finalizeGui()
        {
        }
    }
}
