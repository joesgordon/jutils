package jutils.iris;

import java.io.File;

import javax.swing.JFrame;

import jutils.core.io.IOUtils;
import jutils.core.io.LogUtils;
import jutils.core.io.options.IOptionsCreator;
import jutils.core.io.options.OptionsSerializer;
import jutils.core.io.xs.XsOptions;
import jutils.core.ui.app.AppRunner;
import jutils.iris.ui.IrisFrame;

/*******************************************************************************
 * 
 ******************************************************************************/
public class IrisMain
{
    /**  */
    private static final File USER_OPTIONS_FILE = IOUtils.getUsersFile(
        ".jutils", "iris", "user.xml" );
    /**  */
    private static OptionsSerializer<IrisUserData> OPTIONS;

    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private IrisMain()
    {
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.DEFAULT_LAF = AppRunner.SIMPLE_LAF;

        AppRunner.invokeLater( () -> createFrame() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static JFrame createFrame()
    {
        IrisFrame frame = new IrisFrame();

        return frame.getView();
    }

    /***************************************************************************
     * Gets (or creates) the user options for the Hexedit application.
     * @return the single user options shared by all instances of the calling
     * application.
     **************************************************************************/
    public static OptionsSerializer<IrisUserData> getOptions()
    {
        if( OPTIONS == null )
        {
            OPTIONS = XsOptions.getOptions( IrisUserData.class,
                USER_OPTIONS_FILE, new IrisUserDataCreator() );
        }
        return OPTIONS;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class IrisUserDataCreator
        implements IOptionsCreator<IrisUserData>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public IrisUserData createDefaultOptions()
        {
            return new IrisUserData();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public IrisUserData initialize( IrisUserData options )
        {
            options = new IrisUserData( options );

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
