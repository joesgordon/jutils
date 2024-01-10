package jutils.iris.ui;

import java.io.File;
import java.util.List;

import javax.swing.JComponent;

import jutils.core.ui.event.FileDropTarget;
import jutils.core.ui.event.FileDropTarget.IFileDropEvent;
import jutils.core.ui.event.ItemActionEvent;
import jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class IrisView implements IView<JComponent>
{
    /**  */
    private final JComponent view;
    /**  */
    private final ImagesView imgsView;
    /**  */
    private final RasterView rasterView;

    /***************************************************************************
     * 
     **************************************************************************/
    public IrisView()
    {
        this.imgsView = new ImagesView();
        this.rasterView = new RasterView();

        this.view = createView();

        imgsView.hashCode();

        view.setDropTarget( new FileDropTarget( ( ie ) -> fileDropped( ie ) ) );
    }

    /***************************************************************************
     * @param ie
     **************************************************************************/
    private void fileDropped( ItemActionEvent<IFileDropEvent> ie )
    {
        IFileDropEvent fde = ie.getItem();
        List<File> files = fde.getFiles();

        if( files.size() == 1 )
        {
            File file = files.get( 0 );

            openFile( file );
        }
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    private void openFile( File file )
    {
        // TODO Auto-generated method stub
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        return rasterView.getView();
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
