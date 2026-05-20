package jutils.telemetry.ch09.ui;

import java.awt.Component;

import javax.swing.JComponent;

import jutils.core.ui.StandardFormView;
import jutils.core.ui.fields.IntegerFormField;
import jutils.core.ui.fields.StringFormField;
import jutils.core.ui.model.IDataView;
import jutils.telemetry.ch09.GeneralInformation;

/*******************************************************************************
 * 
 ******************************************************************************/
public class GeneralInformationView implements IDataView<GeneralInformation>
{
    /**  */
    private final JComponent view;

    /**  */
    private final StringFormField programNameField;
    /**  */
    private final StringFormField testItemField;
    /**  */
    private final IntegerFormField dataSourceCountField;
    /**  */
    private final StringFormField checksumField;

    /**  */
    private GeneralInformation general;

    /***************************************************************************
     * 
     **************************************************************************/
    public GeneralInformationView()
    {
        this.programNameField = new StringFormField( "Program Name", 0, 16 );
        this.testItemField = new StringFormField( "Test Item", 0, 64 );
        this.dataSourceCountField = new IntegerFormField( "Data Source Count" );
        this.checksumField = new StringFormField( "Checksum" );

        this.view = createView();

        setData( new GeneralInformation() );

        programNameField.setUpdater( ( d ) -> general.programName = d );
        testItemField.setUpdater( ( d ) -> general.testItem = d );

        setEditable( true );
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    private void setEditable( boolean editable )
    {
        programNameField.setEditable( editable );
        testItemField.setEditable( editable );
        dataSourceCountField.setEditable( false );
        checksumField.setEditable( false );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        StandardFormView form = new StandardFormView();

        form.addField( programNameField );
        form.addField( testItemField );
        form.addField( dataSourceCountField );
        form.addField( checksumField );

        return form.getView();
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
    public GeneralInformation getData()
    {
        return general;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( GeneralInformation data )
    {
        this.general = data;

        this.programNameField.setValue( general.programName );
        this.testItemField.setValue( general.testItem );
        this.dataSourceCountField.setValue( general.dataSourceCount );
        this.checksumField.setValue( general.checksum );
    }
}
