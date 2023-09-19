package jutils.demo.ui;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import jutils.core.ui.model.IView;

/*******************************************************************************
 * Defines the main panel of the demo application.
 ******************************************************************************/
public class DemoView implements IView<JComponent>
{
    /** The component for this view. */
    private final JTabbedPane tabs;

    /***************************************************************************
     * Creates a new view to show all JUtils functionality.
     **************************************************************************/
    public DemoView()
    {
        this.tabs = new JTabbedPane();

        tabs.addTab( "Swing", new SwingView().getView() );

        tabs.setBorder( new EmptyBorder( 10, 10, 10, 10 ) );
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
