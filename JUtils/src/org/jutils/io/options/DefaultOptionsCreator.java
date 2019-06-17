package org.jutils.io.options;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.jutils.io.LogUtils;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class DefaultOptionsCreator<T> implements IOptionsCreator<T>
{
    /**  */
    private final Constructor<T> defaultConstructor;
    /**  */
    private final Constructor<T> copyConstructor;

    /***************************************************************************
     * @param cls
     * @throws IllegalArgumentException
     **************************************************************************/
    public DefaultOptionsCreator( Class<T> cls ) throws IllegalArgumentException
    {
        Constructor<T> defaultConstructor = null;
        Constructor<T> copyConstructor = null;
        Constructor<?> [] cs = cls.getConstructors();

        for( Constructor<?> c : cs )
        {
            c.setAccessible( true );
            Class<?> [] types = c.getParameterTypes();

            if( types.length == 1 && types[0].equals( cls ) )
            {
                @SuppressWarnings( "unchecked")
                Constructor<T> itemCopyConstructor = ( Constructor<T> )c;
                copyConstructor = itemCopyConstructor;
                break;
            }
        }

        try
        {
            defaultConstructor = cls.getDeclaredConstructor();
            defaultConstructor.setAccessible( true );
        }
        catch( NoSuchMethodException ex )
        {
            throw new IllegalArgumentException(
                cls.getName() + " has no default constructor", ex );
        }
        catch( SecurityException ex )
        {
            throw new IllegalArgumentException(
                cls.getName() + " has no accesible constructor", ex );
        }

        this.defaultConstructor = defaultConstructor;
        this.copyConstructor = copyConstructor;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public T createDefaultOptions()
    {
        try
        {
            return defaultConstructor.newInstance();
        }
        catch( InstantiationException ex )
        {
            throw new IllegalStateException( ex );
        }
        catch( IllegalAccessException ex )
        {
            throw new IllegalStateException( ex );
        }
        catch( IllegalArgumentException ex )
        {
            throw new IllegalStateException( ex );
        }
        catch( InvocationTargetException ex )
        {
            throw new IllegalStateException( ex );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public T initialize( T itemRead )
    {
        T newItem = itemRead;

        if( copyConstructor != null )
        {
            try
            {
                newItem = copyConstructor.newInstance( itemRead );
            }
            catch( InstantiationException ex )
            {
                throw new IllegalStateException( ex );
            }
            catch( IllegalAccessException ex )
            {
                throw new IllegalStateException( ex );
            }
            catch( IllegalArgumentException ex )
            {
                throw new IllegalStateException( ex );
            }
            catch( InvocationTargetException ex )
            {
                throw new IllegalStateException( ex );
            }
        }

        return newItem;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void warn( String message )
    {
        LogUtils.printWarning( message );
    }
}
