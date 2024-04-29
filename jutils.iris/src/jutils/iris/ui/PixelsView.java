package jutils.iris.ui;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jutils.core.ui.TabularView;
import jutils.core.ui.TabularView.ITabularModel;
import jutils.core.ui.TabularView.ITabularNotifier;
import jutils.core.ui.hex.HexUtils;
import jutils.core.ui.model.IView;
import jutils.iris.data.DirectImage;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PixelsView implements IView<JComponent>
{
    /**  */
    private final JComponent view;
    /**  */
    private final TabularView table;
    /**  */
    private final ImagePixelsModel model;

    /***************************************************************************
     * 
     **************************************************************************/
    public PixelsView()
    {
        this.model = new ImagePixelsModel();
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
    public void setImage( DirectImage raster )
    {
        this.model.setRaster( raster );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class ImagePixelsModel implements ITabularModel
    {
        /**  */
        private DirectImage raster;
        /**  */
        private ITabularNotifier notifier;

        /**
         * 
         */
        public ImagePixelsModel()
        {
            this.raster = new DirectImage( 16, 16 );
        }

        /**
         * @param raster
         */
        public void setRaster( DirectImage raster )
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
            return raster.height;
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
            return raster.width;
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
            int index = row * raster.width + col;

            return HexUtils.getHexString( raster.pixels[index] );
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
