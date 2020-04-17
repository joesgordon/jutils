package org.jutils.hexedit;

import java.io.File;

import org.jutils.core.io.IOUtils;
import org.jutils.core.io.LogUtils;
import org.jutils.core.io.options.IOptionsCreator;
import org.jutils.core.io.options.OptionsSerializer;
import org.jutils.core.io.xs.XsOptions;
import org.jutils.core.ui.app.FrameRunner;
import org.jutils.hexedit.data.HexeditOptions;

/*******************************************************************************
 * Contains the entry point for the Hexedit application.
 ******************************************************************************/
public class HexeditMain
{
    /** The path to the user options file. */
    public static final File USER_OPTIONS_FILE = IOUtils.getUsersFile(
        ".jutils", "jhex", "options.xml" );

    /**
     * The single user options shared by all instances of the calling
     * application.
     */
    private static OptionsSerializer<HexeditOptions> OPTIONS;

    /***************************************************************************
     * Starts the Hexedit application.
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

        HexeditApp hexApp = new HexeditApp( file );
        FrameRunner.invokeLater( hexApp );
    }

    /***************************************************************************
     * Gets (or creates) the user options for the Hexedit application.
     * @return the single user options shared by all instances of the calling
     * application.
     **************************************************************************/
    public static OptionsSerializer<HexeditOptions> getOptions()
    {
        if( OPTIONS == null )
        {
            OPTIONS = XsOptions.getOptions( HexeditOptions.class,
                USER_OPTIONS_FILE, new HexeditOptionsCreator() );
        }
        return OPTIONS;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class HexeditOptionsCreator
        implements IOptionsCreator<HexeditOptions>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public HexeditOptions createDefaultOptions()
        {
            return new HexeditOptions();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public HexeditOptions initialize( HexeditOptions options )
        {
            options = new HexeditOptions( options );

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
