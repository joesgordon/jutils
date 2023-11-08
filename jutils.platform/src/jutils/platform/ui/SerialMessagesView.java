package jutils.platform.ui;

import java.awt.Dimension;
import java.time.format.DateTimeFormatter;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import jutils.core.SwingUtils;
import jutils.core.time.TimeUtils;
import jutils.core.ui.hex.HexUtils;
import jutils.core.ui.model.DefaultTableItemsConfig;
import jutils.core.ui.model.IView;
import jutils.core.ui.model.ItemsTableModel;
import jutils.platform.data.SerialMessage;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SerialMessagesView implements IView<JComponent>
{
    /**  */
    private final JComponent view;

    /**  */
    private final ItemsTableModel<SerialMessage> msgsModel;
    /**  */
    private final JTable msgsTable;

    /***************************************************************************
     * 
     **************************************************************************/
    public SerialMessagesView()
    {
        this.msgsModel = new ItemsTableModel<SerialMessage>(
            new SerialMsgsConfig() );
        this.msgsTable = new JTable( msgsModel );

        this.view = createView();

        msgsTable.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JScrollPane pane = new JScrollPane( msgsTable );

        pane.setMinimumSize( new Dimension( 200, 200 ) );
        pane.setPreferredSize( new Dimension( 200, 200 ) );

        return pane;
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
     * @param msg
     **************************************************************************/
    public void addItem( SerialMessage msg )
    {
        this.msgsModel.addItem( msg );

        SwingUtils.resizeTable( msgsTable, 0 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class SerialMsgsConfig
        extends DefaultTableItemsConfig<SerialMessage>
    {
        /**  */
        private final DateTimeFormatter dateFmt;
        /**  */
        private final DateTimeFormatter timeFmt;

        /**
         * 
         */
        public SerialMsgsConfig()
        {
            this.dateFmt = TimeUtils.buildDateDisplayFormat();
            this.timeFmt = TimeUtils.buildTimeDisplayFormat();

            super.addCol( "Tx/Rx", String.class,
                ( m ) -> m.isTransmitted ? "Tx" : "Rx" );
            super.addCol( "Date", String.class,
                ( m ) -> m.time.format( dateFmt ) );
            super.addCol( "Time", String.class,
                ( m ) -> m.time.format( timeFmt ) );
            super.addCol( "Contents", String.class,
                ( m ) -> HexUtils.toHexString( m.data, " ", 0,
                    Math.min( 64, m.data.length ) ) );
        }
    }
}
