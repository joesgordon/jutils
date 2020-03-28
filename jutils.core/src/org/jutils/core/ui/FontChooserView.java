package org.jutils.core.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;
import javax.swing.border.LineBorder;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.jutils.core.data.FontDescription;
import org.jutils.core.ui.event.ItemActionListener;
import org.jutils.core.ui.model.IDataView;
import org.jutils.core.ui.model.IView;
import org.jutils.core.ui.model.ItemComboBoxModel;

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

    /***************************************************************************
     * @return
     **************************************************************************/
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

    /***************************************************************************
     * @return
     **************************************************************************/
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

    /***************************************************************************
     * @return
     **************************************************************************/
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

    /***************************************************************************
     * @return
     **************************************************************************/
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

    /***************************************************************************
     * @return
     **************************************************************************/
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
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
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
        /**  */
        private final JPanel panel;
        /**  */
        private final JTextPane textPane;

        /**
         * @param text
         */
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

        /**
         * 
         */
        private void centerText()
        {
            MutableAttributeSet standard = new SimpleAttributeSet();
            StyleConstants.setAlignment( standard,
                StyleConstants.ALIGN_CENTER );
            textPane.getStyledDocument().setParagraphAttributes( 0,
                textPane.getStyledDocument().getLength(), standard, true );
        }

        /**
         * @param s
         */
        public void setAttributes( MutableAttributeSet s )
        {
            StyleConstants.setAlignment( s, StyleConstants.ALIGN_CENTER );

            textPane.getStyledDocument().setParagraphAttributes( 0,
                textPane.getStyledDocument().getLength(), s, true );
        }

        /**
         * @{@inheritDoc}
         */
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
        /**  */
        private final JLabel label;
        /**  */
        private final ColorIcon icon;

        /**
         * 
         */
        public ColorComboRenderer()
        {
            this.icon = new ColorIcon( Color.black );
            this.label = new IconLabel( icon );
        }

        /**
         * @{@inheritDoc}
         */
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
        /**  */
        private static final long serialVersionUID = 4915435822380319297L;
        /**  */
        private final ColorIcon icon;

        /**  */
        private static final int ICON_MARGIN = 3;

        /**
         * @param icon
         */
        public IconLabel( ColorIcon icon )
        {
            this.icon = icon;

            setBorder( BorderFactory.createEmptyBorder( ICON_MARGIN,
                ICON_MARGIN, ICON_MARGIN, ICON_MARGIN ) );
            setIcon( icon );
        }

        /**
         * @{@inheritDoc}
         */
        @Override
        public void paint( Graphics g )
        {
            icon.setIconWidth( this.getWidth() - 2 * ICON_MARGIN );
            icon.setIconHeight( this.getHeight() - 2 * ICON_MARGIN );
            super.paint( g );
        }
    }
}
