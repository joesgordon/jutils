package org.jutils.ui;

import java.awt.*;

import javax.swing.*;

import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;
import org.jutils.ui.fields.StringFormField;
import org.jutils.ui.validation.*;

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
        FrameRunner.invokeLater( new StandardFormMain() );
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
