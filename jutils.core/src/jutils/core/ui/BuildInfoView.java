package jutils.core.ui;

import java.awt.Component;
import java.awt.Dialog.ModalityType;

import javax.swing.JComponent;
import javax.swing.JPanel;

import jutils.core.data.BuildInfo;
import jutils.core.ui.OkDialogView.OkDialogButtons;
import jutils.core.ui.fields.StringFormField;
import jutils.core.ui.model.IDataView;

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
     * @param info
     **************************************************************************/
    public static void show( Component parent, BuildInfo info )
    {
        show( parent, "Build Info", info );
    }

    /***************************************************************************
     * @param parent
     * @param title
     * @param info
     **************************************************************************/
    public static void show( Component parent, String title, BuildInfo info )
    {
        BuildInfoView view = new BuildInfoView();
        OkDialogView dialog = new OkDialogView( parent, view.createView(),
            ModalityType.DOCUMENT_MODAL, OkDialogButtons.OK_ONLY );

        view.setData( info );

        dialog.show( title );
    }
}
