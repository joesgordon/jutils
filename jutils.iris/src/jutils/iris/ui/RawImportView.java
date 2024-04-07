package jutils.iris.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JPanel;

import jutils.core.ui.OkDialogView;
import jutils.core.ui.OkDialogView.OkDialogButtons;
import jutils.core.ui.model.IView;
import jutils.iris.data.RawConfig;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RawImportView implements IView<JComponent>
{
    /**  */
    private JComponent view;
    /**  */
    private final RawConfigView configView;

    // TODO add preview panel

    /***************************************************************************
     * 
     **************************************************************************/
    public RawImportView()
    {
        this.configView = new RawConfigView();
        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( configView.getView(), BorderLayout.WEST );

        return panel;
    }

    /***************************************************************************
     * @param parent
     * @param file
     * @return
     **************************************************************************/
    public RawConfig showDialog( Component parent, File file )
    {
        RawConfig config = null;
        OkDialogView dialogView = new OkDialogView( parent, getView(),
            ModalityType.DOCUMENT_MODAL, OkDialogButtons.OK_CANCEL );

        dialogView.setTitle( "Raw Pixel Read Options for " + file.getName() );

        if( dialogView.show() )
        {
            config = configView.getData();
        }

        return config;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }
}
