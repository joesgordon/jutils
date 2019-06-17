package org.jutils.io;

import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.util.*;

import javax.swing.ImageIcon;

/*******************************************************************************
 * Defines methods of loading icons that reside either in a directory or
 * relative to a class file. Aids the developer in accessing icons in a manner
 * independent of whether the class files are in a directory or a jar file. All
 * icons must be copied to the directory or jar file by the build tool.
 ******************************************************************************/
public class IconLoader
{
    /** The URL at which all the icons reside. */
    public final ResourceLoader loader;
    /**
     * The icon cache so that icons are not loaded more than once per instance
     * of this class.
     */
    private final Map<String, ImageIcon> iconMap;

    /***************************************************************************
     * Creates an icon loader initialized to the provided base path.
     * @param basePath the path containing icons.
     **************************************************************************/
    public IconLoader( File basePath )
    {
        this( new ResourceLoader( basePath ) );
    }

    /***************************************************************************
     * Creates an icon loader initialized to the provide path relative to the
     * provided class.
     * @param baseClass a class close to the icons.
     * @param relativePath the path from the class to the icons.
     **************************************************************************/
    public IconLoader( Class<?> baseClass, String relativePath )
    {
        this( new ResourceLoader( baseClass, relativePath ) );
    }

    /***************************************************************************
     * Creates a new icon loader initialized to the provided URL.
     * @param url the location of the icons.
     **************************************************************************/
    public IconLoader( URL url )
    {
        this( new ResourceLoader( url ) );
    }

    /***************************************************************************
     * Creates a new icon loader initialized to the provided recourse loader.
     * @param resourceLoader the loader initialized to the icons' location.
     **************************************************************************/
    public IconLoader( ResourceLoader resourceLoader )
    {
        this.loader = resourceLoader;
        iconMap = new HashMap<String, ImageIcon>();
    }

    /***************************************************************************
     * Returns the icon with the provided name.
     * @param name the name/relative path to the icon.
     * @return the icon loaded or {@code null} if none found.
     **************************************************************************/
    public ImageIcon getIcon( String name )
    {
        ImageIcon icon = null;

        if( iconMap.containsKey( name ) )
        {
            icon = iconMap.get( name );
        }
        else
        {
            icon = new ImageIcon( loader.getUrl( name ) );
            iconMap.put( name, icon );
        }

        return icon;
    }

    /***************************************************************************
     * Returns the icons with the provided names.
     * @param names the names/relative paths to the icons.
     * @return the list of icons loaded or an empty list if none found.
     **************************************************************************/
    public List<ImageIcon> getIcons( String... names )
    {
        List<ImageIcon> icons = new ArrayList<ImageIcon>();

        for( String name : names )
        {
            icons.add( getIcon( name ) );
        }

        return icons;
    }

    /***************************************************************************
     * Returns the image with the provided name.
     * @param name the name/relative path to the image.
     * @return the image loaded or {@code null} if not found.
     **************************************************************************/
    public Image getImage( String name )
    {
        ImageIcon icon = getIcon( name );
        return icon == null ? null : icon.getImage();
    }

    /***************************************************************************
     * Returns the images with the provided names.
     * @param names the names/relative paths to the images.
     * @return the list of images loaded or an empty list if none found.
     **************************************************************************/
    public List<Image> getImages( String... names )
    {
        List<Image> images = new ArrayList<Image>();

        for( String name : names )
        {
            Image img = getImage( name );

            if( img != null )
            {
                images.add( img );
            }
        }

        return images;
    }

    /***************************************************************************
     * Builds a list of PNGs with the provided prefix that end with 016, 024,
     * 032, 048, 064, 128, and 256.
     * @param prefix the characters prior to the standard sizes suffix.
     * @return the names built.
     **************************************************************************/
    public static String [] buildNameList( String prefix )
    {
        final int [] sizes = new int[] { 16, 24, 32, 48, 64, 128, 256 };
        String [] list = new String[sizes.length];

        for( int i = 0; i < sizes.length; i++ )
        {
            list[i] = String.format( "%s%03d.png", prefix, sizes[i] );
        }

        return list;
    }
}
