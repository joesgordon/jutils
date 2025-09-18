package jutils.core.ui.model;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/*******************************************************************************
 * A {@link ListCellRenderer} that uses the provided decorator to render cells
 * as a {@link JLabel}.
 * @param <T>
 ******************************************************************************/
public class LabelListCellRenderer<T>
    extends ComponentListCellRenderer<JLabel, T>
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

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    protected JLabel getComponent( JList<? extends T> list, T value, int index,
        boolean isSelected, boolean cellHasFocus )
    {
        renderer.getListCellRendererComponent( list, value, index, isSelected,
            cellHasFocus );

        return renderer;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    protected void decorateComponent( JLabel component, JList<? extends T> list,
        T value, int index, boolean isSelected, boolean cellHasFocus )
    {
        decorator.decorate( component, list, value, index, isSelected,
            cellHasFocus );
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
