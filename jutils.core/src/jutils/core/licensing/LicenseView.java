package jutils.core.licensing;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.URL;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jutils.core.ui.ScrollableEditorPaneView;
import jutils.core.ui.model.IView;

public class LicenseView implements IView<JPanel>
{
    private final JPanel panel;
    private final ScrollableEditorPaneView editorPane;
    private final JScrollPane scrollPane;

    public LicenseView( URL pageUrl )
    {
        this.panel = new JPanel( new BorderLayout() );
        this.editorPane = new ScrollableEditorPaneView();
        this.scrollPane = new JScrollPane( editorPane.getView() );

        editorPane.setContentType( "text/html" );
        if( pageUrl != null )
        {
            try
            {
                editorPane.setPage( pageUrl );
            }
            catch( IOException e )
            {
                throw new IllegalArgumentException(
                    pageUrl.getFile() + " not found!" );
            }
        }
        else
        {
            editorPane.setText( "<html>null url</html>" );
        }

        panel.add( scrollPane, BorderLayout.CENTER );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return panel;
    }
}
