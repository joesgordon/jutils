package jutils.ch10.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jutils.core.io.LogUtils;
import jutils.core.io.options.IOptionsCreator;
import jutils.core.io.options.OptionsSerializer;
import jutils.core.io.xs.XsOptions;
import jutils.core.utils.MaxQueue;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Ch10ViewerOptions
{
    /**  */
    private static OptionsSerializer<Ch10ViewerOptions> OPTIONS;

    /**  */
    public final MaxQueue<File> recentFiles;

    /***************************************************************************
     * 
     **************************************************************************/
    public Ch10ViewerOptions()
    {
        this.recentFiles = new MaxQueue<File>( 20 );
    }

    public Ch10ViewerOptions( Ch10ViewerOptions options )
    {
        this();

        if( options.recentFiles != null )
        {
            recentFiles.addAll( options.recentFiles );
        }
    }

    /***************************************************************************
     *  
     **************************************************************************/
    public void removeNonExistentRecents()
    {
        List<File> toRemove = new ArrayList<File>();

        for( File f : recentFiles )
        {
            if( !f.exists() )
            {
                toRemove.add( f );
            }
        }

        for( File f : toRemove )
        {
            recentFiles.remove( f );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static Ch10ViewerOptions getOptions()
    {
        return getSerializer().read();
    }

    /***************************************************************************
     * @param options
     **************************************************************************/
    public static void setOptions( Ch10ViewerOptions options )
    {
        getSerializer().write( options );
    }

    /***************************************************************************
     * Gets (or creates) the user options for the Hexedit application.
     * @return the single user options shared by all instances of the calling
     * application.
     **************************************************************************/
    private static OptionsSerializer<Ch10ViewerOptions> getSerializer()
    {
        if( OPTIONS == null )
        {
            OPTIONS = XsOptions.getOptions( Ch10ViewerOptions.class,
                Ch10ViewerMain.USER_OPTIONS_FILE, new IrisUserDataCreator() );
        }
        return OPTIONS;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class IrisUserDataCreator
        implements IOptionsCreator<Ch10ViewerOptions>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public Ch10ViewerOptions createDefaultOptions()
        {
            return new Ch10ViewerOptions();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Ch10ViewerOptions initialize( Ch10ViewerOptions options )
        {
            options = new Ch10ViewerOptions( options );

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
