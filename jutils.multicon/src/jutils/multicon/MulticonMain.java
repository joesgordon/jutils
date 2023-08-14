package jutils.multicon;

import java.io.File;

import jutils.core.io.IOUtils;
import jutils.core.io.LogUtils;
import jutils.core.io.options.IOptionsCreator;
import jutils.core.io.options.OptionsSerializer;
import jutils.core.io.xs.XsOptions;
import jutils.core.ui.app.AppRunner;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MulticonMain
{
    /**  */
    private static final File OPTIONS_FILE = IOUtils.getUsersFile( ".jutils",
        "multicon", "options.xml" );

    /**  */
    private static OptionsSerializer<MulticonOptions> userOptions;

    /***************************************************************************
     * @param args ignored
     **************************************************************************/
    public static void main( String[] args )
    {
        AppRunner.invokeLater( new MulticonApp(), false );
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
