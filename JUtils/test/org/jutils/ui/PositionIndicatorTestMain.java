package org.jutils.ui;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.LineBorder;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.fields.LongFormField;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PositionIndicatorTestMain
{
    /***************************************************************************
     * @param args ignored
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new PositionIndicatorApp() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class PositionIndicatorApp implements IFrameApp
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public JFrame createFrame()
        {
            StandardFrameView view = new StandardFrameView();
            PositionIndicatorTestView testView = new PositionIndicatorTestView();
            ActionListener listener = ( e ) -> testView.addBookmark();

            view.setTitle( "Position Test Frame" );
            view.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            view.setSize( 700, 500 );
            view.setContent( testView.getView() );
            view.setToolbar( createToolbar( createAddAction( listener ) ) );

            return view.getView();
        }

        /**
         * @param addAction
         * @return
         */
        private static JToolBar createToolbar( Action addAction )
        {
            JToolBar toolbar = new JGoodiesToolBar();

            SwingUtils.addActionToToolbar( toolbar, addAction );

            return toolbar;
        }

        /**
         * @param listener
         * @return
         */
        private static Action createAddAction( ActionListener listener )
        {
            Icon icon = IconConstants.getIcon( IconConstants.EDIT_ADD_16 );
            // TODO Auto-generated method stub
            return new ActionAdapter( listener, "Add Bookmark", icon );
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
    private static final class PositionIndicatorTestView
        implements IView<JComponent>
    {
        /**  */
        private final JPanel view;
        /**  */
        private final LongFormField lengthField;
        /**  */
        private final LongFormField unitLengthField;
        /**  */
        private final LongFormField positionField;
        /**  */
        private final PositionIndicator posIndicator;
        /**  */
        private final LongFormField unitPositionField;
        /**  */
        private final UnitPositionIndicator unitIndicator;

        /**  */
        private static final long LEN = 100000;
        /**  */
        private static final long SIZE = LEN / 70;

        /**
         * 
         */
        public PositionIndicatorTestView()
        {
            this.lengthField = new LongFormField( "Length" );
            this.unitLengthField = new LongFormField( "Unit Length" );
            this.positionField = new LongFormField( "Position" );
            this.posIndicator = new PositionIndicator(
                ( d ) -> Long.toString( d ) );
            this.unitPositionField = new LongFormField( "Unit Position" );
            this.unitIndicator = new UnitPositionIndicator(
                ( d ) -> Long.toString( d ) );
            this.view = createView();

            posIndicator.setLength( LEN );
            posIndicator.setPosition( 0L );
            posIndicator.getView().setBorder( new LineBorder( Color.gray ) );

            unitIndicator.setLength( LEN );
            unitIndicator.setPosition( 0L );
            unitIndicator.setUnitLength( SIZE );
            unitIndicator.getView().setBorder( new LineBorder( Color.gray ) );

            lengthField.setValue( LEN );
            unitLengthField.setValue( SIZE );
            positionField.setValue( posIndicator.getPosition() );
            unitPositionField.setValue( unitIndicator.getPosition() );

            posIndicator.addPositionListener(
                ( d ) -> updatePosition( d, positionField ) );
            unitIndicator.addPositionListener(
                ( d ) -> updatePosition( d, unitPositionField ) );

            lengthField.setUpdater( ( n ) -> {
                posIndicator.setLength( n );
                unitIndicator.setLength( n );
            } );

            positionField.setUpdater( ( n ) -> posIndicator.setPosition( n ) );

            unitLengthField.setUpdater(
                ( n ) -> unitIndicator.setUnitLength( n ) );
            unitPositionField.setUpdater(
                ( n ) -> unitIndicator.setPosition( n ) );
        }

        /**
         * 
         */
        public void addBookmark()
        {
            posIndicator.addBookmark( posIndicator.getPosition() );
            unitIndicator.addBookmark( unitIndicator.getPosition() );
        }

        /**
         * @param position
         * @param field
         */
        private static void updatePosition( long position, LongFormField field )
        {
            field.setValue( position );

            // LogUtils.printDebug( "new position: %d", position );
        }

        /**
         * @return
         */
        private JPanel createView()
        {
            StandardFormView form = new StandardFormView();

            form.addField( lengthField );
            form.addField( positionField );
            form.addComponent( posIndicator.getView() );

            form.addField( unitLengthField );
            form.addField( unitPositionField );
            form.addComponent( unitIndicator.getView() );

            return form.getView();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public JComponent getView()
        {
            return view;
        }
    }
}
