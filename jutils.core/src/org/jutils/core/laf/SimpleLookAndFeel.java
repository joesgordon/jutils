package org.jutils.core.laf;

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
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.jutils.core.io.LogUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SimpleLookAndFeel extends MetalLookAndFeel
{
    /**  */
    private static final long serialVersionUID = 5597691713123621285L;

    /**  */

    public final LafColors colors;

    /***************************************************************************
     * 
     **************************************************************************/
    public SimpleLookAndFeel()
    {
        this( new LafColors() );
    }

    /***************************************************************************
     * @param colors
     **************************************************************************/
    public SimpleLookAndFeel( LafColors colors )
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
        setTextfieldDefaults( table );
        setTitledBorderDefaults( table );
        setToolbarDefaults( table );
        setToggleButtonDefaults( table );
        setCommonDefaults( table );

        printTable( table );
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
        table.put( "ScrollBar.track", colors.control );
        table.put( "ScrollBar.trackHighlight", colors.controlHighlight );

        table.put( "ScrollBar.thumb", colors.control );
        table.put( "ScrollBar.thumbDarkShadow", colors.controlBackground );
        table.put( "ScrollBar.thumbHighlight", colors.controlHighlight );
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
        // table.put( "TabbedPane.light", Color.green );
        table.put( "TabbedPane.selected", colors.bright );
        // table.put( "TabbedPane.tabAreaBackground", Color.red );
        // table.put( "TabbedPane.selectHighlight", Color.blue );
        table.put( "TabbedPane.shadow", colors.shadow );
        // table.put( "TabbedPane.darkShadow", Color.pink );
    }

    /***************************************************************************
     * @param table default properties for UI components.
     **************************************************************************/
    private void setTableDefaults( UIDefaults table )
    {
        table.put( "Table.background", colors.background );
        table.put( "Table.gridColor", colors.controlBackground.brighter() );
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
    private void setTextfieldDefaults( UIDefaults table )
    {
        table.put( "TextField.background", colors.controlBackground );
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
        table.put( "control", colors.controlHighlight );
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
     * @param table
     **************************************************************************/
    private static void printTable( UIDefaults table )
    {
        ArrayList<Entry<Object, Object>> entries = new ArrayList<>(
            table.entrySet() );

        String keyTerm = null;
        Class<?> valTerm = null;

        Collections.sort( entries, ( e1, e2 ) -> {
            return e1.getKey().toString().compareTo( e2.getKey().toString() );
        } );

        keyTerm = "Button";
        // valTerm = Color.class;
        // valTerm = Border.class;

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
    public static final class LafColors
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
        public LafColors()
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
        public Insets getBorderInsets( Component c, Insets nset )
        {
            nset.set( nset.top, nset.left, nset.bottom, nset.right );
            return nset;
        }
    }
}
