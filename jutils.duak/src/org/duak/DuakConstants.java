package org.duak;

import java.io.File;

import org.jutils.core.io.IOUtils;
import org.jutils.core.io.options.IOptionsCreator;
import org.jutils.core.io.options.OptionsSerializer;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class DuakConstants
{
    /**  */
    private final static File OPTIONS_FILE = IOUtils.getUsersFile( ".jutils",
        "duak", "options.xml" );

    /**  */
    private static OptionsSerializer<DuakOptions> options;

    /***************************************************************************
     * 
     **************************************************************************/
    private DuakConstants()
    {
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static OptionsSerializer<DuakOptions> getOptions()
    {
        if( options == null )
        {
            options = OptionsSerializer.getOptions( DuakOptions.class,
                OPTIONS_FILE, new OptionsCreator() );
        }

        return options;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class OptionsCreator implements IOptionsCreator<DuakOptions>
    {
        @Override
        public DuakOptions createDefaultOptions()
        {
            return new DuakOptions();
        }

        @Override
        public DuakOptions initialize( DuakOptions options )
        {
            return new DuakOptions( options );
        }

        @Override
        public void warn( String message )
        {
            // TODO Auto-generated method stub
        }
    }
}
