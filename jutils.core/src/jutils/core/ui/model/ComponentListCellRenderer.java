package jutils.core.ui.model;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

/*******************************************************************************
 * @param <C>
 * @param <D>
 ******************************************************************************/
public abstract class ComponentListCellRenderer<C extends Component, D>
    implements ListCellRenderer<D>
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public final C getListCellRendererComponent( JList<? extends D> list,
        D value, int index, boolean isSelected, boolean cellHasFocus )
    {
        C component = getComponent( list, value, index, isSelected,
            cellHasFocus );

        decorateComponent( component, list, value, index, isSelected,
            cellHasFocus );

        return component;
    }

    /***************************************************************************
     * @param component
     * @param list
     * @param value
     * @param index
     * @param isSelected
     * @param cellHasFocus
     **************************************************************************/
    protected abstract void decorateComponent( C component,
        JList<? extends D> list, D value, int index, boolean isSelected,
        boolean cellHasFocus );

    /***************************************************************************
     * @param list
     * @param value
     * @param index
     * @param isSelected
     * @param cellHasFocus
     * @return
     **************************************************************************/
    protected abstract C getComponent( JList<? extends D> list, D value,
        int index, boolean isSelected, boolean cellHasFocus );
}
