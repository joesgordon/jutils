package org.jutils.apps.jhex;

import java.io.File;

import org.jutils.apps.jhex.data.JHexOptions;
import org.jutils.core.io.IOUtils;
import org.jutils.core.io.LogUtils;
import org.jutils.core.io.options.IOptionsCreator;
import org.jutils.core.io.options.OptionsSerializer;
import org.jutils.core.ui.app.FrameRunner;

/*******************************************************************************
 * Contains the entry point for the JHex application.
 ******************************************************************************/
public class JHexMain
{
    /** The path to the user options file. */
    public static final File USER_OPTIONS_FILE = IOUtils.getUsersFile(
        ".jutils", "jhex", "options.xml" );

    /**
     * The single user options shared by all instances of the calling
     * application.
     */
    private static OptionsSerializer<JHexOptions> OPTIONS;

    /***************************************************************************
     * Starts the JHex application.
     * @param args either an empty array or the path to the file to be displayed
     * when the application starts.
     **************************************************************************/
    public static void main( String [] args )
    {
        File file = null;

        if( args.length == 1 )
        {
            file = new File( args[0] );
            if( !file.isFile() )
            {
                System.err.println(
                    "File does not exist: " + file.getAbsolutePath() );
                System.exit( -1 );
            }
        }

        JHexApp hexApp = new JHexApp( file );
        FrameRunner.invokeLater( hexApp );
    }

    /***************************************************************************
     * Gets (or creates) the user options for the JHex application.
     * @return the single user options shared by all instances of the calling
     * application.
     **************************************************************************/
    public static OptionsSerializer<JHexOptions> getOptions()
    {
        if( OPTIONS == null )
        {
            OPTIONS = OptionsSerializer.getOptions( JHexOptions.class,
                USER_OPTIONS_FILE, new JHexOptionsCreator() );
        }
        return OPTIONS;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class JHexOptionsCreator
        implements IOptionsCreator<JHexOptions>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public JHexOptions createDefaultOptions()
        {
            return new JHexOptions();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public JHexOptions initialize( JHexOptions options )
        {
            options = new JHexOptions( options );

            options.removeNonExistentRecents();

            return options;
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
