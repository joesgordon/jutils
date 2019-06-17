package org.jutils.ui;

import java.awt.*;

import javax.swing.*;

import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;

public class TristateCheckBoxMain
{
    /***************************************************************************
     * @param args String[]
     * @throws Exception
     **************************************************************************/
    public static void main( String args[] ) throws Exception
    {
        FrameRunner.invokeLater( new TristateCheckBoxApp(), false );
    }

    private static final class TristateCheckBoxApp implements IFrameApp
    {
        @Override
        public JFrame createFrame()
        {
            JFrame frame = new JFrame( "TristateCheckBoxTest" );
            frame.getContentPane().setLayout( new GridBagLayout() );

            final TristateCheckBox swingBox = new TristateCheckBox(
                "Testing the tristate checkbox" );

            JRadioButton trueButton = new JRadioButton();
            trueButton.setText( "True" );
            trueButton.addActionListener(
                ( e ) -> swingBox.setState( Boolean.TRUE ) );

            JRadioButton falseButton = new JRadioButton();
            falseButton.setText( "False" );
            falseButton.addActionListener(
                ( e ) -> swingBox.setState( Boolean.FALSE ) );

            JRadioButton kindaButton = new JRadioButton();
            kindaButton.setText( "Kinda" );
            kindaButton.addActionListener( ( e ) -> swingBox.setState( null ) );

            ButtonGroup group = new ButtonGroup();
            group.add( trueButton );
            group.add( falseButton );
            group.add( kindaButton );

            frame.getContentPane().add( swingBox,
                new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.BOTH,
                    new Insets( 10, 10, 10, 10 ), 0, 0 ) );
            frame.getContentPane().add( trueButton,
                new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.BOTH,
                    new Insets( 10, 10, 5, 10 ), 0, 0 ) );
            frame.getContentPane().add( falseButton,
                new GridBagConstraints( 0, 2, 1, 1, 0.0, 1.0,
                    GridBagConstraints.WEST, GridBagConstraints.BOTH,
                    new Insets( 5, 10, 5, 10 ), 0, 0 ) );
            frame.getContentPane().add( kindaButton,
                new GridBagConstraints( 0, 3, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.BOTH,
                    new Insets( 5, 10, 10, 10 ), 0, 0 ) );
            frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

            return frame;
        }

        @Override
        public void finalizeGui()
        {
        }
    }
}
