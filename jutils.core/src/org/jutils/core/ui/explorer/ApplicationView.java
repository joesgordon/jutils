package org.jutils.core.ui.explorer;

import java.awt.Component;
import java.io.File;

import javax.swing.JPanel;

import org.jutils.core.io.parsers.ExistenceType;
import org.jutils.core.io.parsers.FileType;
import org.jutils.core.ui.StandardFormView;
import org.jutils.core.ui.explorer.data.ApplicationConfig;
import org.jutils.core.ui.fields.FileFormField;
import org.jutils.core.ui.fields.StringFormField;
import org.jutils.core.ui.model.IDataView;

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
        this.pathField = new FileFormField( "Path", FileType.FILE,
            ExistenceType.EXISTS, true );
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
