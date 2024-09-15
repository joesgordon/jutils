package jutils.platform.ui;

import java.awt.BorderLayout;
import java.util.function.Consumer;

import javax.swing.JComponent;
import javax.swing.JPanel;

import jutils.core.ui.MessageInputView;
import jutils.core.ui.model.IView;
import jutils.platform.data.SerialMessage;

/*******************************************************************************
 * Defines a view that will display a method for defining messages and a message
 * table.
 ******************************************************************************/
public class SerialConnectionView implements IView<JComponent>
{
    /**  */
    private final JComponent view;

    /**  */
    private final SerialMessagesView msgsView;
    /**  */
    private final MessageInputView msgView;

    /***************************************************************************
     * @param msgHandler
     **************************************************************************/
    public SerialConnectionView( Consumer<byte []> msgHandler )
    {
        this.msgView = new MessageInputView( msgHandler );
        this.msgsView = new SerialMessagesView();

        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( msgsView.getView(), BorderLayout.CENTER );
        panel.add( msgView.getView(), BorderLayout.SOUTH );

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
     * @param msg
     **************************************************************************/
    public void addItem( SerialMessage msg )
    {
        msgsView.addItem( msg );
    }
}
