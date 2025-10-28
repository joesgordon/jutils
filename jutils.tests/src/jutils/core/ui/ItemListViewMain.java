package jutils.core.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import jutils.core.io.LogUtils;
import jutils.core.net.EndPoint;
import jutils.core.net.IpAddress;
import jutils.core.ui.ListView.IItemListModel;
import jutils.core.ui.app.AppRunner;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.event.updater.UpdaterList;
import jutils.core.ui.event.updater.WrappedUpdater;
import jutils.core.ui.fields.DoubleFormField;
import jutils.core.ui.fields.IDataFormField;
import jutils.core.ui.fields.StringFormField;
import jutils.core.ui.model.IDataView;
import jutils.core.ui.model.IView;
import jutils.core.ui.model.LabelListCellRenderer.IListCellLabelDecorator;
import jutils.core.ui.net.EndPointField;

/***************************************************************************
 * 
 **************************************************************************/
public class ItemListViewMain
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.DEFAULT_LAF = AppRunner.JGOODIES_LAF;

        // AppRunner.invokeLater( () -> createFrame() );
        AppRunner.invokeLater( () -> createFrame2() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static JFrame createFrame()
    {
        StandardFrameView frameView = new StandardFrameView();
        AppView view = new AppView();

        frameView.setContent( view.getView() );
        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frameView.setSize( 500, 500 );
        frameView.setTitle( "Item List View Test App" );

        return frameView.getView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static JFrame createFrame2()
    {
        StandardFrameView frameView = new StandardFrameView();

        frameView.setContent( createView2() );
        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frameView.setSize( 800, 800 );
        frameView.setTitle( "Item List View Test App" );

        return frameView.getView();
    }

    private static <D> IUpdater<D> createUpdater( IDataFormField<D> field )
    {
        IUpdater<D> updater = ( d ) -> {
            LogUtils.printDebug( "Setting %s value %s to %s ", field.getName(),
                field.getValue(), d );
        };

        return updater;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static Container createView2()
    {
        StandardFormView form = new StandardFormView();

        EndPointField epff = new EndPointField( "End Point" );
        epff.setUpdater( createUpdater( epff ) );

        DoubleFormField dff = new DoubleFormField( "Anum" );
        dff.setUpdater( createUpdater( dff ) );

        EndPointField listField = new EndPointField( "List End Point" );
        FieldView<EndPoint> dataView = new FieldView<EndPoint>( listField );
        IItemListModel<EndPoint> itemsModel = new IItemListModel<EndPoint>()
        {
            int i = 0;

            @Override
            public EndPoint promptForNew( ListView<EndPoint> view )
            {
                return new EndPoint( new IpAddress( 0 ), i++ );
            }

            @Override
            public String getTitle( EndPoint item )
            {
                return item.toString();
            }
        };

        listField.setUpdater( new WrappedUpdater<>( createUpdater( listField ),
            ( d ) -> dataView.getData().set( d ) ) );

        ItemListView<EndPoint> view;
        view = new ItemListView<>( dataView, itemsModel );

        form.addField( epff );
        form.addField( dff );
        form.addComponent( view.getView() );

        return form.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class AppView implements IView<JComponent>
    {
        /**  */
        private final JPanel view;
        /**  */
        private final ItemListView<TestData> normalItemList;
        /**  */
        private final ItemListView<TestData> rendererItemList;
        /**  */
        private final ListView<TestData> normalList;
        /**  */
        private final ListView<TestData> rendererList;

        /**
         * 
         */
        public AppView()
        {
            TestDataView ntdv = new TestDataView();
            TestDataView rtdv = new TestDataView();

            this.normalItemList = new ItemListView<TestData>( ntdv,
                new TestDataModel() );
            this.rendererItemList = new ItemListView<TestData>( rtdv,
                new TestDataModel() );

            this.normalList = new ListView<>( new TestDataModel() );
            this.rendererList = new ListView<>( new TestDataModel() );

            this.view = createView();

            ntdv.addNameUpdater( ( d ) -> normalItemList.refreshSelected() );
            rtdv.addNameUpdater( ( d ) -> rendererItemList.refreshSelected() );

            rendererItemList.setItemDecorator( new TestDataRenderer() );
            rendererList.setItemDecorator( new TestDataRenderer() );

            normalItemList.setData( new ArrayList<>() );
            rendererItemList.setData( new ArrayList<>() );
            normalList.setData( new ArrayList<>() );
            rendererList.setData( new ArrayList<>() );
        }

        /**
         * @return
         */
        private JPanel createView()
        {
            JPanel panel = new JPanel( new BorderLayout() );
            JTabbedPane tabs = new JTabbedPane();

            tabs.add( "Normal Item List", normalItemList.getView() );
            tabs.add( "Rendered Item List", rendererItemList.getView() );

            tabs.add( "Normal List", normalList.getView() );
            tabs.add( "Rendered List", rendererList.getView() );

            panel.add( tabs, BorderLayout.CENTER );

            return panel;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public JComponent getView()
        {
            return view;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class TestDataView implements IDataView<TestData>
    {
        /**  */
        private final JPanel view;
        /**  */
        private final StringFormField nameField;
        /**  */
        private final UpdaterList<String> nameUpdaters;

        /**  */
        private TestData data;

        /**
         * 
         */
        public TestDataView()
        {
            this.nameField = new StringFormField( "Name" );
            this.view = createView();
            this.nameUpdaters = new UpdaterList<>();

            nameField.setUpdater( ( d ) -> {
                data.name = d;
                nameUpdaters.fire( d );
            } );
        }

        /**
         * @return
         */
        private JPanel createView()
        {
            StandardFormView form = new StandardFormView();

            form.addField( nameField );

            return form.getView();
        }

        /**
         * @param u
         */
        public void addNameUpdater( IUpdater<String> u )
        {
            nameUpdaters.add( u );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Component getView()
        {
            return view;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public TestData getData()
        {
            return data;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setData( TestData data )
        {
            this.data = data;

            nameField.setValue( data.name );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class TestDataModel implements IItemListModel<TestData>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getTitle( TestData item )
        {
            return item.name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public TestData promptForNew( ListView<TestData> view )
        {
            String name = view.promptForName( "Test Data" );

            if( name != null )
            {
                TestData data = new TestData();
                data.name = name;
                return data;
            }

            return null;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class TestDataRenderer
        implements IListCellLabelDecorator<TestData>
    {
        /**  */
        private final ColorIcon icon = new ColorIcon( Color.green );

        /**
         * {@inheritDoc}
         */
        @Override
        public void decorate( JLabel label, JList<? extends TestData> list,
            TestData value, int index, boolean isSelected,
            boolean cellHasFocus )
        {
            icon.setColor( new Color( value.name.hashCode() ) );
            label.setIcon( icon );
            label.setText( value.name );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class TestData
    {
        /**  */
        public String name;
    }
}
