package org.jutils.io;

import java.io.*;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

import org.jutils.Utils;
import org.jutils.ValidationException;

import com.thoughtworks.xstream.XStream;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class XStreamUtils
{
    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private XStreamUtils()
    {
        ;
    }

    /***************************************************************************
     * @param obj Object
     * @param file File
     * @param packages
     * @throws IOException
     **************************************************************************/
    public static void writeObjectXStream( Object obj, File file,
        String... packages ) throws IOException
    {
        try( FileOutputStream stream = new FileOutputStream( file ) )
        {
            writeObjectXStream( obj, stream, packages );
        }
    }

    /***************************************************************************
     * @param obj
     * @param outStream
     * @param packages
     * @throws IOException
     **************************************************************************/
    public static void writeObjectXStream( Object obj, OutputStream outStream,
        String... packages ) throws IOException
    {
        XStream xstream = createXStream( packages );

        xstream.toXML( obj, outStream );
    }

    /***************************************************************************
     * @param obj
     * @param packages
     * @return
     * @throws IOException
     **************************************************************************/
    public static String writeObjectXStream( Object obj, String... packages )
        throws IOException
    {
        XStream xstream = createXStream( packages );

        return xstream.toXML( obj );
    }

    /***************************************************************************
     * @param <T>
     * @param file File
     * @param packages
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ValidationException
     **************************************************************************/
    @SuppressWarnings( "unchecked")
    public static <T> T readObjectXStream( File file, String... packages )
        throws FileNotFoundException, IOException, ValidationException
    {
        Object obj;

        try( FileInputStream fis = new FileInputStream( file ) )
        {
            obj = readObjectXStream( fis, packages );
        }

        return ( T )obj;
    }

    /***************************************************************************
     * @param <T>
     * @param inputStream
     * @param packages
     * @return
     * @throws IOException
     * @throws ValidationException
     **************************************************************************/
    public static <T> T readObjectXStream( InputStream inputStream,
        String... packages ) throws IOException, ValidationException
    {
        XStream xstream = createXStream( packages );

        Object obj = xstream.fromXML( inputStream );
        @SuppressWarnings( "unchecked")
        T t = ( T )obj;

        return t;
    }

    /***************************************************************************
     * @param <T>
     * @param str
     * @param packages
     * @return
     **************************************************************************/
    public static <T> T readObjectXStream( String str, String... packages )
    {
        XStream xstream = createXStream( packages );

        Object obj = xstream.fromXML( str );
        @SuppressWarnings( "unchecked")
        T t = ( T )obj;

        return t;
    }

    /***************************************************************************
     * @param packages
     * @return
     **************************************************************************/
    public static XStream createXStream( String... packages )
    {
        String [] wildcards = new String[packages.length];

        for( int i = 0; i < packages.length; i++ )
        {
            wildcards[i] = packages[i] + ".**";
        }

        XStream xstream = new XStream();

        if( wildcards.length > 0 )
        {
            XStream.setupDefaultSecurity( xstream );
            xstream.allowTypesByWildcard( wildcards );
        }

        return xstream;
    }

    /***************************************************************************
     * Performs a "deep copy" clone of the given object.
     * @param <T>
     * @param obj the object to be cloned.
     * @return a "deep copy" clone of the given object.
     **************************************************************************/
    public static <T> T cloneObject( T obj )
    {
        T clone = null;

        try( ByteArrayOutputStream outputStream = new ByteArrayOutputStream() )
        {
            writeObjectXStream( obj, outputStream );

            byte [] buf = outputStream.toByteArray();

            try( ByteArrayInputStream inputStream = new ByteArrayInputStream(
                buf, 0, buf.length ) )
            {
                outputStream.close();

                @SuppressWarnings( "unchecked")
                T t = ( T )readObjectXStream( inputStream );
                inputStream.close();
                clone = t;
            }
        }
        catch( IOException | ValidationException ex )
        {
            throw new IllegalStateException( ex.getMessage() );
        }

        return clone;
    }

    /***************************************************************************
     * @param cls
     * @return
     **************************************************************************/
    public static List<String> buildDependencyList( Class<?> cls )
    {
        List<Class<?>> classes = new ArrayList<>();

        buildClasses( cls, classes );

        return buildDependencyList( classes );
    }

    /***************************************************************************
     * @param cls
     * @param classes
     **************************************************************************/
    private static void buildClasses( Class<?> cls, List<Class<?>> classes )
    {
        if( classes.contains( cls ) )
        {
            return;
        }
        else if( cls.isArray() )
        {
            cls = cls.getComponentType();
        }
        else if( cls.isPrimitive() )
        {
            return;
        }
        else
        {
            // LogUtils.printDebug( "Adding class " + cls.getName() );
            classes.add( cls );

            for( Field f : cls.getDeclaredFields() )
            {
                if( Modifier.isStatic( f.getModifiers() ) )
                {
                    continue;
                }

                Class<?> fieldClass = f.getType();

                if( f.getGenericType() != null )
                {
                    Type genType = f.getGenericType();

                    if( genType instanceof ParameterizedType )
                    {
                        ParameterizedType paramType = ( ParameterizedType )f.getGenericType();

                        buildClasses( paramType, classes );
                    }
                }

                buildClasses( fieldClass, classes );
            }
        }
    }

    /***************************************************************************
     * @param paramType
     * @param classes
     **************************************************************************/
    private static void buildClasses( ParameterizedType paramType,
        List<Class<?>> classes )
    {
        buildClasses( ( Class<?> )paramType.getRawType(), classes );
        if( paramType.getOwnerType() != null )
        {
            buildClasses( ( Class<?> )paramType.getOwnerType(), classes );
        }

        for( Type t : paramType.getActualTypeArguments() )
        {
            if( t != null )
            {
                if( t instanceof ParameterizedType )
                {
                    buildClasses( ( ParameterizedType )t, classes );
                }
                else if( t instanceof Class<?> )
                {
                    Class<?> fieldClass = ( Class<?> )t;
                    buildClasses( fieldClass, classes );
                }
            }
        }
    }

    /***************************************************************************
     * @param classes
     * @return
     **************************************************************************/
    private static List<String> buildDependencyList( List<Class<?>> classes )
    {
        List<String> packages = new ArrayList<>();

        for( Class<?> c : classes )
        {
            addPackage( c, packages );
        }

        return packages;
    }

    /***************************************************************************
     * @param cls
     * @param packages
     **************************************************************************/
    private static void addPackage( Class<?> cls, List<String> packages )
    {
        Package pkg = cls.getPackage();
        // LogUtils.printDebug( "Getting package for class " + cls.getName() );
        String name = pkg.getName();
        List<String> pkgTokens = Utils.split( name, '.' );

        // LogUtils.printDebug(
        // "Package for class " + cls.getName() + " is " + name );
        // LogUtils.printDebug( "" );

        boolean add = false;

        if( name.isEmpty() )
        {
            add = true;
        }
        else
        {
            String startName = pkgTokens.get( 0 );

            switch( startName )
            {
                case "java":
                case "javax":
                case "sun":
                    add = false;
                    break;

                default:
                    add = true;
                    break;
            }
        }

        if( add )
        {
            for( int i = 0; i < packages.size(); i++ )
            {
                String p = packages.get( i );

                if( p.startsWith( name ) )
                {
                    // replace p with name
                    packages.set( i, name );
                    add = false;
                    break;
                }
                else if( name.startsWith( p ) )
                {
                    add = false;
                    break;
                }
            }

            if( add )
            {
                packages.add( name );
            }
        }
    }

    public static <T> XStream createXStream( Class<? extends T> cls )
    {
        return createXStream(
            buildDependencyList( cls ).toArray( new String[0] ) );
    }
}
