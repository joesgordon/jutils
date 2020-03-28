package org.jutils.apps.filespy.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jutils.core.OptionUtils;
import org.jutils.core.SwingUtils;
import org.jutils.core.Utils;
import org.jutils.core.ui.ExitListener;
import org.jutils.core.ui.SearchableTextArea;
import org.jutils.core.ui.StandardFormView;
import org.jutils.core.ui.StandardFrameView;
import org.jutils.core.ui.app.FrameRunner;
import org.jutils.core.ui.app.IFrameApp;
import org.jutils.core.ui.model.IView;

import com.jgoodies.forms.builder.ButtonStackBuilder;

/*******************************************************************************
 *
 ******************************************************************************/
public class RegexPanel implements IView<JPanel>
{
    /**  */
    private final JPanel view;

    /**  */
    private final SearchableTextArea regexTextArea;
    /**  */
    private final SearchableTextArea resultTextArea;
    /**  */
    private final SearchableTextArea testTextArea;

    /**  */
    private final JCheckBox caseCheckBox;
    /**  */
    private final JCheckBox multilineCheckBox;
    /**  */
    private final JCheckBox commentsCheckBox;
    /**  */
    private final JCheckBox globalCheckBox;

    /***************************************************************************
     *
     **************************************************************************/
    public RegexPanel()
    {
        this.regexTextArea = new SearchableTextArea();
        this.resultTextArea = new SearchableTextArea();
        this.testTextArea = new SearchableTextArea();
        this.caseCheckBox = new JCheckBox();
        this.multilineCheckBox = new JCheckBox();
        this.commentsCheckBox = new JCheckBox();
        this.globalCheckBox = new JCheckBox();

        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        StandardFormView form = new StandardFormView();
        JScrollPane resultScrollPane = new JScrollPane(
            resultTextArea.getView() );
        JScrollPane testScrollPane = new JScrollPane( testTextArea.getView() );

        testTextArea.setText( "Here is" + Utils.NEW_LINE + "some text with" +
            Utils.NEW_LINE + "the word blah in it" + Utils.NEW_LINE +
            "at some" + Utils.NEW_LINE + "point." );

        form.addField( "Regex", createRegexPanel() );
        form.addField( "Test Text", testScrollPane );
        form.addField( "Results", resultScrollPane );

        return form.getView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createRegexPanel()
    {
        JScrollPane regexScrollPane = new JScrollPane();
        JPanel regexPanel = new JPanel( new GridBagLayout() );

        regexTextArea.setText( "blah" );
        regexScrollPane.setViewportView( regexTextArea.getView() );

        Dimension dim = regexScrollPane.getPreferredSize();
        dim.height = 100;
        regexScrollPane.setMinimumSize( dim );
        regexScrollPane.setPreferredSize( dim );

        regexPanel.add( regexScrollPane,
            new GridBagConstraints( 0, 0, 1, 2, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 8 ), 0, 0 ) );

        regexPanel.add( createButtonPanel(),
            new GridBagConstraints( 1, 0, 1, 1, 0.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 0, 4, 0 ), 0, 0 ) );

