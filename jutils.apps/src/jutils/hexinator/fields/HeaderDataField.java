package jutils.hexinator.fields;

import java.io.IOException;
import java.nio.ByteOrder;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import jutils.core.io.DataStream;
import jutils.core.ui.model.IView;
import jutils.core.utils.ByteOrdering;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HeaderDataField extends DefaultDataField
{
    /**  */
    private final LabelView [] fields;

    /***************************************************************************
     * 
     **************************************************************************/
    public HeaderDataField()
    {
        this.fields = new LabelView[8];

        for( int i = 0; i < fields.length; i++ )
        {
            fields[i] = new LabelView();
            fields[i].label.setText( "" + ( fields.length - 1 - i ) );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addTo( String name, JPanel panel, int row )
    {
        DefaultDataField.addTo( fields, name, panel, row );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    protected void setData( DataStream stream ) throws IOException
    {
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setOrder( ByteOrdering order )
    {
        super.setOrder( order );

        for( int i = 0; i < fields.length; i++ )
        {
            int c = order.order == ByteOrder.LITTLE_ENDIAN ? i
                : ( fields.length - 1 - i );
            fields[i].label.setText( "" + c );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class LabelView implements IView<JComponent>
    {
        /**  */
        public final JLabel label;

        /**
         * 
         */
        public LabelView()
        {
            this.label = new JLabel();

            label.setHorizontalAlignment( SwingConstants.CENTER );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public JComponent getView()
        {
            return label;
        }
    }
}
