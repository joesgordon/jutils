package jutils.telemetry.io.ch09;

import jutils.telemetry.data.ch09.DataSource;
import jutils.telemetry.data.ch09.GeneralInformation;

/*******************************************************************************
 * 
 ******************************************************************************/
public class GeneralInfoAsciiReader implements IAsciiReader<GeneralInformation>
{
    /**  */
    private final InformationAsciiReader informationReader;
    /**  */
    private final DataSourceAsciiReader dsReader;
    /**  */
    private final TestInformationAsciiReader testInfoReader;

    /***************************************************************************
     * 
     **************************************************************************/
    public GeneralInfoAsciiReader()
    {
        this.informationReader = new InformationAsciiReader();
        this.dsReader = new DataSourceAsciiReader();
        this.testInfoReader = new TestInformationAsciiReader();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void read( GeneralInformation info, AsciiStore store )
    {
        Integer x;

        info.programName = store.getString( "G\\PN" );
        info.testItem = store.getString( "G\\TA" );

        informationReader.read( info.information, store );

        info.dataSources.clear();
        x = store.getInteger( "G\\DSI\\N" );

        if( x != null )
        {
            for( int i = 0; i < x; i++ )
            {
                DataSource ds = new DataSource();

                dsReader.read( ds, store, i + 1 );

                info.dataSources.add( ds );
            }
        }

        testInfoReader.read( info.testInfo, store );

        info.checksum = store.getString( "G\\SHA" );
        info.comments = store.getString( "G\\COM" );
    }
}
