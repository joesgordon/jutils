package org.jutils.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.*;

import org.jutils.data.FontDescription;
import org.jutils.ui.app.AppRunner;
import org.jutils.ui.app.IApplication;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.model.*;

/*******************************************************************************
 * TODO use auto combo boxes
 ******************************************************************************/
public class FontChooserView implements IDataView<FontDescription>
{
    /**  */
    public final static List<String> FONT_NAMES;
    /**  */
    public final static List<Integer> FONT_SIZES;

    /**  */
    private final JPanel view;
    /**  */
    private final InputListPanel<String> fontNameInputList;
    /**  */
    private final InputListPanel<Integer> fontSizeInputList;
    /**  */
    private final JCheckBox boldCheckBox;
    /**  */
    private final JCheckBox italicCheckBox;
    /**  */
    private final JCheckBox underlineCheckBox;
    /**  */
    private final JCheckBox strikethroughCheckBox;
    /**  */
    private final JCheckBox subscriptCheckBox;
    /**  */
    private final JCheckBox superscriptCheckBox;
    /**  */
    private final ItemComboBoxModel<Color> colorComboModel;
    /**  */
    private final JComboBox<Color> colorComboBox;
    /**  */
    private final FontLabel previewLabel;

    /**  */
    private FontDescription desc;

    static
    {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        FONT_NAMES = Collections.unmodifiableList(
            Arrays.asList( ge.getAvailableFontFamilyNames() ) );
        FONT_SIZES = Collections.unmodifiableList(
            Arrays.asList( new Integer[] { 8, 9, 10, 11, 12, 14, 16, 18, 20, 22,
                24, 26, 28, 36, 48, 72 } ) );
    }

    /***************************************************************************
     * @param owner
     **************************************************************************/
    public FontChooserView()
    {
        this.fontNameInputList = new InputListPanel<>( FONT_NAMES );
        this.fontSizeInputList = new InputListPanel<>( FONT_SIZES );
        this.boldCheckBox = new JCheckBox( "Bold" );
        this.italicCheckBox = new JCheckBox( "Italic" );
        this.underlineCheckBox = new JCheckBox( "Underline" );
        this.strikethroughCheckBox = new JCheckBox( "Strikethrough" );
        this.subscriptCheckBox = new JCheckBox( "Subscript" );
        this.superscriptCheckBox = new JCheckBox( "Superscript" );
        this.colorComboModel = new ItemComboBoxModel<>( buildColors() );
        this.colorComboBox = new JComboBox<>( colorComboModel );
        this.previewLabel = new FontLabel( "Preview Font" );

        setData( new FontDescription() );

        this.view = createView();

    }

    private JPanel createView()
    {
        JPanel contentPanel = new JPanel( new GridBagLayout() );

        contentPanel.add( createFontPanel(),
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );
        contentPanel.add( createEffectsPanel(),
            new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );
        contentPanel.add( createPreviewPanel(),
            new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 2 ) );

        ItemActionListener<String> nameSelectListener = (
            e ) -> updatePreview();

        ItemActionListener<Integer> sizeSelectListener = (
            e ) -> updatePreview();

        fontNameInputList.addSelectionListener( nameSelectListener );
        fontSizeInputList.addSelectionListener( sizeSelectListener );

        ActionListener actionListener = ( e ) -> updatePreview();

        boldCheckBox.addActionListener( actionListener );
        italicCheckBox.addActionListener( actionListener );
        underlineCheckBox.addActionListener( actionListener );
        strikethroughCheckBox.addActionListener( actionListener );
        subscriptCheckBox.addActionListener( actionListener );
        superscriptCheckBox.addActionListener( actionListener );
        colorComboBox.addActionListener( actionListener );

