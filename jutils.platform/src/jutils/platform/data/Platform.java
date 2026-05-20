package jutils.platform.data;

import jutils.core.INamedItem;
import jutils.core.data.SystemProperty;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum Platform implements INamedItem
{
    /**  */
    UNKNOWN( "Unknown" ),
    /**  */
    WINDOWS( "Windows" ),
    /**  */
    LINUX( "Linux" ),
    /**  */
    MAC_OS( "MacOS" ),;

    /**  */
    public final String name;

    /***************************************************************************
     * @param name
     **************************************************************************/
    private Platform( String name )
    {
        this.name = name;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return name;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static Platform getSystemPlatform()
    {
        String name = SystemProperty.OS_NAME.getProperty();

        if( name == null || name.isEmpty() )
        {
            return UNKNOWN;
        }
        else if( name.startsWith( "Mac" ) )
        {
            return MAC_OS;
        }
        else if( name.startsWith( "Linux" ) || name.startsWith( "LINUX" ) )
        {
            return LINUX;
        }
        else if( name.startsWith( "Windows" ) )
        {
            return WINDOWS;
        }

        return UNKNOWN;
    }
}
