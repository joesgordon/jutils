package jutils.demo.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;

import javax.swing.JComponent;
import javax.swing.JPanel;

import jutils.core.INamedItem;
import jutils.core.ui.ListView;
import jutils.core.ui.ListView.IItemListModel;
import jutils.core.ui.TitleView;
import jutils.core.ui.model.IView;

/*******************************************************************************
 * Defines a view to display all swing components in all states.
 ******************************************************************************/
public class SwingView implements IView<JComponent>
{
    /**  */
    private final JPanel view;
    /**  */
    private final ListView<ComponentPanel> choicesView;
    /**  */
    private final TitleView choiceView;

    /**  */
    private final HashMap<ComponentPanel, Supplier<IView<?>>> compViews;

    /***************************************************************************
     * 
     **************************************************************************/
    public SwingView()
    {
        this.compViews = new HashMap<>();
        this.choicesView = new ListView<SwingView.ComponentPanel>(
            new ComponentPanelListModel(), false, false );
        this.choiceView = new TitleView( "Nothing Selected", null );

        compViews.put( ComponentPanel.JRADIOBUTTON,
            () -> new RadioButtonView() );

        this.view = createView();

        choicesView.addSelectedListener(
            ( e ) -> handleSelction( e.getItem() ) );

        choicesView.setData( new ArrayList<>( compViews.keySet() ) );
    }

    /***************************************************************************
     * @param selection
     **************************************************************************/
    private void handleSelction( ComponentPanel selection )
    {
        Supplier<IView<?>> supplier = compViews.get( selection );
        IView<?> view = supplier == null ? null : supplier.get();
        Component comp = view == null ? null : view.getView();

        choiceView.setTitle( selection.name );
        choiceView.setComponent( comp );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        choicesView.getView().setMinimumSize( new Dimension( 200, 50 ) );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
            new Insets( 6, 6, 6, 6 ), 0, 0 );
        panel.add(
            new TitleView( "Swing Component", choicesView.getView() ).getView(),
            constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 6, 0, 6, 6 ), 0, 0 );
        panel.add( choiceView.getView(), constraints );

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
     * 
     **************************************************************************/
    private static enum ComponentPanel implements INamedItem
    {
        /**  */
        JRADIOBUTTON( "JRadioButton" );

        /**  */
        private final String name;

        /**
         * @param name
         */
        private ComponentPanel( String name )
        {
            this.name = name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getName()
        {
            return name;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class ComponentPanelListModel
        implements IItemListModel<ComponentPanel>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getTitle( ComponentPanel item )
        {
            return item.name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ComponentPanel promptForNew( ListView<ComponentPanel> view )
        {
            return null;
        }
    }
}
