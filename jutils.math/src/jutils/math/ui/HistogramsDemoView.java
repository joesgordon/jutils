package jutils.math.ui;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HistogramsDemoView implements IView<JComponent>
{
    /**  */
    private final JComponent view;
    /**  */
    private final HistogramConfigView configView;
    /**  */
    private final HistogramView histogramsView;

    /***************************************************************************
     * 
     **************************************************************************/
    public HistogramsDemoView()
    {
        this.configView = new HistogramConfigView();
        this.histogramsView = new HistogramView();
        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( configView.getView(), BorderLayout.WEST );
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
