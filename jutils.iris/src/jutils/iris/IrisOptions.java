package jutils.iris;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jutils.core.io.LogUtils;
import jutils.core.io.options.IOptionsCreator;
import jutils.core.io.options.OptionsSerializer;
import jutils.core.io.xs.XsOptions;
import jutils.core.utils.MaxQueue;
import jutils.iris.data.RawConfig;

/*******************************************************************************
 * 
 ******************************************************************************/
public class IrisOptions
{
    /**  */
    private static OptionsSerializer<IrisOptions> OPTIONS;

    /** The file last opened by the application. */
    public final MaxQueue<File> lastOpenedFiles;
    /** The file last saved by the application. */
    public final MaxQueue<File> lastSavedFiles;
    /**  */
    public final RawConfig lastRawConfig;

    /***************************************************************************
     * 
     **************************************************************************/
    public IrisOptions()
    {
        this.lastOpenedFiles = new MaxQueue<File>( 10 );
        this.lastSavedFiles = new MaxQueue<File>( 10 );
        this.lastRawConfig = new RawConfig();
    }

    /***************************************************************************
     * @param options
     **************************************************************************/
    public IrisOptions( IrisOptions options )
    {
        this();

        if( options.lastOpenedFiles != null )
        {
            lastOpenedFiles.addAll( options.lastOpenedFiles );
        }
        if( options.lastSavedFiles != null )
        {
            lastSavedFiles.addAll( options.lastSavedFiles );
        }

        lastRawConfig.set( options.lastRawConfig );
    }

    /***************************************************************************
     * Gets the file last opened by the application.
     * @return the last file opened or {@code null} if none exist.
     **************************************************************************/
    public File getLastOpenedFile()
    {
        return lastOpenedFiles.first();
    }

    /***************************************************************************
     * Gets the file last saved by the user.
     * @return the last file saved or {@code null} if none exist.
     **************************************************************************/
    public File getLastSavedFile()
    {
        return lastSavedFiles.first();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void removeNonExistentRecents()
    {
        List<File> toRemove = new ArrayList<File>();

        for( File f : lastOpenedFiles )
        {
            if( !f.exists() )
            {
                toRemove.add( f );
            }
        }

        for( File f : toRemove )
        {
            lastOpenedFiles.remove( f );
        }
    }

    /***************************************************************************
     * @param file
     * @return
     **************************************************************************/
    public RawConfig getLastRawConfig( File file )
    {
        return lastRawConfig;
    }

    /***************************************************************************
     * @param file
     * @param config
     **************************************************************************/
    public void setLastRawConfig( File file, RawConfig config )
    {
        lastRawConfig.set( config );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static IrisOptions getOptions()
    {
        return getSerializer().read();
    }

    /***************************************************************************
     * @param options
     **************************************************************************/
    public static void setOptions( IrisOptions options )
    {
        getSerializer().write( options );
    }

    /***************************************************************************
     * Gets (or creates) the user options for the Hexedit application.
     * @return the single user options shared by all instances of the calling
     * application.
     **************************************************************************/
    private static OptionsSerializer<IrisOptions> getSerializer()
    {
        if( OPTIONS == null )
        {
            OPTIONS = XsOptions.getOptions( IrisOptions.class,
                IrisUtils.USER_OPTIONS_FILE, new IrisUserDataCreator() );
        }
        return OPTIONS;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class IrisUserDataCreator
        implements IOptionsCreator<IrisOptions>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public IrisOptions createDefaultOptions()
        {
            return new IrisOptions();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public IrisOptions initialize( IrisOptions options )
        {
            options = new IrisOptions( options );

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
