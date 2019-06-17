package org.jutils.ui.explorer;

import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.io.*;

import javax.swing.*;

import org.jutils.SwingUtils;
import org.jutils.ValidationException;
import org.jutils.io.XStreamUtils;
import org.jutils.ui.OkDialogView;
import org.jutils.ui.OkDialogView.OkDialogButtons;
import org.jutils.ui.explorer.data.AppManagerConfig;
import org.jutils.ui.fields.BooleanFormField;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * 
 *
 ******************************************************************************/
public class AppManagerView implements IDataView<AppManagerConfig>
{
    /**  */
    private final JPanel view;
    /**  */
    private final BooleanFormField useCustomField;
    /**  */
    private final ExtensionsView extsView;
    /**  */
    private final ApplicationsView appsView;

    /**  */
    private AppManagerConfig config;

    /***************************************************************************
     * @param parent
     **************************************************************************/
    public AppManagerView()
    {
        this.useCustomField = new BooleanFormField(
            "Use Custom Application Manager" );
        this.extsView = new ExtensionsView();
        this.appsView = new ApplicationsView();

        this.view = createView();

        setData( new AppManagerConfig() );

        useCustomField.setUpdater( ( b ) -> {
            config.useCustom = b;
            setComponentsUsingAppManager( b );
        } );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel contentPanel = new JPanel( new GridBagLayout() );
        JTabbedPane tabs = new JTabbedPane();

        useCustomField.getView().setText( useCustomField.getName() );

        tabs.addTab( "Extensions", extsView.getView() );
        tabs.addTab( "Applications", appsView.getView() );

        contentPanel.add( useCustomField.getView(),
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets( 6, 6, 6, 6 ), 0, 0 ) );

        contentPanel.add( tabs,
            new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets( 0, 6, 6, 6 ), 0, 0 ) );

        return contentPanel;
    }

    /***************************************************************************
     * @param frame
     * @return
     **************************************************************************/
    public static AppManagerConfig showDialog( JFrame parent )
    {
        AppManagerView dialog = new AppManagerView();
        OkDialogView dialogView = new OkDialogView( parent, dialog.getView(),
            ModalityType.DOCUMENT_MODAL, OkDialogButtons.OK_ONLY );

        dialogView.setSize( 600, 400 );

        return dialog.getData();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public AppManagerConfig getData()
    {
        return config;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( AppManagerConfig data )
    {
        this.config = data;

        useCustomField.setValue( data.useCustom );

        extsView.setData( data.exts );
        appsView.setData( data.apps );

        setComponentsUsingAppManager( data.useCustom );
    }

    private void setComponentsUsingAppManager( boolean using )
    {
        this.extsView.setEnabled( using );
        this.appsView.setEnabled( using );
    }

    public void openFile( File file )
    {
        try
        {
            AppManagerConfig cfg = XStreamUtils.readObjectXStream( file );

            setData( cfg );
        }
        catch( ValidationException ex )
        {
            SwingUtils.showErrorMessage( getView(),
                "Error reading from file: " + file.getAbsolutePath(),
                "Read Error" );
        }
        catch( FileNotFoundException ex )
        {
            SwingUtils.showErrorMessage( getView(),
                "File not found: " + file.getAbsolutePath(),
                "File Not Found Error" );
        }
        catch( IOException ex )
        {
            SwingUtils.showErrorMessage( getView(),
                "Error reading from file: " + file.getAbsolutePath(),
                "I/O Error" );
        }
    }

    public void saveFile( File file )
    {
        AppManagerConfig cfg = getData();

        try
        {
            XStreamUtils.writeObjectXStream( cfg, file );
        }
        catch( IOException e )
        {
            SwingUtils.showErrorMessage( getView(),
                "Error reading from file: " + file.getAbsolutePath(),
                "I/O Error" );
        }
    }
}
