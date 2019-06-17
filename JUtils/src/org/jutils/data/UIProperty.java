package org.jutils.data;

import java.awt.Color;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.UIManager;
import javax.swing.border.Border;

/*******************************************************************************
 * Defines constants of common properties.
 ******************************************************************************/
public enum UIProperty
{
    /**  */
    BUTTON_BACKGROUND( "Button.background" ),
    /**  */
    CHECKBOX_ICON( "CheckBox.icon" ),
    /**  */
    CONTROL( "control" ),
    /**  */
    CONTROLSHADOW( "controlShadow" ),
    /**  */
    LABEL_FONT( "Label.font" ),
    /**  */
    LABEL_BACKGROUND( "Label.background" ),
    /**  */
    LIST_BACKGROUND( "List.background" ),
    /**  */
    LIST_SELECTIONBACKGROUND( "List.selectionBackground" ),
    /**  */
    LIST_FOREGROUND( "List.foreground" ),
    /**  */
    LIST_SELECTIONFOREGROUND( "List.selectionForeground" ),
    /**  */
    OPTIONPANE_ERRORICON( "OptionPane.errorIcon" ),
    /**  */
    PANEL_BACKGROUND( "Panel.background" ),
    /**  */
    PROGRESSBAR_FOREGROUND( "ProgressBar.foreground" ),
    /**  */
    INTERNALFRAME_ACTIVETITLEBACKGROUND(
        "InternalFrame.activeTitleBackground" ),
    /**  */
    SCROLLBAR_THUMB( "ScrollBar.thumb" ),
    /**  */
    SCROLLBAR_THUMBSHADOW( "ScrollBar.thumbShadow" ),
    /**  */
    TABLEHEADER_FONT( "TableHeader.font" ),
    /**  */
    TEXTAREA_SELECTIONBACKGROUND( "TextArea.selectionBackground" ),
    /**  */
    TEXTFIELD_BORDER( "TextField.border" ),
    /**  */
    TEXTFIELD_INACTIVEBACKGROUND( "TextField.inactiveBackground" );

    /** The key to access a value of the {@link UIManager}. */
    public final String key;

    /***************************************************************************
     * Creates a new UI property with the provided key.
     * @param key the key used by {@link UIManager}.
     **************************************************************************/
    private UIProperty( String key )
    {
        this.key = key;
    }

    /***************************************************************************
     * Uses this property's key as the argument to
     * {@link UIManager#getColor(Object)}.
     * @return the color for the key of this property or {@code null} if either
     * a) the value for the key is not a color or there is no value for the key.
     * @throws NullPointerException if {@link #key} is null.
     **************************************************************************/
    public final Color getColor() throws NullPointerException
    {
        return UIManager.getColor( key );
    }

    /***************************************************************************
     * Uses this property's key as the argument to
     * {@link UIManager#getIcon(Object)}.
     * @return the icon for the key of this property or {@code null} if either
     * a) the value for the key is not a icon or there is no value for the key.
     * @throws NullPointerException if {@link #key} is null.
     **************************************************************************/
    public final Icon getIcon() throws NullPointerException
    {
        return UIManager.getIcon( key );
    }

    /***************************************************************************
     * Uses this property's key as the argument to
     * {@link UIManager#getBorder(Object)}.
     * @return the border for the key of this property or {@code null} if either
     * a) the value for the key is not a border or there is no value for the
     * key.
     * @throws NullPointerException if {@link #key} is null.
     **************************************************************************/
    public final Border getBorder() throws NullPointerException
    {
        return UIManager.getBorder( key );
    }

    /***************************************************************************
     * Uses this property's key as the argument to
     * {@link UIManager#getFont(Object)}.
     * @return the font for the key of this property or {@code null} if either
     * a) the value for the key is not a font or there is no value for the key.
     * @throws NullPointerException if {@link #key} is null.
     **************************************************************************/
    public final Font getFont() throws NullPointerException
    {
        return UIManager.getFont( key );
    }
}
