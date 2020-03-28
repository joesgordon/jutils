package org.jutils.minesweeper.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import org.jutils.core.ui.model.IView;
import org.jutils.minesweeper.MsIcons;
import org.jutils.minesweeper.data.GameOptions;
import org.jutils.minesweeper.data.GridSpace;

/*******************************************************************************
 * 
 ******************************************************************************/
public class GridSpaceView implements IView<JComponent>
{
    /**  */
    public final GridSpace space;

    /**  */
    private final GameOptions options;

    /**  */
    private final boolean isTopEdge;
    /**  */
    private final boolean isLeftEdge;
    /**  */
    private final boolean isBottomEdge;
    /**  */
    private final boolean isRightEdge;

    /**  */
    private final JLabel label;
    /**  */
    private final FlagIcon flag;
    /**  */
    private final Icon mine;

    /***************************************************************************
     * @param space
     * @param options
     * @param isBottomEdge
     * @param isRightEdge
     **************************************************************************/
    public GridSpaceView( GridSpace space, GameOptions options,
        boolean isBottomEdge, boolean isRightEdge )
    {
        this.space = space;
        this.options = options;
        this.isTopEdge = space.y == 0;
        this.isLeftEdge = space.x == 0;
        this.isBottomEdge = isBottomEdge;
        this.isRightEdge = isRightEdge;

        this.label = new JLabel();
        this.flag = new FlagIcon( 22 );
        this.mine = MsIcons.getIcon( "bomb024.png" );

        label.setFont(
            label.getFont().deriveFont( 24.0f ).deriveFont( Font.BOLD ) );
        label.setHorizontalAlignment( SwingConstants.CENTER );
        label.setHorizontalTextPosition( SwingConstants.CENTER );
        // label.setAlignmentX( JLabel.CENTER_ALIGNMENT );

        label.setPreferredSize( new Dimension( 30, 30 ) );
        label.setMinimumSize( label.getPreferredSize() );
        label.setMaximumSize( label.getPreferredSize() );

        label.setOpaque( true );
        label.setBackground( options.palette.form );

        repaint();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void repaint()
    {
        String text = "";
        Color bdrClr = options.palette.border;
        Color bg = label.getBackground();
        Icon icon = null;

        boolean isLight = ( space.x % 2 ) == ( space.y % 2 );

        int bw = 1;

        boolean isTop = isTopEdge || space.isTopBorder;
        boolean isLeft = isLeftEdge || space.isLeftBorder;
        boolean isBottom = isBottomEdge || space.isBottomBorder;
        boolean isRight = isRightEdge || space.isRightBorder;

        int tc = isTop ? bw : 0;
        int lc = isLeft ? bw : 0;
        int bc = isBottom ? bw : 0;
        int rc = isRight ? bw : 0;

        int te = isTop ? 0 : bw;
        int le = isLeft ? 0 : bw;
        int be = isBottom ? 0 : bw;
        int re = isRight ? 0 : bw;

        // t = 2;
        // l = 2;
        // b = 2;
        // r = 2;

        switch( space.status )
        {
            case TILE:
                text = "";
                bg = isLight ? options.palette.tileLight
                    : options.palette.tileDark;
                icon = null;
                break;

            case QUESTION:
                text = "?";
                bg = isLight ? options.palette.tileLight
                    : options.palette.tileDark;
                icon = null;
                break;

            case FLAGGED:
                text = null;
                bg = isLight ? options.palette.tileLight
                    : options.palette.tileDark;
                icon = flag;
                break;

            case REVEALED:
                text = space.numAdj == 0 ? "" : "" + space.numAdj;
                bg = isLight ? options.palette.boardLight
                    : options.palette.boardDark;
                icon = null;
                break;

            default:
                break;
        }

        if( space.isDetonated() )
        {
            text = "B";
            icon = mine;
            bg = Color.red;
        }

        // bdrClr = bg;

        MatteBorder cb = new MatteBorder( tc, lc, bc, rc, bdrClr );
        EmptyBorder eb = new EmptyBorder( te, le, be, re );

        Border border;

        border = new CompoundBorder( cb, eb );

        label.setText( text );
        label.setBackground( bg );
        label.setIcon( icon );
        label.setBorder( border );

        label.invalidate();
        label.repaint();
    }

    /***************************************************************************
     * @{@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return label;
    }
}
