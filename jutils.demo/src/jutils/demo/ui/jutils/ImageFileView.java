package jutils.demo.ui.jutils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import jutils.core.SwingUtils;
import jutils.core.ui.ImageView;
import jutils.core.ui.RecentFilesViews;
import jutils.core.ui.event.FileChooserListener;
import jutils.core.ui.event.FileChooserListener.IFileSelected;
import jutils.core.ui.event.FileChooserListener.ILastFile;
import jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ImageFileView implements IView<JComponent>
{
    /**  */
    // private static final File IMG_FILE_OPTIONS;
    //
    // static
    // {
    // IMG_FILE_OPTIONS = IOUtils.getUsersFile( ".jutils", "images",
    // "files.cfg" );
    // }

    /**  */
    private final JPanel view;
    /**  */
    private final RecentFilesViews recentViews;
    /**  */
    private final ImageView imgView;

    /***************************************************************************
     * 
     **************************************************************************/
    public ImageFileView()
    {
        this.view = new JPanel();
        this.recentViews = new RecentFilesViews();
        this.imgView = new ImageView();

        recentViews.setListeners( ( f, c ) -> openImageFile( f ) );

        view.setLayout( new BorderLayout() );

        view.add( createToolbar(), BorderLayout.NORTH );
        view.add( imgView.getView(), BorderLayout.CENTER );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createToolbar()
    {
        JToolBar toolbar = new JToolBar();

        SwingUtils.setToolbarDefaults( toolbar );

        recentViews.install( toolbar, createOpenListener() );

        return toolbar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private ActionListener createOpenListener()
    {
        String title = "Choose Image";
        IFileSelected fileSelected = ( f ) -> openImageFile( f );
        ILastFile lastSelected = () -> getLastSelected();
        return new FileChooserListener( getView(), title, false, fileSelected,
            lastSelected );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private File getLastSelected()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /***************************************************************************
     * @param f
     **************************************************************************/
    private void openImageFile( File f )
    {
        // TODO Auto-generated method stub
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }
}
