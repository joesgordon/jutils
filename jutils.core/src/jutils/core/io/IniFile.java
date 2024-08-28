package jutils.core.io;

import java.io.BufferedReader;
import java.io.File; // for file seperator
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jutils.core.data.SystemProperty;

/** Implements method to read/write "Windows INI" style text files */
public class IniFile
{

    private final Pattern _section = Pattern.compile( "\\s*\\[([^]]*)\\]\\s*" );
    private final Pattern _keyValue = Pattern.compile( "\\s*([^=]*)=(.*)" );
    private final Map<String, Map<String, String>> _entries = new HashMap<>();

    /**
     * Loads an INI file from the directory where the application was launched
     * @param iniFileName the basename of the INI file.
     * @throws IOException if file not found
     */
    public IniFile( String iniFileName ) throws IOException
    {
        load( SystemProperty.USER_DIR.getProperty() + File.separator +
            iniFileName );
    }

    /**
     * @param file
     * @throws IOException
     */
    public IniFile( File file ) throws IOException
    {
        load( file.getAbsolutePath() );
    }

    /**
     * @param path full path to the INI file to load
     * @throws IOException on file errors
     */
    public void load( String path ) throws IOException
    {
        try( BufferedReader br = new BufferedReader( new FileReader( path ) ) )
        {
            String line;
            String section = null;
            while( ( line = br.readLine() ) != null )
            {
                Matcher m = _section.matcher( line );
                if( m.matches() )
                {
                    section = m.group( 1 ).trim();
                }
                else if( section != null )
                {
                    m = _keyValue.matcher( line );
                    if( m.matches() )
                    {
                        String key = m.group( 1 ).trim();
                        String value = m.group( 2 ).trim();
                        Map<String, String> kv = _entries.get( section );
                        if( kv == null )
                        {
                            _entries.put( section, kv = new HashMap<>() );
                        }
                        kv.put( key, value );
                    }
                }
            }
        }
    }

    /**
     * @param section the text inside []
     * @param key the string before the equals sign
     * @param defaultvalue value to return if the key is not found
     * @return the string found after the equals sign.
     */
    public String getString( String section, String key, String defaultvalue )
    {
        Map<String, String> kv = _entries.get( section );
        if( kv == null )
        {
            return defaultvalue;
        }
        return kv.get( key );
    }

    /**
     * @param section the string inside []
     * @param key the string before the equals sing
     * @param defaultvalue value to return if key is not found in this section
     * @return the value string converted to int
     */
    public int getInt( String section, String key, int defaultvalue )
    {
        Map<String, String> kv = _entries.get( section );
        if( kv == null )
        {
            return defaultvalue;
        }
        return Integer.parseInt( kv.get( key ) );
    }

    /**
     * @param section the string inside [] specifying where to look for key
     * @param key the string before =
     * @param defaultvalue the value to return if key is not found in this
     * section
     * @return the value string, converted to a float
     */
    public float getFloat( String section, String key, float defaultvalue )
    {
        Map<String, String> kv = _entries.get( section );
        if( kv == null )
        {
            return defaultvalue;
        }
        return Float.parseFloat( kv.get( key ) );
    }

    /**
     * @param section the string inside [] specifying where to look for key
     * @param key the string before =
     * @param defaultvalue the value to return if key is not found in this
     * section
     * @return the value string, converted to a double
     */
    public double getDouble( String section, String key, double defaultvalue )
    {
        Map<String, String> kv = _entries.get( section );
        if( kv == null )
        {
            return defaultvalue;
        }
        return Double.parseDouble( kv.get( key ) );
    }
}
