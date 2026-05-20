package jutils.core.pcapng.ui;

import java.io.File;

import jutils.core.io.IOUtils;
import jutils.core.io.options.OptionsSerializer;
import jutils.core.io.xs.XsOptions;
import jutils.core.utils.MaxQueue;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PcapngOptions
{
    /**  */
    public final MaxQueue<File> recentDirs;

    /***************************************************************************
     * 
     **************************************************************************/
    public PcapngOptions()
    {
        this.recentDirs = new MaxQueue<File>( 20 );
    }

    /**  */
    private static final File OPTIONS_FILE;
    /**  */
    private static final OptionsSerializer<PcapngOptions> SERIALIZER;

    static
    {
        OPTIONS_FILE = IOUtils.getUsersFile( ".jutils", "pcap_options.xml" );
        SERIALIZER = XsOptions.getOptions( PcapngOptions.class, OPTIONS_FILE );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static PcapngOptions read()
    {
        return SERIALIZER.getOptions();
    }

    /***************************************************************************
     * @param options
     **************************************************************************/
    public static void write( PcapngOptions options )
    {
        SERIALIZER.write( options );
    }
}
