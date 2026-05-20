package jutils.core.ui.event;

import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

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
            DropActionType action = DropActionType.getAction( dropAction );
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
            throw new RuntimeException( "Unable to drag and drop file", ex );
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
        /** Enumerated value for {@link DnDConstants#ACTION_NONE}. */
        NONE( DnDConstants.ACTION_NONE ),
        /** Enumerated value for {@link DnDConstants#ACTION_LINK}. */
        LINK( DnDConstants.ACTION_LINK ),
        /** Enumerated value for {@link DnDConstants#ACTION_COPY}. */
        COPY( DnDConstants.ACTION_COPY ),
        /** Enumerated value for {@link DnDConstants#ACTION_MOVE}. */
        MOVE( DnDConstants.ACTION_MOVE ),
        /** Enumerated value for {@link DnDConstants#ACTION_COPY_OR_MOVE}. */
        COPY_OR_MOVE( DnDConstants.ACTION_COPY_OR_MOVE ),
        /** Enumerated value for {@link DnDConstants#ACTION_REFERENCE}. */
        REFERENCE( DnDConstants.ACTION_REFERENCE );

        /**  */
        public final int dndAction;

        /**
         * @param dndAction
         */
        private DropActionType( int dndAction )
        {
            this.dndAction = dndAction;
        }

        /***************************************************************************
         * @param action
         * @return
         **************************************************************************/
        public static DropActionType getAction( int action )
        {
            for( DropActionType dat : values() )
            {
                if( dat.dndAction == action )
                {
                    return dat;
                }
            }

            return DropActionType.MOVE;
        }
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
