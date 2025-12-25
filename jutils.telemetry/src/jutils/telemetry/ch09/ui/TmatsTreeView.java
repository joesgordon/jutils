package jutils.telemetry.ch09.ui;

import java.awt.Component;

import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import jutils.core.ui.ComponentView;
import jutils.core.ui.DataNode;
import jutils.core.ui.model.IDataView;
import jutils.telemetry.ch09.TmatsFile;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TmatsTreeView implements IDataView<TmatsFile>
{
    /**  */
    private final JSplitPane splitPane;
    /**  */
    private final JTree tree;
    /**  */
    private final ComponentView rightView;

    /**  */
    private DataNode<TmatsFile> root;
    /**  */
    private TmatsFile tmats;

    /***************************************************************************
     * 
     **************************************************************************/
    public TmatsTreeView()
    {
        this.root = TmatsTreeBuilder.buildTree( new TmatsFile() );
        this.tree = new JTree( new DefaultTreeModel( root ) );
        this.rightView = new ComponentView();
        this.splitPane = new JSplitPane();

        tree.setRootVisible( true );
        tree.addTreeSelectionListener( ( e ) -> handleNodeSelected( e ) );

        splitPane.setLeftComponent( tree );
        splitPane.setRightComponent( rightView.getView() );

        tree.setBorder( new EmptyBorder( 3, 3, 3, 3 ) );

        this.tmats = root.getData();
        // TODO Auto-generated constructor stub
    }

    /***************************************************************************
     * @param e
     **************************************************************************/
    private void handleNodeSelected( TreeSelectionEvent e )
    {
        TreePath path = e.getPath();

        if( path != null )
        {
            Object objNode = path.getLastPathComponent();
            DataNode<?> node = ( DataNode<?> )objNode;

            handleNodeSelected( node );
        }
    }

    /***************************************************************************
     * @param <F>
     * @param node
     **************************************************************************/
    private <F> void handleNodeSelected( DataNode<F> node )
    {
        rightView.setComponent( node.createView().getView() );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Component getView()
    {
        return splitPane;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public TmatsFile getData()
    {
        return tmats;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( TmatsFile data )
    {
        this.tmats = data;

        this.root = TmatsTreeBuilder.buildTree( new TmatsFile() );
        tree.setModel( new DefaultTreeModel( root ) );
        tree.setSelectionPath( new TreePath( root ) );
    }
}
