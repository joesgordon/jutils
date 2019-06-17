package org.jutils.ui;

import java.awt.*;

import javax.swing.*;

import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TitleView implements IView<JPanel>
{
    /**  */
    private final JPanel view;
    /**  */
    private final JLabel titleField;
    /**  */
    private final ComponentView compView;

    /**  */
    private Component comp;

    /***************************************************************************
     * 
     **************************************************************************/
    public TitleView()
    {
        this( null, null );
    }

    /***************************************************************************
     * @param title
     * @param comp
     **************************************************************************/
    public TitleView( String title, Component comp )
    {

        titleField = new JLabel( "Title" );
        compView = new ComponentView();
        view = createView();

        setTitle( title );
        setComponent( comp );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;
        JSeparator separator = new JSeparator();
        GradientPanel titlePanel = createTitlePanel();

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
        panel.add( compView.getView(), constraints );

        setComponent( Box.createVerticalStrut( 20 ) );

        panel.setBorder( BorderFactory.createLineBorder( Color.gray ) );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private GradientPanel createTitlePanel()
    {
        GridBagConstraints constraints;
        GradientPanel titlePanel;

        titlePanel = new GradientPanel( new GridBagLayout(),
            new Color( 58, 110, 167 ) );

        Font bold = titleField.getFont().deriveFont( Font.BOLD );
        titleField.setFont( bold );
        titleField.setForeground( Color.white );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 4, 4, 4, 4 ), 0, 0 );
        titlePanel.add( titleField, constraints );
        return titlePanel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Component getComponent()
    {
        return comp;
    }

    /***************************************************************************
     * @param comp
     **************************************************************************/
    public void setComponent( Component comp )
    {
        this.comp = comp;

        compView.setComponent( comp );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
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
    public String getTitle()
    {
        return titleField.getText();
    }
}
