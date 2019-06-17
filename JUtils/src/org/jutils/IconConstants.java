package org.jutils;

import java.awt.Image;
import java.awt.Window;
import java.io.*;
import java.net.URL;
import java.util.List;

import javax.sound.sampled.*;
import javax.swing.Icon;

import org.jutils.io.IconLoader;

/*******************************************************************************
 * Defines the constants needed to access the icons in this library.
 ******************************************************************************/
public final class IconConstants
{
    /**  */
    public final static String ANALYZE_16 = "analyze016.png";

    /**  */
    public static final String NAV_FIRST_16 = "nav_first16.png";

    /**  */
    public final static String NAV_PREVIOUS_16 = "nav_previous16.png";

    /**  */
    public final static String NAV_PREVIOUS_24 = "nav_previous24.png";

    /**  */
    public final static String NAV_NEXT_16 = "nav_next16.png";

    /**  */
    public final static String NAV_NEXT_24 = "nav_next24.png";

    /**  */
    public final static String NAV_LAST_16 = "nav_last16.png";

    /**  */
    public final static String NAV_DOWN_16 = "nav_down16.png";

    /**  */
    public final static String NAV_UP_16 = "nav_up16.png";

    /**  */
    public final static String NAV_UP_24 = "nav_up24.png";

    /**  */
    public final static String CALENDAR_16 = "calendar16.png";

    /**  */
    public final static String CALENDAR_32 = "calendar32.png";

    /**  */
    public final static String CHAT_16 = "chat16.png";

    /**  */
    public final static String CHAT_24 = "chat24.png";

    /**  */
    public final static String CHAT_32 = "chat32.png";

    /**  */
    public final static String CHAT_48 = "chat48.png";

    /**  */
    public final static String CHAT_64 = "chat64.png";

    /**  */
    public final static String CLOCK_24 = "clock24.png";

    /**  */
    public final static String CLOSE_16 = "close16.png";

    /**  */
    public final static String CHECK_16 = "check16.png";

    /**  */
    public final static String CONFIG_16 = "config16.png";

    /**  */
    public final static String CONFIG_24 = "config24.png";

    /**  */
    public final static String EDIT_ADD_16 = "edit-add16.png";

    /**  */
    public final static String EDIT_CLEAR_16 = "edit-clear16.png";

    /**  */
    public final static String EDIT_DELETE_16 = "edit-delete16.png";

    /**  */
    public final static String EDIT_COPY_16 = "edit-copy16.png";

    /**  */
    public final static String EDIT_PASTE_16 = "edit-paste16.png";

    /**  */
    public final static String EDIT_16 = "edit16.png";

    /**  */
    public final static String FIND_16 = "find16.png";

    /**  */
    public final static String FIND_32 = "find32.png";

    /**  */
    public final static String FONT_16 = "font16.png";

    /**  */
    public final static String FONT_24 = "font24.png";

    /**  */
    public final static String HEX_16 = "hex_016.png";

    /**  */
    public final static String IM_USER_16 = "im-user16.png";

    /**  */
    public final static String IM_USER_OFFLINE_16 = "im-invisible-user16.png";

    /**  */
    public final static String IM_USER_32 = "im-user32.png";

    /**  */
    public final static String IM_USER_OFFLINE_32 = "im-invisible-user32.png";

    /**  */
    public static final String INVALID_16 = "invalid16.png";

    /**  */
    public final static String IMPORT_16 = "document-import16.png";

    /**  */
    public final static String EXPORT_16 = "document-export16.png";

    /**  */
    public final static String LAUNCH_16 = "launch16.png";

    /**  */
    public final static String NEW_FILE_16 = "newFile16.png";

    /**  */
    public final static String OPEN_FILE_16 = "open16.png";

    /**  */
    public final static String OPEN_FOLDER_16 = "folder16.png";

    /**  */
    public final static String OPEN_FOLDER_24 = "folder24.png";

    /**  */
    public final static String OPEN_FOLDER_32 = "folder32.png";

    /**  */
    public final static String PAGEMAG_16 = "document-preview16.png";

    /**  */
    public final static String PAGEMAG_24 = "document-preview24.png";

