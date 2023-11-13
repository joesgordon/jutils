package jutils.platform.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jutils.core.ui.StandardFormView;
import jutils.core.ui.fields.StringAreaFormField;
import jutils.core.ui.model.IView;
import jutils.platform.PlatformUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DemoPlatformView implements IView<JComponent>
{
    /**  */
    private final JComponent view;
    /**  */
    private final JLabel initField;
    /**  */
    private final JLabel isInitField;
    /**  */
    private final JLabel destroyField;
    /**  */
    private final StringAreaFormField serialListField;

    /***************************************************************************
     * 
     **************************************************************************/
    public DemoPlatformView()
    {
        this.initField = new JLabel();
        this.isInitField = new JLabel();
        this.destroyField = new JLabel();
        this.serialListField = new StringAreaFormField( "Serial Ports" );

        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        StandardFormView form = new StandardFormView();

        JComponent initComp = createResultField( "Init",
            ( e ) -> handleInitialize(), initField );
        JComponent isInitComp = createResultField( "Is Init'd?",
            ( e ) -> handleIsInitialized(), isInitField );
        JComponent destroyComp = createResultField( "Destroy",
            ( e ) -> handleDestroy(), destroyField );

        form.addField( "Initialize", initComp );
        form.addField( "Initialized?", isInitComp );
        form.addField( "Destroy", destroyComp );
        form.addField( serialListField );

        return form.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void handleInitialize()
    {
        boolean result = PlatformUtils.getPlatform().initialize();

        initField.setText( String.format( "%s", result ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void handleIsInitialized()
    {
        boolean result = PlatformUtils.getPlatform().isInialized();

        isInitField.setText( String.format( "%s", result ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void handleDestroy()
    {
        boolean result = PlatformUtils.getPlatform().destroy();

        destroyField.setText( String.format( "%s", result ) );
    }

    /***************************************************************************
     * @param text
     * @param listener
     * @param label
     * @return
     **************************************************************************/
    private static JComponent createResultField( String text,
        ActionListener listener, JLabel label )
    {
        JButton button = new JButton( text );

        button.addActionListener( listener );

        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 4 ), 0, 0 );
        panel.add( button, constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( label, constraints );

        return panel;
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
