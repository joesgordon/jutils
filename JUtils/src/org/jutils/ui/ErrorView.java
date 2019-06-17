package org.jutils.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.*;
import javax.swing.border.Border;

import org.jutils.ui.model.IDataView;

/*******************************************************************************
 *
 ******************************************************************************/
public class ErrorView implements IDataView<String>
{
    /**  */
    private final JComponent view;
    /**  */
    private final JScrollPane tracePane;
    /**  */
    private final JTextArea stacktraceField;

    /***************************************************************************
     * 
     **************************************************************************/
    public ErrorView()
    {
        this.stacktraceField = new JTextArea();
        this.tracePane = new JScrollPane( stacktraceField );
        this.view = createView();

        stacktraceField.setEditable( false );
        stacktraceField.setLineWrap( false );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        // tracePane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
        tracePane.setPreferredSize( new Dimension( 400, 200 ) );
        tracePane.setMinimumSize( new Dimension( 400, 200 ) );
        tracePane.getVerticalScrollBar().setUnitIncrement( 12 );

        // panel.add( new JSeparator(), BorderLayout.NORTH );
        panel.add( tracePane, BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * @param border
     **************************************************************************/
    public void setBorder( Border border )
    {
        tracePane.setBorder( border );
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
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getData()
    {
        return stacktraceField.getText();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( String text )
    {
        stacktraceField.setText( text );
        stacktraceField.setCaretPosition( 0 );
    }
}
