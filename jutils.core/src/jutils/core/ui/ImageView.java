package jutils.core.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jutils.core.ui.model.IView;

/*******************************************************************************
 * Defines a view that will display an {@link java.awt.Image}.
 ******************************************************************************/
public class ImageView implements IView<JComponent>
{
    /**  */
    private final JLabel imgLabel;
    /**  */
    private final JPanel view;

    /***************************************************************************
     * 
     **************************************************************************/
    public ImageView()
    {
        this.imgLabel = new JLabel();
        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 4, 4, 4, 4 ), 0, 0 );
        panel.add( imgLabel, constraints );

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

    /***************************************************************************
     * @param img
     **************************************************************************/
    public void setImage( Image img )
    {
        imgLabel.setIcon( new ImageIcon( img ) );
    }
}
