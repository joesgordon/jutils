package org.jutils.ui.explorer.data;

import java.io.File;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ApplicationConfig implements Comparable<Object>
{
    /** The name of the program/application. */
    public String name;
    /** The full path to the application */
    public File path;
    /** The arguments to be applied to all invocations of the application. */
    public String args;

    /***************************************************************************
     * 
     **************************************************************************/
    public ApplicationConfig()
    {
        this.name = "";
        this.path = null;
        this.args = "";
    }

    public ApplicationConfig( String name, File path, String args )
    {
        this.name = name;
        this.path = path;
        this.args = args;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String toString()
    {
        return name;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int compareTo( Object obj )
    {
        return name.compareToIgnoreCase( obj.toString() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int hashCode()
    {
        return name.hashCode();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean equals( Object obj )
    {
        if( obj != null )
        {
            return compareTo( obj ) == 0;
        }

        return false;
    }

}
