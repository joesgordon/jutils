package jutils.core.ui.hex;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;

import jutils.core.ui.model.IDataView;

/*******************************************************************************
 *
 ******************************************************************************/
public class ByteArrayView implements IDataView<byte []>
{
    /**  */
    private final JComponent view;
    /**  */
    private final HexPanel table;
    /**  */
    private final ValueView values;

    /**  */
    private byte [] bytes;

    /***************************************************************************
     * 
     **************************************************************************/
    public ByteArrayView()
    {
        this.table = new HexPanel();
        this.values = new ValueView( false );

        this.view = createView();

        this.bytes = new byte[0];

        table.addRangeSelectedListener( ( s, e ) -> handleRangeSelected( s ) );
    }

    /***************************************************************************
     * @param start
     * @param end
     **************************************************************************/
    private void handleRangeSelected( int start )
    {
        values.setBytes( bytes, start );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 1.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( table.getView(), constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( values.getView(), constraints );

        // constraints = new GridBagConstraints( 2, 0, 1, 1, 1.0, 1.0,
        // GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
        // new Insets( 0, 0, 0, 0 ), 0, 0 );
        // panel.add( Box.createHorizontalStrut( 0 ), constraints );

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
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public byte [] getData()
    {
        return bytes;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( byte [] data )
    {
        this.bytes = data;

        table.setBuffer( new ByteBuffer( bytes ) );

        Dimension dim = table.getView().getPreferredSize();

        table.getView().setMinimumSize( dim );
    }
}
