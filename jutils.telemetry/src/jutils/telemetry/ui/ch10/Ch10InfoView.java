package jutils.telemetry.ui.ch10;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jutils.core.ui.StandardFormView;
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
    private final JPanel channelsPanel;

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
        this.channelsPanel = new JPanel( new GridBagLayout() );

        this.view = createView();

        ChannelRow.addHeader( channelsPanel );
        ChannelRow.closeTable( channelsPanel, 1 );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );
        JScrollPane channelsPane = new JScrollPane( channelsPanel );

        panel.add( createForm(), BorderLayout.NORTH );
        panel.add( channelsPane );

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

        channelsPanel.removeAll();
        int r = 1;

        ChannelRow.addHeader( channelsPanel );
        for( Ch10Channel c : file.channels )
        {
            ChannelRow row = new ChannelRow();

            row.addTo( channelsPanel, r++, c );
        }
        ChannelRow.closeTable( channelsPanel, r );
        channelsPanel.invalidate();
        channelsPanel.repaint();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class ChannelRow
    {
        /**
         * 
         */
        public ChannelRow()
        {
        }

        /**
         * @param panel
         */
        public static void addHeader( JPanel panel )
        {
            int col = 0;

            addHeader( panel, "ID", col++ );
            addHeader( panel, "Type", col++ );
            addHeader( panel, "Start", col++ );
            addHeader( panel, "End", col++ );
            addHeader( panel, "Duration", col++ );
            addHeader( panel, "# Packets", col++ );
        }

        /**
         * @param panel
         * @param text
         * @param col
         */
        private static void addHeader( JPanel panel, String text, int col )
        {
            JLabel label = new JLabel( text );
            Font f = label.getFont();
            f = f.deriveFont( Font.BOLD );
            label.setFont( f );
            GridBagConstraints constraints = new GridBagConstraints( col, 0, 1,
                1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets( 2, 8, 8, 8 ), 0, 0 );
            panel.add( label, constraints );
        }

        /**
         * @param panel
         * @param row
         * @param channel
         */
        public void addTo( JPanel panel, int row, Ch10Channel channel )
        {
            int col = 0;

            String startTime = Ch10Utils.reltimeToSecondsString(
                channel.startTime );
            String endTime = Ch10Utils.reltimeToSecondsString(
                channel.startTime );
            String duration = Ch10Utils.reltimeToSecondsString(
                channel.endTime - channel.startTime );

            addField( panel, row, col++, "" + channel.id );
            addField( panel, row, col++, channel.dataType.family.getName() );
            addField( panel, row, col++, startTime );
            addField( panel, row, col++, endTime );
            addField( panel, row, col++, duration );
            addField( panel, row, col++, "" + channel.packetCount );
        }

        /**
         * @param panel
         * @param row
         * @param col
         * @param text
         */
        private void addField( JPanel panel, int row, int col, String text )
        {
            JLabel label = new JLabel( text );
            Font f = label.getFont();
            label.setFont( f );
            GridBagConstraints constraints = new GridBagConstraints( col, row,
                1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets( 2, 4, 2, 4 ), 0, 0 );
            panel.add( label, constraints );
        }

        /**
         * @param panel
         * @param row
         */
        public static void closeTable( JPanel panel, int row )
        {
            GridBagConstraints constraints = new GridBagConstraints( 0, row, 7,
                1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 );
            panel.add( Box.createHorizontalStrut( 0 ), constraints );
        }
    }
}
