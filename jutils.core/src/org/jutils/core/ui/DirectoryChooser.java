package org.jutils.core.ui;

import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Window;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jutils.core.OptionUtils;
import org.jutils.core.SwingUtils;
import org.jutils.core.Utils;
import org.jutils.core.ValidationException;
import org.jutils.core.io.IOUtils;
import org.jutils.core.io.LogUtils;
import org.jutils.core.io.parsers.StringParser;
import org.jutils.core.ui.app.AppRunner;
import org.jutils.core.ui.app.IApplication;
import org.jutils.core.ui.validation.IValidityChangedListener;
import org.jutils.core.ui.validation.ValidationTextField;
import org.jutils.core.ui.validation.ValidationView;
import org.jutils.core.ui.validation.Validity;
import org.jutils.core.ui.validators.ITextValidator;

/*******************************************************************************
 *
 ******************************************************************************/
public class DirectoryChooser
{
    /**  */
    private final JDialog dialog;
    /**  */
    private final JLabel messageLabel;
    /**  */
    private final ValidationTextField dirsField;
    /**  */
    private final DirectoryTree tree;

    /**  */
    private File [] selected;

    /***************************************************************************
     * @param owner
     **************************************************************************/
    public DirectoryChooser( Window owner )
    {
        this( owner, "Choose Directory" );
    }

    /***************************************************************************
     * @param owner Frame
     * @param title String
     **************************************************************************/
    public DirectoryChooser( Window owner, String title )
    {
        this( owner, title, "Please choose a folder or folders:" );
    }

    /***************************************************************************
     * @param owner Frame
     * @param title String
     * @param message String
     **************************************************************************/
    public DirectoryChooser( Window owner, String title, String message )
    {
        this( owner, title, message, null );
    }

    /***************************************************************************
     * @param owner
     * @param title
     * @param message
     * @param paths
     **************************************************************************/
    public DirectoryChooser( Window owner, String title, String message,
        String paths )
    {
        this.dialog = new JDialog( owner, title, ModalityType.DOCUMENT_MODAL );
        this.tree = new DirectoryTree();
        this.messageLabel = new JLabel();
        this.dirsField = new ValidationTextField();

        this.selected = null;

        tree.setSelectedPaths( paths );

        dialog.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
        dialog.setContentPane( createContentPanel( message ) );
        dialog.setSize( 600, 500 );
        dialog.validate();
        dialog.setLocationRelativeTo( owner );
    }

