package jutils.plot.ui;

import java.awt.Component;

import jutils.core.io.parsers.ExistenceType;
import jutils.core.io.parsers.FileType;
import jutils.core.ui.StandardFormView;
import jutils.core.ui.fields.FileFormField;
import jutils.core.ui.fields.IntegerFormField;
import jutils.core.ui.model.IDataView;
import jutils.plot.data.SaveOptions;

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
        this.outputField = new FileFormField( "File", FileType.FILE,
            ExistenceType.PARENT_EXISTS );
        this.widthField = new IntegerFormField( "Width", "px" );
        this.heightField = new IntegerFormField( "Height", "px" );

        setData( new SaveOptions() );

        outputField.addExtension( "PNG file", "png" );
        outputField.addExtension( "JPEG file", "jpg" );
        outputField.addExtension( "BMP file", "bmp" );

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
