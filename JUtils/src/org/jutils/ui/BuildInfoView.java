package org.jutils.ui;

import java.awt.Component;
import java.awt.Dialog.ModalityType;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jutils.data.BuildInfo;
import org.jutils.ui.OkDialogView.OkDialogButtons;
import org.jutils.ui.fields.StringFormField;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BuildInfoView implements IDataView<BuildInfo>
{
    /**  */
    private final JPanel view;
    /**  */
    private final StringFormField versionField;
    /**  */
    private final StringFormField dateField;

    /**  */
    private BuildInfo info;

    /***************************************************************************
     * 
     **************************************************************************/
    public BuildInfoView()
    {
        this.versionField = new StringFormField( "Version" );
        this.dateField = new StringFormField( "Build Date" );

        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        StandardFormView form = new StandardFormView();

        versionField.setEditable( false );
        dateField.setEditable( false );

        form.addField( versionField );
        form.addField( dateField );

        return form.getView();
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
    public BuildInfo getData()
    {
        return info;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( BuildInfo info )
    {
        this.info = info;
        versionField.setValue( info.version );
        dateField.setValue( info.buildDate );
    }

    /***************************************************************************
     * @param info
     **************************************************************************/
    public static void show( BuildInfo info )
    {
        show( null, info );
    }

    /***************************************************************************
     * @param parent
     **************************************************************************/
    private static void show( Component parent, BuildInfo info )
    {
        BuildInfoView view = new BuildInfoView();
        OkDialogView dialog = new OkDialogView( parent, view.createView(),
            ModalityType.DOCUMENT_MODAL, OkDialogButtons.OK_ONLY );

        view.setData( info );

        dialog.show( "JUtils Build Info", null );
    }
}
