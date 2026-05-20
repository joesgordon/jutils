package jutils.multicon;

import java.io.File;

import jutils.core.io.IOUtils;
import jutils.core.io.LogUtils;
import jutils.core.io.options.IOptionsCreator;
import jutils.core.io.options.OptionsSerializer;
import jutils.core.io.xs.XsOptions;
import jutils.core.ui.app.AppRunner;
import jutils.platform.PlatformUtils;

/*******************************************************************************
 * Defines the main entry point for the Multicon application.
 ******************************************************************************/
public class MulticonMain
{
    /** The path to Multicon's user's options file. */
    private static final File OPTIONS_FILE = IOUtils.getUsersFile( ".jutils",
        "multicon", "options.xml" );

    /** The single serializer instance for user options. */
    private static OptionsSerializer<MulticonOptions> userOptions;

    /***************************************************************************
     * Main entry point for the Multicon application.
     * @param args ignored
     **************************************************************************/
    public static void main( String[] args )
    {
        AppRunner.DEFAULT_LAF = AppRunner.JGOODIES_LAF;

        PlatformUtils.getPlatform().initialize();

        AppRunner.invokeLater( new MulticonApp(), true );
    }

    /***************************************************************************
     * @return the user options for this application/library instance.
     **************************************************************************/
    public static OptionsSerializer<MulticonOptions> getUserData()
    {
        if( userOptions == null )
        {
            userOptions = XsOptions.getOptions( MulticonOptions.class,
                OPTIONS_FILE, new McCommOptionsCreator() );
        }

        return userOptions;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class McCommOptionsCreator
        implements IOptionsCreator<MulticonOptions>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public MulticonOptions createDefaultOptions()
        {
            return new MulticonOptions();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public MulticonOptions initialize( MulticonOptions options )
        {
            return new MulticonOptions( options );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void warn( String message )
        {
            LogUtils.printWarning( message );
        }
    }
}
