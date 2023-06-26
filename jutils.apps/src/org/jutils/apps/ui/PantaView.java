package org.jutils.apps.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.jutils.core.ui.StandardFormView;
import org.jutils.core.ui.model.IView;

/*******************************************************************************
 *
 ******************************************************************************/
public class PantaView implements IView<JComponent>
{
    /**  */
    private final JTabbedPane tabs;

    /***************************************************************************
     * 
     **************************************************************************/
    public PantaView()
    {
        this.tabs = new JTabbedPane();

        tabs.addTab( "Swing", createSwingTab() );

        tabs.setBorder( new EmptyBorder( 10, 10, 10, 10 ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createSwingTab()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;
        JComponent comp;
        final int spc = 6;

        int r = 0;

        comp = createRadioButtonPanel();
        comp.setBorder( new TitledBorder( "JRadioButton" ) );
        constraints = new GridBagConstraints( 0, r++, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( spc, spc, 0, spc ), 0, 0 );
        panel.add( comp, constraints );

        constraints = new GridBagConstraints( 0, r++, 2, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, spc, 0 ), 0, 0 );
        panel.add( Box.createHorizontalStrut( 0 ), constraints );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createRadioButtonPanel()
    {
        StandardFormView form = new StandardFormView();
        JRadioButton btn;
        ButtonGroup group = new ButtonGroup();

        btn = new JRadioButton( "Button Text" );
        form.addField( "Unselected", btn );
        group.add( btn );

        btn = new JRadioButton( "Button Text" );
        btn.setSelected( true );
        form.addField( "Selected", btn );
        group.add( btn );

        btn = new JRadioButton( "Button Text" );
        btn.setEnabled( false );
        form.addField( "Unselected (Disabled)", btn );
        group.add( btn );

        btn = new JRadioButton( "Button Text" );
        btn.setSelected( true );
        btn.setEnabled( false );
        form.addField( "Selected (Disabled)", btn );

        return form.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return tabs;
    }
}
