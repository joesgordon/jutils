package org.jutils.ui.hex;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import org.jutils.*;
import org.jutils.io.BitBuffer;
import org.jutils.io.BitPosition;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.model.IView;
import org.jutils.utils.BitArray;

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

        action = new ActionAdapter( new FindAgainListener( this, true ),
            "Find Next", null );
        key = KeyStroke.getKeyStroke( "F3" );
        action.putValue( Action.ACCELERATOR_KEY, key );
        inMap.put( key, "findNextAction" );
        acMap.put( "findNextAction", action );

        action = new ActionAdapter( new FindAgainListener( this, false ),
            "Find Previous", null );
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
        Icon icon;
        KeyStroke key;
        InputMap inMap = panel.getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW );
        ActionMap acMap = panel.getActionMap();

        SwingUtils.setToolbarDefaults( toolbar );

        // ---------------------------------------------------------------------
        // Setup left shift.
        // ---------------------------------------------------------------------
        icon = IconConstants.getIcon( IconConstants.NAV_PREVIOUS_16 );
        action = new ActionAdapter( new ShiftListener( this, -1 ), "Shift Left",
            icon );
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
        action = new ActionAdapter( new ShiftListener( this, 1 ), "Shift Right",
            icon );
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
        action = new ActionAdapter( new FindListener( this ), "Find Bits",
            icon );
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
    public BitPosition getSelectedPostion()
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
    private void resetData()
    {
        leftButton.setEnabled( bitOffset > 0 );
        rightButton.setEnabled( bitOffset < 7 );
        offLabel.setText( "bit: " + bitOffset );

        hexPanel.setBuffer( new ByteBuffer( buffer.buffer ) );
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
     * 
     **************************************************************************/
    private void shitTo()
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

        bits = SwingUtils.showQuestion( parent,
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

            shitTo();

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

            JOptionPane.showMessageDialog( view, msg, "Pattern Not Found",
                JOptionPane.ERROR_MESSAGE );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ShiftListener implements ActionListener
    {
        private final ShiftHexView view;
        private final int dir;

        public ShiftListener( ShiftHexView view, int dir )
        {
            this.view = view;
            this.dir = dir;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            int off = view.bitOffset + dir;

            if( off > -1 && off < 8 )
            {
                view.bitOffset += dir;

                view.shitTo();
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FindAgainListener implements ActionListener
    {
        private final ShiftHexView view;
        private final boolean findForward;

        public FindAgainListener( ShiftHexView view, boolean findForward )
        {
            this.view = view;
            this.findForward = findForward;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            int bitIncrement = findForward ? 1 : -1;

            if( view.lastSearch != null )
            {
                BitPosition pos = view.getSelectedPostion();

                if( pos == null )
                {
                    if( view.lastMatch != null )
                    {
                        pos = new BitPosition( view.lastMatch );
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

                view.find( view.lastSearch, pos, findForward );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FindListener implements ActionListener
    {
        private final ShiftHexView view;

        public FindListener( ShiftHexView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            BitArray bits = promptForBinaryString( view.view, view.lastSearch,
                view.bytesLists );

            if( bits != null && bits.size() > 0 )
            {
                try
                {
                    int start = view.hexPanel.getSelectedByte();

                    start = start > -1 ? start : 0;

                    BitPosition pos = new BitPosition( start, 0 );
                    view.find( bits, pos, true );
                }
                catch( NumberFormatException ex )
                {
                    JOptionPane.showMessageDialog( view.view,
                        "Cannot parse " + bits.toString() +
                            " as a binary string:" + Utils.NEW_LINE +
                            ex.getMessage(),
                        "Parse Error", JOptionPane.ERROR_MESSAGE );
                }
            }
        }
    }
}
