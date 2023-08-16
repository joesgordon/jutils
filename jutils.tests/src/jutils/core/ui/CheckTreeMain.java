package jutils.core.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import jutils.core.ui.app.AppRunner;
import jutils.core.ui.fields.ComboFormField;

/*******************************************************************************
 *
 ******************************************************************************/
public class CheckTreeMain
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.invokeLater( () -> createFrame() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static JFrame createFrame()
    {
        StandardFrameView frameView = new StandardFrameView();

        frameView.setContent( createContent() );
        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frameView.setSize( 500, 600 );
        frameView.setTitle( "Check Tree Test" );

        return frameView.getView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static Container createContent()
    {
        JPanel panel = new JPanel( new BorderLayout() );
        JTree tree = new JTree();
        CheckTreeManager manager = new CheckTreeManager( tree );
        JScrollPane treePane = new JScrollPane( tree );

        ScrollableEditorPaneView textField = new ScrollableEditorPaneView();
        JScrollPane textPane = new JScrollPane( textField.getView() );

        manager.addCheckListener( ( p ) -> textField.appendText(
            String.format( "%s selected = %b\n",
                p.getLastPathComponent().toString(),
                manager.getSelectionModel().isPathSelected( p, true ) ),
            null ) );

        treePane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
        textPane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );

        JSplitPane pane = new JSplitPane( JSplitPane.VERTICAL_SPLIT, treePane,
            textPane );

        pane.setDividerLocation( 300 );

        panel.add( pane, BorderLayout.CENTER );
        panel.add( createForm( tree, manager, textField ), BorderLayout.SOUTH );

        // panel.add( treePane, BorderLayout.CENTER );
        // panel.add( textPane, BorderLayout.SOUTH );

        return panel;
    }

    /***************************************************************************
     * @param tree
     * @param manager
     * @param textField
     * @return
     **************************************************************************/
    private static Component createForm( JTree tree, CheckTreeManager manager,
        ScrollableEditorPaneView textField )
    {
        StandardFormView form = new StandardFormView();
        ComboFormField<TreeNode> nodesField = new ComboFormField<>( "Node",
            getNodes( tree ) );

        nodesField.setUpdater( ( n ) -> textField.appendText( String.format(
            "checked %s and selected = %b\n", n.toString(),
            manager.getSelectionModel().isPathSelected( getPath( n ), true ) ),
            null ) );

        form.addField( nodesField );

        return form.getView();
    }

    /***************************************************************************
     * @param tree
     * @return
     **************************************************************************/
    private static List<TreeNode> getNodes( JTree tree )
    {
        List<TreeNode> nodes = new ArrayList<>();

        TreeNode root = ( TreeNode )tree.getModel().getRoot();

        nodes.add( root );

        addChildNodes( nodes, root );

        return nodes;
    }

    /***************************************************************************
     * @param nodes
     * @param node
     **************************************************************************/
    private static void addChildNodes( List<TreeNode> nodes, TreeNode node )
    {
        for( int i = 0; i < node.getChildCount(); i++ )
        {
            TreeNode child = node.getChildAt( i );
            nodes.add( child );
            addChildNodes( nodes, child );
        }
    }

    /***************************************************************************
     * @param treeNode
     * @return
     **************************************************************************/
    private static TreePath getPath( TreeNode treeNode )
    {
        List<Object> nodes = new ArrayList<Object>();
        if( treeNode != null )
        {
            nodes.add( treeNode );
            treeNode = treeNode.getParent();
            while( treeNode != null )
            {
                nodes.add( 0, treeNode );
                treeNode = treeNode.getParent();
            }
        }

        return nodes.isEmpty() ? null : new TreePath( nodes.toArray() );
    }
}
