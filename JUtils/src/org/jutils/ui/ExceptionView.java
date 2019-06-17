package org.jutils.ui;

import java.awt.*;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import org.jutils.Utils;
import org.jutils.ui.fields.StringFormField;
import org.jutils.ui.model.IView;

/*******************************************************************************
 *
 ******************************************************************************/
public class ExceptionView implements IView<JComponent>
{
    /**  */
    private final TitleView titlePanel;
    /**  */
    private final StringFormField messageField;
    /**  */
    private final ErrorView stacktraceField;

    /***************************************************************************
     * 
     **************************************************************************/
    public ExceptionView()
    {
        this.messageField = new StringFormField( "Message", 0, null );
        this.stacktraceField = new ErrorView();
        this.titlePanel = new TitleView( "", createView() );

        messageField.setEditable( false );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        stacktraceField.setBorder( new MatteBorder( 1, 0, 0, 0, Color.gray ) );

        panel.add( createForm(), BorderLayout.NORTH );
        panel.add( stacktraceField.getView(), BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createForm()
    {
        StandardFormView form = new StandardFormView();

        form.addField( messageField );

        return form.getView();
    }

    /***************************************************************************
     * @param th
     **************************************************************************/
    public void setException( Throwable th )
    {
        titlePanel.setTitle( th.getClass().getName() );
        messageField.setValue( th.getMessage() );
        stacktraceField.setData( Utils.printStackTrace( th ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return titlePanel.getView();
    }
}
