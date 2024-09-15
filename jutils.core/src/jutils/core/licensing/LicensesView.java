package jutils.core.licensing;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import jutils.core.io.ResourceLoader;
import jutils.core.ui.OkDialogView;
import jutils.core.ui.OkDialogView.OkDialogButtons;
import jutils.core.ui.app.AppRunner;
import jutils.core.ui.model.IView;

/***************************************************************************
 * 
 **************************************************************************/
public class LicensesView implements IView<JComponent>
{
    /**  */
    private static final String LICENSE_JUTILS = "jutilsLicense.txt";
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
    public LicensesView()
    {
        this.tabbedPane = new JTabbedPane();

        ResourceLoader loader = new ResourceLoader( getClass(), "./" );

        LicenseView jutilsPanel = new LicenseView(
            loader.getUrl( LICENSE_JUTILS ) );
        LicenseView formsPanel = new LicenseView(
            loader.getUrl( LICENSE_FORMS ) );
        LicenseView looksPanel = new LicenseView(
            loader.getUrl( LICENSE_LOOKS ) );
        LicenseView xpp3Panel = new LicenseView(
            loader.getUrl( LICENSE_XPP3 ) );
        LicenseView xstreamPanel = new LicenseView(
            loader.getUrl( LICENSE_XSTREAM ) );
        LicenseView crystalClearPanel = new LicenseView(
            loader.getUrl( LICENSE_CRYSTALCLEAR ) );
        LicenseView farmFreshPanel = new LicenseView(
            loader.getUrl( LICENSE_FARMFRESH ) );
        LicenseView openIconPanel = new LicenseView(
            loader.getUrl( LICENSE_OPENICON ) );

        tabbedPane.addTab( "JUtils", jutilsPanel.getView() );
        tabbedPane.addTab( "JGoodies Forms", formsPanel.getView() );
        tabbedPane.addTab( "JGoodies Looks", looksPanel.getView() );
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
        AppRunner.DEFAULT_LAF = AppRunner.SIMPLE_LAF;

        AppRunner.invokeLater( () -> createAndShowUi() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static void createAndShowUi()
    {
        LicensesView view = new LicensesView();
        OkDialogView dialogView = new OkDialogView( null, view.getView(),
            OkDialogButtons.OK_ONLY );

        dialogView.show( "Licenses", new Dimension( 700, 500 ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JTabbedPane getView()
    {
        return tabbedPane;
    }
}
