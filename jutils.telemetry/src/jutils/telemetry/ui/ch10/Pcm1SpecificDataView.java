package jutils.telemetry.ui.ch10;

import java.awt.Component;

import javax.swing.JComponent;

import jutils.core.ui.StandardFormView;
import jutils.core.ui.fields.BooleanFormField;
import jutils.core.ui.fields.ComboFormField;
import jutils.core.ui.fields.IntegerFormField;
import jutils.core.ui.fields.NamedItemDescriptor;
import jutils.core.ui.model.IDataView;
import jutils.telemetry.data.ch10.MajorLockStatus;
import jutils.telemetry.data.ch10.MinorLockStatus;
import jutils.telemetry.data.ch10.Pcm1SpecificData;
import jutils.telemetry.data.ch10.Pcm1Word;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Pcm1SpecificDataView implements IDataView<Pcm1SpecificData>
{
    /**  */
    private final JComponent view;
    /**  */
    private final IntegerFormField syncOffsetField;
    /**  */
    private final BooleanFormField unpackedModeField;
    /**  */
    private final BooleanFormField packedModeField;
    /**  */
    private final BooleanFormField throughputModeField;
    /**  */
    private final BooleanFormField alignmentModeField;
    /**  */
    private final IntegerFormField modeReservedField;
    /**  */
    private final ComboFormField<MajorLockStatus> majorStatusField;
    /**  */
    private final ComboFormField<MinorLockStatus> minorStatusField;
    /**  */
    private final BooleanFormField minorIndicatorField;
    /**  */
    private final BooleanFormField majorIndicatorField;
    /**  */
    private final BooleanFormField iphIndicatorField;
    /**  */
    private final IntegerFormField reservedField;

    /**  */
    private Pcm1SpecificData specificData;

    /***************************************************************************
     * 
     **************************************************************************/
    public Pcm1SpecificDataView()
    {
        this.syncOffsetField = new IntegerFormField( "SYNC Offset", 0,
            ( int )Pcm1Word.SYNC_OFFSET.getMax() );
        this.unpackedModeField = new BooleanFormField( "Unpacked Mode" );
        this.packedModeField = new BooleanFormField( "Packed Mode" );
        this.throughputModeField = new BooleanFormField( "Throughput Mode" );
        this.alignmentModeField = new BooleanFormField( "Alignment Mode" );
        this.modeReservedField = new IntegerFormField( "Mode (Reserved)", 0,
            ( int )Pcm1Word.MODE_RESERVED.getMax() );
        this.majorStatusField = new ComboFormField<>( "Major Lock Status",
            MajorLockStatus.values(), new NamedItemDescriptor<>() );
        this.minorStatusField = new ComboFormField<>( "Minor Lock Status",
            MinorLockStatus.values(), new NamedItemDescriptor<>() );
        this.minorIndicatorField = new BooleanFormField( "Alignment Mode" );
        this.majorIndicatorField = new BooleanFormField( "Alignment Mode" );
        this.iphIndicatorField = new BooleanFormField( "Alignment Mode" );
        this.reservedField = new IntegerFormField( "Reserved", 0,
            ( int )Pcm1Word.RESERVED.getMax() );

        this.view = createView();

        setData( new Pcm1SpecificData() );

        setEditable( false );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        StandardFormView form = new StandardFormView();

        form.addField( syncOffsetField );
        form.addField( unpackedModeField );
        form.addField( packedModeField );
        form.addField( throughputModeField );
        form.addField( alignmentModeField );
        form.addField( modeReservedField );
        form.addField( majorStatusField );
        form.addField( minorStatusField );
        form.addField( minorIndicatorField );
        form.addField( majorIndicatorField );
        form.addField( iphIndicatorField );
        form.addField( reservedField );

        return form.getView();
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    public void setEditable( boolean editable )
    {
        syncOffsetField.setEditable( editable );
        unpackedModeField.setEditable( editable );
        packedModeField.setEditable( editable );
        throughputModeField.setEditable( editable );
        alignmentModeField.setEditable( editable );
        modeReservedField.setEditable( editable );
        majorStatusField.setEditable( editable );
        minorStatusField.setEditable( editable );
        minorIndicatorField.setEditable( editable );
        majorIndicatorField.setEditable( editable );
        iphIndicatorField.setEditable( editable );
        reservedField.setEditable( editable );
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
    public Pcm1SpecificData getData()
    {
        return this.specificData;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( Pcm1SpecificData data )
    {
        this.specificData = data;

        syncOffsetField.setValue( specificData.syncOffset );
        unpackedModeField.setValue( specificData.unpackedMode );
        packedModeField.setValue( specificData.packedMode );
        throughputModeField.setValue( specificData.throughputMode );
        alignmentModeField.setValue( specificData.alignmentMode );
        modeReservedField.setValue( specificData.modeReserved );
        majorStatusField.setValue( specificData.majorStatus );
        minorStatusField.setValue( specificData.minorStatus );
        minorIndicatorField.setValue( specificData.minorIndicator );
        majorIndicatorField.setValue( specificData.majorIndicator );
        iphIndicatorField.setValue( specificData.iphIndicator );
        reservedField.setValue( specificData.reserved );
    }
}
