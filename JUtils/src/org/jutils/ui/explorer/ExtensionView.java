package org.jutils.ui.explorer;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.jutils.IconConstants;
import org.jutils.ui.*;
import org.jutils.ui.ListView.IItemListModel;
import org.jutils.ui.explorer.data.ExtensionConfig;
import org.jutils.ui.fields.StringFormField;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ExtensionView implements IDataView<ExtensionConfig>
{
    /**  */
    private final JPanel view;
    /**  */
    private final StringFormField extField;
    /**  */
    private final StringFormField descField;
    /**  */
    private final ListView<String> appList;

    /**  */
    private ExtensionConfig extension;

    /***************************************************************************
     * 
     **************************************************************************/
    public ExtensionView()
    {
        this.extField = new StringFormField( "Extension" );
        this.descField = new StringFormField( "Description" );
        this.appList = new ListView<>( new ExtensionItemListModel() );
        this.view = createView();

        setData( new ExtensionConfig( "a", "A File" ) );

        extField.setUpdater( ( s ) -> extension.ext = s );
        descField.setUpdater( ( s ) -> extension.description = s );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        StandardFormView form = new StandardFormView();
        JButton defaultButton = new JButton(
            IconConstants.getIcon( IconConstants.CHECK_16 ) );
        defaultButton.setToolTipText( "Make program extension default" );

        TitleView titleView = new TitleView( "Applications",
            appList.getView() );

        appList.addSeparatorToToolbar();
        appList.addToToolbar( defaultButton );

        form.addField( extField );
        form.addField( descField );
        form.addComponent( titleView.getView() );

        return form.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public ExtensionConfig getData()
    {
        return extension;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( ExtensionConfig ext )
    {
        this.extension = ext;

        extField.setValue( ext.ext );
        descField.setValue( ext.description );
        appList.setData( ext.apps );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ExtensionItemListModel
        implements IItemListModel<String>
    {
        @Override
        public String getTitle( String item )
        {
            return item;
        }

        @Override
        public String promptForNew( ListView<String> view )
        {
            // TODO display drop down in a dialog with all programs.

            return null;
        }
    }
}
