package jutils.filespy;

import java.io.File;

import javax.swing.JFrame;

import jutils.core.io.IOUtils;
import jutils.core.io.LogUtils;
import jutils.core.io.options.IOptionsCreator;
import jutils.core.io.options.OptionsSerializer;
import jutils.core.io.xs.XsOptions;
import jutils.core.ui.app.AppRunner;
import jutils.filespy.data.FileSpyData;
import jutils.filespy.ui.FileSpyFrameView;

/*******************************************************************************
 *
 ******************************************************************************/
public class FileSpyMain
{
    /**  */
    public static final File USER_OPTIONS_FILE = IOUtils.getUsersFile(
        ".jutils", "filespy", "options.xml" );

    /**  */
    private static final OptionsSerializer<FileSpyData> OPTIONS;

    static
    {
        OPTIONS = XsOptions.getOptions( FileSpyData.class, USER_OPTIONS_FILE,
            new OptionsCreator() );
    }

    /***************************************************************************
     * Application entry point.
     * @param args String[]
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.invokeLater( () -> createFrame() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static JFrame createFrame()
    {
        FileSpyFrameView frameView = new FileSpyFrameView();
        JFrame frame = frameView.getView();

        return frame;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static OptionsSerializer<FileSpyData> getOptions()
    {
        return OPTIONS;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class OptionsCreator implements IOptionsCreator<FileSpyData>
    {
        @Override
        public FileSpyData createDefaultOptions()
        {
            FileSpyData data = new FileSpyData();

            return data;
        }

        @Override
        public FileSpyData initialize( FileSpyData item_read )
        {
            return new FileSpyData( item_read );
        }

        @Override
        public void warn( String message )
        {
            LogUtils.printWarning( message );
        }
    }
}
