package org.jutils.ui.model;

import java.awt.Component;

import javax.swing.*;

/*******************************************************************************
 * A {@link ListCellRenderer} that uses the provided decorator to render cells
 * as a {@link JLabel}.
 ******************************************************************************/
public class LabelListCellRenderer<T> implements ListCellRenderer<T>
{
    /**  */
    private final DefaultListCellRenderer renderer;
    /** The decorator to be used to render cells. */
    private final IListCellLabelDecorator<T> decorator;

    /***************************************************************************
     * @param decorator the decorator used to render cells.
     **************************************************************************/
    public LabelListCellRenderer( IListCellLabelDecorator<T> decorator )
    {
        this.renderer = new DefaultListCellRenderer();
        this.decorator = decorator;
    }

    @Override
    public Component getListCellRendererComponent( JList<? extends T> list,
        T value, int index, boolean isSelected, boolean cellHasFocus )
    {
        Component c = renderer.getListCellRendererComponent( list, value, index,
            isSelected, cellHasFocus );

        decorator.decorate( renderer, list, value, index, isSelected,
            cellHasFocus );

        return c;
    }

    /***************************************************************************
     * A decorator used to render a cell of a {@link JList} as a label.
     **************************************************************************/
    public static interface IListCellLabelDecorator<T>
    {
        /***********************************************************************
         * Decorates the provided label with the value of the list.
         * @param label the label to be decorated.
         * @param list the list in which the label is rendered.
         * @param value the value in the list that used to decorate the label.
         * @param index the index to be rendered.
         * @param isSelected {@code true} if the cell is selected.
         * @param cellHasFocus {@code true} if the cell has focus.
         **********************************************************************/
        public void decorate( JLabel label, JList<? extends T> list, T value,
            int index, boolean isSelected, boolean cellHasFocus );
    }
}
