package jutils.telemetry.ch09.ui;

import java.awt.Component;

import javax.swing.JComponent;

import jutils.core.timestamps.ui.DateField;
import jutils.core.ui.StandardFormView;
import jutils.core.ui.fields.IntegerFormField;
import jutils.core.ui.fields.StringFormField;
import jutils.core.ui.model.IDataView;
import jutils.telemetry.ch09.Information;

/*******************************************************************************
 * 
 ******************************************************************************/
public class InformationView implements IDataView<Information>
{
    /**  */
    private final JComponent view;

    /** */
    private final StringFormField filenameField;
    /** */
    private final StringFormField revisionLevelField;
    /** */
    private final DateField originationDateField;
    /** */
    private final IntegerFormField revisionNumberField;

    /**  */
    private Information info;

    /***************************************************************************
     * 
     **************************************************************************/
    public InformationView()
    {
        this.filenameField = new StringFormField( "Filename", 0, 256 );
        this.revisionLevelField = new StringFormField( "Revision Level", 2, 2 );
        this.originationDateField = new DateField( "Origination Date" );
        this.revisionNumberField = new IntegerFormField( "Revision Number", 0,
            9999 );

        this.view = createView();

        setData( new Information() );

        filenameField.setUpdater( ( d ) -> info.filename = d );
        revisionLevelField.setUpdater( ( d ) -> info.revisionLevel = d );
        originationDateField.setUpdater( ( d ) -> info.originationDate = d );
        revisionNumberField.setUpdater( ( d ) -> info.revisionNumber = d );

        setEditable( true );
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    private void setEditable( boolean editable )
    {
        filenameField.setEditable( editable );
        revisionLevelField.setEditable( editable );
        originationDateField.setEditable( editable );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        StandardFormView form = new StandardFormView();

        form.addField( filenameField );
        form.addField( revisionLevelField );
        form.addField( originationDateField );

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
    public Information getData()
    {
        return info;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( Information data )
    {
        this.info = data;

        filenameField.setValue( info.filename );
        revisionLevelField.setValue( info.revisionLevel );
        originationDateField.setValue( info.originationDate );
    }
}
