package jutils.telemetry.ch10;

import jutils.core.io.FieldPrinter;
import jutils.core.io.FieldPrinter.ITierPrinter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Pcm1SpecificData implements ITierPrinter
{
    /**  */
    public int syncOffset;
    /**  */
    public boolean unpackedMode;
    /**  */
    public boolean packedMode;
    /**  */
    public boolean throughputMode;
    /**  */
    public boolean alignmentMode;
    /**  */
    public int modeReserved;
    /**  */
    public MajorLockStatus majorStatus;
    /**  */
    public MinorLockStatus minorStatus;
    /**  */
    public boolean minorIndicator;
    /**  */
    public boolean majorIndicator;
    /**  */
    public boolean iphIndicator;
    /**  */
    public int reserved;

    /***************************************************************************
     * 
     **************************************************************************/
    public Pcm1SpecificData()
    {
        this.syncOffset = 0;
        this.unpackedMode = false;
        this.packedMode = false;
        this.throughputMode = true;
        this.alignmentMode = false;
        this.modeReserved = 0;
        this.majorStatus = MajorLockStatus.NOT_LOCKED;
        this.minorStatus = MinorLockStatus.CHECK;
        this.minorIndicator = false;
        this.majorIndicator = false;
        this.iphIndicator = false;
        this.reserved = 0;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printField( "SYNC Offset", syncOffset );
        printer.printField( "Unpacked Mode", unpackedMode );
        printer.printField( "Packed Mode", packedMode );
        printer.printField( "Throughput Mode", throughputMode );
        printer.printField( "Alignment Mode", alignmentMode );
        printer.printField( "Mode (Reserved)", modeReserved );
        printer.printField( "Major Lock Status", majorStatus );
        printer.printField( "Minor Lock Status", minorStatus );
        printer.printField( "Minor Indicator", minorIndicator );
        printer.printField( "Major Indicator", majorIndicator );
        printer.printField( "Intra-Packet Header Indicator", iphIndicator );
        printer.printField( "Reserved", reserved );

    }

    /***************************************************************************
     * Returns {@code true} if the {@link #alignmentMode} is {@code false}.
     * @return {@code true} if the PCM data is 16-bit aligned.
     **************************************************************************/
    public boolean is16BitAligned()
    {
        return alignmentMode == false;
    }

    /***************************************************************************
     * Returns {@code true} if the {@link #alignmentMode} is {@code true}.
     * @return {@code true} if the PCM data is 32-bit aligned.
     **************************************************************************/
    public boolean is32BitAligned()
    {
        return alignmentMode == true;
    }
}
