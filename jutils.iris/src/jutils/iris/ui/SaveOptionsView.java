package jutils.iris.ui;

import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;

import jutils.core.io.parsers.ExistenceType;
import jutils.core.io.parsers.FileType;
import jutils.core.ui.ListView;
import jutils.core.ui.ListView.IItemListModel;
import jutils.core.ui.OkDialogView;
import jutils.core.ui.OkDialogView.OkDialogButtons;
import jutils.core.ui.StandardFormView;
import jutils.core.ui.fields.ComboFormField;
import jutils.core.ui.fields.FileFormField;
import jutils.core.ui.fields.IntegerFormField;
import jutils.core.ui.fields.NamedItemDescriptor;
import jutils.core.ui.model.IDataView;
import jutils.iris.data.IRasterAlbum;
import jutils.iris.data.SaveFormat;
import jutils.iris.data.SaveOptions;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SaveOptionsView implements IDataView<SaveOptions>
{
    /**  */
    private final JComponent view;
    /**  */
    private final IntegerFormField indexField;
    /**  */
    private final IntegerFormField countField;
    /**  */
    private final ComboFormField<SaveFormat> formatField;
    /**  */
    private final FileFormField dirField;
    /**   */
    private final ListView<String> namesField;

    /**  */
    private SaveOptions data;

    /***************************************************************************
     * 
     **************************************************************************/
    public SaveOptionsView()
    {
        this.indexField = new IntegerFormField( "Index", null, 4, 0, null );
        this.countField = new IntegerFormField( "Count", null, 4, 0, null );
        this.formatField = new ComboFormField<>( "Format", SaveFormat.values(),
            new NamedItemDescriptor<>() );
        this.dirField = new FileFormField( "Directory", FileType.DIRECTORY,
            ExistenceType.EXISTS, false, true );
        this.namesField = new ListView<String>( new NamesModel(), false,
            false );

        indexField.getTextField().setMinimumSize(
            indexField.getTextField().getPreferredSize() );

        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints = new GridBagConstraints();
        int m = StandardFormView.DEFAULT_FORM_MARGIN;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0, 1.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( createForm(), constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( m, 0, m, m ), 0, 0 );
        panel.add( namesField.getView(), constraints );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createForm()
    {
        StandardFormView form = new StandardFormView();

        form.addField( indexField );
        form.addField( countField );
        form.addField( formatField );
        form.addField( dirField );

        return form.getView();
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    public void setEditable( boolean editable )
    {
        indexField.setEditable( editable );
        countField.setEditable( editable );
        formatField.setEditable( editable );
        dirField.setEditable( editable );
        namesField.setEnabled( editable );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public SaveOptions getData()
    {
        return this.data;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( SaveOptions options )
    {
        this.data = options;

        indexField.setValue( options.index );
        countField.setValue( options.count );
        formatField.setValue( options.format );
        dirField.setValue( options.dir );
        namesField.setData( options.names );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class NamesModel implements IItemListModel<String>
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
            return null;
        }
    }

    /***************************************************************************
     * @param parent
     * @param album
     * @return
     **************************************************************************/
    public SaveOptions showDialog( Component parent, IRasterAlbum album )
    {
        SaveOptions config = null;
        OkDialogView dialogView = new OkDialogView( parent, this.getView(),
            ModalityType.DOCUMENT_MODAL, OkDialogButtons.OK_CANCEL );

        dialogView.setTitle( "Raw Pixel Read Options for " + album.getName() );

        if( dialogView.show() )
        {
            config = this.getData();
        }

        return config;

    }
}
