package jutils.core.ui;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import jutils.core.Utils;
import jutils.core.io.IOUtils;

/***************************************************************************
 *
 **************************************************************************/
public final class TextAreaUtils
{
    /***************************************************************************
     *
     **************************************************************************/
    private TextAreaUtils()
    {
    }

    /***************************************************************************
     * @param reader Reader
     * @throws IOException
     **************************************************************************/
    public static void loadFrom( Reader reader, JTextArea to )
        throws IOException
    {
        BufferedReader buffer = new BufferedReader( reader );

        for( String line = buffer.readLine(); line != null; line = buffer.readLine() )
        {
            to.append( line + Utils.NEW_LINE );
        }
    }

    /***************************************************************************
     * @param stream InputStream
     * @throws IOException
     **************************************************************************/
    public static void loadFrom( InputStream stream, JTextArea to )
        throws IOException
    {
        BufferedInputStream buffer = new BufferedInputStream( stream );
        byte [] byteBuffer = new byte[1024 * 16];
        int bytesRead = 0;

        while( ( bytesRead = buffer.read( byteBuffer ) ) > 0 )
        {
            to.append( new String( byteBuffer, 0, bytesRead,
                IOUtils.get8BitEncoding() ) );
        }
    }

    /***************************************************************************
     * @param text String
     **************************************************************************/
    public static void addTextSafely( String text, JTextArea to )
    {
        SwingUtilities.invokeLater( new SafeTextAdder( text, to ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class SafeTextAdder implements Runnable
    {
        private final JTextArea area;
        private final String text;

        public SafeTextAdder( String txt, JTextArea area )
        {
            this.area = area;
            this.text = txt;
        }

        @Override
        public void run()
        {
            area.append( text );
        }
    }
}
