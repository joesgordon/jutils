package jutils.core.pcap.ui;

import java.io.File;

import jutils.core.io.IOUtils;
import jutils.core.io.options.OptionsSerializer;
import jutils.core.io.xs.XsOptions;
import jutils.core.utils.MaxQueue;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PcapOptions
{
    /**  */
    public final MaxQueue<File> recentDirs;

    /***************************************************************************
     * 
     **************************************************************************/
    public PcapOptions()
    {
        this.recentDirs = new MaxQueue<File>( 20 );
    }

    /**  */
    private static final File OPTIONS_FILE;
    /**  */
    private static final OptionsSerializer<PcapOptions> SERIALIZER;

    static
    {
        OPTIONS_FILE = IOUtils.getUsersFile( ".jutils", "pcap_options.xml" );
        SERIALIZER = XsOptions.getOptions( PcapOptions.class, OPTIONS_FILE );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static PcapOptions read()
    {
        return SERIALIZER.getOptions();
    }

    /***************************************************************************
     * @param options
     **************************************************************************/
    public static void write( PcapOptions options )
    {
        SERIALIZER.write( options );
    }
}
