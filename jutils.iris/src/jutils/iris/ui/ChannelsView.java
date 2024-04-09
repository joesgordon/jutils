package jutils.iris.ui;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jutils.core.ui.TabularView;
import jutils.core.ui.TabularView.ITabularModel;
import jutils.core.ui.TabularView.ITabularNotifier;
import jutils.core.ui.model.IView;
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

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class RasterChannelsModel implements ITabularModel
    {
        /**  */
        private IRaster raster;
        /**  */
        private ITabularNotifier notifier;

        /**
         * 
         */
        public RasterChannelsModel()
        {
            this.raster = new Mono8Raster( 16, 16 );
        }

        /**
         * @param raster
         */
        public void setRaster( IRaster raster )
        {
            this.raster = raster;

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
            String s = "";

            for( int c = 0; c < raster.getConfig().channelCount; c++ )
            {
                int v = raster.getChannelAt( row, col, c );

                if( c > 0 )
                {
                    s += ", ";
                }

                s = s + v;
            }

            return s;
        }

        @Override
        public void setValue( Object value, int row, int col )
        {
        }

        @Override
        public boolean isCellEditable( int row, int col )
        {
            return false;
        }
    }
}
