package jutils.iris.demo;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import jutils.core.ui.model.IView;
import jutils.math.ui.HistogramView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HistogramsDemoView implements IView<JComponent>
{
    /**  */
    private final JComponent view;
    /**  */
    private final HistogramView histogramsView;

    /***************************************************************************
     * 
     **************************************************************************/
    public HistogramsDemoView()
    {
        this.histogramsView = new HistogramView();
        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( histogramsView.getView(), BorderLayout.CENTER );

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
