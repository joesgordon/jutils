package org.jutils.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import javax.swing.*;

import org.jutils.SwingUtils;
import org.jutils.ui.model.IView;

/*******************************************************************************
 *
 ******************************************************************************/
public class FindDialog implements IView<JDialog>
{
    /**  */
    private final JDialog dialog;
    /**  */
    private final JTextField findTextField = new JTextField();
    /**  */
    private final JTextArea errorLabel = new JTextArea();
    /**  */
    private final JCheckBox matchCheckBox = new JCheckBox();
    /**  */
    private final JCheckBox regexCheckBox = new JCheckBox();
    /**  */
    private final JCheckBox wrapCheckBox = new JCheckBox();

    /**  */
    private final ArrayList<FindListener> findListeners = new ArrayList<FindListener>();

    /***************************************************************************
     *
     **************************************************************************/
    public FindDialog()
    {
        this( new Frame(), "FindDialog", false );
    }

    /***************************************************************************
     * @param owner Frame
     * @param title String
     * @param modal boolean
     **************************************************************************/
    public FindDialog( Frame owner, String title, boolean modal )
    {
        this.dialog = new JDialog( owner, title, modal );

        // ---------------------------------------------------------------------
        // Set tab-order.
        // ---------------------------------------------------------------------
        List<JComponent> focusList = new ArrayList<JComponent>();

        focusList.add( this.findTextField );

        focusList.add( this.matchCheckBox );
        focusList.add( this.regexCheckBox );
        focusList.add( this.wrapCheckBox );

        // ---------------------------------------------------------------------
        // Setup dialog.
        // ---------------------------------------------------------------------
        dialog.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );

        SwingUtils.installEscapeCloseOperation( dialog );

        dialog.setContentPane( createContent( focusList ) );
        dialog.pack();

        setOptions( null );

        // ----------------------------------------------------------------------
        // Set tab-order.
        // ----------------------------------------------------------------------

        dialog.setFocusTraversalPolicy( new FocusPolicyList( focusList ) );
    }

    private Container createContent( List<JComponent> focusList )
    {
        JPanel contentPane = new JPanel( new GridBagLayout() );
        JButton findButton = new JButton();
        JLabel findLabel = new JLabel();
        JButton cancelButton = new JButton();

        focusList.add( findButton );
        focusList.add( cancelButton );

        findLabel.setText( "Find What:" );
        findTextField.setColumns( 25 );

        dialog.getRootPane().setDefaultButton( findButton );
        findButton.setText( "Find" );
        findButton.addActionListener( new FindTextListener( this ) );
        findButton.setDefaultCapable( true );

        cancelButton.setText( "Cancel" );
        cancelButton.addActionListener( new CancelListener( this ) );

        errorLabel.setText( "" );
        errorLabel.setEditable( false );
        errorLabel.setBackground( dialog.getBackground() );
        errorLabel.setBorder( BorderFactory.createLineBorder( Color.red ) );
        errorLabel.setVisible( false );

        contentPane.add( findLabel,
            new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 5, 5, 5, 5 ), 0, 0 ) );
        contentPane.add( findTextField,
            new GridBagConstraints( 1, 0, 2, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 5, 5, 5, 5 ), 0, 0 ) );
        contentPane.add( findButton,
            new GridBagConstraints( 3, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 5, 5, 5, 5 ), 0, 0 ) );

        contentPane.add( createOptionsPanel(),
            new GridBagConstraints( 0, 1, 2, 2, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );
        contentPane.add( cancelButton,
            new GridBagConstraints( 3, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets( 5, 5, 5, 5 ), 0, 0 ) );

        contentPane.add( errorLabel,
            new GridBagConstraints( 2, 2, 2, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        return contentPane;
    }

    private Component createOptionsPanel()
    {
        JPanel optionsPanel = new JPanel( new GridBagLayout() );
        optionsPanel.setBorder( BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Options" ) );

        matchCheckBox.setText( "Match Case" );

        regexCheckBox.setText( "Regular Expression" );

        wrapCheckBox.setText( "Wrap Around" );

        optionsPanel.add( matchCheckBox,
            new GridBagConstraints( 0, 0, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        optionsPanel.add( regexCheckBox,
            new GridBagConstraints( 0, 1, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        optionsPanel.add( wrapCheckBox,
            new GridBagConstraints( 0, 2, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        return optionsPanel;
    }

    /***************************************************************************
     * @param fl FindListener
     **************************************************************************/
    public void addFindListener( FindListener fl )
    {
        findListeners.add( fl );
    }

    /***************************************************************************
     * @param fl FindListener
     **************************************************************************/
    public void removeFindListener( FindListener fl )
    {
        findListeners.remove( fl );
    }

    /***************************************************************************
     * @param e ActionEvent
     **************************************************************************/
    public void findText()
    {
        FindOptions options = getOptions();
        if( options.textToFind.length() > 0 )
        {
            for( int i = findListeners.size() - 1; i > -1; i-- )
            {
                FindListener fl = findListeners.get( i );
                try
                {
                    fl.findText( options );
                    errorLabel.setVisible( false );
                }
                catch( PatternSyntaxException ex )
                {
                    errorLabel.setText( ex.getMessage() );
                    errorLabel.setVisible( true );
                }
            }
        }

        dialog.setVisible( errorLabel.isVisible() );
    }

    /***************************************************************************
     * @return FindOptions
     **************************************************************************/
    public FindOptions getOptions()
    {
        FindOptions options = new FindOptions();

        options.textToFind = findTextField.getText();
        options.matchCase = matchCheckBox.isSelected();
        options.wrapAround = wrapCheckBox.isSelected();
        options.useRegex = regexCheckBox.isSelected();

        return options;
    }

    /***************************************************************************
     * @param options FindOptions
     **************************************************************************/
    public void setOptions( FindOptions options )
    {
        if( options == null )
        {
            options = new FindOptions();
        }

        findTextField.setText( options.textToFind );
        matchCheckBox.setSelected( options.matchCase );
        wrapCheckBox.setSelected( options.wrapAround );
        regexCheckBox.setSelected( options.useRegex );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JDialog getView()
    {
        return dialog;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class CancelListener implements ActionListener
    {
        private FindDialog adaptee;

        public CancelListener( FindDialog adaptee )
        {
            this.adaptee = adaptee;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            ExitListener.doDefaultCloseOperation( adaptee.dialog );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FindTextListener implements ActionListener
    {
        private FindDialog adaptee;

        public FindTextListener( FindDialog adaptee )
        {
            this.adaptee = adaptee;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            adaptee.findText();
        }
    }
}
