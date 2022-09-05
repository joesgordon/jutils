package org.jutils.core.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.JPanel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PanelPainter extends JPanel
{
    /** Chosen by fair dice roll. Guaranteed to be random. */
    private static final long serialVersionUID = 4L;

    /**  */
    private final IPainter painter;

    /***************************************************************************
     * @param painter
     **************************************************************************/
    public PanelPainter( IPainter painter )
    {
        this.painter = painter;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void paintComponent( Graphics g )
    {
        super.paintComponent( g );

        painter.paint( this, ( Graphics2D )g );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static interface IPainter
    {
        /**
         * @param c
         * @param g
         */
        void paint( JComponent c, Graphics2D g );

    }
}
