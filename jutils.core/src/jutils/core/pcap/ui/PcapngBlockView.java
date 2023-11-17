package jutils.core.pcap.ui;

import javax.swing.JComponent;
import javax.swing.JPanel;

import jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PcapngBlockView implements IView<JComponent>
{
    /**  */
    private final JComponent view;

    /***************************************************************************
     * 
     **************************************************************************/
    public PcapngBlockView()
    {
        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static JComponent createView()
    {
        JPanel panel = new JPanel();

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
