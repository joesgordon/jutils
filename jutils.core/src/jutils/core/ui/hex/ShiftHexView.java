package jutils.core.ui.hex;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import jutils.core.IconConstants;
import jutils.core.OptionUtils;
import jutils.core.SwingUtils;
import jutils.core.Utils;
import jutils.core.io.BitBuffer;
import jutils.core.io.BitPosition;
import jutils.core.ui.event.ActionAdapter;
import jutils.core.ui.model.IView;
import jutils.core.utils.BitArray;

/*******************************************************************************
 * Displays an array of bytes and allows the user to shift the bytes from an
 * initial starting bit offset of 0 to 7 and back again.
 ******************************************************************************/
public class ShiftHexView implements IView<JComponent>
{
    /**  */
    private final JPanel view;
    /**  */
    private final HexPanel hexPanel;
    /**  */
    private final JButton leftButton;
    /**  */
    private final JButton rightButton;
    /**  */
    private final JLabel offLabel;

    /**  */
    private BitBuffer orig;
    /**  */
    private BitBuffer buffer;

    /**  */
    private BitArray lastSearch;
    /**  */
    private BitPosition lastMatch;
    /** The number of bytes the buffer is currently shifted. */
    private int bitOffset;

    /**  */
    private List<byte []> bytesLists;

    /***************************************************************************
     * 
     **************************************************************************/
    public ShiftHexView()
    {
        this.hexPanel = new HexPanel();
        this.leftButton = new JButton();
        this.rightButton = new JButton();
        this.offLabel = new JLabel();
        this.bytesLists = new ArrayList<>();

        this.view = createView();

        this.bitOffset = 0;

        setData( new byte[] { 0 } );

        // ---------------------------------------------------------------------
        // Setup keystrokes.
        // ---------------------------------------------------------------------
        KeyStroke key;
        InputMap inMap = view.getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW );
        ActionMap acMap = view.getActionMap();
        Action action;
        ActionListener listener;

        listener = ( e ) -> handleFindAgain( true );
        action = new ActionAdapter( listener, "Find Next", null );
        key = KeyStroke.getKeyStroke( "F3" );
        action.putValue( Action.ACCELERATOR_KEY, key );
        inMap.put( key, "findNextAction" );
        acMap.put( "findNextAction", action );

        listener = ( e ) -> handleFindAgain( false );
        action = new ActionAdapter( listener, "Find Previous", null );
        key = KeyStroke.getKeyStroke( "shift F3" );
        action.putValue( Action.ACCELERATOR_KEY, key );
        inMap.put( key, "findPreviousAction" );
        acMap.put( "findPreviousAction", action );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( createButtons( panel ), BorderLayout.NORTH );
        panel.add( hexPanel.getView(), BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createButtons( JPanel panel )
    {
        JToolBar toolbar = new JToolBar();
        Action action;
        ActionListener listener;
        Icon icon;
        KeyStroke key;
        InputMap inMap = panel.getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW );
        ActionMap acMap = panel.getActionMap();

        SwingUtils.setToolbarDefaults( toolbar );

        // ---------------------------------------------------------------------
        // Setup left shift.
        // ---------------------------------------------------------------------
        icon = IconConstants.getIcon( IconConstants.NAV_PREVIOUS_16 );
        listener = ( e ) -> handleShift( -1 );
        action = new ActionAdapter( listener, "Shift Left", icon );
        SwingUtils.addActionToToolbar( toolbar, action, leftButton );

        key = KeyStroke.getKeyStroke( KeyEvent.VK_LEFT,
            InputEvent.ALT_DOWN_MASK, false );
        action.putValue( Action.ACCELERATOR_KEY, key );
        inMap.put( key, "prevAction" );
        acMap.put( "prevAction", action );

        // ---------------------------------------------------------------------
        // Setup right shift.
        // ---------------------------------------------------------------------
        icon = IconConstants.getIcon( IconConstants.NAV_NEXT_16 );
        listener = ( e ) -> handleShift( 1 );
        action = new ActionAdapter( listener, "Shift Right", icon );
        SwingUtils.addActionToToolbar( toolbar, action, rightButton );

        key = KeyStroke.getKeyStroke( KeyEvent.VK_RIGHT,
            InputEvent.ALT_DOWN_MASK, false );
        action.putValue( Action.ACCELERATOR_KEY, key );
        inMap.put( key, "nextAction" );
        acMap.put( "nextAction", action );

        // ---------------------------------------------------------------------
        // Setup find.
        // ---------------------------------------------------------------------
        icon = IconConstants.getIcon( IconConstants.FIND_16 );
        listener = ( e ) -> handleFind();
        action = new ActionAdapter( listener, "Find Bits", icon );
        SwingUtils.addActionToToolbar( toolbar, action );

        key = KeyStroke.getKeyStroke( "control F" );
        action.putValue( Action.ACCELERATOR_KEY, key );
        inMap.put( key, "findAction" );
        acMap.put( "findAction", action );
        // action.putValue( Action.MNEMONIC_KEY, ( int )'n' );

        // key = KeyStroke.getKeyStroke( "F3" );
        // action.putValue( Action.ACCELERATOR_KEY, key );
        // inMap.put( key, "findNextAction" );
        // acMap.put( "findNextAction", action );
        //
        // key = KeyStroke.getKeyStroke( "shift F3" );
        // action.putValue( Action.ACCELERATOR_KEY, key );
        // inMap.put( key, "findPrevAction" );
        // acMap.put( "findPrevAction", action );

        toolbar.addSeparator();

        toolbar.add( offLabel );

        return toolbar;
    }

