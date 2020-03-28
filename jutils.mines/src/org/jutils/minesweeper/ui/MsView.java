package org.jutils.minesweeper.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import org.jutils.core.IconConstants;
import org.jutils.core.SwingUtils;
import org.jutils.core.ui.event.updater.IUpdater;
import org.jutils.core.ui.fields.ComboFormField;
import org.jutils.core.ui.fields.NamedItemDescriptor;
import org.jutils.core.ui.model.IView;
import org.jutils.minesweeper.data.Difficulty;
import org.jutils.minesweeper.data.GameOptions;
import org.jutils.minesweeper.data.GameStatus;
import org.jutils.minesweeper.data.GameStatus.GameResult;
import org.jutils.minesweeper.data.GridSpace;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MsView implements IView<JComponent>
{
    /**  */
    private final JComponent view;
    /**  */
    private final ComboFormField<Difficulty> difficultyField;
    /**  */
    private final JLabel flagField;
    /**  */
    private final JLabel timeField;
    /**  */
    private final JPanel grid;

    /**  */
    private final GameStatus status;
    /**  */
    private final List<GridSpaceView> spaces;
    /**  */
    private final Timer gameTimer;

    /***************************************************************************
     * 
     **************************************************************************/
    public MsView()
    {
        this.difficultyField = new ComboFormField<Difficulty>( "Difficulty",
            Difficulty.values(), new NamedItemDescriptor<>() );
        this.flagField = new JLabel();
        this.timeField = new JLabel();
        this.grid = new JPanel( new GridBagLayout() );
        this.status = new GameStatus();
        this.spaces = new ArrayList<>();
        this.gameTimer = new Timer( 99999999,
            ( e ) -> setTime( status.getGameTime() ) );

        gameTimer.stop();
        gameTimer.setInitialDelay( 100 );
        gameTimer.setDelay( 100 );

        flagField.setFont(
            flagField.getFont().deriveFont( 18.0f ).deriveFont( Font.BOLD ) );
        flagField.setText( "" + Difficulty.EASY.numFlags );
        flagField.setVerticalAlignment( SwingConstants.CENTER );

        flagField.setBorder( new EmptyBorder( -4 /* top */, 0, 0, 0 ) );

        timeField.setFont(
            flagField.getFont().deriveFont( 18.0f ).deriveFont( Font.BOLD ) );
        timeField.setVerticalAlignment( SwingConstants.CENTER );

        timeField.setBorder( new EmptyBorder( -4 /* top */, 0, 0, 0 ) );

        difficultyField.setValue( Difficulty.EASY );

        resetBoard();

        this.view = createView();

        difficultyField.setUpdater( ( d ) -> resetBoard() );
    }

    /***************************************************************************
     * @param time
     **************************************************************************/
    private void setTime( long time )
    {
        timeField.setText( String.format( "%03d", time ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void resetBoard()
    {
        Difficulty difficulty = difficultyField.getValue();

        flagField.setText( "" + difficulty.numFlags );
        timeField.setText( "000" );

        status.generateSpaces( difficulty );
        // status.layoutMines();
        drawGrid();

        JFrame frame = SwingUtils.getComponentsJFrame( getView() );

        if( frame != null )
        {
            frame.pack();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void drawGrid()
    {
        Difficulty difficulty = status.difficulty;
        int size = difficulty.size;

        spaces.clear();
        grid.removeAll();

        for( int c = 0; c < size; c++ )
        {
            for( int r = 0; r < size; r++ )
            {
                int index = r * size + c;
                boolean isBottom = r == ( size - 1 );
                boolean isRight = c == ( size - 1 );

                GridSpace space = status.spaces.get( index );
                GridBagConstraints constraints;
                GridSpaceView sview = new GridSpaceView( space, status.options,
                    isBottom, isRight );

                LabelMouseListener ml = new LabelMouseListener(
                    ( b ) -> handleClick( space, b ) );

                sview.getView().addMouseListener( ml );

                constraints = new GridBagConstraints( c, r, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE,
                    new Insets( 0, 0, 0, 0 ), 0, 0 );
                grid.add( sview.getView(), constraints );

                spaces.add( sview );
            }
        }

        grid.invalidate();
        grid.repaint();
    }

    /***************************************************************************
     * @param space
     * @param isLeft
     **************************************************************************/
    private void handleClick( GridSpace space, boolean isLeft )
    {
        if( isLeft )
        {
            if( !status.started )
            {
                status.startGame( space.index );

                gameTimer.start();
            }

            GameResult result = status.reveal( space );

            if( result != GameResult.PLAYING )
            {
                gameTimer.stop();
                setTime( status.getGameTime() );

                if( result == GameResult.LOST )
                {
                    status.revealAll();
                }
            }
        }
        else
        {
            status.toggleFlag( space );
            flagField.setText( "" + status.flagsLeft );
        }

        for( GridSpaceView gsv : spaces )
        {
            gsv.repaint();
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        grid.setBackground( status.options.palette.form );
        grid.setBorder( new EmptyBorder( 0, 10, 10, 10 ) );

        panel.add( createControls(), BorderLayout.NORTH );
        panel.add( grid, BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createControls()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        panel.setBackground( status.options.palette.form );

        int x = 0;

        // difficultyField.getView().setBackground( FORM_BG );

        constraints = new GridBagConstraints( x++, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 8, 8, 8, 0 ), 0, 0 );
        panel.add( difficultyField.getView(), constraints );

        JButton newButton = new JButton(
            IconConstants.getIcon( IconConstants.NEW_FILE_16 ) );

        newButton.addActionListener( ( e ) -> resetBoard() );

        constraints = new GridBagConstraints( x++, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 8, 8, 8, 0 ), 0, 0 );
        panel.add( newButton, constraints );

        // constraints = new GridBagConstraints( x++, 0, 1, 1, 0.25, 0.0,
        // GridBagConstraints.WEST, GridBagConstraints.NONE,
        // new Insets( 8, 6, 8, 0 ), 0, 0 );
        // panel.add( Box.createHorizontalStrut( 0 ), constraints );

        constraints = new GridBagConstraints( x++, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 8, 60, 8, 0 ), 0, 0 );
        panel.add( new JLabel( new FlagIcon( 22 ) ), constraints );

        constraints = new GridBagConstraints( x++, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 8, 6, 8, 0 ), 0, 0 );
        panel.add( flagField, constraints );

        constraints = new GridBagConstraints( x++, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 8, 10, 8, 0 ), 0, 0 );
        panel.add(
            new JLabel( IconConstants.getIcon( IconConstants.CLOCK_24 ) ),
            constraints );

        constraints = new GridBagConstraints( x++, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 8, 6, 8, 0 ), 0, 0 );
        panel.add( timeField, constraints );

        constraints = new GridBagConstraints( x++, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 8, 6, 8, 0 ), 0, 0 );
        panel.add( Box.createHorizontalStrut( 0 ), constraints );

        return panel;
    }

    /***************************************************************************
     * @{@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public GameOptions getOptions()
    {
        return status.options;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class LabelMouseListener extends MouseAdapter
    {
        /**  */
        private final IUpdater<Boolean> clickCallback;

        /**
         * @param clickCallback
         */
        public LabelMouseListener( IUpdater<Boolean> clickCallback )
        {
            this.clickCallback = clickCallback;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseReleased( MouseEvent e )
        {
            clickCallback.update( SwingUtilities.isLeftMouseButton( e ) );
        }
    }
}
