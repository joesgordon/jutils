package jutils.core.ui.explorer;

import java.awt.Dialog.ModalityType;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import jutils.core.OptionUtils;
import jutils.core.ValidationException;
import jutils.core.io.xs.XsUtils;
import jutils.core.ui.OkDialogView;
import jutils.core.ui.OkDialogView.OkDialogButtons;
import jutils.core.ui.explorer.data.AppManagerConfig;
import jutils.core.ui.fields.BooleanFormField;
import jutils.core.ui.model.IDataView;

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
            AppManagerConfig cfg = XsUtils.readObjectXStream( file );

            setData( cfg );
        }
        catch( ValidationException ex )
        {
            OptionUtils.showErrorMessage( getView(),
                "Error reading from file: " + file.getAbsolutePath(),
                "Read Error" );
        }
        catch( FileNotFoundException ex )
        {
            OptionUtils.showErrorMessage( getView(),
                "File not found: " + file.getAbsolutePath(),
                "File Not Found Error" );
        }
        catch( IOException ex )
        {
            OptionUtils.showErrorMessage( getView(),
                "Error reading from file: " + file.getAbsolutePath(),
                "I/O Error" );
        }
    }

    public void saveFile( File file )
    {
        AppManagerConfig cfg = getData();

        try
        {
            XsUtils.writeObjectXStream( cfg, file );
        }
        catch( IOException e )
        {
            OptionUtils.showErrorMessage( getView(),
                "Error reading from file: " + file.getAbsolutePath(),
                "I/O Error" );
        }
        catch( ValidationException ex )
        {
            OptionUtils.showErrorMessage( getView(), ex.getMessage(),
                "Serialization Error" );
        }
    }
}