    /***************************************************************************
     * @param message
     * @return
     **************************************************************************/
    private JPanel createContentPanel( String message )
    {
        JPanel mainPanel = new JPanel( new GridBagLayout() );
        JScrollPane scrollPane = new JScrollPane( tree.getView() );
        GridBagConstraints constraints;

        messageLabel.setText( message );

        dirsField.setValidator( new DirsValidator() );
        dirsField.addValidityChanged( new DirFieldListener( this ) );

        tree.addSelectedListener( ( e ) -> dirsField.setText(
            Utils.collectionToString( e.getItem(), File.pathSeparator ) ) );

        scrollPane.setMinimumSize(
            new Dimension( messageLabel.getPreferredSize().width,
                messageLabel.getPreferredSize().width ) );
        scrollPane.setPreferredSize( scrollPane.getMinimumSize() );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 8, 8, 0, 8 ), 0, 0 );
        mainPanel.add( messageLabel, constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 8, 8, 8, 8 ), 0, 0 );
        mainPanel.add( scrollPane, constraints );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 8, 8, 0, 8 ), 0, 0 );
        mainPanel.add( new ValidationView( dirsField ).getView(), constraints );

        constraints = new GridBagConstraints( 0, 3, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
            new Insets( 8, 4, 8, 4 ), 0, 0 );
        mainPanel.add( createButtonPanel(), constraints );

        return mainPanel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createButtonPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;
        JButton newButton;
        JButton okButton;
        JButton cancelButton;

        // ---------------------------------------------------------------------
        //
        // ---------------------------------------------------------------------
        newButton = new JButton();
        newButton.setText( "New Directory" );
        newButton.addActionListener( ( e ) -> handleNewDirectoryPressed() );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 2, 4, 2, 2 ), 4, 0 );
        panel.add( newButton, constraints );

        // ---------------------------------------------------------------------
        //
        // ---------------------------------------------------------------------
        okButton = new JButton();
        okButton.setText( "OK" );
        okButton.addActionListener( ( e ) -> handleOkPressed() );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 2, 2, 2, 2 ), 4, 0 );
        panel.add( okButton, constraints );

        // ---------------------------------------------------------------------
        //
        // ---------------------------------------------------------------------
        cancelButton = new JButton();
        cancelButton.setText( "Cancel" );
        cancelButton.addActionListener( ( e ) -> dialog.dispose() );

        constraints = new GridBagConstraints( 2, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 2, 2, 2, 4 ), 4, 0 );
        panel.add( cancelButton, constraints );

        Dimension dim = SwingUtils.getMaxComponentSize( newButton, okButton,
            cancelButton );

        newButton.setMinimumSize( dim );
        newButton.setPreferredSize( dim );
        okButton.setMinimumSize( dim );
        okButton.setPreferredSize( dim );
        cancelButton.setMinimumSize( dim );
        cancelButton.setPreferredSize( dim );

        return panel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void handleNewDirectoryPressed()
    {
        File [] selectedFiles = tree.getSelected();

        if( selectedFiles.length == 1 )
        {
            StringParser nameParser = new StringParser();

            String name = OptionUtils.promptForValue( dialog, "Directory Name",
                nameParser, "Enter the new directory name:" );

            if( name != null )
            {
                File parentDir = selectedFiles[0];
                createNewDirectory( parentDir, name );
            }
        }
        else
        {
            OptionUtils.showErrorMessage( dialog,
                "Please choose only one directory when creating a sub-directory.",
                "Cannot Create New Directory" );
        }
    }

    private void createNewDirectory( File parentDir, String name )
    {
        File newDir = new File( parentDir, name );

        if( !newDir.exists() )
        {
            if( !newDir.mkdir() )
            {
                OptionUtils.showErrorMessage( dialog,
                    "Please check the permissions on the parent directory.",
                    "Cannot Create New Directory" );
            }
            else
            {
                tree.refreshSelected();
                setSelectedPaths( newDir.getAbsolutePath() );
                tree.expandSelected();
            }
        }
        else
        {
            OptionUtils.showErrorMessage( dialog,
                "The directory or file, '" + name + "', already exists.",
                "Cannot Create New Directory" );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void handleOkPressed()
    {
        selected = tree.getSelected();
        dialog.dispose();
    }

    /***************************************************************************
     * @param visible
     **************************************************************************/
    public void setVisible( boolean visible )
    {
        if( visible && !dialog.isVisible() )
        {
            dialog.validate();
            dialog.setLocationRelativeTo( dialog.getParent() );
        }
        dialog.setVisible( visible );
    }

    /***************************************************************************
     * @param iconImages
     **************************************************************************/
    public void setIconImages( List<Image> iconImages )
    {
        dialog.setIconImages( iconImages );
    }

    /***************************************************************************
     * @param args ignored
     **************************************************************************/
    public static void main( String [] args )
    {
        IApplication app = new IApplication()
        {
            @Override
            public String getLookAndFeelName()
            {
                return null;
            }

            @Override
            public void createAndShowUi()
            {
                DirectoryChooser dialog = new DirectoryChooser( null );

                dialog.setSelectedPaths( new File( IOUtils.USERS_DIR,
                    "garbage_jfdkslfjsdl" ).getAbsolutePath() );

                dialog.setVisible( true );

                String paths = dialog.getSelectedPaths();
                LogUtils.printDebug( paths );
            }
        };

        AppRunner.invokeLater( app );
    }

    /***************************************************************************
     * @return File[]
     **************************************************************************/
    public File [] getSelected()
    {
        return selected;
    }

    /***************************************************************************
     * @param paths String
     **************************************************************************/
    public void setSelectedPaths( String paths )
    {
        tree.setSelectedPaths( paths );

        dirsField.setText( tree.getSelectedPaths() );
    }

    /***************************************************************************
     * @return String
     **************************************************************************/
    public String getSelectedPaths()
    {
        return tree.getSelectedPaths();
    }

    /***************************************************************************
     * @param width
     * @param height
     **************************************************************************/
    public void setSize( int width, int height )
    {
        dialog.setSize( width, height );
    }

    /***************************************************************************
     * @param title
     **************************************************************************/
    public void setTitle( String title )
    {
        dialog.setTitle( title );
    }

    /***************************************************************************
     * @param message
     **************************************************************************/
    public void setMessage( String message )
    {
        messageLabel.setText( message );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class DirFieldListener implements IValidityChangedListener
    {
        private final DirectoryChooser chooser;

        public DirFieldListener( DirectoryChooser chooser )
        {
            this.chooser = chooser;
        }

        @Override
        public void signalValidity( Validity validity )
        {
            if( validity.isValid )
            {
                chooser.tree.setSelectedPaths( chooser.dirsField.getText() );
            }
            else
            {
                chooser.tree.clearSelection();
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class DirsValidator implements ITextValidator
    {
        @Override
        public void validateText( String text ) throws ValidationException
        {
            if( text == null )
            {
                throw new ValidationException( "Null text" );
            }

            File [] dirs = IOUtils.getFilesFromString( text );

            if( dirs.length < 1 )
            {
                throw new ValidationException( "Empty paths string" );
            }

            for( File d : dirs )
            {
                IOUtils.validateDirInput( d, "Directories Chosen" );
            }
        }
    }
}
