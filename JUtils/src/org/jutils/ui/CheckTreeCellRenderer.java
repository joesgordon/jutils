package org.jutils.ui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

/*******************************************************************************
 *
 ******************************************************************************/
public class CheckTreeCellRenderer implements TreeCellRenderer
{
    /**  */
    private final JPanel panel;
    /**  */
    private final CheckTreeSelectionModel selectionModel;
    /**  */
    private final TreeCellRenderer delegate;
    /**  */
    private final TristateCheckBox checkBox = new TristateCheckBox();

    /***************************************************************************
     * @param delegate TreeCellRenderer
     * @param selectionModel CheckTreeSelectionModel
     **************************************************************************/
    public CheckTreeCellRenderer( TreeCellRenderer delegate,
        CheckTreeSelectionModel selectionModel )
    {
        this.panel = new JPanel();
        this.delegate = delegate;
        this.selectionModel = selectionModel;
        panel.setLayout( new BorderLayout() );
        panel.setOpaque( false );
        checkBox.setOpaque( false );
    }

    /***************************************************************************
     * @param tree JTree
     * @param value Object
     * @param selected boolean
     * @param expanded boolean
     * @param leaf boolean
     * @param row int
     * @param hasFocus boolean
     * @return Component
     **************************************************************************/
    @Override
    public Component getTreeCellRendererComponent( JTree tree, Object value,
        boolean selected, boolean expanded, boolean leaf, int row,
        boolean hasFocus )
    {
        Component renderer = delegate.getTreeCellRendererComponent( tree, value,
            selected, expanded, leaf, row, hasFocus );

        TreePath path = tree.getPathForRow( row );
        if( path != null )
        {
            if( selectionModel.isPathSelected( path, true ) )
            {
                checkBox.setState( Boolean.TRUE );
            }
            else
            {
                checkBox.setState( selectionModel.isPartiallySelected( path )
                    ? null : Boolean.FALSE );
            }
        }

        panel.removeAll();
        panel.add( checkBox, BorderLayout.WEST );
        panel.add( renderer, BorderLayout.CENTER );

        return panel;
    }
}
