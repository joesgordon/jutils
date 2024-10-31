package jutils.telemetry.data.ch09;

import java.util.ArrayList;
import java.util.List;

import jutils.core.io.FieldPrinter;
import jutils.core.io.FieldPrinter.ITierPrinter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Tmats implements ITierPrinter
{
    /**  */
    public static final String EXT_TMT = "tmt";
    /**  */
    public static final String EXT_TMA = "tma";
    /**  */
    public static final String [] EXTENSIONS = { EXT_TMT, EXT_TMA };

    /**  */
    public final GeneralInformation general;
    /**  */
    public final List<Transmission> transmissions;
    /**  */
    public final List<Recorder> recorders;
    /**  */
    public final List<Multiplex> multiplexes;
    /**  */
    public final List<Pcm> pcms;
    /**  */
    public final List<PcmMeasurement> pcmMeasurements;

    /***************************************************************************
     * 
     **************************************************************************/
    public Tmats()
    {
        this.general = new GeneralInformation();
        this.transmissions = new ArrayList<>();
        this.recorders = new ArrayList<>();
        this.multiplexes = new ArrayList<>();
        this.pcms = new ArrayList<>();
        this.pcmMeasurements = new ArrayList<>();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printTier( "General Information", general );
        printer.printTiers( "Transmission Attributes", transmissions );
        printer.printTiers( "Recorder-Reproducer Attributes", recorders );
        printer.printTiers( "Multiplex/Modulation Attributes", multiplexes );
        printer.printTiers( "PCM Format Attributes", pcms );
        printer.printTiers( "PCM Measurement Description Attributes",
            pcmMeasurements );
    }
}
