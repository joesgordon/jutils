package jutils.telemetry;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jutils.core.io.IOUtils;
import jutils.core.io.LogUtils;
import jutils.core.io.options.IOptionsCreator;
import jutils.core.io.options.OptionsSerializer;
import jutils.core.io.xs.XsOptions;
import jutils.core.utils.MaxQueue;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TmatsEditorOptions
{
    /**  */
    public static final File USER_OPTIONS_FILE = IOUtils.getUsersFile(
        ".jutils", "tm", "tede_options.xml" );
    /**  */
    private static OptionsSerializer<TmatsEditorOptions> OPTIONS;

    /**  */
    public final MaxQueue<File> recentFiles;

    /***************************************************************************
     * 
     **************************************************************************/
    public TmatsEditorOptions()
    {
        this.recentFiles = new MaxQueue<File>( 20 );
    }

    /***************************************************************************
     * @param options
     **************************************************************************/
    public TmatsEditorOptions( TmatsEditorOptions options )
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
    public static TmatsEditorOptions getOptions()
    {
        return getSerializer().read();
    }

    /***************************************************************************
     * @param options
     **************************************************************************/
    public static void setOptions( TmatsEditorOptions options )
    {
        getSerializer().write( options );
    }

    /***************************************************************************
     * Gets (or creates) the user options for the Hexedit application.
     * @return the single user options shared by all instances of the calling
     * application.
     **************************************************************************/
    private static OptionsSerializer<TmatsEditorOptions> getSerializer()
    {
        if( OPTIONS == null )
        {
            OPTIONS = XsOptions.getOptions( TmatsEditorOptions.class,
                USER_OPTIONS_FILE, new IrisUserDataCreator() );
        }
        return OPTIONS;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class IrisUserDataCreator
        implements IOptionsCreator<TmatsEditorOptions>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public TmatsEditorOptions createDefaultOptions()
        {
            return new TmatsEditorOptions();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public TmatsEditorOptions initialize( TmatsEditorOptions options )
        {
            options = new TmatsEditorOptions( options );

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