    private void handleFind()
    {
        BitArray bits = promptForBinaryString( view, lastSearch, bytesLists );

        if( bits != null && bits.size() > 0 )
        {
            try
            {
                int start = hexPanel.getSelectedByte();

                start = start > -1 ? start : 0;

                BitPosition pos = new BitPosition( start, 0 );
                find( bits, pos, true );
            }
            catch( NumberFormatException ex )
            {
                OptionUtils.showErrorMessage( view,
                    "Cannot parse " + bits.toString() + " as a binary string:" +
                        Utils.NEW_LINE + ex.getMessage(),
                    "Parse Error" );
            }
        }
    }

    /***************************************************************************
     * @param findForward
     **************************************************************************/
    private void handleFindAgain( boolean findForward )
    {
        int bitIncrement = findForward ? 1 : -1;

        if( lastSearch != null )
        {
            BitPosition pos = getSelectedPosition();

            if( pos == null )
            {
                if( lastMatch != null )
                {
                    pos = new BitPosition( lastMatch );
                    pos.increment( bitIncrement );
                }
                else
                {
                    pos = new BitPosition();
                }
            }
            else
            {
                pos.increment( bitIncrement );
            }

            find( lastSearch, pos, findForward );
        }

    }

    /***************************************************************************
     * @param dir
     **************************************************************************/
    private void handleShift( int dir )
    {
        int off = bitOffset + dir;

        if( off > -1 && off < 8 )
        {
            bitOffset += dir;

            shiftDataToOffset();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void shiftDataToOffset()
    {
        int bitIndex = bitOffset % 8;
        int byteIndex = bitOffset / 8;

        buffer.buffer[0] = 0;
        buffer.buffer[buffer.buffer.length - 1] = 0;

        orig.setPosition( 0, 0 );
        buffer.setPosition( byteIndex, bitIndex );
        orig.writeTo( buffer, orig.bitCount() );

        resetData();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void resetData()
    {
        leftButton.setEnabled( bitOffset > 0 );
        rightButton.setEnabled( bitOffset < 7 );
        offLabel.setText( "bit: " + bitOffset );

        hexPanel.setBuffer( new ByteBuffer( buffer.buffer ) );
    }

    /***************************************************************************
     * @param bytes
     **************************************************************************/
    public void setData( byte [] bytes )
    {
        this.orig = new BitBuffer( bytes );
        this.buffer = new BitBuffer( Arrays.copyOf( bytes, bytes.length + 1 ) );
        this.bitOffset = 0;

        resetData();
    }

    /***************************************************************************
     * @param bytesLists
     **************************************************************************/
    public void setListOfBytes( List<byte []> bytesLists )
    {
        this.bytesLists.clear();
        this.bytesLists.addAll( bytesLists );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public BitPosition getSelectedPosition()
    {
        BitPosition pos = null;
        int selectedByte = hexPanel.getSelectedByte();

        if( selectedByte > -1 )
        {
            int bitIndex = ( 8 - bitOffset ) % 8;
            int byteIndex = selectedByte - ( bitIndex > 0 ? 1 : 0 );

            pos = new BitPosition( byteIndex, bitIndex );
        }

        return pos;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * @param parent
     * @param lastSearch2
     * @return
     **************************************************************************/
    public static BitArray promptForBinaryString( Component parent,
        BitArray lastSearch, List<byte []> bytesLists )
    {
        BitArrayView bitsView = new BitArrayView( bytesLists );
        String search = lastSearch == null ? "0000" : lastSearch.toString();
        BitArray bits = new BitArray();

        bits.set( search );

        bitsView.setData( bits );

        bits = OptionUtils.showQuestionView( parent,
            "Enter binary string to be found:", "Enter Search String",
            bitsView );

        return bits;
    }

    /***************************************************************************
     * @param bitsToFind
     * @param start
     * @param findForward
     **************************************************************************/
    private void find( BitArray bitsToFind, BitPosition start,
        boolean findForward )
    {
        BitPosition pos = orig.find( bitsToFind, start, findForward );

        this.lastSearch = bitsToFind;

        if( pos != null )
        {
            bitOffset = ( 8 - pos.getBit() ) % 8;

            shiftDataToOffset();

            resetData();

            int off = pos.getByte();

            off += pos.getBit() == 0 ? 0 : 1;

            hexPanel.setSelected( off, off );

            this.lastMatch = pos;
        }
        else
        {
            String begStr = findForward ? start.toString() : "the beginning";
            String endStr = findForward ? "the end" : start.toString();

            String msg = "Pattern not found between " + begStr + " and " +
                endStr;

            OptionUtils.showErrorMessage( view, msg, "Pattern Not Found" );
        }
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    public void setEditable( boolean editable )
    {
        hexPanel.setEditable( editable );
    }
}
