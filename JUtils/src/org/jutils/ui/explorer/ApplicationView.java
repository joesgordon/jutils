package org.jutils.ui.explorer;

import java.awt.Component;
import java.io.File;

import javax.swing.JPanel;

import org.jutils.io.parsers.ExistenceType;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.explorer.data.ApplicationConfig;
import org.jutils.ui.fields.FileFormField;
import org.jutils.ui.fields.StringFormField;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ApplicationView implements IDataView<ApplicationConfig>
{
    /**  */
    private final JPanel view;

    /**  */
    private final StringFormField nameField;

    /**  */
    private final FileFormField pathField;

    /**  */
    private final StringFormField argsField;

    /**  */
    private ApplicationConfig app;

    /***************************************************************************
     * 
     **************************************************************************/
    public ApplicationView()
    {
        this.nameField = new StringFormField( "Name" );
        this.pathField = new FileFormField( "Path", ExistenceType.FILE_ONLY,
            true );
        this.argsField = new StringFormField( "Arguments", 0, null );

        this.view = createView();

        setData( new ApplicationConfig( "Notepad",
            new File( "/Windows/System32/notepad.exe" ),
            "Opens/Edits ASCII files" ) );

        nameField.setUpdater( ( s ) -> app.name = s );
        pathField.setUpdater( ( f ) -> app.path = f );
        argsField.setUpdater( ( s ) -> app.args = s );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        StandardFormView form = new StandardFormView();

        form.addField( nameField );
        form.addField( pathField );
        form.addField( argsField );

        return form.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public ApplicationConfig getData()
    {
        return app;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( ApplicationConfig data )
    {
        this.app = data;

        nameField.setValue( data.name );
        pathField.setValue( data.path );
        argsField.setValue( data.args );
    }
}
