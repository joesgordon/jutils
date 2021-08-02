package org.jutils.core.ui.hex;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.jutils.core.io.IOUtils;
import org.jutils.core.ui.StandardFormView;
import org.jutils.core.ui.StandardFrameView;
import org.jutils.core.ui.app.FrameRunner;
import org.jutils.core.ui.fields.StringFormField;

/*******************************************************************************
 * Test UI for {@link HexBytesField}.
 ******************************************************************************/
public class HexBytesFieldMain
{
    /***************************************************************************
     * Main entry point for the test application.
     * @param args ignored.
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( () -> createFrame() );
    }

    /***************************************************************************
     * Creates the test frame to be displayed.
     * @return the new test frame.
     **************************************************************************/
    private static JFrame createFrame()
    {
        StandardFrameView frameView = new StandardFrameView();
        JFrame frame = frameView.getView();

        StandardFormView form = new StandardFormView();

        List<byte []> quickList = new ArrayList<>();

        quickList.add( HexUtils.asArray( 0xDE, 0xAD ) );
        quickList.add( HexUtils.asArray( 0xBE, 0xEF ) );
        quickList.add( HexUtils.asArray( 0xCA, 0xFE ) );

        HexBytesField f1 = new HexBytesField( "Hex Bytes", quickList );
        f1.setValue( quickList.get( 0 ) );
        form.addField( f1 );

        StringFormField f2 = new StringFormField( "ASCII String", 0, null,
            false );
        form.addField( f2 );

        f1.setUpdater( ( d ) -> f2.setValue(
            new String( d, IOUtils.get8BitEncoding() ) ) );

        f2.setUpdater(
            ( d ) -> f1.setValue( d.getBytes( IOUtils.get8BitEncoding() ) ) );

        frameView.setContent( form.getView() );
        frameView.setSize( 400, 400 );
        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        return frame;
    }
}
