package jutils.telemetry.ch09.ui;

import java.awt.Component;

import javax.swing.JComponent;

import jutils.core.ui.StandardFormView;
import jutils.core.ui.fields.FileFormField;
import jutils.core.ui.model.IDataView;
import jutils.telemetry.ch09.TmatsFile;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TmatsFileView implements IDataView<TmatsFile>
{
    /**  */
    private final JComponent view;
    /**  */
    private final FileFormField fileField;

    /**  */
    private TmatsFile file;

    /***************************************************************************
     * 
     **************************************************************************/
    public TmatsFileView()
    {
        this.fileField = new FileFormField( "File" );

        this.view = createView();

        setData( new TmatsFile() );

        fileField.setEditable( false );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        StandardFormView form = new StandardFormView();

        form.addField( fileField );

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
    public TmatsFile getData()
    {
        return file;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( TmatsFile data )
    {
        this.file = data;

        // TODO Auto-generated method stub
    }
}
