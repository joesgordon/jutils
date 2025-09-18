package jutils.core.ui;

import java.awt.GridBagLayout;

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
        this.view = new JPanel();

        view.setLayout( new GridBagLayout() );
        // view.
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
