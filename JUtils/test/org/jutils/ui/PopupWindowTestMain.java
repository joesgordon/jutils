package org.jutils.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;
import org.jutils.ui.calendar.DateView;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.hex.HexPanel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PopupWindowTestMain implements IFrameApp
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new PopupWindowTestMain() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        PwtFrame frame = new PwtFrame();

        return frame.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void finalizeGui()
    {
    }

    /***************************************************************************
     *
     **************************************************************************/
    private static final class PwtFrame
    {
        /**  */
        public final StandardFrameView frame;
        /**  */
        public final JButton testButton;
        /**  */
        public final JButton testJpButton;

        /**
         * 
         */
        public PwtFrame()
        {
            this.testButton = new JButton();
            this.testJpButton = new JButton();
            this.frame = new StandardFrameView();

            frame.setTitle( "Popup Window Test Main" );
            frame.setToolbar( createToolbar() );
            frame.setContent( createContent() );
            frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

            frame.setSize( 500, 500 );
        }

        /**
         * @return
         */
        public JFrame getView()
        {
            return frame.getView();
        }

        /**
         * @return
         */
        private JToolBar createToolbar()
        {
            JToolBar toolbar = new JGoodiesToolBar();

            SwingUtils.addActionToToolbar( toolbar, createTestAction(),
                testButton );

            SwingUtils.addActionToToolbar( toolbar, createTestJpAction(),
                testJpButton );

            return toolbar;
        }

        /**
         * @return
         */
        private Action createTestAction()
        {
            ActionListener l = ( e ) -> showPopup();
            Icon icon = IconConstants.getIcon( IconConstants.ANALYZE_16 );

            return new ActionAdapter( l, "Test", icon );
        }

        /**
         * @return
         */
        private Action createTestJpAction()
        {
            ActionListener l = ( e ) -> showJPopup();
            Icon icon = IconConstants.getIcon( IconConstants.OPEN_FILE_16 );

            return new ActionAdapter( l, "Test", icon );
        }

        /**
         * 
         */
        private void showPopup()
        {
            DateView view = new DateView();
            PopupWindow popup = new PopupWindow( true, view.getView() );

            popup.show( testButton, testButton.getWidth(),
                testButton.getHeight() );
        }

        /**
         * 
         */
        private void showJPopup()
        {
            DateView view = new DateView();
            JPopupMenu popup = new JPopupMenu();

            popup.add( view.getView() );

            popup.show( testButton, testButton.getWidth(),
                testButton.getHeight() );
        }

        /**
         * @return
         */
        private static Container createContent()
        {
            JPanel panel = new JPanel( new BorderLayout() );
            HexPanel hex = new HexPanel();

            panel.add( hex.getView(), BorderLayout.CENTER );

            return panel;
        }
    }
}
