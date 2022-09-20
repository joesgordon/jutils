package org.jutils.core.io.xs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.jutils.core.Utils;
import org.jutils.core.ValidationException;

import com.thoughtworks.xstream.InitializationException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.security.AnyTypePermission;
import com.thoughtworks.xstream.security.NoTypePermission;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class XsUtils
{
    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private XsUtils()
    {
        ;
    }

    /***************************************************************************
     * @param obj Object
     * @param file File
     * @param packages
     * @throws IOException
     * @throws ValidationException
     **************************************************************************/
    public static void writeObjectXStream( Object obj, File file,
        String... packages ) throws IOException, ValidationException
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
     * @throws ValidationException
     **************************************************************************/
    public static void writeObjectXStream( Object obj, OutputStream outStream,
        String... packages ) throws IOException, ValidationException
    {
        try
        {
            XStream xstream = createXStream( packages );

            xstream.toXML( obj, outStream );
        }
        catch( XStreamException ex )
        {
            throw new ValidationException( "Unable to write object to XStream",
                ex );
        }
    }

    /***************************************************************************
     * @param obj
     * @param packages
     * @return
     * @throws IOException
     * @throws ValidationException
     **************************************************************************/
    public static String writeObjectXStream( Object obj, String... packages )
        throws IOException, ValidationException
    {
        try
        {
            XStream xstream = createXStream( packages );

            return xstream.toXML( obj );
        }
        catch( XStreamException ex )
        {
            throw new ValidationException( "Unable to write object to XStream",
                ex );
        }
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
     * @throws ValidationException
     **************************************************************************/
    public static <T> T readObjectXStream( String str, String... packages )
        throws ValidationException
    {
        XStream xstream = createXStream( packages );

        try
        {
            Object obj = xstream.fromXML( str );
            @SuppressWarnings( "unchecked")
            T t = ( T )obj;

            return t;
        }
        catch( XStreamException ex )
        {
            throw new ValidationException( "Unable read from XML string", ex );
        }
    }

    /***************************************************************************
     * @param packages
     * @return
     * @throws ValidationException
     **************************************************************************/
    public static XStream createXStream( String... packages )
    {
        String [] wildcards = new String[packages.length];

        for( int i = 0; i < packages.length; i++ )
        {
            wildcards[i] = packages[i] + ".**";
        }

        try
        {
            XStream xstream = new XStream();

            if( wildcards.length > 0 )
            {
                XStream.setupDefaultSecurity( xstream );
                xstream.allowTypesByWildcard( wildcards );
                xstream.addPermission( new AnyTypePermission() );
                xstream.denyPermission( new NoTypePermission() );
            }

            xstream.registerConverter(
                new ArrayDequeConverter( xstream.getMapper() ) );

            return xstream;
        }
        catch( InitializationException ex )
        {
            throw new RuntimeException( "Unable to initialize XStream", ex );
        }
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
    public static List<String> buildDependencyList( Class<?>... clss )
    {
        List<Class<?>> classes = new ArrayList<>();

        for( Class<?> cls : clss )
        {
            buildClasses( cls, classes );
        }

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

    /***************************************************************************
     * @param <T>
     * @param cls
     * @return
     * @throws ValidationException
     **************************************************************************/
    public static XStream createXStream( Class<?>... clss )
    {
        List<String> list = buildDependencyList( clss );

        list.add( "java.io" );
        list.add( "sun.awt.shell" );

        String [] array = list.toArray( new String[0] );

        // LogUtils.printDebug( "Dependencies: %s",
        // Utils.collectionToString( list ) );

        return createXStream( array );
    }
}
