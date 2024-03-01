package jutils.core.ui;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Random;

import javax.swing.Icon;
import javax.swing.JFrame;

import jutils.core.IconConstants;
import jutils.core.OptionUtils;
import jutils.core.ValidationException;
import jutils.core.io.IDataSerializer;
import jutils.core.io.IDataStream;
import jutils.core.io.IItemStream;
import jutils.core.io.IParser;
import jutils.core.io.IStringWriter;
import jutils.core.io.LogUtils;
import jutils.core.io.ReferenceItemStream;
import jutils.core.io.ReferenceStream;
import jutils.core.io.parsers.LongParser;
import jutils.core.ui.app.AppRunner;
import jutils.core.ui.app.IFrameApp;
import jutils.core.ui.event.ActionAdapter;
import jutils.core.ui.model.ITableConfig;

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
        AppRunner.invokeLater( new PaginatedTableViewApp() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class PaginatedTableViewApp implements IFrameApp
    {
        /**  */
        private final Random rand = new Random( 42 );

        /**
         * {@inheritDoc}
         */
        @Override
        public JFrame createFrame()
        {
            StandardFrameView frameView = new StandardFrameView();
            IStringWriter<Integer> intWriter = ( i ) -> "The # is " + i;
            ReferenceStream<Integer> refStream;
            try
            {
                ReferenceStream<Integer> rs = new ReferenceStream<>(
                    new IntegerSerializer() );
                refStream = rs;
            }
            catch( IOException ex )
            {
                throw new RuntimeException( ex );
            }

            IItemStream<Integer> stream = new ReferenceItemStream<>(
                refStream );
            PaginatedTableView<Integer> view = new PaginatedTableView<>(
                new IntTableConfig(), stream, intWriter, 5 );

            frameView.setContent( view.getView() );

            frameView.setTitle( "PaginatedTableView Test" );
            frameView.setSize( 680, 400 );
            frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

            JFrame frame = frameView.getView();

            frame.setIconImages( IconConstants.getPageMagImages() );

            ActionListener listener;
            Icon icon;

            listener = ( e ) -> view.addItem( rand.nextInt() );
            icon = IconConstants.getIcon( IconConstants.EDIT_ADD_16 );
            view.addToToolbar( new ActionAdapter( listener, "Add Row", icon ) );

            listener = ( e ) -> handleSetSelected( view );
            icon = IconConstants.getIcon( IconConstants.FIND_16 );
            view.addToToolbar(
                new ActionAdapter( listener, "Set Selected", icon ) );

            return frame;
        }

        /**
         * @param view
         */
        private static void handleSetSelected(
            PaginatedTableView<Integer> view )
        {
            IParser<Long> parser = new LongParser( 0L, view.getItemCount() );
            Long index = OptionUtils.promptForValue( view.getView(), "Index",
                parser, "Choose Index to select" );

            if( index != null )
            {
                Integer value = view.setSelected( index );

                LogUtils.printDebug( "Value %d at index %d was selected", value,
                    index );
            }
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