        regexPanel.add( createOptionsPanel(),
            new GridBagConstraints( 1, 1, 1, 1, 0.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        return regexPanel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createButtonPanel()
    {
        ButtonStackBuilder builder = new ButtonStackBuilder();
        JButton closeButton = new JButton();
        JButton testButton = new JButton();

        testButton.setText( "Test" );
        testButton.addActionListener( new TestRegexListener( this ) );

        closeButton.setText( "Close" );
        closeButton.addActionListener( new CloseListener( this ) );

        builder.addFixed( testButton );
        builder.addRelatedGap();
        builder.addFixed( closeButton );

        return builder.getPanel();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createOptionsPanel()
    {
        StandardFormView form = new StandardFormView();

        caseCheckBox.setText( "Case-Insensitive" );
        caseCheckBox.setToolTipText( "Enables case-insensitive matching." );
        multilineCheckBox.setText( "Multiline" );
        multilineCheckBox.setToolTipText( "^ and $ match just after or just " +
            "before, respectively, a line terminator or the end of the " +
            "input sequence." );
        commentsCheckBox.setText( "Comments" );
        commentsCheckBox.setSelected( true );
        commentsCheckBox.setToolTipText(
            "whitespace is ignored, and embedded " +
                "comments starting with # are ignored until the end of a line." );
        globalCheckBox.setText( "Global" );
        globalCheckBox.setToolTipText(
            ". matches any character, including a " + "line terminator." );

        form.addComponent( caseCheckBox );
        form.addComponent( multilineCheckBox );
        form.addComponent( commentsCheckBox );
        form.addComponent( globalCheckBox );

        form.getView().setBorder(
            BorderFactory.createTitledBorder( "Options" ) );

        return form.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void testRegex()
    {
        boolean caseFlag = caseCheckBox.isSelected();
        boolean multilineFlag = multilineCheckBox.isSelected();
        boolean commentsFlag = commentsCheckBox.isSelected();
        boolean globalFlag = globalCheckBox.isSelected();
        int flags = 0;
        Pattern pattern = null;
        Matcher matcher = null;
        CharSequence seq = null;
        StringBuffer resultStr = new StringBuffer();

        flags |= caseFlag ? Pattern.CASE_INSENSITIVE : flags;
        flags |= multilineFlag ? Pattern.MULTILINE : flags;
        flags |= commentsFlag ? Pattern.COMMENTS : flags;
        flags |= globalFlag ? Pattern.DOTALL : flags;

        try
        {
            pattern = Pattern.compile( regexTextArea.getText(), flags );
            seq = testTextArea.getSequence();
            matcher = pattern.matcher( seq );

            if( matcher.find() )
            {
                int indexFirst = matcher.start();
                int indexLast = matcher.end();
                String text = seq.subSequence( indexFirst,
                    indexLast ).toString();

                resultStr.append( "Matched Text: " + Utils.NEW_LINE );
                resultStr.append( text + Utils.NEW_LINE );
                resultStr.append( "Starts At: " + indexFirst + Utils.NEW_LINE );
                resultStr.append( "Ends At: " + indexLast + Utils.NEW_LINE );

                for( int i = 1; i <= matcher.groupCount(); i++ )
                {
                    resultStr.append( "Group " + i + ": '" );
                    resultStr.append( seq.subSequence( matcher.start( i ),
                        matcher.end( i ) ) + "'" + Utils.NEW_LINE );
                }

                resultTextArea.setText( text );
            }
            else
            {
                resultTextArea.setText( "No Result Found" );
            }
            resultTextArea.setText( resultStr.toString() );
        }
        catch( PatternSyntaxException ex )
        {
            OptionUtils.showErrorMessage(
                SwingUtils.getComponentsWindow( view ), ex.getMessage(),
                "ERROR" );
            regexTextArea.getView().requestFocus();
            regexTextArea.select( ex.getIndex() - 1, ex.getIndex() );
        }
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        IFrameApp app = new IFrameApp()
        {
            @Override
            public void finalizeGui()
            {
            }

            @Override
            public JFrame createFrame()
            {
                RegexPanel view = new RegexPanel();
                StandardFrameView frameView = new StandardFrameView();
                JFrame frame = frameView.getView();

                frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                frame.setTitle( "Regex Friend" );
                frame.setContentPane( view.getView() );
                frame.setSize( new Dimension( 500, 500 ) );

                return frame;
            }
        };

        FrameRunner.invokeLater( app );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class CloseListener implements ActionListener
    {
        private RegexPanel view;

        public CloseListener( RegexPanel adaptee )
        {
            this.view = adaptee;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            Window win = SwingUtils.getComponentsWindow( view.view );

            ExitListener.doDefaultCloseOperation( win );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class TestRegexListener implements ActionListener
    {
        private RegexPanel view;

        public TestRegexListener( RegexPanel adaptee )
        {
            this.view = adaptee;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            view.testRegex();
        }
    }
}
