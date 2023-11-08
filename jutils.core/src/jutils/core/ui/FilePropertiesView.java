package jutils.core.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import jutils.core.io.IOUtils;
import jutils.core.io.StringPrintStream;
import jutils.core.time.TimeUtils;
import jutils.core.ui.OkDialogView.OkDialogButtons;
import jutils.core.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FilePropertiesView implements IDataView<File>
{
    /**  */
    private final JPanel view;
    /**  */
    private final FileIcon icon;
    /**  */
    private final JTextField pathField;
    /**  */
    private final JTextField descField;
    /**  */
    private final JTextField sizeField;
    /**  */
    private final JTextField dateField;
    /**  */
    private final JTextField typeField;
    /**  */
    private final VerboseMessageView msgview;
    /**  */
    private final DateTimeFormatter fmt;

    // TODO Create a better properties view.

    /**  */
    private File file;

    /***************************************************************************
     * 
     **************************************************************************/
    public FilePropertiesView()
    {
        this.icon = new FileIcon();
        this.pathField = new JTextField( 20 );
        this.descField = new JTextField( 20 );
        this.sizeField = new JTextField( 20 );
        this.dateField = new JTextField( 20 );
        this.typeField = new JTextField( 20 );
        this.msgview = new VerboseMessageView();
        this.fmt = TimeUtils.buildTimeDisplayFormat();
        this.view = createView();

        this.file = null;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( createForm(), BorderLayout.NORTH );
        panel.add( msgview.getView(), BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createForm()
    {
        StandardFormView form = new StandardFormView( 3, 4 );

        pathField.setEditable( false );
        pathField.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );

        descField.setEditable( false );
        descField.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );

        sizeField.setEditable( false );
        sizeField.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );

        dateField.setEditable( false );
        dateField.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );

        typeField.setEditable( false );
        typeField.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );

        IconTextField itf = new IconTextField( pathField );

        itf.setIcon( icon );

        form.addField( "Path", itf.getView() );
        form.addField( "Type", typeField );
        form.addField( "Description", descField );
        form.addField( "Size", sizeField );
        form.addField( "Last Modified", dateField );

        return form.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public File getData()
    {
        return file;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( File file )
    {
        this.file = file;

        long len = file.length();
        String desc = icon.fileSys.getSystemTypeDescription( file );
        String ext = IOUtils.getFileExtension( file );
        String type = "Does Not Exist";
        LocalDateTime time = TimeUtils.fromLinuxEpoch( file.lastModified() );

        if( file.exists() )
        {
            type = "Unknown";

            if( file.isFile() )
            {
                type = "File";
            }
            else if( file.isDirectory() )
            {
                type = "Directory";
            }
        }

        desc += ext.isEmpty() ? "" : " (" + ext + ")";

        icon.setFile( file );
        pathField.setText( file.getAbsolutePath() );
        typeField.setText( type );
        sizeField.setText( IOUtils.byteCount( len ) + " (" + len + " bytes)" );
        dateField.setText( time.format( fmt ) );
        descField.setText( desc );

        pathField.setCaretPosition( 0 );

        try( StringPrintStream msg = new StringPrintStream() )
        {
            msg.println( "  Can Execute: %s", file.canExecute() );
            msg.println( "     Can Read: %s", file.canRead() );
            msg.println( "    Can Write: %s", file.canWrite() );
            msg.println( "    Is Hidden: %s", file.isHidden() );

            msg.println();

            msg.println( "--- Volume Info ---" );
            msg.println( "   Free Space: %s",
                IOUtils.byteCount( file.getFreeSpace() ) );
            msg.println( "  Total Space: %s",
                IOUtils.byteCount( file.getTotalSpace() ) );
            msg.println( " Usable Space: %s",
                IOUtils.byteCount( file.getUsableSpace() ) );

            msgview.setMessages( "Additional Properties", msg.toString() );
        }
    }

    /***************************************************************************
     * @param parent
     **************************************************************************/
    public void show( Component parent )
    {
        OkDialogView okView = new OkDialogView( parent, getView(),
            ModalityType.DOCUMENT_MODAL, OkDialogButtons.OK_ONLY );

        // okView.getView().setIconImage( image );

        String title = "Properties";

        title = file == null ? title : file.getName() + " " + title;

        okView.show( title, new Dimension( 450, 400 ) );
    }

    /***************************************************************************
     * @param f
     * @param parent
     **************************************************************************/
    public static void show( File f, Component parent )
    {
        FilePropertiesView view = new FilePropertiesView();

        view.setData( f );
        view.show( parent );
    }
}
