package org.jutils.chart.ui;

import java.awt.Component;

import org.jutils.chart.data.SaveOptions;
import org.jutils.io.parsers.ExistenceType;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.fields.FileFormField;
import org.jutils.ui.fields.IntegerFormField;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SaveView implements IDataView<SaveOptions>
{
    /**  */
    private final FileFormField outputField;
    /**  */
    private final IntegerFormField widthField;
    /**  */
    private final IntegerFormField heightField;

    /**  */
    private SaveOptions options;

    /***************************************************************************
     * 
     **************************************************************************/
    public SaveView()
    {
        this.outputField = new FileFormField( "File",
            ExistenceType.DO_NOT_CHECK, true, true );
        this.widthField = new IntegerFormField( "Width", "px" );
        this.heightField = new IntegerFormField( "Height", "px" );

        setData( new SaveOptions() );

        outputField.setUpdater( ( f ) -> options.file = f );
        widthField.setUpdater( ( i ) -> options.size.width = i );
        heightField.setUpdater( ( i ) -> options.size.height = i );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Component getView()
    {
        StandardFormView form = new StandardFormView();

        form.addField( outputField );
        form.addField( widthField );
        form.addField( heightField );

        return form.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public SaveOptions getData()
    {
        return options;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( SaveOptions data )
    {
        this.options = data;

        outputField.setValue( data.file );
        widthField.setValue( data.size.width );
        heightField.setValue( data.size.height );
    }
}
