package org.jutils.ui;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;

import javax.swing.*;

import org.jutils.ValidationException;
import org.jutils.ui.app.AppRunner;
import org.jutils.ui.app.IApplication;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MessageExceptionView implements IView<JComponent>
{
    /**  */
    private final JPanel view;
    /**  */
    private final ExceptionView exceptionField;
    /**  */
    private final JTextField messageField;

    /***************************************************************************
     * 
     **************************************************************************/
    public MessageExceptionView()
    {
        this.exceptionField = new ExceptionView();
        this.messageField = new JTextField( 20 );
        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        int row = 0;
        GridBagConstraints constraints;

        messageField.setEditable( false );
        messageField.setBorder( null );

        constraints = new GridBagConstraints( 0, row++, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 8, 0 ), 0, 0 );
        panel.add( messageField, constraints );

        constraints = new GridBagConstraints( 0, row++, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( exceptionField.getView(), constraints );

        return panel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * @param message
     **************************************************************************/
    public void setMessage( String message )
    {
        if( message != null )
        {
            messageField.setVisible( true );
            messageField.setText( message );
        }
        else
        {
            messageField.setVisible( false );
            messageField.setText( "" );
        }
    }

    /***************************************************************************
     * @param th
     **************************************************************************/
    public void setException( Throwable th )
    {
        exceptionField.setException( th );
    }

    /***************************************************************************
     * @param parent
     * @param title
     * @param th
     **************************************************************************/
    public static void showExceptionDialog( Component parent, String title,
        Throwable th )
    {
        showExceptionDialog( parent, null, title, th );
    }

    /***************************************************************************
     * @param parent
     * @param message
     * @param title
     * @param th
     **************************************************************************/
    public static void showExceptionDialog( Component parent, String message,
        String title, Throwable th )
    {
        MessageExceptionView meView = new MessageExceptionView();

        meView.setMessage( message );
        meView.setException( th );

        JOptionPane.showMessageDialog( parent, meView.getView(), title,
            JOptionPane.ERROR_MESSAGE );
    }

    /***************************************************************************
     * @param ex
     * @param title
     * @param message
     **************************************************************************/
    public static void invokeLater( Throwable ex, String title, String message )
    {
        IApplication app = new DefaultApp( ex, title, message );

        AppRunner.invokeLater( app );
    }

    /***************************************************************************
     * @param ex
     * @param title
     * @param message
     * @throws InvocationTargetException
     * @throws InterruptedException
     **************************************************************************/
    public static void invokeAndWait( Throwable ex, String title,
        String message ) throws InvocationTargetException, InterruptedException
    {
        IApplication app = new DefaultApp( ex, title, message );

        AppRunner.invokeAndWait( app );
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        ValidationException ex = new ValidationException(
            "Wrong, three tries for a quarter." );

        invokeLater( ex, "WRONG!", "It didn't work" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class DefaultApp implements IApplication
    {
        private final Throwable ex;
        private final String title;
        private final String message;

        public DefaultApp( Throwable ex, String title, String message )
        {
            this.ex = ex;
            this.title = title;
            this.message = message;
        }

        @Override
        public String getLookAndFeelName()
        {
            return null;
        }

        @Override
        public void createAndShowUi()
        {
            showExceptionDialog( null, title, message, ex );
        }
    }
}
