package org.jutils.core.ui;

import java.awt.BorderLayout;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jutils.core.data.UIProperty;
import org.jutils.core.ui.app.FrameRunner;
import org.jutils.core.ui.app.IFrameApp;

/*******************************************************************************
 * 
 ******************************************************************************/
public class StandardUncaughtExceptionHandlerMain
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new StandardUncaughtExceptionHandlerApp() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class StandardUncaughtExceptionHandlerApp
        implements IFrameApp
    {
        @Override
        public JFrame createFrame()
        {
            StandardFrameView frameView = new StandardFrameView();
            Icon icon = UIProperty.OPTIONPANE_ERRORICON.getIcon();
            JButton button = new JButton( "Push Me to Error!", icon );
            JPanel buttonPanel = new JPanel();

            button.addActionListener( ( e ) -> {
                throw new RuntimeException( "ZOMG!!! The worst thing ever!" );
            } );

            buttonPanel.add( button );

            JPanel contentPanel = new JPanel( new BorderLayout() );

            contentPanel.add( buttonPanel, BorderLayout.CENTER );

            frameView.setContent( contentPanel );
            frameView.setSize( 500, 500 );
            frameView.setTitle( "Standard Uncaught Exception Handler Test UI" );
            frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

            return frameView.getView();
        }

        @Override
        public void finalizeGui()
        {
        }
    }
}
