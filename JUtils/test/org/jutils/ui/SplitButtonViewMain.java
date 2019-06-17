package org.jutils.ui;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;
import org.jutils.ui.event.ActionAdapter;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class SplitButtonViewMain
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new SplitButtonViewApp() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class SplitButtonViewApp implements IFrameApp
    {
        @Override
        public JFrame createFrame()
        {
            StandardFrameView frameView = new StandardFrameView();

            frameView.setContent( createContent() );
            frameView.setSize( 500, 500 );
            frameView.setTitle( "Split Button View Test" );
            frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

            return frameView.getView();
        }

        private Container createContent()
        {
            JPanel panel = new JPanel( new BorderLayout() );

            panel.add( createToolbar(), BorderLayout.NORTH );
            panel.add( createCenterPanel(), BorderLayout.CENTER );

            return panel;
        }

        private Component createToolbar()
        {
            JToolBar toolbar = new JGoodiesToolBar();
            SplitButtonView<String> sbv = createButtonView( "Open" );

            SwingUtils.addActionToToolbar( toolbar, createNewAction() );
            sbv.install( toolbar );
            SwingUtils.addActionToToolbar( toolbar, createSaveAction() );

            sbv = createButtonView( "" );
            sbv.install( toolbar );
            SwingUtils.addActionToToolbar( toolbar, createSaveAction() );

            return toolbar;
        }

        private Action createNewAction()
        {
            ActionListener listener = ( e ) -> getClass();
            Icon icon = IconConstants.getIcon( IconConstants.NEW_FILE_16 );
            return new ActionAdapter( listener, "New", icon );
        }

        private Action createSaveAction()
        {
            ActionListener listener = ( e ) -> getClass();
            Icon icon = IconConstants.getIcon( IconConstants.SAVE_16 );
            return new ActionAdapter( listener, "Save", icon );
        }

        private static Component createCenterPanel()
        {
            JPanel panel = new JPanel();
            SplitButtonView<String> sbv = createButtonView( "Open" );

            panel.add( sbv.getView() );

            return panel;
        }

        private static SplitButtonView<String> createButtonView( String text )
        {
            Icon icon = IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 );
            List<String> items = new ArrayList<>();

            items.add( "Yellow" );
            items.add( "Blue" );
            items.add( "Chartreuse" );
            items.add( "Green" );
            items.add( "Red" );
            items.add( "Salmon" );

            for( int i = 0; i < 100; i++ )
            {
                items.add( "Color #" + i );
            }

            SplitButtonView<String> sbv = new SplitButtonView<>( text, icon,
                items );

            return sbv;
        }

        @Override
        public void finalizeGui()
        {
        }
    }
}
