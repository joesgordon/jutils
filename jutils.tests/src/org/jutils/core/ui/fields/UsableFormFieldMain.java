package org.jutils.core.ui.fields;

import java.awt.Container;

import javax.swing.JFrame;

import org.jutils.core.IconConstants;
import org.jutils.core.ui.StandardFormView;
import org.jutils.core.ui.StandardFrameView;
import org.jutils.core.ui.app.FrameRunner;
import org.jutils.core.utils.Usable;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UsableFormFieldMain
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( () -> createFrame() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static JFrame createFrame()
    {
        StandardFrameView frameView = new StandardFrameView();
        JFrame frame = frameView.getView();

        frameView.setContent( createContent() );
        frameView.setTitle( "UsableFormField Test" );
        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frameView.setSize( 600, 500 );
        frameView.getView().setIconImages( IconConstants.getPageMagImages() );

        return frame;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static Container createContent()
    {
        StandardFormView form = new StandardFormView();
        UsableFormField<Integer> unintializedField = new UsableFormField<>(
            new IntegerFormField( "Uninitialized" ) );
        UsableFormField<Integer> noInputUnusedField = new UsableFormField<>(
            new IntegerFormField( "No Input Unused" ) );
        UsableFormField<Integer> noInputUsedField = new UsableFormField<>(
            new IntegerFormField( "No Input Used" ) );

        noInputUnusedField.setValue( new Usable<>( false, null ) );
        noInputUsedField.setValue( new Usable<>( true, null ) );

        form.addField( unintializedField );
        form.addField( noInputUnusedField );
        form.addField( noInputUsedField );

        return form.getView();
    }
}
