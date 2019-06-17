package org.jutils.ui;

import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.jutils.SwingUtils;
import org.jutils.Utils;
import org.jutils.io.FilePrintStream;
import org.jutils.io.IOUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class StandardUncaughtExceptionHandler
    implements Thread.UncaughtExceptionHandler
{
    /**  */
    private final JFrame frame;
    /**  */
    private final MessageExceptionView exView;

    /**  */
    private static boolean showingDialog = false;

    /***************************************************************************
     * @param frame
     **************************************************************************/
    public StandardUncaughtExceptionHandler( JFrame frame )
    {
        this.frame = frame;
        this.exView = new MessageExceptionView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void uncaughtException( Thread thread, Throwable th )
    {
        try
        {
            th.printStackTrace();

            handleUncaughtException( thread, th );
        }
        catch( Throwable ex )
        {
            ex.printStackTrace();
        }
    }

    /***************************************************************************
     * @param thread
     * @param th
     **************************************************************************/
    private void handleUncaughtException( Thread thread, Throwable th )
    {
        File exFile = IOUtils.getUsersFile( ".uncaught_exception" );

        try( FilePrintStream stream = new FilePrintStream( exFile ) )
        {
            String name = thread.getName();

            String trace = Utils.printStackTrace( th );

            name = name == null ? "null" : name;

            stream.println( "Thread %s", name );
            stream.println( trace );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
        }

        if( !showingDialog )
        {
            showingDialog = true;
            try
            {
                displayException( th );
            }
            finally
            {
                showingDialog = false;
            }
        }
    }

    /***************************************************************************
     * @param ex
     **************************************************************************/
    private void displayException( Throwable ex )
    {
        exView.setMessage( "The following error has occurred. You may " +
            "choose to ignore and continue or quit." );

        exView.setException( ex );

        String [] choices = new String[] { "Continue", "Quit" };
        String choice = SwingUtils.showConfirmMessage( frame, exView.getView(),
            "Unhandled Error", choices, choices[1], true );

        if( choice == null || choice.equals( choices[1] ) )
        {
            System.exit( 1 );
        }
    }
}
