package org.jutils.ui;

import java.awt.*;
import java.io.File;
import java.util.List;

import javax.swing.*;

import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;
import org.jutils.ui.event.FileChooserListener;
import org.jutils.ui.event.FileChooserListener.IFileSelected;
import org.jutils.ui.event.FileChooserListener.ILastFile;
import org.jutils.ui.event.FileDropTarget;
import org.jutils.ui.fields.BooleanFormField;
import org.jutils.ui.fields.IntegerFormField;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FilePropertiesViewMain
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new FilePropertiesViewApp() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class FilePropertiesViewApp implements IFrameApp
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public JFrame createFrame()
        {
            StandardFrameView frameView = new StandardFrameView();

            frameView.setContent( createContent() );
            frameView.setSize( 500, 500 );
            frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            frameView.setTitle( "File Properties Test" );

            return frameView.getView();
        }

        /**
         * @return
         */
        private static Container createContent()
        {
            JPanel panel = new JPanel( new GridBagLayout() );
            GridBagConstraints constraints = new GridBagConstraints();
            JButton button = new JButton( "Show File Props" );
            IFileSelected fileSelected = ( f ) -> show( f, panel );
            ILastFile ilf = () -> null;
            FileChooserListener listener = new FileChooserListener( panel,
                "Choose File", true, fileSelected, ilf );

            listener.setAdditional( createExample() );

            button.setDropTarget( new FileDropTarget(
                ( e ) -> handleFileDropped( e.getItem().getFiles(), panel ) ) );
            button.addActionListener( listener );

            constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets( 0, 0, 0, 0 ), 20, 10 );
            panel.add( button, constraints );

            return panel;
        }

        /**
         * @return
         */
        private static JComponent createExample()
        {
            StandardFormView form = new StandardFormView();
            IntegerFormField intField = new IntegerFormField( "Int", null, 10 );
            BooleanFormField boolField = new BooleanFormField( "Boolean" );

            intField.setValue( 0 );

            form.addField( intField );
            form.addField( boolField );

            return form.getView();
        }

        /**
         * @param list
         * @param parent
         */
        private static void handleFileDropped( List<File> list,
            Component parent )
        {
            if( !list.isEmpty() )
            {
                show( list.get( 0 ), parent );
            }
        }

        /**
         * @param f
         * @param parent
         */
        private static void show( File f, Component parent )
        {
            FilePropertiesView.show( f, parent );
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
