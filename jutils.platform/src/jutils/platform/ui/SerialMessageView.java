package jutils.platform.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.time.format.DateTimeFormatter;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import jutils.core.time.TimeUtils;
import jutils.core.ui.hex.ByteArrayView;
import jutils.core.ui.model.IDataView;
import jutils.platform.data.SerialMessage;

public class SerialMessageView implements IDataView<SerialMessage>
{
    /** Callback that builds a string representation of the message content. */
    private final IDataView<SerialMessage> msgView;

    /** The main panel for this view. */
    private final JPanel view;
    /** The field for message meta-data (e.g. Rx/Tx, time, remote IP/Port). */
    private final JTextField infoField;
    /** The bytes of the message. */
    private final ByteArrayView bytesField;

    /** The message being displayed. */
    private SerialMessage msg;

    /***************************************************************************
     * 
     **************************************************************************/
    public SerialMessageView()
    {
        this( null, false );
    }

    /***************************************************************************
     * @param parsedView
     * @param addScrollPane
     **************************************************************************/
    public SerialMessageView( IDataView<SerialMessage> parsedView,
        boolean addScrollPane )
    {
        this.msgView = parsedView;

        this.infoField = new JTextField( 5 );
        this.bytesField = new ByteArrayView();
        this.view = createView( addScrollPane );

        // setData( new SerialMessage( true, new EndPoint( 186 ), new EndPoint(
        // 282
        // ),
        // new byte[0] ) );
    }

    /***************************************************************************
     * @param addScrollPane
     * @return
     **************************************************************************/
    private JPanel createView( boolean addScrollPane )
    {
        JPanel panel = new JPanel( new BorderLayout() );
        JTabbedPane tabs = new JTabbedPane();

        if( msgView != null )
        {
            tabs.add( "Fields", createFieldsPanel( addScrollPane ) );
        }
        tabs.add( "Bytes", createBytesPanel() );

        panel.add( createInfoPanel(), BorderLayout.NORTH );
        panel.add( tabs, BorderLayout.CENTER );

        tabs.requestFocusInWindow();

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createInfoPanel()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        infoField.setEditable( false );
        infoField.setBorder( new EmptyBorder( 0, 0, 4, 0 ) );

        panel.add( infoField, BorderLayout.NORTH );

        return panel;
    }

    /***************************************************************************
     * @param addScrollPane
     * @return
     **************************************************************************/
    private Component createFieldsPanel( boolean addScrollPane )
    {
        JPanel panel = new JPanel( new BorderLayout() );

        Component comp = msgView.getView();

        if( addScrollPane )
        {
            JScrollPane pane = new JScrollPane( msgView.getView() );
            comp = pane;
        }

        panel.add( comp, BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createBytesPanel()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( bytesField.getView(), BorderLayout.CENTER );

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
    public SerialMessage getData()
    {
        return msg;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( SerialMessage msg )
    {
        this.msg = msg;

        infoField.setText( buildInfoText( msg ) );

        if( msgView != null )
        {
            msgView.setData( msg );
        }

        bytesField.setData( msg.data );
    }

    /***************************************************************************
     * Builds the string that describes the message reception/transmission time
     * and remote address/port.
     * @param msg the message to be described.
     * @return the built string.
     **************************************************************************/
    private static String buildInfoText( SerialMessage msg )
    {
        StringBuilder str = new StringBuilder();
        DateTimeFormatter dtf = TimeUtils.buildDateTimeDisplayFormat();

        str.append( "Message with " );
        str.append( msg.data.length );
        str.append( " bytes " );
        str.append( msg.isTransmitted ? " transmitted " : "received " );
        str.append( "at " );
        str.append( msg.time.format( dtf ) );

        return str.toString();
    }
}
