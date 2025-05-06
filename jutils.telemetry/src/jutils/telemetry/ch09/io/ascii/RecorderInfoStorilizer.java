package jutils.telemetry.ch09.io.ascii;

import jutils.telemetry.ch09.Ethernet;
import jutils.telemetry.ch09.HwModule;
import jutils.telemetry.ch09.RecorderInfo;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RecorderInfoStorilizer implements IStorilizer<RecorderInfo>
{
    /**  */
    private final PocStorilizer pocStorilizer;
    /**  */
    private final HwModuleStorilizer moduleStorilizer;
    /**  */
    private final RmmStorilizer rmmStorilizer;
    /**  */
    private final EthernetStorilizer ethStorilizer;

    /***************************************************************************
     * 
     **************************************************************************/
    public RecorderInfoStorilizer()
    {
        this.pocStorilizer = new PocStorilizer();
        this.moduleStorilizer = new HwModuleStorilizer();
        this.rmmStorilizer = new RmmStorilizer();
        this.ethStorilizer = new EthernetStorilizer();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void read( RecorderInfo info, AsciiStore store )
    {
        info.manufacturer = store.getString( "RI1" );
        info.model = store.getString( "RI2" );
        info.isOriginal = store.getString( "RI3" );
        info.originalDateTime = store.getString( "RI4" );

        pocStorilizer.read( info.creatingPoc, store );

        info.copyDateTime = store.getString( "RI5" );

        pocStorilizer.read( info.copyingPoc, store.createSubstore( "D" ) );

        info.isModified = store.getString( "RI6" );
        info.modificationType = store.getString( "RI7" );
        info.modificationDateTime = store.getString( "RI8" );

        pocStorilizer.read( info.modifyingPoc, store.createSubstore( "M" ) );

        info.isContinuous = store.getString( "CRE" );
        info.setupSource = store.getString( "RSS" );
        info.serialNumber = store.getString( "RI9" );
        info.firmwareRevision = store.getString( "RI10" );

        info.moduleCount = store.readItems( "RIM\\N", info.modules,
            moduleStorilizer, () -> new HwModule() );

        info.rmmCount = store.readItems( "RMM\\N", info.rmms, rmmStorilizer,
            () -> new HwModule() );

        info.ethernetCount = store.readItems( "EI\\N", info.ethernets,
            ethStorilizer, () -> new Ethernet() );
    }
}
