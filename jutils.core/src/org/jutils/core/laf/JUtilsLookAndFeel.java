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

    /***************************************************************************
     * 
     **************************************************************************/
    public JUtilsLookAndFeel()
    {
        super();
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
        Color bg = new Color( 0x121212 );
        Color ctrlBg = new Color( 0x303030 );
        Color ctrlHighlight = new Color( 0x606060 );
        Color fg = new Color( 0xF0F0F0 );
        Color control = new Color( 0x204060 );
        Color dark = new Color( 0x111A1F );
        Color bright = new Color( 0x0579C8 );

        table.put( "Button.background", bg );
        // table.put( "Button.border", new LineBorder( ctrlBg ) );
        table.put( "Button.foreground", fg );
        // table.put( "Button.margin", new Insets( 50, 50, 50, 50 ) );

        table.put( "CheckBox.background", bg );
        table.put( "CheckBox.foreground", fg );

        table.put( "Label.background", bg );
        table.put( "Label.foreground", fg );

        table.put( "List.background", ctrlBg );
        table.put( "List.foreground", fg );
        table.put( "List.selectionBackground", bright );
        table.put( "List.selectionForeground", fg );

        table.put( "Menu.background", bg );
        table.put( "Menu.foreground", fg );
        table.put( "Menu.selectionBackground", bright );
        table.put( "Menu.selectionForeground", fg );

        table.put( "MenuBar.background", bg );
        table.put( "MenuBar.foreground", fg );

        table.put( "MenuItem.acceleratorForeground", fg );
        table.put( "MenuItem.background", bg );
        table.put( "MenuItem.foreground", fg );
        table.put( "MenuItem.selectionBackground", bright );
        table.put( "MenuItem.selectionForeground", fg );

        table.put( "OptionPane.background", bg );
        table.put( "OptionPane.foreground", fg );
        table.put( "OptionPane.messageForeground", fg );

        table.put( "Panel.background", bg );

        table.put( "PopupMenu.background", bg );

        table.put( "ProgressBar.foreground", bright );
        table.put( "ProgressBar.background", ctrlBg );
        table.put( "ProgressBar.selectionBackground", bright );
        table.put( "ProgressBar.selectionBackground", fg );

        table.put( "RadioButton.foreground", fg );
        table.put( "RadioButton.background", bg );

        table.put( "ScrollBar.track", ctrlBg );
        table.put( "ScrollBar.thumb", control );

        table.put( "ScrollPane.background", bg );

        table.put( "Separator.highlight", bg );
        table.put( "Separator.shadow", bg );

        table.put( "Slider.foreground", control );

        table.put( "ScrollBar.thumbDarkShadow", fg );
        table.put( "ScrollBar.thumbHighlight", ctrlBg );
        table.put( "ScrollBar.thumbShadow", bg );

        table.put( "TabbedPane.background", bg );
        table.put( "TabbedPane.foreground", fg );
        // table.put( "TabbedPane.light", Color.green );
        table.put( "TabbedPane.selected", bright );
        // table.put( "TabbedPane.tabAreaBackground", Color.red );
        // table.put( "TabbedPane.selectHighlight", Color.blue );
        table.put( "TabbedPane.shadow", dark );
        // table.put( "TabbedPane.darkShadow", Color.pink );

        table.put( "Table.background", ctrlBg );
        table.put( "Table.gridColor", ctrlBg.brighter() );
        table.put( "Table.foreground", fg );
        // table.put( "Table.gridColor", lightBg );
        table.put( "Table.selectionBackground", bright );
        table.put( "Table.selectionForeground", fg );

        table.put( "TableHeader.background", ctrlBg );
        table.put( "TableHeader.foreground", fg );

        table.put( "TextField.background", ctrlHighlight );
        table.put( "TextField.foreground", fg );
        table.put( "TextField.inactiveForeground", bg );
        table.put( "TextField.selectionBackground", bright );
        table.put( "TextField.selectionForeground", fg );

        table.put( "TitledBorder.titleColor", fg );

        table.put( "ToolBar.background", bg );

        table.put( "Viewport.background", ctrlBg );

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

    /**
     * @param table
     */
    private void printTable( UIDefaults table )
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
}
