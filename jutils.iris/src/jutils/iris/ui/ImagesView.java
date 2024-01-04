package jutils.iris.ui;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import jutils.core.ui.PositionIndicator;
import jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ImagesView implements IView<JComponent>
{
    /**  */
    private final JComponent view;
    /**  */
    private final PositionIndicator positionView;

    /***************************************************************************
     * 
     **************************************************************************/
    public ImagesView()
    {
        this.positionView = new PositionIndicator();

        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( positionView.getView(), BorderLayout.SOUTH );

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
