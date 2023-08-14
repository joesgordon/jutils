package jutils.core.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import jutils.core.IconConstants;
import jutils.core.SwingUtils;
import jutils.core.ui.StandardFrameView;
import jutils.core.ui.TabularItemsView;
import jutils.core.ui.TabularItemsView.ITabularItemsModel;
import jutils.core.ui.TabularView.ITabularNotifier;
import jutils.core.ui.app.AppRunner;
import jutils.core.ui.event.ActionAdapter;
import jutils.core.ui.model.IView;

public class TabularItemsViewMain
{
    public static void main( String [] args )
    {
        AppRunner.invokeLater( () -> createFrame() );
    }

    private static JFrame createFrame()
    {
        StandardFrameView frameView = new StandardFrameView();
        JFrame frame = frameView.getView();
        TivTestView tivView = new TivTestView();

        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frameView.setSize( 800, 800 );
        frameView.setTitle( "TabularItemsView Test App" );
        frameView.setContent( tivView.getView() );

        return frame;
    }

    private static final class TivTestView implements IView<JComponent>
    {
        /**  */
        private final JPanel view;
        /**  */
        private final TabularItemsView<Integer> itemsView;
        /**  */
        private IntegerItemsModel model;

        /**
         * 
         */
        public TivTestView()
        {
            this.itemsView = new TabularItemsView<>();
            this.model = new IntegerItemsModel();
            this.view = createView();
        }

        /**
         * @return
         */
        private JPanel createView()
        {
            JPanel panel = new JPanel( new BorderLayout() );

            itemsView.setModel( model );

            panel.add( createToolbar(), BorderLayout.NORTH );
            panel.add( itemsView.getView(), BorderLayout.CENTER );

            return panel;
        }

        /**
         * @return
         */
        private JToolBar createToolbar()
        {
            JToolBar toolbar = new JToolBar();

            SwingUtils.setToolbarDefaults( toolbar );

            SwingUtils.addActionToToolbar( toolbar, createAddAction() );

            SwingUtils.addActionToToolbar( toolbar, createReplaceAction() );

            SwingUtils.addActionToToolbar( toolbar, createClearAction() );

            return toolbar;
        }

        /**
         * @return
         */
        private Action createAddAction()
        {
            ActionListener listener = ( e ) -> handleAddItem();
            Icon icon = IconConstants.getIcon( IconConstants.EDIT_ADD_16 );
            return new ActionAdapter( listener, "Add Item", icon );
        }

        /**
         * @return
         */
        private Action createReplaceAction()
        {
            ActionListener listener = ( e ) -> handleReplaceItems();
            Icon icon = IconConstants.getIcon( IconConstants.ANALYZE_16 );
            return new ActionAdapter( listener, "Replace Items", icon );
        }

        private Action createClearAction()
        {
            ActionListener listener = ( e ) -> handleClearItems();
            Icon icon = IconConstants.getIcon( IconConstants.EDIT_DELETE_16 );
            return new ActionAdapter( listener, "Clear Items", icon );
        }

        private void handleAddItem()
        {
            model.addItem( model.items.size() );
        }

        private void handleReplaceItems()
        {
            int size = model.items.size();
            int count = size < 10 ? size * 2 : size / 2;
            List<Integer> items = new ArrayList<>();

            for( int i = 0; i < count; i++ )
            {
                items.add( count - 1 - i );
            }

            model.setItems( items );
        }

        private void handleClearItems()
        {
            model.setItems( new ArrayList<>() );
        }

        @Override
        public JComponent getView()
        {
            return view;
        }
    }

    private static final class IntegerItemsModel
        implements ITabularItemsModel<Integer>
    {
        /**  */
        protected final List<Integer> items;
        /**  */
        protected ITabularNotifier notifier;

        public IntegerItemsModel()
        {
            this.items = new ArrayList<>();
        }

        public void addItem( Integer item )
        {
            items.add( item );

            if( notifier != null )
            {
                int row = items.size() - 1;
                notifier.fireRowsInserted( row, row );
            }
        }

        public void setItems( List<Integer> items )
        {
            this.items.clear();
            this.items.addAll( items );

            notifier.fireDataChanged();
        }

        @Override
        public void registerNotifier( ITabularNotifier notifier )
        {
            this.notifier = notifier;
        }

        @Override
        public int getCount()
        {
            return items.size();
        }

        @Override
        public Integer getItem( int index )
        {
            return items.get( index );
        }

        @Override
        public String getItemName( Integer item, int index )
        {
            return "" + index;
        }

        @Override
        public int getPropertyCount()
        {
            return 1;
        }

        @Override
        public String getPropertyName( int col )
        {
            switch( col )
            {
                case 0:
                    return "Value";

                default:
                    break;
            }

            return null;
        }

        @Override
        public Class<?> getPropertyClass( int col )
        {
            switch( col )
            {
                case 0:
                    return Integer.class;

                default:
                    break;
            }

            return null;
        }

        @Override
        public Object getProperty( Integer item, int row, int col )
        {
            switch( col )
            {
                case 0:
                    return item;

                default:
                    break;
            }

            return null;
        }

        @Override
        public void setProperty( Object value, int row, int col )
        {
        }

        @Override
        public boolean isPropertyEditable( Integer item, int row, int col )
        {
            return false;
        }
    }
}
