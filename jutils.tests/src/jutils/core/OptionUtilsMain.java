package jutils.core;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import jutils.core.OptionUtils.StockIcon;
import jutils.core.data.SystemProperty;
import jutils.core.laf.UIProperty;
import jutils.core.ui.StandardFrameView;
import jutils.core.ui.app.AppRunner;
import jutils.core.ui.fields.IntegerFormField;
import jutils.core.ui.model.IView;
import jutils.core.utils.ByteOrdering;
import jutils.core.utils.IGetter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class OptionUtilsMain
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.invokeLater( () -> createFrame() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static JFrame createFrame()
    {
        OptionUtilsView view = new OptionUtilsView();
        StandardFrameView frameView = new StandardFrameView();
        JFrame frame = frameView.getView();

        frameView.setTitle( "OptionUtils Tester" );
        frameView.setSize( 800, 500 );
        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frameView.setContent( view.getView() );

        return frame;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class OptionUtilsView implements IView<JComponent>
    {
        /**  */
        private JComponent view;

        /**
         * 
         */
        public OptionUtilsView()
        {
            this.view = createView();
        }

        /**
         * @return
         */
        private static JComponent createView()
        {
            JPanel panel = new JPanel( new GridBagLayout() );
            int r = 0;

            addTestLine( panel, r++,
                "Show Combo with Question icon and Ok/Cancel buttons",
                () -> OptionUtils.showComboDialog( panel, "Test Message",
                    "Test Title", "Byte Order", ByteOrdering.values(),
                    ByteOrdering.BIG_ENDIAN ) );

            addTestLine( panel, r++,
                "Show Combo with Error icon and Ok/Cancel buttons",
                () -> OptionUtils.showComboDialog( panel, "Test Message",
                    "Test Title", "System Property", SystemProperty.values(),
                    SystemProperty.JAVA_SPECIFICATION_NAME, StockIcon.ERROR ) );

            addTestLine( panel, r++,
                "Show Combo with Custom icon and Ok/Cancel buttons",
                () -> OptionUtils.showComboDialog( panel, "Test Message",
                    "Test Title", "UI Property", UIProperty.values(),
                    UIProperty.LIST_BACKGROUND,
                    IconConstants.getIcon( IconConstants.CALENDAR_32 ) ) );

            addTestLine( panel, r++,
                "Show Confirm message with Question icon and Ok/Cancel buttons",
                () -> OptionUtils.showConfirmMessage( panel,
                    "Confirm this message, please", "Test Message" ) );

            addTestLine( panel, r++,
                "Show Option message with Question icon and Ok/Cancel buttons",
                () -> OptionUtils.showOptionMessage( panel, "Choose your mood:",
                    "Test Message", ByteOrdering.values(),
                    ByteOrdering.NETWORK_ORDER ) );

            addTestLine( panel, r++,
                "Show Option message (resizable) with Question icon and Ok/Cancel buttons",
                () -> OptionUtils.showOptionMessage( panel,
                    "Choose your favorite character:", "Test Message",
                    new String[] { "Lister", "Holly", "Cat", "Rimmer",
                        "Kristine" },
                    "Rimmer", true ) );

            addTestLine( panel, r++,
                "Show Editable message with Question icon and Ok/Cancel buttons",
                () -> OptionUtils.showEditableMessage( panel,
                    "Choose your favorite cat:", "Test Message",
                    new String[] { "Garfield", "Nermal", "Heathcliff" },
                    "Garfield" ) );

            addTestLine( panel, r++,
                "Show Error message with Error icon and Ok button",
                () -> OptionUtils.showErrorMessage( panel,
                    "An error message would normally go here",
                    "Test Message" ) );

            addTestLine( panel, r++,
                "Show Info message with Info icon and Ok button",
                () -> OptionUtils.showInfoMessage( panel,
                    "An info message would normally go here",
                    "Test Message" ) );

            addTestLine( panel, r++,
                "Show OkCancel message with Question icon and Yepperdoodles/Cancel button",
                () -> OptionUtils.showOkCancelDialog( panel,
                    "This message can be an object", "Test Message",
                    "Yepperdoodles", () -> {
                    } ) );

            addTestLine( panel, r++,
                "Show Question message with Question icon and Ok/Cancel button",
                () -> OptionUtils.showQuestionField( panel,
                    "What is your favorite number?", "Test Message",
                    new IntegerFormField( "Number" ) ) );

            addTestLine( panel, r++,
                "Show Question message Question Info icon and Yes/No buttons",
                () -> OptionUtils.showQuestionMessage( panel,
                    "Do you like George Wendt?", "Test Message" ) );

            addTestLine( panel, r++,
                "Show Question message with Question icon and Yes/No/Cancel buttons",
                () -> OptionUtils.showQuestionMessage( panel,
                    "Do you like eating beans?", "Test Message", "Yarp",
                    "Narp" ) );

            addTestLine( panel, r++,
                "Show Question message with Question icon and Yes/No/Cancel buttons",
                () -> OptionUtils.showQuestionMessage( panel,
                    "Do you like to watch George Wendt eating beans?",
                    "Test Message", "Yep", "Nope", "Nevermind" ) );

            addTestLine( panel, r++,
                "Show Warning message with Warning icon and Ok button",
                () -> OptionUtils.showWarningMessage( panel,
                    "A warning message would normally go here",
                    "Test Message" ) );

            GridBagConstraints constraints = new GridBagConstraints( 0, r++, 4,
                1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 );
            panel.add( Box.createHorizontalStrut( 0 ), constraints );

            panel.setBorder( new EmptyBorder( 4, 4, 4, 4 ) );

            JScrollPane pane = new JScrollPane( panel );

            pane.getVerticalScrollBar().setUnitIncrement( 12 );
            pane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );

            return pane;
        }

        /**
         * @param panel
         * @param row
         * @param text
         * @param callback
         */
        private static void addTestLine( JPanel panel, int row, String text,
            Runnable callback )
        {
            addTestLine( panel, row, text, () -> {
                callback.run();
                return "<void>";
            } );
        }

        /**
         * @param <T>
         * @param panel
         * @param row
         * @param text
         * @param callback
         */
        private static <T> void addTestLine( JPanel panel, int row, String text,
            IGetter<T> callback )
        {
            JButton button = new JButton( "Show Dialog" );
            JLabel titleLabel = new JLabel( text );
            JLabel resultLabel = new JLabel();
            GridBagConstraints constraints;

            button.addActionListener( ( e ) -> {
                T result = callback.get();
                String t = result == null ? "<null>" : result.toString();
                resultLabel.setText( t );
            } );

            int top = row == 0 ? 0 : 6;
            int col = 0;

            constraints = new GridBagConstraints( col++, row, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets( top, 0, 0, 0 ), 0, 0 );
            panel.add( titleLabel, constraints );

            constraints = new GridBagConstraints( col++, row, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets( top, 4, 0, 0 ), 20, 10 );
            panel.add( button, constraints );

            constraints = new GridBagConstraints( col++, row, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets( top, 4, 0, 0 ), 0, 0 );
            panel.add( resultLabel, constraints );
        }

        /**
         * @{@inheritDoc}
         */
        @Override
        public JComponent getView()
        {
            return view;
        }
    }
}
