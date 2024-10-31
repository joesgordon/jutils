package jutils.telemetry.ui.ch10;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;

import jutils.core.ui.ItemsTable;
import jutils.core.ui.StandardFormView;
import jutils.core.ui.model.DefaultTableItemsConfig;
import jutils.core.ui.model.IDataView;
import jutils.telemetry.data.ch10.Ch10Channel;
import jutils.telemetry.data.ch10.Ch10File;
import jutils.telemetry.data.ch10.Ch10Utils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Ch10InfoView implements IDataView<Ch10File>
{
    /**  */
    private final Component view;
    /**  */
    private final JLabel nameField;
    /**  */
    private final JLabel packetCountField;
    /**  */
    private final JLabel channelCountField;
    /**  */
    private final ItemsTable<Ch10Channel> channelsTable;

    /**  */
    private Ch10File file;

    /***************************************************************************
     * 
     **************************************************************************/
    public Ch10InfoView()
    {
        this.nameField = new JLabel( "" );
        this.packetCountField = new JLabel( "" );
        this.channelCountField = new JLabel( "" );
        this.channelsTable = new ItemsTable<>( new ChannelConfig() );

        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( createForm(), BorderLayout.NORTH );
        panel.add( channelsTable.getView(), BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createForm()
    {
        StandardFormView form = new StandardFormView();

        form.addField( "Name", nameField );
        form.addField( "# Packets", packetCountField );
        form.addField( "# Channels", channelCountField );

        return form.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Component getView()
    {
        return this.view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Ch10File getData()
    {
        return this.file;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( Ch10File data )
    {
        this.file = data;

        nameField.setText( file.name );
        packetCountField.setText( "" + file.packets.size() );
        channelCountField.setText( "" + file.channels.size() );

        channelsTable.model.setItems( file.channels );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class ChannelConfig
        extends DefaultTableItemsConfig<Ch10Channel>
    {
        /**
         * 
         */
        public ChannelConfig()
        {
            super();

            super.addCol( "ID", Short.class, ( d ) -> d.id );
            super.addCol( "Type", String.class, ( d ) -> d.dataType.name );
            super.addCol( "Start", String.class,
                ( d ) -> Ch10Utils.reltimeToSecondsString( d.startTime ) );
            super.addCol( "End", String.class,
                ( d ) -> Ch10Utils.reltimeToSecondsString( d.endTime ) );
            super.addCol( "Duration", String.class,
                ( d ) -> Ch10Utils.reltimeToSecondsString(
                    d.endTime - d.startTime ) );
            super.addCol( "# Packets", Integer.class, ( d ) -> d.packetCount );
        }
    }
}
