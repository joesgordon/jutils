package org.jutils.ui;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.event.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.*;

import org.jutils.SwingUtils;
import org.jutils.io.IOUtils;
import org.jutils.io.LogUtils;
import org.jutils.ui.event.*;
import org.jutils.ui.event.FileDropTarget.DropActionType;
import org.jutils.ui.event.FileDropTarget.IFileDropEvent;
import org.jutils.ui.model.IView;
import org.jutils.utils.IteratorEnumerationAdapter;

/*******************************************************************************
 * Displays a tree of directories which do not get loaded until the user expands
 * the tree node. This has the unfortunate consequence of having a '+' next to
 * empty directories until it is clicked. This feature exists because there is
 * not an interruptable way to list a directory and it sometimes takes 30
 * seconds to load the "Network" directory in windows.
 ******************************************************************************/
public class DirectoryTree implements IView<JTree>
{
    /**
     * The file system view used to get the roots of the file system and icons
     * of files.
     */
    private static final FileSystemView FILE_SYSTEM = FileSystemView.getFileSystemView();

    /** The tree that displays the file system. */
    private final JTree tree;
    /**
     * The root node of the file system. This will contain a null file and will
     * not be displayed.
     */
    private final FileTreeNode root;
    /**  */
    private final DefaultTreeModel treeModel;
    /**  */
    private final ItemActionList<List<File>> selectedListeners;
    /**  */
    private final SelectionListener localSelectedListener;

    /***************************************************************************
     * 
     **************************************************************************/
    public DirectoryTree()
    {
        this( new FileTreeNode() );
    }

    /***************************************************************************
     * @param rootFile File
     **************************************************************************/
    public DirectoryTree( File rootFile )
    {
        this( new FileTreeNode( null, rootFile ) );
    }

