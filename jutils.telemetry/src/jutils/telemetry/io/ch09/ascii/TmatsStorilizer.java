package jutils.telemetry.io.ch09.ascii;

import jutils.telemetry.data.ch09.DataSource;
import jutils.telemetry.data.ch09.Recorder;
import jutils.telemetry.data.ch09.Tmats;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TmatsStorilizer implements IStorilizer<Tmats>
{
    /**  */
    private final GeneralInfoStorilizer generalStorilizer;
    /**  */
    private final RecorderStorilizer recorderStorilizer;

    /***************************************************************************
     * 
     **************************************************************************/
    public TmatsStorilizer()
    {
        this.generalStorilizer = new GeneralInfoStorilizer();
        this.recorderStorilizer = new RecorderStorilizer();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void read( Tmats setup, AsciiStore store )
    {
        // LogUtils.printDebug( "%s", FieldPrinter.toString( store ) );
        generalStorilizer.read( setup.general, store.createSubstore( "G\\" ) );

        int recorderCount = 0;

        for( DataSource ds : setup.general.dataSources )
        {
            switch( ds.type )
            {
                case DIRECT_SRC:
                    break;

                case DIST_SRC:
                    break;

                case OTHER:
                    recorderCount++;
                    break;

                case REPRODUCER:
                    break;

                case RF:
                    break;

                case STORAGE:
                    recorderCount++;
                    break;

                case TAPE:
                    recorderCount++;
                    break;
            }
        }

        setup.recorders.clear();
        for( int i = 0; i < recorderCount; i++ )
        {
            int recorderNum = i + 1;
            String storeName = "R-" + recorderNum + "\\";
            AsciiStore recorderStore = store.createSubstore( storeName );

            Recorder r = new Recorder();

            recorderStorilizer.read( r, recorderStore );

            setup.recorders.add( r );
        }
    }
}
