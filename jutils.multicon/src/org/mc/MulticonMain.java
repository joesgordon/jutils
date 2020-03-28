package org.mc;

import java.io.File;

import org.jutils.core.io.IOUtils;
import org.jutils.core.io.LogUtils;
import org.jutils.core.io.options.IOptionsCreator;
import org.jutils.core.io.options.OptionsSerializer;
import org.jutils.core.ui.app.FrameRunner;

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
        FrameRunner.invokeLater( new MulticonApp(), false );
    }

    /***************************************************************************
     * @return the user options for this application/library instance.
     **************************************************************************/
    public static OptionsSerializer<MulticonOptions> getUserData()
    {
        if( userOptions == null )
        {
            userOptions = OptionsSerializer.getOptions( MulticonOptions.class,
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
