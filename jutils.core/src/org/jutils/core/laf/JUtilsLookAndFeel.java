package org.jutils.core.laf;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map.Entry;

import javax.swing.UIDefaults;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.jutils.core.io.LogUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class JUtilsLookAndFeel extends MetalLookAndFeel
{
    /**  */
    private static final long serialVersionUID = 5597691713123621285L;

    /**  */

    public final LafColors colors;

    /***************************************************************************
     * 
     **************************************************************************/
    public JUtilsLookAndFeel()
    {
        this( new LafColors() );
    }

    /***************************************************************************
     * @param colors
     **************************************************************************/
    public JUtilsLookAndFeel( LafColors colors )
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

        table.put( "TabbedPaneUI", JUtilsTabbedPaneUI.class.getName() );
        table.put( "ToolBarUI", JUtilsToolBarUI.class.getName() );
        table.put( "ScrollBarUI", JUtilsScrollBarUI.class.getCanonicalName() );
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

        this.setColors( defaults );

        return defaults;
    }

    /***************************************************************************
     * @param table
     **************************************************************************/
    private void setColors( UIDefaults table )
    {
        table.put( "Button.background", colors.background );
        // table.put( "Button.border", new LineBorder( ctrlBg ) );
        table.put( "Button.foreground", colors.foreground );
        // table.put( "Button.margin", new Insets( 50, 50, 50, 50 ) );

        table.put( "CheckBox.background", colors.background );
        table.put( "CheckBox.foreground", colors.foreground );

        table.put( "Label.background", colors.background );
        table.put( "Label.foreground", colors.foreground );

        table.put( "List.background", colors.controlBackground );
        table.put( "List.foreground", colors.foreground );
        table.put( "List.selectionBackground", colors.bright );
        table.put( "List.selectionForeground", colors.foreground );

        table.put( "Menu.background", colors.background );
        table.put( "Menu.foreground", colors.foreground );
        table.put( "Menu.selectionBackground", colors.bright );
        table.put( "Menu.selectionForeground", colors.foreground );

        table.put( "MenuBar.background", colors.background );
        table.put( "MenuBar.foreground", colors.foreground );

        table.put( "MenuItem.acceleratorForeground", colors.foreground );
        table.put( "MenuItem.background", colors.background );
        table.put( "MenuItem.foreground", colors.foreground );
        table.put( "MenuItem.selectionBackground", colors.bright );
        table.put( "MenuItem.selectionForeground", colors.foreground );

        table.put( "OptionPane.background", colors.background );
        table.put( "OptionPane.foreground", colors.foreground );
        table.put( "OptionPane.messageForeground", colors.foreground );

        table.put( "Panel.background", colors.background );

        table.put( "PopupMenu.background", colors.background );

        table.put( "ProgressBar.foreground", colors.bright );
        table.put( "ProgressBar.background", colors.controlBackground );
        table.put( "ProgressBar.selectionBackground", colors.bright );
        table.put( "ProgressBar.selectionBackground", colors.foreground );

        table.put( "RadioButton.foreground", colors.foreground );
        table.put( "RadioButton.background", colors.background );

        table.put( "ScrollBar.track", colors.controlBackground );
        table.put( "ScrollBar.thumb", colors.control );

        table.put( "ScrollPane.background", colors.background );

        table.put( "Separator.highlight", colors.background );
        table.put( "Separator.shadow", colors.background );

        table.put( "Slider.foreground", colors.control );

        table.put( "ScrollBar.thumbDarkShadow", colors.foreground );
        table.put( "ScrollBar.thumbHighlight", colors.controlBackground );
        table.put( "ScrollBar.thumbShadow", colors.background );

        table.put( "TabbedPane.background", colors.background );
        table.put( "TabbedPane.foreground", colors.foreground );
        // table.put( "TabbedPane.light", Color.green );
        table.put( "TabbedPane.selected", colors.bright );
        // table.put( "TabbedPane.tabAreaBackground", Color.red );
        // table.put( "TabbedPane.selectHighlight", Color.blue );
        table.put( "TabbedPane.shadow", colors.dark );
        // table.put( "TabbedPane.darkShadow", Color.pink );

        table.put( "Table.background", colors.controlBackground );
        table.put( "Table.gridColor", colors.controlBackground.brighter() );
        table.put( "Table.foreground", colors.foreground );
        // table.put( "Table.gridColor", lightBg );
        table.put( "Table.selectionBackground", colors.bright );
        table.put( "Table.selectionForeground", colors.foreground );

        table.put( "TableHeader.background", colors.controlBackground );
        table.put( "TableHeader.foreground", colors.foreground );

        table.put( "TextField.background", colors.controlHighlight );
        table.put( "TextField.foreground", colors.foreground );
        table.put( "TextField.inactiveForeground", colors.background );
        table.put( "TextField.selectionBackground", colors.bright );
        table.put( "TextField.selectionForeground", colors.foreground );

        table.put( "TitledBorder.titleColor", colors.foreground );

        table.put( "ToolBar.background", colors.background );

        table.put( "Viewport.background", colors.controlBackground );

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

        printTable( table );

        // JButton button;
        //
        // button.getDisabledIcon();
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

        keyTerm = "icon";
        // valTerm = Color.class;

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
        public Color dark;
        /**  */
        public Color bright;

        public LafColors()
        {
            this.background = new Color( 0x121212 );
            this.controlBackground = new Color( 0x303030 );
            this.controlHighlight = new Color( 0x606060 );
            this.foreground = new Color( 0xF0F0F0 );
            this.control = new Color( 0x204060 );
            this.dark = new Color( 0x111A1F );
            this.bright = new Color( 0x0579C8 );
        }
    }
}
