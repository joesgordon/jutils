package jutils.platform.ui;

import javax.swing.JComponent;

import jutils.core.io.parsers.IntegerParser;
import jutils.core.ui.StandardFormView;
import jutils.core.ui.fields.BooleanFormField;
import jutils.core.ui.fields.ComboFormField;
import jutils.core.ui.fields.NamedItemDescriptor;
import jutils.core.ui.model.IDataView;
import jutils.platform.data.BaudRate;
import jutils.platform.data.DtrControl;
import jutils.platform.data.Parity;
import jutils.platform.data.RtsControl;
import jutils.platform.data.SerialParams;
import jutils.platform.data.StopBits;
import jutils.platform.data.WordSize;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SerialParamsView implements IDataView<SerialParams>
{
    /**  */
    private final JComponent view;

    /**  */
    private final BooleanFormField binaryModeField;
    /**  */
    private final ComboFormField<Integer> baudField;
    /**  */
    private final ComboFormField<WordSize> wordSizeField;
    /**  */
    private final ComboFormField<Parity> parityField;
    /**  */
    private final ComboFormField<StopBits> stopBitsField;
    /**  */
    private final BooleanFormField ctsField;
    /**  */
    private final BooleanFormField dsrField;
    /**  */
    private final ComboFormField<DtrControl> dtrField;
    /**  */
    private final ComboFormField<RtsControl> rtsField;
    /**  */
    private final BooleanFormField swFlowOutField;
    /**  */
    private final BooleanFormField swFlowInField;

    /**  */
    private SerialParams data;

    /***************************************************************************
     * 
     **************************************************************************/
    public SerialParamsView()
    {
        this.binaryModeField = new BooleanFormField( "Binary Mode Enabled" );
        this.baudField = new ComboFormField<>( "Baud Rate",
            BaudRate.getBauds() );
        this.wordSizeField = new ComboFormField<>( "Word Size",
            WordSize.values(), new NamedItemDescriptor<>() );
        this.parityField = new ComboFormField<>( "Parity", Parity.values(),
            new NamedItemDescriptor<>() );
        this.stopBitsField = new ComboFormField<>( "Stop Bits",
            StopBits.values(), new NamedItemDescriptor<>() );
        this.ctsField = new BooleanFormField( "CTS Enabled" );
        this.dsrField = new BooleanFormField( "DSR Enabled" );
        this.dtrField = new ComboFormField<>( "DTR Control",
            DtrControl.values(), new NamedItemDescriptor<>() );
        this.rtsField = new ComboFormField<>( "RTS Control",
            RtsControl.values(), new NamedItemDescriptor<>() );
        this.swFlowOutField = new BooleanFormField( "SW Flow Output Enabled" );
        this.swFlowInField = new BooleanFormField( "SW Flow Input Enabled" );

        this.view = createView();

        setData( new SerialParams() );

        baudField.setUserEditable(
            new IntegerParser( BaudRate.B110.value, null ) );

        binaryModeField.setUpdater( ( d ) -> data.binaryModeEnabled = d );
        baudField.setUpdater( ( d ) -> data.baudRate = d );
        wordSizeField.setUpdater( ( d ) -> data.size = d );
        parityField.setUpdater( ( d ) -> data.parity = d );
        stopBitsField.setUpdater( ( d ) -> data.stopBits = d );
        ctsField.setUpdater( ( d ) -> data.ctsEnabled = d );
        dsrField.setUpdater( ( d ) -> data.dsrEnabled = d );
        dtrField.setUpdater( ( d ) -> data.dtrControl = d );
        rtsField.setUpdater( ( d ) -> data.rtsControl = d );
        swFlowOutField.setUpdater( ( d ) -> data.swFlowOutputEnabled = d );
        swFlowInField.setUpdater( ( d ) -> data.swFlowInputEnabled = d );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        StandardFormView form = new StandardFormView();

        form.addField( binaryModeField );
        form.addField( baudField );
        form.addField( wordSizeField );
        form.addField( parityField );
        form.addField( stopBitsField );
        form.addField( ctsField );
        form.addField( dsrField );
        form.addField( dtrField );
        form.addField( rtsField );
        form.addField( swFlowOutField );
        form.addField( swFlowInField );

        return form.getView();
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    public void setEditable( boolean editable )
    {
        binaryModeField.setEditable( editable );
        baudField.setEditable( editable );
        wordSizeField.setEditable( editable );
        parityField.setEditable( editable );
        stopBitsField.setEditable( editable );
        ctsField.setEditable( editable );
        dsrField.setEditable( editable );
        dtrField.setEditable( editable );
        rtsField.setEditable( editable );
        swFlowOutField.setEditable( editable );
        swFlowInField.setEditable( editable );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public SerialParams getData()
    {
        return data;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( SerialParams config )
    {
        this.data = config;

        binaryModeField.setValue( config.binaryModeEnabled );
        baudField.setValue( config.baudRate );
        wordSizeField.setValue( config.size );
        parityField.setValue( config.parity );
        stopBitsField.setValue( config.stopBits );
        ctsField.setValue( config.ctsEnabled );
        dsrField.setValue( config.dsrEnabled );
        dtrField.setValue( config.dtrControl );
        rtsField.setValue( config.rtsControl );
        swFlowOutField.setValue( config.swFlowOutputEnabled );
        swFlowInField.setValue( config.swFlowInputEnabled );
    }
}
