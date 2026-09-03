package jutils.core.io;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import jutils.core.utils.IGetter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ClipSound implements Closeable
{
    /**  */
    private final Clip clip;

    /***************************************************************************
     * @param streamGetter
     * @throws LineUnavailableException
     * @throws IOException
     * @throws UnsupportedAudioFileException
     **************************************************************************/
    public ClipSound( IGetter<InputStream> streamGetter )
        throws LineUnavailableException, IOException,
        UnsupportedAudioFileException
    {
        try( InputStream is = streamGetter.get();
             BufferedInputStream sstream = new BufferedInputStream( is );
             AudioInputStream stream = AudioSystem.getAudioInputStream(
                 sstream ) )
        {
            AudioFormat format = stream.getFormat();
            DataLine.Info info = new DataLine.Info( Clip.class, format );
            Line line = AudioSystem.getLine( info );

            this.clip = ( Clip )line;

            clip.open( stream );
        }
    }

    /***************************************************************************
     * @param listener
     **************************************************************************/
    public void addLineListener( LineListener listener )
    {
        clip.addLineListener( listener );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void play()
    {
        clip.setFramePosition( 0 );
        clip.start();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void close() throws IOException
    {
        clip.close();
    }
}
