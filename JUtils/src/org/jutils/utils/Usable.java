package org.jutils.utils;

import org.jutils.ValidationException;
import org.jutils.io.IStringWriter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Usable<T>
{
    /**  */
    public boolean isUsed;
    /**  */
    public T data;

    /***************************************************************************
     * 
     **************************************************************************/
    public Usable()
    {
        this( false );
    }

    /***************************************************************************
     * @param isUsed
     **************************************************************************/
    public Usable( boolean isUsed )
    {
        this.isUsed = isUsed;
    }

    /***************************************************************************
     * @param isUsed
     * @param data
     **************************************************************************/
    public Usable( boolean isUsed, T data )
    {
        this.isUsed = isUsed;
        this.data = data;
    }

    /***************************************************************************
     * @param usable
     **************************************************************************/
    public Usable( Usable<T> usable )
    {
        this();

        set( usable );
    }

    /***************************************************************************
     * @param usable
     **************************************************************************/
    public void set( Usable<T> usable )
    {
        if( usable != null )
        {
            this.isUsed = usable.isUsed;
            this.data = usable.data;
        }
    }

    /***************************************************************************
     * @param name
     * @param usable
     * @throws ValidationException
     **************************************************************************/
    public static void validate( String name, Usable<?> usable )
        throws ValidationException
    {
        if( usable.isUsed && usable.data == null )
        {
            throw new ValidationException( name + " is used but not defined." );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String toString()
    {
        return ( isUsed ? "enabled" : "disabled" ) + " " +
            ( data == null ? "" : data.toString() );
    }

    /***************************************************************************
     * @param stringMarshaller
     * @return
     **************************************************************************/
    public String toString( IStringWriter<T> stringMarshaller )
    {
        if( isUsed )
        {
            return "disabled";
        }

        return String.format( "enabled: %s",
            data == null ? "" : stringMarshaller.toString( data ) );
    }

    /***************************************************************************
     * @param defaultValue
     * @return
     **************************************************************************/
    public T get( T defaultValue )
    {
        return isUsed ? data : defaultValue;
    }
}
