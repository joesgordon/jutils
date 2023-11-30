package jutils.demo.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

import jutils.core.INamedItem;
import jutils.core.io.LogUtils;
import jutils.core.ui.ListView;
import jutils.core.ui.ListView.IItemListModel;
import jutils.core.ui.TitleView;
import jutils.core.ui.model.IView;
import jutils.demo.ui.jutils.DateTimeViews;
import jutils.demo.ui.jutils.MsgInputDemoView;
import jutils.demo.ui.jutils.ScreenFormFieldView;

/*******************************************************************************
 * Defines a view to display all JUtils components.
 ******************************************************************************/
public class JUtilsView implements IView<JComponent>
{
    /**  */
    private final JPanel view;
    /**  */
    private final ListView<JUtilsComponent> choicesView;
    /**  */
    private final TitleView choiceView;

    /**  */
    private final HashMap<JUtilsComponent, IViewCreator> compViews;

    /***************************************************************************
     * 
     **************************************************************************/
    public JUtilsView()
    {
        this.compViews = new HashMap<>();
        this.choicesView = new ListView<>( new JUtilsComponentListModel(),
            false, false );
        this.choiceView = new TitleView( "Nothing Selected", null );

        buildViewCreators( compViews );

        this.view = createView();

        choicesView.addSelectedListener(
            ( e ) -> handleSelction( e.getItem() ) );

        choicesView.setData( new ArrayList<>( compViews.keySet() ) );

        if( compViews.size() != JUtilsComponent.values().length )
        {
            LogUtils.printError( "Not all component types accounted for." );
        }
    }

    /***************************************************************************
     * @param views
     **************************************************************************/
    private static void buildViewCreators(
        Map<JUtilsComponent, IViewCreator> views )
    {
        views.put( JUtilsComponent.MESSAGE_INPUT_VIEW,
            () -> new MsgInputDemoView() );
        views.put( JUtilsComponent.DATETIME_VIEWS, () -> new DateTimeViews() );
        views.put( JUtilsComponent.SCREEN_FIELD_VIEW,
            () -> new ScreenFormFieldView() );
    }

    /***************************************************************************
     * @param selection
     **************************************************************************/
    private void handleSelction( JUtilsComponent selection )
    {
        if( selection == null )
        {
            choiceView.setTitle( "No selection" );
            choiceView.setComponent( null );
            return;
        }

        IViewCreator supplier = compViews.get( selection );
        IView<?> view = supplier == null ? null : supplier.get();
        Component comp = view == null ? null : view.getView();

        if( comp == null )
        {
            LogUtils.printError( "%s has no demo view.", selection.name );
        }

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
    private static enum JUtilsComponent implements INamedItem
    {
        /**  */
        DATETIME_VIEWS( "Date/Time Views" ),
        /**  */
        SCREEN_FIELD_VIEW( "Screen Field View" ),
        /**  */
        MESSAGE_INPUT_VIEW( "Message Input View" ),;

        /**  */
        private final String name;

        /**
         * @param name
         */
        private JUtilsComponent( String name )
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
    private static final class JUtilsComponentListModel
        implements IItemListModel<JUtilsComponent>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getTitle( JUtilsComponent item )
        {
            return item.name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public JUtilsComponent promptForNew( ListView<JUtilsComponent> view )
        {
            return null;
        }
    }
}
