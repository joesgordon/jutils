package jutils.iris.ui;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jutils.core.ui.TabularView;
import jutils.core.ui.TabularView.ITabularModel;
import jutils.core.ui.TabularView.ITabularNotifier;
import jutils.core.ui.model.IView;
import jutils.iris.data.ChannelPlacement;
import jutils.iris.data.RasterConfig;
import jutils.iris.rasters.IRaster;
import jutils.iris.rasters.Mono8Raster;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChannelsView implements IView<JComponent>
{
    /**  */
    private final JComponent view;
    /**  */
    private final TabularView table;
    /**  */
    private final RasterChannelsModel model;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChannelsView()
    {
        this.model = new RasterChannelsModel();
        this.table = new TabularView();
        this.view = createView();

        table.setModel( model );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );
        JScrollPane pane = new JScrollPane( table.getView() );

        pane.getVerticalScrollBar().setUnitIncrement( 12 );

        panel.add( pane, BorderLayout.CENTER );

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
     * @param raster
     **************************************************************************/
    public void setRaster( IRaster raster )
    {
        this.model.setRaster( raster );
    }

    private static interface IChannelsValue
    {
        public String build( int row, int col );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class RasterChannelsModel implements ITabularModel
    {
        /**  */
        private final IChannelsValue singleBuilder;
        /**  */
        private final IChannelsValue channelsBuilder;

        /**  */
        private IRaster raster;
        /**  */
        private ITabularNotifier notifier;
        /**  */
        private IChannelsValue valueBuilder;

        /**
         * 
         */
        public RasterChannelsModel()
        {
            this.singleBuilder = ( r, c ) -> getSingleValue( r, c );
            this.channelsBuilder = ( r, c ) -> getChannelsValue( r, c );
            this.raster = new Mono8Raster( 16, 16 );
            this.valueBuilder = channelsBuilder;
        }

        /**
         * @param row
         * @param col
         * @return
         */
        private String getSingleValue( int row, int col )
        {
            return "" + raster.getPixelAt( col, row );
        }

        /**
         * @param row
         * @param col
         * @return
         */
        private String getChannelsValue( int row, int col )
        {
            String s = "";

            for( int c = 0; c < raster.getConfig().channelCount; c++ )
            {
                int v = raster.getChannelAt( col, row, c );

                if( c > 0 )
                {
                    s += ", ";
                }

                s = s + v;
            }

            return s;
        }

        /**
         * @param raster
         */
        public void setRaster( IRaster raster )
        {
            this.raster = raster;

            RasterConfig config = raster.getConfig();
            boolean isSingle = config.channelCount == 1 ||
                config.channelLoc == ChannelPlacement.BAYER;

            this.valueBuilder = isSingle ? singleBuilder : channelsBuilder;

            if( notifier != null )
            {
                this.notifier.fireStructureChanged();
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void registerNotifier( ITabularNotifier notifier )
        {
            this.notifier = notifier;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getRowCount()
        {
            return raster.getHeight();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getRowName( int row )
        {
            return "" + row;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getColCount()
        {
            return raster.getWidth();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getColName( int col )
        {
            return "" + col;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Class<?> getColClass( int col )
        {
            return String.class;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Object getValue( int row, int col )
        {
            return valueBuilder.build( row, col );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setValue( Object value, int row, int col )
        {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isCellEditable( int row, int col )
        {
            return false;
        }
    }
}
