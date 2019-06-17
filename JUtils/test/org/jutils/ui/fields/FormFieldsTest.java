package org.jutils.ui.fields;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.io.parsers.ExistenceType;
import org.jutils.ui.*;
import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.hex.HexUtils;
import org.jutils.ui.model.IView;
import org.jutils.utils.BitArray;
import org.jutils.utils.Usable;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FormFieldsTest
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new FormFieldTestApp() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class FormFieldTestApp implements IFrameApp
    {
        @Override
        public JFrame createFrame()
        {
            UiTestFrameView frameView = new UiTestFrameView();

            return frameView.getView();
        }

        @Override
        public void finalizeGui()
        {
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class UiTestFrameView implements IView<JFrame>
    {
        private final StandardFrameView frameView;
        private final DefaultListModel<IFormField> itemsModel;
        private final JList<IFormField> itemsList;
        private final ComponentView cview;
        private final Action editableAction;

        private boolean editable;

        public UiTestFrameView()
        {
            this.frameView = new StandardFrameView();
            this.itemsModel = new DefaultListModel<>();
            this.itemsList = new JList<>( itemsModel );
            this.cview = new ComponentView();

            this.editableAction = createEditableAction();
            this.editable = true;

            frameView.setContent( createContent() );
            frameView.setTitle( "Integer Form Field Test" );
            frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            frameView.setSize( 600, 500 );
            frameView.getView().setIconImages(
                IconConstants.getPageMagImages() );

            createViews( itemsModel );

            itemsList.setCellRenderer( new ItemRenderer() );
            itemsList.addListSelectionListener(
                new ItemSelectedListener( this ) );
        }

        private Container createContent()
        {
            JPanel panel = new JPanel( new BorderLayout() );

            panel.add( createToolbar(), BorderLayout.NORTH );
            panel.add( createSelectionPanel(), BorderLayout.CENTER );

            return panel;
        }

        private Component createToolbar()
        {
            JToolBar toolbar = new JGoodiesToolBar();

            SwingUtils.addActionToToolbar( toolbar, editableAction );

            return toolbar;
        }

        private Action createEditableAction()
        {
            ActionListener listener = ( e ) -> toggleEditable();
            Icon icon = new ColorIcon( Color.blue, 16 );
            return new ActionAdapter( listener, "Toggle Editable", icon );
        }

        private void toggleEditable()
        {
            IFormField field = itemsList.getSelectedValue();

            if( field != null && field instanceof IDataFormField )
            {
                IDataFormField<?> dff = ( IDataFormField<?> )field;
                editable = !editable;
                Color c = editable ? Color.blue : Color.red;
                Icon icon = new ColorIcon( c, 16 );

                editableAction.putValue( Action.SMALL_ICON, icon );
                dff.setEditable( editable );
            }
        }

        private JPanel createSelectionPanel()
        {
            JPanel panel = new JPanel( new GridBagLayout() );
            GridBagConstraints constraints;

            JScrollPane pane = new JScrollPane( itemsList );

            Dimension size = pane.getPreferredSize();
            size.width = 200;

            pane.setMinimumSize( size );
            pane.setPreferredSize( size );

            constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 4, 4, 4, 4 ), 0, 0 );
            panel.add( pane, constraints );

            constraints = new GridBagConstraints( 1, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 4, 0, 4, 4 ), 0, 0 );
            panel.add( cview.getView(), constraints );

            return panel;
        }

        private static void createViews(
            DefaultListModel<IFormField> itemsModel )
        {
            itemsModel.addElement( createFormFieldItem(
                new BitsFormField( "Bits Field" ), new BitArray( "FAF320" ) ) );
            itemsModel.addElement( createFormFieldItem(
                new BooleanFormField( "Boolean Form Field" ), true ) );
            itemsModel.addElement( createFormFieldItem(
                new ButtonedFormField<>(
                    new IntegerFormField( "Buttoned Integer Form Field" ) ),
                8 ) );
            itemsModel.addElement( createFormFieldItem(
                new ComboFormField<>( "Combo Form Field",
                    Character.UnicodeScript.values() ),
                Character.UnicodeScript.JAVANESE ) );
            itemsModel.addElement( createFormFieldItem(
                new FileFormField( "File Form Field (File/Save)",
                    ExistenceType.DO_NOT_CHECK ),
                new File( "/" ) ) );
            itemsModel.addElement( createFormFieldItem(
                new FileFormField( "File Form Field (not set)",
                    ExistenceType.DO_NOT_CHECK ),
                null ) );
            itemsModel.addElement( createFormFieldItem(
                new FileFormField( "File Form Field (not set, false)",
                    ExistenceType.DO_NOT_CHECK, false ),
                null ) );
            itemsModel.addElement( createFormFieldItem(
                new FileFormField( "File Form Field (Dir/Save)",
                    ExistenceType.DIRECTORY_ONLY ),
                new File( "/" ) ) );
            itemsModel.addElement( createFormFieldItem(
                new HexByteFormField( "Hex Byte Form Field" ), ( byte )0x81 ) );
            itemsModel.addElement( createFormFieldItem(
                new HexBytesFormField( "Hex Byte Form Field" ),
                HexUtils.fromHexStringToArray( "FE6B2840" ) ) );
            itemsModel.addElement( createFormFieldItem(
                new HexIntFormField( "Hex Int Form Field" ), 1729 ) );
            itemsModel.addElement( createFormFieldItem(
                new HexLongFormField( "Hex Long Form Field" ),
                0xBA5E1E55DEADBEEFL ) );
            itemsModel.addElement( createFormFieldItem(
                new IntegerFormField( "Integer Form Field" ), 6 ) );
            itemsModel.addElement(
                createFormFieldItem( new LongFormField( "Long Form Field" ),
                    60L * 186282L * 60L * 24L ) );
            itemsModel.addElement( createFormFieldItem(
                new ShortFormField( "Short Form Field" ), ( short )1992 ) );
            itemsModel.addElement(
                createFormFieldItem( new StringFormField( "String Form Field" ),
                    "Flibbidy Gibblets" ) );
            itemsModel.addElement( createFormFieldItem(
                new UsableFormField<>(
                    new StringFormField( "Usable Form Field" ) ),
                new Usable<>( true, "Gibblidy Flibbets" ) ) );
        }

        private static <D> IFormField createFormFieldItem(
            IDataFormField<D> field, D data )
        {
            field.setValue( data );

            return field;
        }

        @Override
        public JFrame getView()
        {
            return frameView.getView();
        }
    }

    private static final class ItemRenderer
        implements ListCellRenderer<IFormField>
    {
        private final DefaultListCellRenderer renderer = new DefaultListCellRenderer();

        @Override
        public Component getListCellRendererComponent(
            JList<? extends IFormField> list, IFormField value, int index,
            boolean isSelected, boolean cellHasFocus )
        {
            Component c = renderer.getListCellRendererComponent( list, value,
                index, isSelected, cellHasFocus );

            String text = "";

            if( value != null )
            {
                text = value.getName();
            }

            renderer.setText( text );

            return c;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class ItemSelectedListener
        implements ListSelectionListener
    {
        private final UiTestFrameView view;

        public ItemSelectedListener( UiTestFrameView view )
        {
            this.view = view;
        }

        @Override
        public void valueChanged( ListSelectionEvent e )
        {
            IFormField field = view.itemsList.getSelectedValue();

            if( field != null )
            {
                StandardFormView form = new StandardFormView();

                form.addField( field );

                view.cview.setComponent( form.getView() );
                view.editable = false;
                view.toggleEditable();
                view.editableAction.setEnabled( true );
            }
            else
            {
                view.cview.setComponent( new JPanel() );
                view.editableAction.setEnabled( false );
            }
        }
    }
}