        return contentPanel;
    }

    private Component createEffectsPanel()
    {
        JPanel effectsPanel = new JPanel( new GridBagLayout() );
        effectsPanel.setBorder( BorderFactory.createTitledBorder( "Effects" ) );

        boldCheckBox.setToolTipText( "Bold font" );

        italicCheckBox.setToolTipText( "Italic font" );

        underlineCheckBox.setToolTipText( "Underline font" );

        strikethroughCheckBox.setToolTipText( "Strikethrough font" );

        subscriptCheckBox.setToolTipText( "Subscript font" );

        superscriptCheckBox.setToolTipText( "Superscript font" );

        effectsPanel.add( boldCheckBox,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 0, 4, 2, 2 ), 0, 0 ) );
        effectsPanel.add( italicCheckBox,
            new GridBagConstraints( 1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 0, 2, 2, 2 ), 0, 0 ) );
        effectsPanel.add( underlineCheckBox,
            new GridBagConstraints( 2, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 0, 2, 2, 4 ), 0, 0 ) );

        effectsPanel.add( strikethroughCheckBox,
            new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 2, 4, 2, 2 ), 0, 0 ) );
        effectsPanel.add( subscriptCheckBox,
            new GridBagConstraints( 1, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        effectsPanel.add( superscriptCheckBox,
            new GridBagConstraints( 2, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 2, 2, 2, 4 ), 0, 0 ) );

        effectsPanel.add( createColorPanel(),
            new GridBagConstraints( 0, 2, 3, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 2, 4, 4, 4 ), 0, 0 ) );

        return effectsPanel;
    }

    private Component createColorPanel()
    {
        JPanel colorPanel = new JPanel( new GridBagLayout() );
        JLabel colorLabel = new JLabel( "Color:" );

        colorComboBox.setToolTipText( "Font color" );
        colorComboBox.setRenderer( new ColorComboRenderer() );

        colorPanel.add( colorLabel,
            new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets( 0, 0, 0, 2 ), 0, 0 ) );

        colorPanel.add( colorComboBox,
            new GridBagConstraints( 1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 0, 2, 0, 0 ), 0, 0 ) );

        return colorPanel;
    }

    private Component createPreviewPanel()
    {
        JPanel previewPanel = new JPanel( new GridBagLayout() );
        previewPanel.setBorder( BorderFactory.createTitledBorder( "Preview" ) );

        previewPanel.add( previewLabel.getView(),
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        return previewPanel;
    }

    private Component createFontPanel()
    {
        JPanel fontPanel = new JPanel( new GridBagLayout() );
        fontPanel.setBorder( BorderFactory.createTitledBorder( "Font" ) );

        fontNameInputList.setToolTipText( "Font name" );
        fontSizeInputList.setToolTipText( "Font size" );

        fontPanel.add( fontNameInputList.getView(),
            new GridBagConstraints( 0, 0, 1, 1, 0.5, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 4, 4, 2 ), 0, 0 ) );

        fontPanel.add( fontSizeInputList.getView(),
            new GridBagConstraints( 1, 0, 1, 1, 0.5, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 2, 4, 4 ), 0, 0 ) );

        return fontPanel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public FontDescription getData()
    {
        desc.name = fontNameInputList.getSelected();
        desc.size = fontSizeInputList.getSelected();
        desc.bold = boldCheckBox.isSelected();
        desc.italic = italicCheckBox.isSelected();
        desc.underline = underlineCheckBox.isSelected();
        desc.strikeThrough = strikethroughCheckBox.isSelected();
        desc.subscript = subscriptCheckBox.isSelected();
        desc.superscript = superscriptCheckBox.isSelected();
        desc.color = colorComboModel.getSelectedItem();

        return desc;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( FontDescription data )
    {
        this.desc = data;

        fontNameInputList.setSelected( data.name );
        fontSizeInputList.setSelected( data.size );

        boldCheckBox.setSelected( data.bold );
        italicCheckBox.setSelected( data.italic );
        underlineCheckBox.setSelected( data.underline );
        strikethroughCheckBox.setSelected( data.strikeThrough );
        subscriptCheckBox.setSelected( data.subscript );
        superscriptCheckBox.setSelected( data.superscript );

        colorComboBox.setSelectedItem( data.color );

        updatePreview();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    protected void updatePreview()
    {
        getData();
        SimpleAttributeSet attributes = new SimpleAttributeSet();
        desc.getAttributes( attributes );
        previewLabel.setAttributes( attributes );
        // LogUtils.printDebug( "attr: " + attributes );
    }

    /***************************************************************************
     * @param argv
     **************************************************************************/
    public static void main( String argv[] )
    {
        IApplication app = new IApplication()
        {
            @Override
            public void createAndShowUi()
            {
                FontChooserView chooser = new FontChooserView();
                OkDialogView dialogView = new OkDialogView( null,
                    chooser.getView() );
                JDialog dlg = dialogView.getView();
                dlg.addWindowListener( new WindowAdapter()
                {
                    @Override
                    public void windowClosing( WindowEvent e )
                    {
                        System.exit( 0 );
                    }
                } );
                dlg.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
                dlg.pack();
                dlg.setLocationByPlatform( true );
                dlg.setVisible( true );
            }

            @Override
            public String getLookAndFeelName()
            {
                return null;
            }
        };
        AppRunner.invokeLater( app );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static List<Color> buildColors()
    {
        int [] values = new int[] { 0, 128, 192, 255 };
        List<Color> colors = new ArrayList<>(
            values.length * values.length * values.length );
        for( int r = 0; r < values.length; r++ )
        {
            for( int g = 0; g < values.length; g++ )
            {
                for( int b = 0; b < values.length; b++ )
                {
                    Color c = new Color( values[r], values[g], values[b] );
                    colors.add( c );
                }
            }
        }

        return colors;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FontLabel implements IView<JPanel>
    {
        private final JPanel panel;
        private final JTextPane textPane;

        public FontLabel( String text )
        {
            this.panel = new JPanel( new GridBagLayout() );
            textPane = new JTextPane();

            textPane.setEditable( false );
            textPane.setBorder( null );
            textPane.setOpaque( false );
            textPane.setText( text );
            textPane.setFocusable( false );

            panel.setBackground( Color.white );
            panel.setForeground( Color.black );
            panel.setOpaque( true );
            panel.setBorder( new LineBorder( Color.black ) );
            panel.setMinimumSize( new Dimension( 120, 80 ) );
            panel.setPreferredSize( new Dimension( 120, 80 ) );
            panel.add( textPane,
                new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets( 0, 0, 0, 0 ), 0, 0 ) );

            centerText();
        }

        private void centerText()
        {
            MutableAttributeSet standard = new SimpleAttributeSet();
            StyleConstants.setAlignment( standard,
                StyleConstants.ALIGN_CENTER );
            textPane.getStyledDocument().setParagraphAttributes( 0,
                textPane.getStyledDocument().getLength(), standard, true );
        }

        public void setAttributes( MutableAttributeSet s )
        {
            StyleConstants.setAlignment( s, StyleConstants.ALIGN_CENTER );

            textPane.getStyledDocument().setParagraphAttributes( 0,
                textPane.getStyledDocument().getLength(), s, true );
        }

        @Override
        public JPanel getView()
        {
            return panel;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ColorComboRenderer implements ListCellRenderer<Color>
    {
        private final JLabel label;
        private final ColorIcon icon;

        public ColorComboRenderer()
        {
            this.icon = new ColorIcon( Color.black );
            this.label = new IconLabel( icon );
        }

        @Override
        public Component getListCellRendererComponent(
            JList<? extends Color> list, Color value, int index,
            boolean isSelected, boolean cellHasFocus )
        {
            icon.setColor( value );

            return label;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class IconLabel extends JLabel
    {
        private static final long serialVersionUID = 4915435822380319297L;
        private final ColorIcon icon;

        private static final int ICON_MARGIN = 3;

        public IconLabel( ColorIcon icon )
        {
            this.icon = icon;

            setBorder( BorderFactory.createEmptyBorder( ICON_MARGIN,
                ICON_MARGIN, ICON_MARGIN, ICON_MARGIN ) );
            setIcon( icon );
        }

        @Override
        public void paint( Graphics g )
        {
            icon.setIconWidth( this.getWidth() - 2 * ICON_MARGIN );
            icon.setIconHeight( this.getHeight() - 2 * ICON_MARGIN );
            super.paint( g );
        }
    }
}
