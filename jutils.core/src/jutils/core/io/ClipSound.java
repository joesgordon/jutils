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

/*******************************************************************************
 * Defines a clip that loads the audio into memory and may be played/replayed.
 ******************************************************************************/
public class ClipSound implements Closeable
{
    /** The audio loaded into memory. */
    private final Clip clip;

    /***************************************************************************
     * @param stream the open input stream to a supported clip; see
     * {@link AudioSystem#getAudioInputStream(InputStream)}.
     * @throws LineUnavailableException if a matching line is not available due
     * to resource restrictions.
     * @throws IOException any I/O error that occurs.
     * @throws UnsupportedAudioFileException if the stream does not point to
     * valid audio file data recognized by the system
     **************************************************************************/
    public ClipSound( InputStream stream ) throws LineUnavailableException,
        IOException, UnsupportedAudioFileException
    {
        this( () -> stream );
    }

    /***************************************************************************
     * @param opener callback that opens a stream.
     * @throws LineUnavailableException if a matching line is not available due
     * to resource restrictions.
     * @throws IOException any I/O error that occurs.
     * @throws UnsupportedAudioFileException if the stream does not point to
     * valid audio file data recognized by the system
     **************************************************************************/
    public ClipSound( IStreamOpener opener ) throws LineUnavailableException,
        IOException, UnsupportedAudioFileException
    {
        try( InputStream is = opener.openStream();
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
     * Adds a listener to this line. Whenever the line's status changes, the
     * listener's update() method is called with a LineEvent object that
     * describes the change.
     * @param listener callback invoked when status changes.
     **************************************************************************/
    public void addLineListener( LineListener listener )
    {
        clip.addLineListener( listener );
    }

    /***************************************************************************
     * Plays the clip from the beginning.
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

    /***************************************************************************
     * Defines a method of opening an input stream.
     **************************************************************************/
    public static interface IStreamOpener
    {
        /**
         * Opens an input stream (or returns a newly opened one).
         * @return the open input stream.
         */
        public InputStream openStream();
    }
}
