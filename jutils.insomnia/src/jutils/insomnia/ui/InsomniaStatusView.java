package jutils.insomnia.ui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JComponent;

import jutils.core.time.TimeUtils;
import jutils.core.ui.LedLabel;
import jutils.core.ui.StandardFormView;
import jutils.core.ui.model.IDataView;
import jutils.insomnia.data.InsomniaStatus;

/*******************************************************************************
 * 
 ******************************************************************************/
public class InsomniaStatusView implements IDataView<InsomniaStatus>
{
    /**  */
    private final JComponent view;

    /**  */
    private final LedLabel statusLabel;

    /**  */
    private InsomniaStatus status;

    /***************************************************************************
     * 
     **************************************************************************/
    public InsomniaStatusView()
    {
        this.statusLabel = new LedLabel( 24 );
        this.view = createView();

        Font font = statusLabel.getView().getFont();

        font = font.deriveFont( 24.0f ).deriveFont( Font.BOLD );

        statusLabel.getView().setFont( font );

        setData( new InsomniaStatus() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        StandardFormView form = new StandardFormView();

        form.addComponent( statusLabel.getView() );

        return form.getView();
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
    public InsomniaStatus getData()
    {
        return status;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( InsomniaStatus status )
    {
        this.status = status;

        Color c = Color.black;

        switch( status.currentActivity.state )
        {
            case ACTIVE:
                c = Color.green;
                break;

            case INACTIVE:
                c = Color.blue;
                break;

            case IDLE:
                c = Color.gray;
                break;

            default:
                c = Color.red;
                break;
        }

        long millis = status.currentActivity.duration;

        millis /= 100;
        millis *= 100;

        String text = status.currentActivity.state.name + " (" +
            TimeUtils.durationToString( millis ) + ")";

        statusLabel.setColor( c );
        statusLabel.setText( text );
    }

}
