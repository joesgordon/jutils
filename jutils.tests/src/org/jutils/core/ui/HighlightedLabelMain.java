package org.jutils.core.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.jutils.core.ui.app.AppRunner;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HighlightedLabelMain
{
    /***************************************************************************
     * UHighlightedLabel demo
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
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();

        HighlightedLabel label1 = new HighlightedLabel(
            new Font( "Monospaced", Font.PLAIN, 12 ) );
        label1.setText( "Sample un-highlighted" );

        HighlightedLabel label2 = new HighlightedLabel(
            new Font( "Sans Serif", Font.BOLD, 16 ) );
        label2.setText( "Sample with highlight" );
        label2.setHighlight( 12, 9 );

        HighlightedLabel label3 = new HighlightedLabel(
            new Font( "Sans Serif", Font.BOLD, 16 ) );
        label3.setText( "Sample with highlight" );
        label3.setHighlight( 12, 9 );
        label3.setHorizontalAlignment( SwingConstants.CENTER );

        HighlightedLabel label4 = new HighlightedLabel( "Another sample" );
        label4.setHighlightColor( Color.magenta );
        label4.setHighlight( 8, 3 );

        panel.setLayout( new GridBagLayout() );
        panel.add( label1,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.33,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        panel.add( label2,
            new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.33,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        panel.add( label3,
            new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.33,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        panel.add( label4,
            new GridBagConstraints( 0, 3, 1, 1, 1.0, 0.33,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );

        frame.setTitle( "UHighlightedLabel demo" );
        frame.setContentPane( panel );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setPreferredSize( new Dimension( 350, 150 ) );
        frame.setLocationRelativeTo( null );
        frame.setVisible( true );

        return frame;
    }
}
