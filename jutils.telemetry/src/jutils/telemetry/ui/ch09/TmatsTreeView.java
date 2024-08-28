package jutils.telemetry.ui.ch09;

import java.awt.Component;

import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import jutils.core.ui.model.IDataView;
import jutils.telemetry.data.ch09.TmatsFile;

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
        this.splitPane = new JSplitPane();

        tree.setRootVisible( true );
        tree.addTreeSelectionListener( ( e ) -> handleNodeSelected( e ) );

        splitPane.setLeftComponent( tree );

        this.tmats = root.data;
        // TODO Auto-generated constructor stub
    }

    private void handleNodeSelected( TreeSelectionEvent e )
    {
        TreePath path = e.getPath();

        if( path != null )
        {
            Object objNode = path.getLastPathComponent();
            DataNode<?> node = ( DataNode<?> )objNode;

            handleNodeSelected( node );
        }
        // TODO Auto-generated method stub
    }

    private <F> void handleNodeSelected( DataNode<F> node )
    {
        splitPane.setRightComponent( node.viewCreator.get().getView() );
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

        // TODO Auto-generated method stub
    }
}
