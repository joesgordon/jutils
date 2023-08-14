package jutils.core.licensing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.net.URL;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import jutils.core.io.ResourceLoader;
import jutils.core.ui.OkDialogView;
import jutils.core.ui.ScrollableEditorPaneView;
import jutils.core.ui.OkDialogView.OkDialogButtons;
import jutils.core.ui.app.AppRunner;
import jutils.core.ui.app.IApplication;
import jutils.core.ui.model.IView;

/***************************************************************************
 * 
 **************************************************************************/
public class LicenseDialog implements IView<JComponent>
{
    /**  */
    private static final String LICENSE_FORMS = "jgoodiesFormsLicense.html";
    /**  */
    private static final String LICENSE_LOOKS = "jgoodiesLooksLicense.html";
    /**  */
    private static final String LICENSE_XPP3 = "xpp3License.html";
    /**  */
    private static final String LICENSE_XSTREAM = "xstreamLicense.html";
    /**  */
    private static final String LICENSE_OPENICON = "openIconLibraryLicense.html";
    /**  */
    private static final String LICENSE_FARMFRESH = "farmFreshWebIconsLicense.html";
    /**  */
    private static final String LICENSE_CRYSTALCLEAR = "crystalClearLicense.html";

    /**  */
    private final JTabbedPane tabbedPane;

    /***************************************************************************
     * 
     **************************************************************************/
    public LicenseDialog()
    {
        this.tabbedPane = new JTabbedPane();

        ResourceLoader loader = new ResourceLoader( getClass(), "./" );

        LicensePanel jgFormsPanel = new LicensePanel(
            loader.getUrl( LICENSE_FORMS ) );
        LicensePanel jgLooksPanel = new LicensePanel(
            loader.getUrl( LICENSE_LOOKS ) );
        LicensePanel xpp3Panel = new LicensePanel(
            loader.getUrl( LICENSE_XPP3 ) );
        LicensePanel xstreamPanel = new LicensePanel(
            loader.getUrl( LICENSE_XSTREAM ) );
        LicensePanel crystalClearPanel = new LicensePanel(
            loader.getUrl( LICENSE_CRYSTALCLEAR ) );
        LicensePanel farmFreshPanel = new LicensePanel(
            loader.getUrl( LICENSE_FARMFRESH ) );
        LicensePanel openIconPanel = new LicensePanel(
            loader.getUrl( LICENSE_OPENICON ) );

        tabbedPane.addTab( "JGoodies Forms", jgFormsPanel.getView() );
        tabbedPane.addTab( "JGoodies Looks", jgLooksPanel.getView() );
        tabbedPane.addTab( "XPP3", xpp3Panel.getView() );
        tabbedPane.addTab( "XStream", xstreamPanel.getView() );
        tabbedPane.addTab( "Crystal Clear Icons", crystalClearPanel.getView() );
        tabbedPane.addTab( "Farm-Fresh Web Icons", farmFreshPanel.getView() );
        tabbedPane.addTab( "Open Icon Library", openIconPanel.getView() );
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.invokeLater( new IApplication()
        {
            @Override
            public String getLookAndFeelName()
            {
                return null;
            }

            @Override
            public void createAndShowUi()
            {
                LicenseDialog view = new LicenseDialog();
                OkDialogView dialogView = new OkDialogView( null,
                    view.getView(), OkDialogButtons.OK_ONLY );

                dialogView.show( "Licenses", new Dimension( 700, 500 ) );
            }
        } );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JTabbedPane getView()
    {
        return tabbedPane;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class LicensePanel implements IView<JPanel>
    {
        private final JPanel panel;
        private final ScrollableEditorPaneView editorPane;
        private final JScrollPane scrollPane;

        public LicensePanel( URL pageUrl )
        {
            this.panel = new JPanel( new BorderLayout() );
            this.editorPane = new ScrollableEditorPaneView();
            this.scrollPane = new JScrollPane( editorPane.getView() );

            editorPane.setContentType( "text/html" );
            if( pageUrl != null )
            {
                try
                {
                    editorPane.setPage( pageUrl );
                }
                catch( IOException e )
                {
                    throw new IllegalArgumentException(
                        pageUrl.getFile() + " not found!" );
                }
            }
            else
            {
                editorPane.setText( "<html>null url</html>" );
            }

            panel.add( scrollPane, BorderLayout.CENTER );
        }

        @Override
        public JPanel getView()
        {
            return panel;
        }
    }
}
