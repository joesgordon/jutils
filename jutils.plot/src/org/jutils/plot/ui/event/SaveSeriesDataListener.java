package org.jutils.plot.ui.event;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.jutils.core.OptionUtils;
import org.jutils.core.Utils;
import org.jutils.core.io.IOUtils;
import org.jutils.core.io.options.OptionsSerializer;
import org.jutils.core.ui.event.FileChooserListener.IFileSelected;
import org.jutils.core.ui.event.FileChooserListener.ILastFile;
import org.jutils.core.ui.model.IDataView;
import org.jutils.plot.app.PlotConstants;
import org.jutils.plot.app.UserData;
import org.jutils.plot.io.FilteredWriter;
import org.jutils.plot.model.Series;
import org.jutils.plot.model.ChartOptions.PointRemovalMethod;

/***************************************************************************
 * 
 **************************************************************************/
public class SaveSeriesDataListener implements ILastFile, IFileSelected
{
    /**  */
    private final IDataView<Series> view;
    /**  */
    private final PointRemovalMethod removalMethod;

    /***************************************************************************
     * @param view
     * @param removalMethod
     **************************************************************************/
    public SaveSeriesDataListener( IDataView<Series> view,
        PointRemovalMethod removalMethod )
    {
        this.view = view;
        this.removalMethod = removalMethod;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public File getLastFile()
    {
        Series s = view.getData();

        return getDefaultFile( s );
    }

    /***************************************************************************
     * @param s
     * @return
     **************************************************************************/
    public static File getDefaultFile( Series s )
    {
        File file = s.getResourceFile();

        if( file != null )
        {
            String ext = IOUtils.getFileExtension( file );
            String name = IOUtils.removeFilenameExtension( file );

            file = new File( file.getParentFile(), name + "_filtered." + ext );
        }
        else
        {
            OptionsSerializer<UserData> options = PlotConstants.getOptions();

            file = options.getOptions().lastDataFile;
        }

        return file;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void fileChosen( File file )
    {
        Series s = view.getData();
        File toFile = file;
        File fromFile = s.getResourceFile();

        try
        {
            saveFile( s, fromFile, toFile, removalMethod );
        }
        catch( IOException ex )
        {
            OptionUtils.showErrorMessage( view.getView(),
                "Unable to save file: " + Utils.NEW_LINE +
                    toFile.getAbsolutePath() + Utils.NEW_LINE + Utils.NEW_LINE +
                    ex.getMessage(),
                "I/O Error" );
        }
    }

    /***************************************************************************
     * @param s
     * @param toFile
     * @param removalMethod
     * @throws FileNotFoundException
     * @throws IOException
     **************************************************************************/
    public static void saveFile( Series s, File toFile,
        PointRemovalMethod removalMethod )
        throws FileNotFoundException, IOException
    {
        saveFile( s, null, toFile, removalMethod );
    }

    /***************************************************************************
     * @param s
     * @param fromFile
     * @param toFile
     * @throws FileNotFoundException
     * @throws IOException
     **************************************************************************/
    public static void saveFile( Series s, File fromFile, File toFile,
        PointRemovalMethod removalMethod )
        throws FileNotFoundException, IOException
    {
        OptionsSerializer<UserData> options = PlotConstants.getOptions();

        options.getOptions().lastDataFile = toFile;

        if( fromFile == null )
        {
            FilteredWriter.write( toFile, s.data, removalMethod );
        }
        else
        {
            FilteredWriter.write( fromFile, toFile, s.data, removalMethod );
        }
    }
}