    /**  */
    public final static String PAGEMAG_32 = "document-preview32.png";

    /**  */
    public final static String PAGEMAG_64 = "document-preview64.png";

    /**  */
    public final static String PAGEMAG_128 = "document-preview128.png";

    /**  */
    public final static String REFRESH_16 = "refresh16.png";

    /**  */
    public final static String REFRESH_24 = "refresh24.png";

    /**  */
    public final static String SAVE_16 = "save16.png";

    /**  */
    public final static String SAVE_AS_16 = "saveAs16.png";

    /**  */
    public static final String SHOW_DATA = "flashlight.png";

    /**  */
    public final static String SORT_DOWN_16 = "sortDown16.png";

    /**  */
    public final static String STOP_16 = "stop16.png";

    /**  */
    public final static String USER_ADD_24 = "userAdd24.png";

    /**  */
    public final static String UNDO_16 = "undo16.png";

    /**  */
    public final static String REDO_16 = "redo16.png";

    /** The icon loader to be used to access icons in this project. */
    public final static IconLoader loader = new IconLoader( IconConstants.class,
        "icons" );

    /***************************************************************************
     * Private constructor to prevent instantiation.
     **************************************************************************/
    private IconConstants()
    {
    }

    /***************************************************************************
     * Returns the page magnification images as a list for setting as a windows
     * icons.
     * @return the list of page magnification images.
     * @see Window#setIconImages(List)
     **************************************************************************/
    public static List<Image> getPageMagImages()
    {
        return loader.getImages( PAGEMAG_16, PAGEMAG_24, PAGEMAG_32, PAGEMAG_64,
            PAGEMAG_128 );
    }

    /***************************************************************************
     * Plays a notification sound.
     **************************************************************************/
    public static void playNotify()
    {
        Runnable r = () -> {
            try( InputStream is = loader.loader.getInputStream( "done.wav" );
                 BufferedInputStream sstream = new BufferedInputStream( is ) )
            {
                // new JavaSoundAudioClip( is ).play();

                // try( AudioStream audioStream = new AudioStream( is ) )
                // {
                // AudioPlayer.player.start( audioStream );
                // }

                try( AudioInputStream stream = AudioSystem.getAudioInputStream(
                    sstream ) )
                {
                    AudioFormat format = stream.getFormat();
                    DataLine.Info info = new DataLine.Info( Clip.class,
                        format );

                    try( Clip clip = ( Clip )AudioSystem.getLine( info ) )
                    {
                        clip.open( stream );
                        clip.start();

                        while( !clip.isRunning() )
                        {
                            Thread.sleep( 10 );
                        }

                        while( clip.isRunning() )
                        {
                            Thread.sleep( 10 );
                        }
                    }
                    catch( LineUnavailableException ex )
                    {
                        ex.printStackTrace();
                    }
                    catch( InterruptedException ex )
                    {
                    }
                }
                catch( UnsupportedAudioFileException ex )
                {
                    ex.printStackTrace();
                }
            }
            catch( IOException ex )
            {
                ex.printStackTrace();
            }
        };
        new Thread( r, "Wave Player" ).start();
    }

    /***************************************************************************
     * Returns an icon for the provided name.
     * @param name the name of the icon to be loaded.
     * @return the common icon.
     * @see IconLoader#getIcon(String)
     **************************************************************************/
    public static Icon getIcon( String name )
    {
        return loader.getIcon( name );
    }

    /***************************************************************************
     * Loads each image with the provided name.
     * @param names the names of the images to be loaded as a list.
     * @return the list of images loaded from the provided names.
     **************************************************************************/
    public static List<Image> getImages( String... names )
    {
        return loader.getImages( names );
    }

    /***************************************************************************
     * Loads an image with the provided name.
     * @param name the name of the image to load.
     * @return the loaded image or {@code null} if none exists with name.
     **************************************************************************/
    public static Image getImage( String name )
    {
        return loader.getImage( name );
    }

    /***************************************************************************
     * Builds a {@code URL} that represents the full path of the resource with
     * the provided name.
     * @param name the name of the resource.
     * @return the path to the resource.
     **************************************************************************/
    public static URL getUrl( String name )
    {
        return loader.loader.getUrl( name );
    }
}
