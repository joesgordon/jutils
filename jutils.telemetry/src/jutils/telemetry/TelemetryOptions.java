package jutils.telemetry;

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
public class TelemetryOptions
{
    /**  */
    private static OptionsSerializer<TelemetryOptions> OPTIONS;

    /**  */
    public final MaxQueue<File> recentFiles;

    /***************************************************************************
     * 
     **************************************************************************/
    public TelemetryOptions()
    {
        this.recentFiles = new MaxQueue<File>( 20 );
    }

    /***************************************************************************
     * @param options
     **************************************************************************/
    public TelemetryOptions( TelemetryOptions options )
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
    public static TelemetryOptions getOptions()
    {
        return getSerializer().read();
    }

    /***************************************************************************
     * @param options
     **************************************************************************/
    public static void setOptions( TelemetryOptions options )
    {
        getSerializer().write( options );
    }

    /***************************************************************************
     * Gets (or creates) the user options for the Hexedit application.
     * @return the single user options shared by all instances of the calling
     * application.
     **************************************************************************/
    private static OptionsSerializer<TelemetryOptions> getSerializer()
    {
        if( OPTIONS == null )
        {
            OPTIONS = XsOptions.getOptions( TelemetryOptions.class,
                TelemetryMain.USER_OPTIONS_FILE, new IrisUserDataCreator() );
        }
        return OPTIONS;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class IrisUserDataCreator
        implements IOptionsCreator<TelemetryOptions>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public TelemetryOptions createDefaultOptions()
        {
            return new TelemetryOptions();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public TelemetryOptions initialize( TelemetryOptions options )
        {
            options = new TelemetryOptions( options );

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
