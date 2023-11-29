package jutils.core.laf;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map.Entry;

import javax.swing.UIDefaults;
import javax.swing.UIDefaults.LazyValue;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;

import jutils.core.io.LogUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SimpleLookAndFeel extends MetalLookAndFeel
{
    /**  */
    private static final long serialVersionUID = 5597691713123621285L;

    /**  */

    public final SimpleLafColors colors;

    /***************************************************************************
     * 
     **************************************************************************/
    public SimpleLookAndFeel()
    {
        this( new SimpleLafColors() );
    }

    /***************************************************************************
     * Initializes this look-and-feel with the provided colors.
     * @param colors the colors of the components to be used for this
     * look-and-feel instance.
     **************************************************************************/
    public SimpleLookAndFeel( SimpleLafColors colors )
    {
        super();

        this.colors = colors;
    }

    /***************************************************************************
     * @{@inheritDoc}
     **************************************************************************/
    @Override
    protected void initClassDefaults( UIDefaults table )
    {
        super.initClassDefaults( table );

        table.put( "TabbedPaneUI", SimpleTabbedPaneUI.class.getName() );
        table.put( "ToolBarUI", SimpleToolBarUI.class.getName() );
        table.put( "ScrollBarUI", SimpleScrollBarUI.class.getCanonicalName() );
    }

    /***************************************************************************
     * @{@inheritDoc}
     **************************************************************************/
    @Override
    protected void initComponentDefaults( UIDefaults table )
    {
        super.initComponentDefaults( table );

        // setColors( table );
    }

    /***************************************************************************
     * @{@inheritDoc}
     **************************************************************************/
    @Override
    public UIDefaults getDefaults()
    {
        UIDefaults defaults = super.getDefaults();

        this.setDefaults( defaults );

        return defaults;
    }

    /***************************************************************************
     * @param table default properties for UI components.
     **************************************************************************/
    private void setDefaults( UIDefaults table )
    {
        setButtonDefaults( table );
        setCheckboxDefaults( table );
        setLabelDefaults( table );
        setListDefaults( table );
        setMenuDefaults( table );
        setMenubarDefaults( table );
        setMenuitemDefaults( table );
        setOptionPaneDefaults( table );
        setPanelDefaults( table );
        setPopupMenuDefaults( table );
        setProgressbarDefaults( table );
        setRadioButtonDefaults( table );
        setSeparatorDefaults( table );
        setSliderDefaults( table );
        setScrollpaneDefaults( table );
        setTableDefaults( table );
        setTabsDefaults( table );
        setTextAreaDefaults( table );
        setTextfieldDefaults( table );
        setTitledBorderDefaults( table );
        setToolbarDefaults( table );
        setToggleButtonDefaults( table );
        setCommonDefaults( table );

        // String keyTerm = "";
        // Class<?> valTerm = null;

        // keyTerm = "panel";
        // valTerm = Color.class;
        // valTerm = Insets.class;

        // printTable( table, keyTerm, valTerm );
    }

    /***************************************************************************
     * @param table default properties for UI components.
     **************************************************************************/
    private void setButtonDefaults( UIDefaults table )
    {
        table.put( "Button.background", colors.background );
        table.put( "Button.border", new SimpleButtonBorder() );
        table.put( "Button.foreground", colors.foreground );
    }

    /***************************************************************************
     * @param table default properties for UI components.
     **************************************************************************/
    private void setCheckboxDefaults( UIDefaults table )
    {
        table.put( "CheckBox.background", colors.background );
        table.put( "CheckBox.foreground", colors.foreground );
    }

    /***************************************************************************
     * @param table default properties for UI components.
     **************************************************************************/
    private void setLabelDefaults( UIDefaults table )
    {
        table.put( "Label.background", colors.background );
        table.put( "Label.foreground", colors.foreground );
    }

    /***************************************************************************
     * @param table default properties for UI components.
     **************************************************************************/
    private void setListDefaults( UIDefaults table )
    {
        table.put( "List.background", colors.controlBackground );
        table.put( "List.foreground", colors.foreground );
        table.put( "List.selectionBackground", colors.bright );
        table.put( "List.selectionForeground", colors.foreground );
    }

    /***************************************************************************
     * @param table default properties for UI components.
     **************************************************************************/
    private void setMenuDefaults( UIDefaults table )
    {
        table.put( "Menu.background", colors.background );
        table.put( "Menu.border", new EmptyBorder( 2, 2, 2, 2 ) );
        table.put( "Menu.foreground", colors.foreground );
        table.put( "Menu.selectionBackground", colors.bright );
        table.put( "Menu.selectionForeground", colors.foreground );
    }

    /***************************************************************************
     * @param table default properties for UI components.
     **************************************************************************/
    private void setMenubarDefaults( UIDefaults table )
    {
        table.put( "MenuBar.background", colors.background );
        table.put( "MenuBar.foreground", colors.foreground );
        table.put( "MenuBar.border",
            new MatteBorder( 0, 0, 1, 0, colors.shadow ) );
    }

    /***************************************************************************
     * @param table default properties for UI components.
     **************************************************************************/
    private void setMenuitemDefaults( UIDefaults table )
    {
        table.put( "MenuItem.acceleratorForeground", colors.foreground );
        table.put( "MenuItem.background", colors.background );
        table.put( "MenuItem.border", new EmptyBorder( 2, 2, 2, 2 ) );
        table.put( "MenuItem.foreground", colors.foreground );
        table.put( "MenuItem.selectionBackground", colors.bright );
        table.put( "MenuItem.selectionForeground", colors.foreground );
    }

    /***************************************************************************
     * @param table default properties for UI components.
     **************************************************************************/
    private void setOptionPaneDefaults( UIDefaults table )
    {
        table.put( "OptionPane.background", colors.background );
        table.put( "OptionPane.foreground", colors.foreground );
        table.put( "OptionPane.messageForeground", colors.foreground );
    }

    /***************************************************************************
     * @param table default properties for UI components.
     **************************************************************************/
    private void setPanelDefaults( UIDefaults table )
    {
        table.put( "Panel.background", colors.background );
    }

    /***************************************************************************
     * @param table default properties for UI components.
     **************************************************************************/
    private void setPopupMenuDefaults( UIDefaults table )
    {
        table.put( "PopupMenu.background", colors.background );
        table.put( "PopupMenu.border", new LineBorder( colors.shadow ) );
    }

    /***************************************************************************
     * @param table default properties for UI components.
     **************************************************************************/
    private void setProgressbarDefaults( UIDefaults table )
    {
        table.put( "ProgressBar.background", colors.controlBackground );
        table.put( "ProgressBar.foreground", colors.controlHighlight );
        table.put( "ProgressBar.selectionBackground", colors.bright );
        table.put( "ProgressBar.selectionBackground", colors.foreground );
    }

    /***************************************************************************
     * @param table default properties for UI components.
     **************************************************************************/
    private void setRadioButtonDefaults( UIDefaults table )
    {
        table.put( "RadioButton.foreground", colors.foreground );
        table.put( "RadioButton.background", colors.background );
        table.put( "RadioButton.highlight", colors.controlHighlight );
        table.put( "RadioButton.select", colors.controlHighlight );
        table.put( "RadioButton.inactive", colors.controlBackground );
        table.put( "RadioButton.margin", new InsetsUIResource( 0, 0, 0, 0 ) );
        table.put( "RadioButton.icon",
            ( LazyValue )t -> new SimpleRadioIcon() );
    }

    /***************************************************************************
     * @param table default properties for UI components.
     **************************************************************************/
    private void setSeparatorDefaults( UIDefaults table )
    {
        table.put( "Separator.background", colors.shadow );
        table.put( "Separator.foreground", colors.controlBackground );
    }

    /***************************************************************************
     * @param table default properties for UI components.
     **************************************************************************/
    private void setSliderDefaults( UIDefaults table )
    {
        table.put( "Slider.foreground", colors.control );
    }

    /***************************************************************************
     * @param table default properties for UI components.
     **************************************************************************/
    private void setScrollpaneDefaults( UIDefaults table )
    {
        table.put( "ScrollBar.track", colors.background );
        table.put( "ScrollBar.trackHighlight", colors.highlight );

        table.put( "ScrollBar.thumb", colors.background );
        table.put( "ScrollBar.thumbDarkShadow", colors.shadow );
        table.put( "ScrollBar.thumbHighlight", colors.foreground );
        table.put( "ScrollBar.thumbShadow", colors.shadow );

        table.put( "ScrollPane.background", colors.background );

        table.put( "Viewport.background", colors.background );
    }

    /***************************************************************************
     * @param table default properties for UI components.
     **************************************************************************/
    private void setTabsDefaults( UIDefaults table )
    {
        table.put( "TabbedPane.background", colors.background );
        table.put( "TabbedPane.foreground", colors.foreground );
        // table.put( "TabbedPane.light", Color.red );
        table.put( "TabbedPane.selected", colors.bright );
        // table.put( "TabbedPane.tabAreaBackground", Color.red );
        // table.put( "TabbedPane.selectHighlight", Color.red );
        table.put( "TabbedPane.shadow", colors.shadow );
        table.put( "TabbedPane.darkShadow", colors.shadow );
        // table.put( "TabbedPane.borderHightlightColor", Color.red );
    }

    /***************************************************************************
     * @param table default properties for UI components.
     **************************************************************************/
    private void setTableDefaults( UIDefaults table )
    {
        table.put( "Table.background", colors.background );
        table.put( "Table.gridColor", colors.shadow );
        table.put( "Table.foreground", colors.foreground );
        // table.put( "Table.gridColor", lightBg );
        table.put( "Table.selectionBackground", colors.bright );
        table.put( "Table.selectionForeground", colors.foreground );

        table.put( "TableHeader.background", colors.background );
        table.put( "TableHeader.foreground", colors.foreground );
        table.put( "TableHeader.cellBorder",
            ( LazyValue )t -> new TableHeaderBorder() );
    }

    /***************************************************************************
     * @param table default properties for UI components.
     **************************************************************************/
    private void setTextAreaDefaults( UIDefaults table )
    {
        table.put( "TextArea.background", colors.controlBackground );
        table.put( "TextArea.caretForeground", colors.foreground );
        table.put( "TextArea.foreground", colors.foreground );
        table.put( "TextArea.inactiveForeground", colors.background );
        table.put( "TextArea.selectionBackground", colors.bright );
        table.put( "TextArea.selectionForeground", colors.foreground );
    }

    /***************************************************************************
     * @param table default properties for UI components.
     **************************************************************************/
    private void setTextfieldDefaults( UIDefaults table )
    {
        table.put( "TextField.background", colors.controlBackground );
        table.put( "TextField.caretForeground", colors.foreground );
        table.put( "TextField.foreground", colors.foreground );
        table.put( "TextField.inactiveForeground", colors.background );
        table.put( "TextField.selectionBackground", colors.bright );
        table.put( "TextField.selectionForeground", colors.foreground );
    }

    /***************************************************************************
     * @param table default properties for UI components.
     **************************************************************************/
    private void setTitledBorderDefaults( UIDefaults table )
    {
        table.put( "TitledBorder.titleColor", colors.foreground );
    }

    /***************************************************************************
     * @param table default properties for UI components.
     **************************************************************************/
    private void setToolbarDefaults( UIDefaults table )
    {
        table.put( "ToolBar.background", colors.background );
        // table.put( "ToolBar.nonrolloverBorder",
        // ( LazyValue )t -> new SimpleButtonBorder() );
        // table.put( "ToolBar.rolloverBorder",
        // ( LazyValue )t -> new SimpleButtonBorder() );

        table.put( "ToolBarUI", SimpleToolBarUI.class.getCanonicalName() );
    }

    /***************************************************************************
     * @param table default properties for UI components.
     **************************************************************************/
    private void setToggleButtonDefaults( UIDefaults table )
    {
        table.put( "ToggleButton.background", colors.background );
        table.put( "ToggleButton.select", colors.controlHighlight );
        table.put( "ToggleButton.border", table.get( "Button.border" ) );
        table.remove( "ToggleButton.gradient" );
    }

    /***************************************************************************
     * @param table default properties for UI components.
     **************************************************************************/
    private void setCommonDefaults( UIDefaults table )
    {
        table.put( "control", colors.background );
        // table.put( "controlDkShadow", colors.controlHighlight );
        table.put( "controlShadow", colors.controlHighlight );
        table.put( "window", Color.RED );

        // Color c = Color.red;
        //
        // table.put( "activeCaptionBorder", c );
        // table.put( "desktop", c );
        // table.put( "Button.focus", c );
        // table.put( "CheckBox.focus", c );
        // table.put( "Desktop.background", c );
        // table.put( "ProgressBar.foreground", c );
        // table.put( "RadioButton.focus", c );
        // table.put( "ScrollBar.thumb", c );
        // table.put( "Slider.focus", c );
        // table.put( "TabbedPane.focus", c );
        // table.put( "Slider.foreground", c );
        // table.put( "Table.dropLineColor", c );
        // table.put( "ToggleButton.focus", c );
        // table.put( "Tree.selectionBorderColor", c );
    }

    /***************************************************************************
     * @param table table to be printed.
     * @param keyTerm text to look for in the key or {@code null}.
     * @param valTerm type of value to look for or {@code null}.
     **************************************************************************/
    static void printTable( UIDefaults table, String keyTerm, Class<?> valTerm )
    {
        ArrayList<Entry<Object, Object>> entries = new ArrayList<>(
            table.entrySet() );

        Collections.sort( entries, ( e1, e2 ) -> {
            return e1.getKey().toString().compareTo( e2.getKey().toString() );
        } );

        keyTerm = keyTerm == null ? null : keyTerm.toLowerCase();

        for( Entry<Object, Object> entry : entries )
        {
            Object val = entry.getValue();
            String key = entry.getKey().toString();
            String valStr = "\"null\"";
            String valClassStr = "\"null\"";

            boolean print = true;

            if( val != null )
            {
                Class<?> valClass = val.getClass();
                valClassStr = valClass.toString();

                if( Color.class.isAssignableFrom( valClass ) )
                {
                    Color c = ( Color )val;
                    valStr = String.format( "0x%06X", 0x00FFFFFF & c.getRGB() );
                }
                else
                {
                    valStr = val.toString();
                }

                if( valTerm != null && !valTerm.isAssignableFrom( valClass ) )
                {
                    print = false;
                }
            }
            else if( valTerm != null )
            {
                print = false;
            }

            if( keyTerm != null && !key.toLowerCase().contains( keyTerm ) )
            {
                print = false;
            }

            if( print )
            {
                LogUtils.printInfo( "%s -> %s (%s)", key, valStr, valClassStr );
            }
        }

        // Utils.printStackTrace();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class SimpleLafColors
    {
        /**  */
        public Color background;
        /**  */
        public Color controlBackground;
        /**  */
        public Color controlHighlight;
        /**  */
        public Color foreground;
        /**  */
        public Color control;
        /**  */
        public Color shadow;
        /**  */
        public Color highlight;
        /**  */
        public Color bright;

        /**
         * 
         */
        public SimpleLafColors()
        {
            this.background = new Color( 0x444444 );
            this.controlBackground = new Color( 0x0C2D40 );
            this.controlHighlight = new Color( 0x46A5DC );
            this.foreground = new Color( 0xF0F0F0 );
            this.control = new Color( 0x204060 );
            this.shadow = new Color( 0x111A1F );
            this.highlight = new Color( 0x7A7A7A );
            this.bright = new Color( 0x0579C8 );
        }
    }

    /**
     *
     */
    @SuppressWarnings( "serial")
    public class TableHeaderBorder extends javax.swing.border.AbstractBorder
    {
        /**
         * The border insets.
         */
        protected Insets editorBorderInsets = new Insets( 2, 2, 2, 0 );

        /**
         * {@inheritDoc}
         */
        @Override
        public void paintBorder( Component c, Graphics g, int x, int y, int w,
            int h )
        {
            g.translate( x, y );

            g.setColor( colors.shadow );
            g.drawLine( w - 1, 0, w - 1, h - 1 );
            g.drawLine( 1, h - 1, w - 1, h - 1 );
            g.setColor( colors.highlight );
            g.drawLine( 0, 0, w - 2, 0 );
            g.drawLine( 0, 0, 0, h - 2 );

            g.translate( -x, -y );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Insets getBorderInsets( Component c, Insets nset )
        {
            nset.set( nset.top, nset.left, nset.bottom, nset.right );
            return nset;
        }
    }
}
