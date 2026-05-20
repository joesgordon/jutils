package jutils.telemetry.ch09.io.ascii;

import jutils.telemetry.ch09.DataSource;
import jutils.telemetry.ch09.GeneralInformation;

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

        info.dataSourceCount = store.readItems( "POC\\N", info.dataSources,
            dsReader, () -> new DataSource() );

        testInfoReader.read( info.testInfo, store );

        info.checksum = store.getString( "SHA" );
        info.comments = store.getString( "COM" );
    }
}
