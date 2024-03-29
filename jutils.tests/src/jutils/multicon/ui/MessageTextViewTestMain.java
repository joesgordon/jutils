package jutils.multicon.ui;

import java.awt.Container;

import javax.swing.JFrame;

import jutils.core.ui.StandardFrameView;
import jutils.core.ui.TextHexView;
import jutils.core.ui.app.AppRunner;
import jutils.core.ui.app.IFrameApp;

/*******************************************************************************
 *
 ******************************************************************************/
public class MessageTextViewTestMain
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.invokeLater( new MessageTextViewTestApp() );
    }

    /***************************************************************************
     *
     **************************************************************************/
    private static class MessageTextViewTestApp implements IFrameApp
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public JFrame createFrame()
        {
            StandardFrameView frame = new StandardFrameView();

            frame.setContent( createContent() );
            frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

            frame.setSize( 800, 800 );

            return frame.getView();
        }

        /**
         * @return
         */
        private static Container createContent()
        {
            TextHexView view = new TextHexView();

            return view.getView();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void finalizeGui()
        {
        }
    }
}
