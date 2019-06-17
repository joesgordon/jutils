package org.jutils.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.jutils.ui.ListView.IItemListModel;
import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;

/**
 *
 */
public class ListViewMain
{
    /**
     * @param args
     */
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new ListViewApp() );
    }

    /**
     *
     */
    private static final class ListViewApp implements IFrameApp
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public JFrame createFrame()
        {
            StandardFrameView frameView = new StandardFrameView();
            ListView<String> listView = new ListView<String>(
                new StringListModel() );

            List<String> items = new ArrayList<>();

            items.add( "blue" );
            items.add( "black" );
            items.add( "apple" );
            items.add( "white" );
            items.add( "red" );
            items.add( "green" );
            items.add( "orange" );
            items.add( "purple" );
            items.add( "chartreuse" );
            items.add( "yellow" );
            items.add( "brown" );

            listView.setData( items );

            frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            frameView.setTitle( "List View Test" );
            frameView.setContent( listView.getView() );
            frameView.setSize( 600, 600 );

            return frameView.getView();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void finalizeGui()
        {
        }
    }

    /**
     *
     */
    private static final class StringListModel implements IItemListModel<String>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getTitle( String item )
        {
            return item;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String promptForNew( ListView<String> view )
        {
            return view.promptForName( "String" );
        }
    }
}
