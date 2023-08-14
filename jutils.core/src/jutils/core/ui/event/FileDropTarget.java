package jutils.core.ui.event;

import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import jutils.core.io.LogUtils;

/*******************************************************************************
 * Class be added to a {@link JComponent} when the user drags a file onto the
 * component.
 ******************************************************************************/
public class FileDropTarget extends DropTarget
{
    /**  */
    private static final long serialVersionUID = 4549799315841069147L;
    /** List of listeners to be called when the file is dropped. */
    private final ItemActionListener<IFileDropEvent> droppedListener;

    /***************************************************************************
     * @param droppedListener
     **************************************************************************/
    public FileDropTarget( ItemActionListener<IFileDropEvent> droppedListener )
    {
        this.droppedListener = droppedListener;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public synchronized void drop( DropTargetDropEvent evt )
    {
        try
        {
            int dropAction = evt.getDropAction();
            DropActionType action = getAction( dropAction );
            Object xferData;
            DefaultFileDropEvent dfde;
            ItemActionEvent<IFileDropEvent> iae;

            evt.acceptDrop( dropAction );

            xferData = evt.getTransferable().getTransferData(
                DataFlavor.javaFileListFlavor );
            @SuppressWarnings( "unchecked")
            List<File> droppedFiles = ( List<File> )xferData;

            dfde = new DefaultFileDropEvent( evt, droppedFiles, action );
            iae = new ItemActionEvent<IFileDropEvent>( this, dfde );

            ItemActionRunnable.invokeLater( droppedListener, iae );
        }
        catch( Exception ex )
        {
            LogUtils.printError( "Unable to drag and drop file", ex );
        }
    }

    /***************************************************************************
     * @param action
     * @return
     **************************************************************************/
    private static DropActionType getAction( int action )
    {
        switch( action )
        {
            case DnDConstants.ACTION_LINK:
                return DropActionType.LINK;
            case DnDConstants.ACTION_COPY:
                return DropActionType.COPY;
            case DnDConstants.ACTION_MOVE:
                return DropActionType.MOVE;
            default:
                return DropActionType.MOVE;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static interface IFileDropEvent
    {
        /**
         * @return
         */
        public DropTargetDropEvent getEvent();

        /**
         * @return
         */
        public List<File> getFiles();

        /**
         * @return
         */
        public DropActionType getActionType();
    }

    /***************************************************************************
     * Enumeration for constants in {@link DnDConstants} that start with
     * {@code ACTION_}. Those not listed are treated as
     * {@link DropActionType#MOVE}.
     **************************************************************************/
    public static enum DropActionType
    {
        /** Enumerated value for {@link DnDConstants#ACTION_LINK}. */
        LINK,
        /** Enumerated value for {@link DnDConstants#ACTION_COPY}. */
        COPY,
        /** Enumerated value for {@link DnDConstants#ACTION_MOVE}. */
        MOVE;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ItemActionRunnable<T> implements Runnable
    {
        /**  */
        private final ItemActionListener<T> listener;
        /**  */
        private final ItemActionEvent<T> event;

        /**
         * @param listener
         * @param event
         */
        public ItemActionRunnable( ItemActionListener<T> listener,
            ItemActionEvent<T> event )
        {
            this.listener = listener;
            this.event = event;
        }

        /**
         * @{@inheritDoc}
         */
        @Override
        public void run()
        {
            listener.actionPerformed( event );
        }

        /**
         * @param <T>
         * @param listener
         * @param event
         */
        public static <T> void invokeLater( ItemActionListener<T> listener,
            ItemActionEvent<T> event )
        {
            SwingUtilities.invokeLater(
                new ItemActionRunnable<>( listener, event ) );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class DefaultFileDropEvent implements IFileDropEvent
    {
        /**  */
        private final DropTargetDropEvent event;
        /**  */
        private final List<File> files;
        /**  */
        private final DropActionType action;

        /**
         * @param event
         * @param files
         * @param action
         */
        public DefaultFileDropEvent( DropTargetDropEvent event,
            List<File> files, DropActionType action )
        {
            this.event = event;
            this.files = files;
            this.action = action;
        }

        /**
         * @{@inheritDoc}
         */
        @Override
        public DropTargetDropEvent getEvent()
        {
            return event;
        }

        /**
         * @{@inheritDoc}
         */
        @Override
        public List<File> getFiles()
        {
            return files;
        }

        /**
         * @{@inheritDoc}
         */
        @Override
        public DropActionType getActionType()
        {
            return action;
        }
    }
}
