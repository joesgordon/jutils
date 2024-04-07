package jutils.iris.ui;

import javax.swing.JComponent;
import javax.swing.JPanel;

import jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PlanesView implements IView<JComponent>
{
    /**  */
    private final JComponent view;

    /***************************************************************************
     * 
     **************************************************************************/
    public PlanesView()
    {
        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JPanel panel = new JPanel();
        // TODO Auto-generated method stub
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
