package org.jutils.core.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;

import org.jutils.core.IconConstants;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CollapsibleView
{
    /**  */
    private final JPanel panel;
    /**  */
    private final GradientPanel titlePanel;
    /**  */
    private final JLabel titleField;
    /**  */
    private final JSeparator separator;
    /**  */
    private final JPanel componentPanel;

    /**  */
    private final JToggleButton collapseButton;

    /***************************************************************************
     * 
     **************************************************************************/
    public CollapsibleView()
    {
        GridBagConstraints constraints;

        this.componentPanel = new JPanel( new BorderLayout() );
        this.panel = new JPanel( new GridBagLayout() );
        this.titleField = new JLabel( "Title" );
        this.separator = new JSeparator();
        this.collapseButton = new JToggleButton(
            IconConstants.getIcon( "collapse.png" ) );
        this.titlePanel = createTitlePanel();

        // componentPanel.setBorder( BorderFactory.createLineBorder( Color.red )
        // );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( titlePanel, constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( separator, constraints );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( Box.createHorizontalStrut( 0 ), constraints );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( componentPanel, constraints );

        setComponent( Box.createVerticalStrut( 20 ) );

        panel.setBorder( BorderFactory.createLineBorder( Color.gray ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private GradientPanel createTitlePanel()
    {
        GridBagConstraints constraints;
        GradientPanel titlePanel;

        collapseButton.addActionListener(
            ( e ) -> setCollapsed( collapseButton.isSelected() ) );
        collapseButton.setSelectedIcon( IconConstants.getIcon( "expand.png" ) );

        collapseButton.setBorderPainted( false );
        collapseButton.setOpaque( false );

        titlePanel = new GradientPanel( new GridBagLayout(),
            new Color( 58, 110, 167 ) );

        Font bold = titleField.getFont().deriveFont( Font.BOLD );
        titleField.setFont( bold );
        titleField.setForeground( Color.white );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 1.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 4, 4, 4, 4 ), 0, 0 );
        titlePanel.add( collapseButton, constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 4, 4, 4, 4 ), 0, 0 );
        titlePanel.add( titleField, constraints );
        return titlePanel;
    }

    /***************************************************************************
     * @param comp
     **************************************************************************/
    public void setComponent( Component comp )
    {
        componentPanel.removeAll();
        componentPanel.add( comp, BorderLayout.CENTER );
    }

    /***************************************************************************
     * @param title
     **************************************************************************/
    public void setTitle( String title )
    {
        titleField.setText( title );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public JComponent getView()
    {
        return panel;
    }

    /***************************************************************************
     * @param collapsed
     **************************************************************************/
    public void setCollapsed( boolean collapsed )
    {
        collapseButton.setSelected( collapsed );

        if( collapsed )
        {
            componentPanel.setVisible( false );
            separator.setVisible( false );
            // componentPanel.removeAll();
            componentPanel.revalidate();
        }
        else
        {
            componentPanel.setVisible( true );
            // setComponent( comp );
            separator.setVisible( true );
            componentPanel.revalidate();
        }
    }
}
