package org.jutils.pattern;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.jutils.*;
import org.jutils.ui.*;
import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.fields.StringFormField;
import org.jutils.ui.model.IView;

/*******************************************************************************
 *
 ******************************************************************************/
public class PatternTesterViewMain implements IView<JComponent>
{
    /**  */
    private final JPanel view;
    /**  */
    private final StringFormField testField;
    /**  */
    private final StringPatternView patternView;
    /**  */
    private final StringPatternField patternField;

    /**  */
    private final StringFormField viewResultField;
    /**  */
    private final StringFormField fieldResultField;

    /***************************************************************************
     * 
     **************************************************************************/
    public PatternTesterViewMain()
    {
        this.testField = new StringFormField( "Test String" );
        this.patternView = new StringPatternView();
        this.patternField = new StringPatternField( "Pattern" );

        this.fieldResultField = new StringFormField( "Field Result", 0, null );
        this.viewResultField = new StringFormField( "View Result", 0, null );

        this.view = createView();

        testField.setValue(
            "The quick brown deer jumped over the lazy thingy" );
        StringPattern pattern = new StringPattern();
        patternField.setValue( pattern );
        patternView.setData( pattern );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( addToolbar(), BorderLayout.NORTH );
        panel.add( createForm(), BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component addToolbar()
    {
        JToolBar toolbar = new JGoodiesToolBar();

        SwingUtils.addActionToToolbar( toolbar, createCheckAction() );

        return toolbar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createCheckAction()
    {
        Icon icon = IconConstants.getIcon( IconConstants.ANALYZE_16 );
        ActionListener listener = ( e ) -> testPatterns();
        return new ActionAdapter( listener, "Test", icon );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void testPatterns()
    {
        String testString = testField.getValue();

        StringPattern pattern;
        IMatcher matcher;

        pattern = patternField.getValue();
        try
        {
            matcher = pattern.createMatcher();
            fieldResultField.setValue( "" + matcher.matches( testString ) );
        }
        catch( ValidationException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        pattern = patternView.getData();
        try
        {
            matcher = pattern.createMatcher();
            viewResultField.setValue( "" + matcher.matches( testString ) );
        }
        catch( ValidationException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createForm()
    {
        StandardFormView form = new StandardFormView();

        TitleView viewview = new TitleView( "Pattern View",
            patternView.getView() );

        form.addField( testField );
        form.addField( patternField );
        form.addField( fieldResultField );
        form.addComponent( viewview.getView() );
        form.addField( viewResultField );

        return form.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new PatternTesterApp() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class PatternTesterApp implements IFrameApp
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public JFrame createFrame()
        {
            StandardFrameView frameView = new StandardFrameView();
            PatternTesterViewMain ptv = new PatternTesterViewMain();

            frameView.setContent( ptv.getView() );
            frameView.setSize( 400, 600 );
            frameView.setTitle( "Pattern Tester" );
            frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

            return frameView.getView();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void finalizeGui()
        {
        }
    }
}
