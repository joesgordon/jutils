package org.jutils.apps.summer.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

import org.jutils.core.IconConstants;
import org.jutils.core.OptionUtils;
import org.jutils.core.SwingUtils;
import org.jutils.core.ui.JGoodiesToolBar;
import org.jutils.core.ui.event.ActionAdapter;
import org.jutils.core.ui.event.FileChooserListener;
import org.jutils.core.ui.event.FileChooserListener.IFileSelected;
import org.jutils.core.ui.event.FileChooserListener.ILastFile;
import org.jutils.core.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TextView implements IDataView<String>
{
    /**  */
    private final JPanel view;
    /**  */
    private final JTextArea textField;
    /**  */
    private final FileChooserListener openListener;
    /**  */
    private final Action openAction;
    /**  */
    private final FileChooserListener saveListener;
    /**  */
    private final SaveListener saveFileListener;
    /**  */
    private final Action saveAction;

    /**  */
    private String text;

    /***************************************************************************
     * 
     **************************************************************************/
    public TextView()
    {
        Icon icon;

        this.view = new JPanel();
        this.textField = new JTextArea();

        this.openListener = new FileChooserListener( view, "Open File", false,
            new OpenListener( this ) );
        icon = IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 );
        this.openAction = new ActionAdapter( openListener, "Open File", icon );

        this.saveFileListener = new SaveListener( this );
        this.saveListener = new FileChooserListener( view, "Save File", true,
            saveFileListener, saveFileListener );
        icon = IconConstants.getIcon( IconConstants.SAVE_16 );
        this.saveAction = new ActionAdapter( saveListener, "Save File", icon );

        createContent();

        setData( "" );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JGoodiesToolBar();

        SwingUtils.addActionToToolbar( toolbar, openAction );
        SwingUtils.addActionToToolbar( toolbar, saveAction );

        return toolbar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Container createContent()
    {
        view.setLayout( new BorderLayout() );
        JScrollPane pane = new JScrollPane( textField );

        textField.setFont( SwingUtils.getFixedFont( 12 ) );

        pane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );

        view.add( createToolbar(), BorderLayout.NORTH );
        view.add( pane, BorderLayout.CENTER );

        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getData()
    {
        return text;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( String text )
    {
        this.text = text;

        textField.setText( text );
    }

    /***************************************************************************
     * @param description
     * @param extension
     **************************************************************************/
    public void setExtension( String description, String extension )
    {
        openListener.removeAllExtensions();
        openListener.addExtension( description, extension );

        saveListener.removeAllExtensions();
        saveListener.addExtension( description, extension );
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    public void setEditable( boolean editable )
    {
        this.textField.setEditable( editable );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void showSave()
    {
        showSave( null );
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    public void showSave( File file )
    {
        saveFileListener.defaultFile = file;

        saveAction.actionPerformed( null );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class OpenListener implements IFileSelected
    {
        private final TextView view;

        /**
         * @param view
         */
        public OpenListener( TextView view )
        {
            this.view = view;
        }

        /**
         * @{@inheritDoc}
         */
        @Override
        public void fileChosen( File file )
        {
            try( Scanner scnr = new Scanner( file ) )
            {
                String text = scnr.useDelimiter( "\\A" ).next();

                view.setData( text );
            }
            catch( FileNotFoundException ex )
            {
                OptionUtils.showErrorMessage( view.view,
                    "Cannot open file for reading: " + file.getAbsolutePath(),
                    "Cannot Open File" );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class SaveListener implements IFileSelected, ILastFile
    {
        /**  */
        private final TextView view;

        /**  */
        public File defaultFile;

        /**
         * @param view
         */
        public SaveListener( TextView view )
        {
            this.view = view;
            this.defaultFile = null;
        }

        /**
         * @{@inheritDoc}
         */
        @Override
        public File getLastFile()
        {
            return defaultFile;
        }

        /**
         * @{@inheritDoc}
         */
        @Override
        public void fileChosen( File file )
        {
            try( PrintStream stream = new PrintStream( file ) )
            {
                stream.print( view.text );
            }
            catch( FileNotFoundException ex )
            {
                OptionUtils.showErrorMessage( view.view,
                    "Cannot open file for writing: " + file.getAbsolutePath(),
                    "Cannot Open File" );
            }
        }
    }
}
