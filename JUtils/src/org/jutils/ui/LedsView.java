package org.jutils.ui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import org.jutils.SwingUtils;
import org.jutils.ui.model.IView;

/*******************************************************************************
 *
 ******************************************************************************/
public class LedsView implements IView<JComponent>
{
    /**  */
    private final int rows;
    /**  */
    private final int cols;

    /**  */
    private final JPanel view;
    /**  */
    private final JPanel labelsPanel;

    /***************************************************************************
     * @param rows
     * @param cols
     **************************************************************************/
    public LedsView( int rows, int cols )
    {
        this.rows = rows;
        this.cols = cols;

        this.view = new JPanel( new GridBagLayout() );

        GridLayout labelLayout = new GridLayout( rows, cols );

        this.labelsPanel = new JPanel( labelLayout );

        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.5, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        view.add( Box.createHorizontalStrut( 0 ), constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        view.add( labelsPanel, constraints );

        constraints = new GridBagConstraints( 2, 0, 1, 1, 0.5, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        view.add( Box.createHorizontalStrut( 0 ), constraints );

        labelLayout.setHgap( 4 );
        labelLayout.setVgap( 4 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void resizeAllSame()
    {
        Component [] comps = new Component[labelsPanel.getComponentCount()];

        for( int i = 0; i < comps.length; i++ )
        {
            comps[i] = labelsPanel.getComponent( i );
        }

        SwingUtils.setMaxComponentSize( comps );
    }

    /***************************************************************************
     * @param field
     * @param row
     * @param col
     **************************************************************************/
    public void addField( LedLabel field, int row, int col )
    {
        if( row < 0 )
        {
            throw new IllegalArgumentException(
                "Row cannot be negative: " + row );
        }
        else if( col < 0 )
        {
            throw new IllegalArgumentException(
                "Column cannot be negative: " + row );
        }
        else if( row >= rows )
        {
            throw new IllegalArgumentException( "Row, " + row +
                ", cannot be greater than the number of rows, " + rows );
        }
        else if( col >= cols )
        {
            throw new IllegalArgumentException( "Column, " + col +
                ", cannot be greater than the number of rows, " + cols );
        }

        labelsPanel.add( createIndicatorView( field ) );
    }

    /***************************************************************************
     * @param field
     * @return
     **************************************************************************/
    private Component createIndicatorView( LedLabel field )
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        panel.setBorder( new EtchedBorder() );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 1, 2, 2, 2 ), 0, 0 );
        panel.add( field.getView(), constraints );

        return panel;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }
}
