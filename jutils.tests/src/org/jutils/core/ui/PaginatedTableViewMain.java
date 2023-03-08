package org.jutils.core.ui;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Random;

import javax.swing.Icon;
import javax.swing.JFrame;

import org.jutils.core.IconConstants;
import org.jutils.core.ValidationException;
import org.jutils.core.io.IDataSerializer;
import org.jutils.core.io.IDataStream;
import org.jutils.core.io.IStringWriter;
import org.jutils.core.ui.app.FrameRunner;
import org.jutils.core.ui.app.IFrameApp;
import org.jutils.core.ui.event.ActionAdapter;
import org.jutils.core.ui.model.ITableConfig;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PaginatedTableViewMain
{
    /***************************************************************************
     * @param args ignored
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new PaginatedTableViewApp() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class PaginatedTableViewApp implements IFrameApp
    {
        private final Random rand = new Random( 42 );

        @Override
        public JFrame createFrame()
        {
            StandardFrameView frameView = new StandardFrameView();
            IStringWriter<Integer> intWriter = ( i ) -> "The # is " + i;
            PaginatedTableView<Integer> view = new PaginatedTableView<>(
                new IntTableConfig(), new IntegerSerializer(), intWriter );

            frameView.setContent( view.getView() );

            frameView.setTitle( "Net Messages View Test" );
            frameView.setSize( 680, 400 );
            frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

            JFrame frame = frameView.getView();

            frame.setIconImages( IconConstants.getPageMagImages() );

            ActionListener listener = ( e ) -> view.addItem( rand.nextInt() );
            Icon icon = IconConstants.getIcon( IconConstants.EDIT_ADD_16 );
            view.addToToolbar( new ActionAdapter( listener, "Add Row", icon ) );

            return frame;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void finalizeGui()
        {
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class IntegerSerializer implements IDataSerializer<Integer>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public Integer read( IDataStream stream )
            throws IOException, ValidationException
        {
            return stream.readInt();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write( Integer data, IDataStream stream ) throws IOException
        {
            stream.writeInt( data.intValue() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class IntTableConfig implements ITableConfig<Integer>
    {
        /**  */
        private static final String [] COLS = new String[] { "Decimal", "Hex",
            "Binary" };
        /**  */
        private static final Class<?> [] CLASSES = new Class<?>[] {
            Integer.class, String.class, String.class };

        /**
         * {@inheritDoc}
         */
        @Override
        public String [] getColumnNames()
        {
            return COLS;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Class<?> [] getColumnClasses()
        {
            return CLASSES;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Object getItemData( Integer item, int col )
        {
            switch( col )
            {
                case 0:
                    return item;

                case 1:
                    return Integer.toHexString( item.intValue() );

                case 2:
                    return Integer.toBinaryString( item.intValue() );
            }

            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public <F> void setItemData( Integer item, int col, F data )
        {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isCellEditable( Integer item, int col )
        {
            return false;
        }
    }
}
