package jutils.iris.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import jutils.core.OptionUtils;
import jutils.core.io.LogUtils;
import jutils.core.ui.event.FileDropTarget;
import jutils.core.ui.event.FileDropTarget.IFileDropEvent;
import jutils.core.ui.event.ItemActionEvent;
import jutils.core.ui.model.IView;
import jutils.iris.data.IRasterAlbum;
import jutils.iris.io.IRasterAlbumReader;
import jutils.iris.readers.StandardImageReader;

/*******************************************************************************
 * 
 ******************************************************************************/
public class IrisView implements IView<JComponent>
{
    /**  */
    private final JComponent view;
    /**  */
    private final AlbumView imgsView;

    /**  */
    private final List<IRasterAlbumReader> readers;

    /***************************************************************************
     * 
     **************************************************************************/
    public IrisView()
    {
        this.imgsView = new AlbumView();

        this.readers = new ArrayList<>();

        this.view = createView();

        imgsView.hashCode();

        view.setDropTarget( new FileDropTarget( ( ie ) -> fileDropped( ie ) ) );

        readers.add( new StandardImageReader() );
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
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        return imgsView.getView();
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    public void openFile( File file )
    {
        LogUtils.printDebug( "opening file %s", file );

        IRasterAlbumReader fileReader = null;

        for( IRasterAlbumReader reader : readers )
        {
            if( reader.isReadable( file ) )
            {
                fileReader = reader;
                break;
            }
        }

        if( fileReader == null )
        {
            OptionUtils.showErrorMessage( getView(),
                "No reader found for file " + file.getName(), "Read Error" );
            return;
        }

        IRasterAlbum album = fileReader.readFile( file, getView() );

        setImages( album );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<IRasterAlbumReader> getReaders()
    {
        return new ArrayList<>( readers );
    }

    /***************************************************************************
     * @param images
     **************************************************************************/
    public void setImages( IRasterAlbum images )
    {
        imgsView.setImages( images );
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
