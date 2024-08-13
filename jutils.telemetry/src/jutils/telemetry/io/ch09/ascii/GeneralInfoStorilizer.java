package jutils.telemetry.io.ch09.ascii;

import jutils.telemetry.data.ch09.DataSource;
import jutils.telemetry.data.ch09.GeneralInformation;

/*******************************************************************************
 * 
 ******************************************************************************/
public class GeneralInfoStorilizer implements IStorilizer<GeneralInformation>
{
    /**  */
    private final InformationStorilizer informationReader;
    /**  */
    private final DataSourceStorilizer dsReader;
    /**  */
    private final TestInformationStorilizer testInfoReader;

    /***************************************************************************
     * 
     **************************************************************************/
    public GeneralInfoStorilizer()
    {
        this.informationReader = new InformationStorilizer();
        this.dsReader = new DataSourceStorilizer();
        this.testInfoReader = new TestInformationStorilizer();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void read( GeneralInformation info, AsciiStore store )
    {
        info.programName = store.getString( "PN" );
        info.testItem = store.getString( "TA" );

        informationReader.read( info.information, store );

        info.dataSources.clear();
        info.dataSourceCount = store.getInteger( "DSI\\N" );

        if( info.dataSourceCount != null )
        {
            for( int i = 0; i < info.dataSourceCount; i++ )
            {
                int dsNum = i + 1;
                AsciiStore dsStore = store.createSubstore( "", "-" + dsNum );
                DataSource ds = new DataSource();

                dsReader.read( ds, dsStore );

                info.dataSources.add( ds );
            }
        }

        testInfoReader.read( info.testInfo, store );

        info.checksum = store.getString( "SHA" );
        info.comments = store.getString( "COM" );
    }
}