    /***************************************************************************
     * @param root FileTreeNode
     **************************************************************************/
    private DirectoryTree( FileTreeNode root )
    {
        this.root = root;
        this.treeModel = new DefaultTreeModel( root );
        this.tree = new JTree( treeModel );
        this.selectedListeners = new ItemActionList<>();
        this.localSelectedListener = new SelectionListener( this );

        // super.putClientProperty( "JTree.lineStyle", "None" );

        tree.setShowsRootHandles( true );
        tree.setRootVisible( false );
        tree.setEditable( false );
        tree.expandPath( new TreePath( root ) );
        tree.setCellRenderer( new Renderer() );
        tree.addTreeWillExpandListener( new ExpansionListener() );

        tree.setModel( treeModel );

        tree.setDropTarget(
            new FileDropTarget( new FileDroppedListener( this ) ) );
        tree.addTreeSelectionListener( localSelectedListener );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JTree getView()
    {
        return tree;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void clearSelection()
    {
        tree.clearSelection();
    }

    /***************************************************************************
     * @return String
     **************************************************************************/
    public String getSelectedPaths()
    {
        File [] selected = getSelected();

        return IOUtils.getStringFromFiles( selected );
    }

    /***************************************************************************
     * @return File[]
     **************************************************************************/
    public File [] getSelected()
    {
        TreePath [] paths = tree.getSelectionPaths();

        if( paths == null )
        {
            return new File[] {};
        }

        List<File> files = new ArrayList<>( paths.length );

        for( int i = 0; i < paths.length; i++ )
        {
            Object lpc = paths[i].getLastPathComponent();

            if( lpc instanceof FileTreeNode )
            {
                FileTreeNode node = ( FileTreeNode )lpc;

                files.add( node.file );
            }
            else
            {
                LogUtils.printWarning( "Node not a DirNode: " + lpc.toString() +
                    " of class " + lpc.getClass().getName() );
            }
        }

        return files.toArray( new File[] {} );
    }

    /***************************************************************************
     * @param paths String
     **************************************************************************/
    public void setSelectedPaths( String paths )
    {
        File [] dirs = null;

        if( paths != null )
        {
            dirs = IOUtils.getFilesFromString( paths );
        }

        setSelected( dirs );
    }

    /***************************************************************************
     * @param dirs File[]
     **************************************************************************/
    public void setSelected( File [] dirs )
    {
        localSelectedListener.setEnabled( false );

        if( dirs == null )
        {
            clearSelection();
            return;
        }

        TreePath [] treePaths = getTreePaths( dirs );

        // LogUtils.printDebug( "Selecting " + dirs.length + "
        // directories"
        // );

        if( treePaths.length > 0 )
        {
            tree.scrollPathToVisible( treePaths[0] );
            tree.setSelectionPaths( treePaths );
        }

        localSelectedListener.setEnabled( true );
    }

    /***************************************************************************
     * @param l the callback invoked when the selection has changed.
     **************************************************************************/
    public void addSelectedListener( ItemActionListener<List<File>> l )
    {
        selectedListeners.addListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void expandSelected()
    {
        TreePath path = tree.getSelectionPath();

        if( path != null )
        {
            // LogUtils.printDebug( "Expanding %s",
            // path.getLastPathComponent().toString() );
            tree.expandPath( path );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void refreshSelected()
    {
        TreePath [] paths = tree.getSelectionPaths();

        if( paths != null )
        {
            for( TreePath path : paths )
            {
                Object lastComp = path.getLastPathComponent();
                FileTreeNode node = ( FileTreeNode )lastComp;

                refreshNode( node );
            }
        }
    }

    /***************************************************************************
     * @param dirs
     * @return
     **************************************************************************/
    private TreePath [] getTreePaths( File [] dirs )
    {
        ArrayList<TreePath> paths = new ArrayList<TreePath>();
        TreePath [] treePaths = null;

        // For each directory to be selected
        for( int i = 0; i < dirs.length; i++ )
        {
            File dir = dirs[i];
            TreePath path = getTreePath( dir );

            if( path != null )
            {
                paths.add( path );
            }
            else
            {
                LogUtils.printError( dir.getAbsolutePath() +
                    " is not an ancestor of any system root" );
            }
        }

        treePaths = new TreePath[paths.size()];
        paths.toArray( treePaths );

        return treePaths;
    }

    /***************************************************************************
     * @param dir
     * @return
     **************************************************************************/
    private TreePath getTreePath( File dir )
    {
        TreePath path = null;
        File [] filesPath = getParentage( dir );
        FileTreeNode dmtr = root;

        // LogUtils.printDebug( "Dir: " + dir.getAbsolutePath() );
        // for( int i = 0; i < filesPath.length; i++ )
        // {
        // LogUtils.printDebug( "\tDir: " + filesPath[i].getAbsolutePath() );
        // }

        for( int i = 0; i < filesPath.length; i++ )
        {
            tree.expandPath( SwingUtils.getPath( dmtr ) );

            FileTreeNode node = getNodeWithFile( dmtr, filesPath[i] );

            if( node == null )
            {
                LogUtils.printError(
                    "Could not find path " + dir.getAbsolutePath() + " in " +
                        dmtr.file.getAbsolutePath() );
                path = SwingUtils.getPath( dmtr );
                break;
            }

            dmtr = node;

            if( i == filesPath.length - 1 )
            {
                path = new TreePath( node.getPath() );
            }
        }

        return path;
    }

    /***************************************************************************
     * @param dir
     * @return
     **************************************************************************/
    private static File [] getParentage( File dir )
    {
        ArrayList<File> files = new ArrayList<File>( 10 );
        File [] array;

        // because new File( "" ).isDirectory() returns false
        // see https://stackoverflow.com/questions/5883808
        dir = dir.getAbsoluteFile();

        // LogUtils.printDebug( "Dir: " + dir.getAbsolutePath() );

        if( !dir.isDirectory() )
        {
            dir = FILE_SYSTEM.getParentDirectory( dir );
            // LogUtils.printDebug( "\tDNE-Parent: " + dir );
        }

        if( dir != null )
        {
            files.add( dir );
        }

        while( ( dir = FILE_SYSTEM.getParentDirectory( dir ) ) != null )
        {
            // LogUtils.printDebug( "\tParent: " + dir.getAbsolutePath() );
            files.add( dir );
        }

        Collections.reverse( files );
        array = new File[files.size()];
        files.toArray( array );

        return array;
    }

    /***************************************************************************
     * @param node
     * @param dir
     * @return
     **************************************************************************/
    private static FileTreeNode getNodeWithFile( FileTreeNode node, File dir )
    {
        if( dir == null )
        {
            return null;
        }
        File canDir = null;

        try
        {
            canDir = dir.getCanonicalFile();
        }
        catch( IOException e )
        {
            return null;
        }

        for( int i = 0; i < node.getChildCount(); i++ )
        {
            FileTreeNode fn = node.getChildAt( i );
            File file;
            try
            {
                file = fn.file.getCanonicalFile();
            }
            catch( IOException e )
            {
                continue;
            }

            if( file.equals( canDir ) )
            {
                return fn;
            }
            else if( file.getAbsoluteFile().equals( dir.getAbsoluteFile() ) )
            {
                LogUtils.printWarning( "Same path different files ? " +
                    dir.getPath() + ":" + file.getPath() );
                return fn;
            }
            else if( file.getAbsolutePath().compareTo(
                dir.getAbsolutePath() ) == 0 )
            {
                LogUtils.printWarning( "Somehow " + file.getAbsolutePath() +
                    " which is a " + file.getClass() + " != " +
                    dir.getAbsolutePath() + " which is a " + file.getClass() );
                return fn;
            }
        }

        return null;
    }

    /***************************************************************************
     * @param path
     **************************************************************************/
    private void expandDirectoryPath( TreePath path )
    {
        Object lastComp = path.getLastPathComponent();
        FileTreeNode node = ( FileTreeNode )lastComp;

        if( node.getChildCount() == 0 )
        {
            refreshNode( node );
        }
    }

    /***************************************************************************
     * @param node
     **************************************************************************/
    private void refreshNode( FileTreeNode node )
    {
        File [] files = null;

        try
        {
            files = node.file.listFiles();

        }
        catch( SecurityException ex )
        {
            files = null;
        }

        files = files == null ? new File[0] : files;

        Arrays.sort( files );

        node.children.clear();

        for( File f : files )
        {
            if( f.isDirectory() )
            {
                // LogUtils.printDebug( "Adding %s to %s", f.getName(),
                // node.file.getAbsolutePath() );
                node.children.add( new FileTreeNode( node, f ) );
            }
        }

        // treeModel.reload( node );
        treeModel.nodeStructureChanged( node );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class FileTreeNode implements MutableTreeNode
    {
        /**  */
        public final FileTreeNode parent;
        /**  */
        public final File file;

        /**  */
        private final List<FileTreeNode> children;

        /**
         * Creates the root node for a file system.
         */
        public FileTreeNode()
        {
            this( null, null );

            File [] roots = FILE_SYSTEM.getRoots();

            if( roots != null )
            {
                for( File root : roots )
                {
                    children.add( new FileTreeNode( this, root ) );
                }
            }
        }

        /**
         * @return
         */
        public Object [] getPath()
        {
            List<FileTreeNode> nodes = new ArrayList<>();

            FileTreeNode node = this;

            while( node != null )
            {
                nodes.add( node );
                node = node.parent;
            }

            Collections.reverse( nodes );

            return nodes.toArray();
        }

        /**
         * @param parent
         * @param file
         */
        public FileTreeNode( FileTreeNode parent, File file )
        {
            this.parent = parent;
            this.file = file;
            this.children = new ArrayList<>();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public FileTreeNode getChildAt( int childIndex )
        {
            return children.get( childIndex );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getChildCount()
        {
            return children.size();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public TreeNode getParent()
        {
            return parent;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getIndex( TreeNode node )
        {
            return children.indexOf( node );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean getAllowsChildren()
        {
            return file == null ? true : file.isDirectory();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isLeaf()
        {
            return file == null ? false : !file.isDirectory();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Enumeration<FileTreeNode> children()
        {
            return new IteratorEnumerationAdapter<>( children.iterator() );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void insert( MutableTreeNode child, int index )
        {
            FileTreeNode fileTreeChild = ( FileTreeNode )child;

            children.add( index, fileTreeChild );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void remove( int index )
        {
            children.remove( index );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void remove( MutableTreeNode child )
        {
            FileTreeNode fileTreeChild = ( FileTreeNode )child;

            children.remove( fileTreeChild );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setUserObject( Object object )
        {
            throw new UnsupportedOperationException();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void removeFromParent()
        {
            parent.remove( this );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setParent( MutableTreeNode newParent )
        {
            throw new UnsupportedOperationException();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private final class ExpansionListener implements TreeWillExpandListener
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public void treeWillExpand( TreeExpansionEvent event )
            throws ExpandVetoException
        {
            TreePath path = event.getPath();

            expandDirectoryPath( path );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void treeWillCollapse( TreeExpansionEvent event )
            throws ExpandVetoException
        {
            // Intentionally not used.
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class Renderer implements TreeCellRenderer
    {
        /**  */
        private final DefaultTreeCellRenderer renderer;
        /**  */
        private final Map<File, FileData> iconCache;

        /**
         * 
         */
        public Renderer()
        {
            this.renderer = new DefaultTreeCellRenderer();
            this.iconCache = new HashMap<>();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Component getTreeCellRendererComponent( JTree tree, Object value,
            boolean sel, boolean expanded, boolean leaf, int row,
            boolean hasFocus )
        {
            renderer.getTreeCellRendererComponent( tree, value, sel, expanded,
                leaf, row, hasFocus );

            if( value instanceof FileTreeNode )
            {
                FileTreeNode node = ( FileTreeNode )value;
                File f = node.file;

                FileData fd = iconCache.get( f );

                if( fd == null )
                {
                    fd = new FileData( f );
                    iconCache.put( f, fd );
                }

                renderer.setIcon( fd.icon );
                renderer.setText( fd.name );
            }

            return renderer;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class FileData
    {
        /**  */
        public final Icon icon;
        /**  */
        public final String name;

        /**
         * @param file
         */
        public FileData( File file )
        {
            this.icon = DirectoryTree.FILE_SYSTEM.getSystemIcon( file );
            this.name = DirectoryTree.FILE_SYSTEM.getSystemDisplayName( file );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FileDroppedListener
        implements ItemActionListener<IFileDropEvent>
    {
        /**  */
        private final DirectoryTree tree;

        /**
         * @param tree
         */
        public FileDroppedListener( DirectoryTree tree )
        {
            this.tree = tree;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void actionPerformed( ItemActionEvent<IFileDropEvent> event )
        {
            IFileDropEvent drop = event.getItem();
            List<File> files = drop.getFiles();

            if( drop.getActionType() == DropActionType.COPY )
            {
                List<File> selected = new ArrayList<>();
                selected.addAll( Arrays.asList( tree.getSelected() ) );
                selected.addAll( files );
                tree.setSelected( selected.toArray( new File[0] ) );
            }
            else
            {
                tree.setSelected( files.toArray( new File[0] ) );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class SelectionListener implements TreeSelectionListener
    {
        /**  */
        private final DirectoryTree dirTree;
        /**  */
        private boolean enabled;

        /**
         * @param dirTree
         */
        public SelectionListener( DirectoryTree dirTree )
        {
            this.dirTree = dirTree;
        }

        /**
         * @param enabled
         */
        public void setEnabled( boolean enabled )
        {
            this.enabled = enabled;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void valueChanged( TreeSelectionEvent e )
        {
            TreePath [] paths = dirTree.tree.getSelectionPaths();

            if( !enabled || paths == null || paths.length == 0 )
            {
                return;
            }

            List<File> files = new ArrayList<>( paths.length );

            for( TreePath path : paths )
            {
                FileTreeNode node = ( FileTreeNode )path.getLastPathComponent();

                files.add( node.file );
            }

            dirTree.selectedListeners.fireListeners( dirTree, files );
        }
    }
}
