package jutils.core.ui;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jutils.core.ui.StandardFormView;
import jutils.core.ui.app.AppRunner;
import jutils.core.ui.app.IFrameApp;
import jutils.core.ui.fields.StringFormField;
import jutils.core.ui.validation.AggregateValidityChangedManager;
import jutils.core.ui.validation.IValidityChangedListener;
import jutils.core.ui.validation.Validity;

public class StandardFormMain implements IFrameApp
{
    @Override
    public JFrame createFrame()
    {
        JFrame frame = new JFrame();
        frame.setTitle( "Standard Form Test" );

        frame.setContentPane( createContentPane() );

        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize( 500, 500 );

        return frame;
    }

    private static Container createContentPane()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;
        AggregateValidityChangedManager manager = new AggregateValidityChangedManager();

        JLabel label = new JLabel( "?" );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( label, constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( createForm( manager ), constraints );

        manager.addValidityChanged( new AggregateListener( label ) );

        return panel;
    }

    private static Container createForm(
        AggregateValidityChangedManager manager )
    {
        StandardFormView view = new StandardFormView();

        StringFormField field1 = new StringFormField( "Field 1", 1, 30 );

        StringFormField field2 = new StringFormField( "Field 2", 1, 25 );

        view.addField( field1 );
        manager.addField( field1 );

        view.addField( field2 );
        manager.addField( field2 );

        return view.getView();
    }

    @Override
    public void finalizeGui()
    {
    }

    public static void main( String [] args )
    {
        AppRunner.invokeLater( new StandardFormMain() );
    }

    private static class AggregateListener implements IValidityChangedListener
    {
        private final JLabel label;

        public AggregateListener( JLabel label )
        {
            this.label = label;
        }

        @Override
        public void signalValidity( Validity v )
        {
            label.setText( v.toString() );
        }
    }
}
