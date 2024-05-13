package jutils.ch10.ui;

import java.awt.Component;

import javax.swing.JComponent;

import jutils.core.ui.StandardFormView;
import jutils.core.ui.fields.BooleanFormField;
import jutils.core.ui.fields.ComboFormField;
import jutils.core.ui.fields.DecimalByteFormField;
import jutils.core.ui.fields.HexShortFormField;
import jutils.core.ui.fields.IntegerFormField;
import jutils.core.ui.fields.LongFormField;
import jutils.core.ui.fields.NamedItemDescriptor;
import jutils.core.ui.fields.ShortFormField;
import jutils.core.ui.model.IDataView;
import jutils.core.utils.BitMasks;
import jutils.telemetry.ch10.ChecksumPresence;
import jutils.telemetry.ch10.DataType;
import jutils.telemetry.ch10.DataTypeVersion;
import jutils.telemetry.ch10.PacketHeader;
import jutils.telemetry.ch10.SecHdrTimeFormat;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PacketHeaderView implements IDataView<PacketHeader>
{
    /**  */
    private final JComponent view;

    /**  */
    private final HexShortFormField syncField;
    /**  */
    private final ShortFormField channelIdField;
    /**  */
    private final IntegerFormField packetLengthField;
    /**  */
    private final IntegerFormField dataLengthField;
    /**  */
    private final ComboFormField<DataTypeVersion> dataVersionField;
    /**  */
    private final DecimalByteFormField sequenceNumField;
    /**  */
    private final ComboFormField<ChecksumPresence> checksumPresentField;
    /**  */
    private final ComboFormField<SecHdrTimeFormat> secHdrTimeFmtField;
    /**  */
    private final BooleanFormField dataOverflowField;
    /**  */
    private final BooleanFormField rtcSyncErrorField;
    /**  */
    private final BooleanFormField iptsTimeSourceField;
    /**  */
    private final BooleanFormField secHdrPresentField;
    /**  */
    private final ComboFormField<DataType> dataTypeField;
    /**  */
    private final LongFormField relativeTimeCounterField;
    /**  */
    private final HexShortFormField headerChecksumField;

    /**  */
    private PacketHeader header;

    /***************************************************************************
     * 
     **************************************************************************/
    public PacketHeaderView()
    {
        this.syncField = new HexShortFormField( "SYNC Field" );
        this.channelIdField = new ShortFormField( "Channel ID" );
        this.packetLengthField = new IntegerFormField( "Packet Length" );
        this.dataLengthField = new IntegerFormField( "Data Length" );
        this.dataVersionField = new ComboFormField<>( "",
            DataTypeVersion.values(), new NamedItemDescriptor<>() );
        this.sequenceNumField = new DecimalByteFormField( "Sequence Number" );
        this.checksumPresentField = new ComboFormField<>( "Checksum Presence",
            ChecksumPresence.values(), new NamedItemDescriptor<>() );
        this.secHdrTimeFmtField = new ComboFormField<>(
            "Secondary Header Time Format", SecHdrTimeFormat.values(),
            new NamedItemDescriptor<>() );
        this.dataOverflowField = new BooleanFormField( "Data Overflow" );
        this.rtcSyncErrorField = new BooleanFormField( "RTC Sync Error" );
        this.iptsTimeSourceField = new BooleanFormField( "IPTS Time Source" );
        this.secHdrPresentField = new BooleanFormField(
            "Secondar Header Present" );
        this.dataTypeField = new ComboFormField<>( "Data Type",
            DataType.values(), new NamedItemDescriptor<>() );
        this.relativeTimeCounterField = new LongFormField(
            "Relative Time Counter", 0L, BitMasks.getFieldMask( 48 ) );
        this.headerChecksumField = new HexShortFormField( "Checksum" );

        this.view = createView();

        setData( new PacketHeader() );

        syncField.setUpdater( ( d ) -> header.sync = d );
        channelIdField.setUpdater( ( d ) -> header.channelId = d );
        packetLengthField.setUpdater( ( d ) -> header.packetLength = d );
        dataLengthField.setUpdater( ( d ) -> header.dataLength = d );
        dataVersionField.setUpdater( ( d ) -> header.dataVersion = d );
        sequenceNumField.setUpdater( ( d ) -> header.sequenceNumber = d );
        checksumPresentField.setUpdater( ( d ) -> header.checksumPresent = d );
        secHdrTimeFmtField.setUpdater( ( d ) -> header.secHdrTimeFmt = d );
        dataOverflowField.setUpdater( ( d ) -> header.dataOverflow = d );
        rtcSyncErrorField.setUpdater( ( d ) -> header.rtcSyncError = d );
        iptsTimeSourceField.setUpdater( ( d ) -> header.iptsTimeSource = d );
        secHdrPresentField.setUpdater( ( d ) -> header.secHdrPresent = d );
        dataTypeField.setUpdater( ( d ) -> header.dataType = d );
        relativeTimeCounterField.setUpdater(
            ( d ) -> header.relativeTimeCounter = d );
        headerChecksumField.setUpdater( ( d ) -> header.checksum = d );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        StandardFormView form = new StandardFormView();

        form.addField( syncField );
        form.addField( channelIdField );
        form.addField( packetLengthField );
        form.addField( dataLengthField );
        form.addField( dataVersionField );
        form.addField( sequenceNumField );
        form.addField( checksumPresentField );
        form.addField( secHdrTimeFmtField );
        form.addField( dataOverflowField );
        form.addField( rtcSyncErrorField );
        form.addField( iptsTimeSourceField );
        form.addField( secHdrPresentField );
        form.addField( dataTypeField );
        form.addField( relativeTimeCounterField );
        form.addField( headerChecksumField );

        return form.getView();
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    public void setEditable( boolean editable )
    {
        syncField.setEditable( editable );
        channelIdField.setEditable( editable );
        packetLengthField.setEditable( editable );
        dataLengthField.setEditable( editable );
        dataVersionField.setEditable( editable );
        sequenceNumField.setEditable( editable );
        checksumPresentField.setEditable( editable );
        secHdrTimeFmtField.setEditable( editable );
        dataOverflowField.setEditable( editable );
        rtcSyncErrorField.setEditable( editable );
        iptsTimeSourceField.setEditable( editable );
        secHdrPresentField.setEditable( editable );
        dataTypeField.setEditable( editable );
        relativeTimeCounterField.setEditable( editable );
        headerChecksumField.setEditable( editable );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public PacketHeader getData()
    {
        return header;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( PacketHeader data )
    {
        this.header = data;

        syncField.setValue( header.sync );
        channelIdField.setValue( header.channelId );
        packetLengthField.setValue( header.packetLength );
        dataLengthField.setValue( header.dataLength );
        dataVersionField.setValue( header.dataVersion );
        sequenceNumField.setValue( header.sequenceNumber );
        checksumPresentField.setValue( header.checksumPresent );
        secHdrTimeFmtField.setValue( header.secHdrTimeFmt );
        dataOverflowField.setValue( header.dataOverflow );
        rtcSyncErrorField.setValue( header.rtcSyncError );
        iptsTimeSourceField.setValue( header.iptsTimeSource );
        secHdrPresentField.setValue( header.secHdrPresent );
        dataTypeField.setValue( header.dataType );
        relativeTimeCounterField.setValue( header.relativeTimeCounter );
        headerChecksumField.setValue( header.checksum );
    }
}
