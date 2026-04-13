package jutils.core.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import jutils.core.ui.app.AppRunner;

/*******************************************************************************
 * 
 ******************************************************************************/
public class StatusBarViewMain
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.DEFAULT_LAF = AppRunner.JGOODIES_LAF;

        AppRunner.invokeLater( () -> createFrame() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static JFrame createFrame()
    {
        StatusBarTest statusBar = new StatusBarTest();
        StandardFrameView view = new StandardFrameView( statusBar );
        JFrame frame = view.getView();

        view.setTitle( "Status Bar Test" );
        view.setSize( 400, 400 );
        view.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        return frame;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class StatusBarTest extends StatusBarView
    {
        private final JLabel customStatus;

        /**
         * 
         */
        public StatusBarTest()
        {
            super();

            this.customStatus = new JLabel( "fdjsklfjds" );

            createAdditionalStatus();
        }

        /**
         * {@inheritDoc}
         */
        protected void createAdditionalStatus()
        {
            JPanel panel = super.additionalStatus;
            panel.setLayout( new GridBagLayout() );
            GridBagConstraints constraints;

            int c = 0;

            constraints = new GridBagConstraints( c++, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets( 0, 4, 0, 4 ), 0, 0 );
            panel.add( new JSeparator( SwingConstants.VERTICAL ), constraints );

            constraints = new GridBagConstraints( c++, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 0, 4, 0, 4 ), 0, 0 );
            panel.add( customStatus, constraints );

            constraints = new GridBagConstraints( c++, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets( 0, 4, 0, 4 ), 0, 0 );
            panel.add( new JSeparator( SwingConstants.VERTICAL ), constraints );
        }
    }
}
