package jutils.core.ui.fields;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Action;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import jutils.core.IconConstants;
import jutils.core.SwingUtils;
import jutils.core.io.parsers.ExistenceType;
import jutils.core.io.parsers.FileType;
import jutils.core.net.EndPoint;
import jutils.core.net.IpAddress;
import jutils.core.ui.ColorIcon;
import jutils.core.ui.ComponentView;
import jutils.core.ui.JGoodiesToolBar;
import jutils.core.ui.StandardFormView;
import jutils.core.ui.StandardFrameView;
import jutils.core.ui.app.AppRunner;
import jutils.core.ui.event.ActionAdapter;
import jutils.core.ui.hex.HexUtils;
import jutils.core.ui.model.IView;
import jutils.core.ui.net.EndPointField;
import jutils.core.ui.net.IpAddressField;
import jutils.core.utils.BitArray;
import jutils.core.utils.Usable;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FormFieldsMain
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
    public static JFrame createFrame()
    {
        UiTestFrameView frameView = new UiTestFrameView();

        return frameView.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class UiTestFrameView implements IView<JFrame>
    {
        /**  */
        private final StandardFrameView frameView;
        /**  */
        private final DefaultListModel<IFormField> itemsModel;
        /**  */
        private final JList<IFormField> itemsList;
        /**  */
        private final ComponentView cview;
        /**  */
        private final Action editableAction;

        /**  */
        private boolean editable;

        /**
         * 
         */
        public UiTestFrameView()
        {
            this.frameView = new StandardFrameView();
            this.itemsModel = new DefaultListModel<>();
            this.itemsList = new JList<>( itemsModel );
            this.cview = new ComponentView();

            this.editableAction = createEditableAction();
            this.editable = true;

            frameView.setContent( createContent() );
            frameView.setTitle( "Form Fields Test" );
            frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            frameView.setSize( 600, 500 );
            frameView.getView().setIconImages(
                IconConstants.getPageMagImages() );

            createViews( itemsModel );

            itemsList.setCellRenderer( new ItemRenderer() );
            itemsList.addListSelectionListener(
                new ItemSelectedListener( this ) );
        }

        /**
         * @return
         */
        private Container createContent()
        {
            JPanel panel = new JPanel( new BorderLayout() );

            panel.add( createToolbar(), BorderLayout.NORTH );
            panel.add( createSelectionPanel(), BorderLayout.CENTER );

            return panel;
        }

        /**
         * @return
         */
        private Component createToolbar()
        {
            JToolBar toolbar = new JGoodiesToolBar();

            SwingUtils.addActionToToolbar( toolbar, editableAction );

            return toolbar;
        }

        /**
         * @return
         */
        private Action createEditableAction()
        {
            ActionListener listener = ( e ) -> toggleEditable();
            Icon icon = new ColorIcon( Color.blue, 16 );
            return new ActionAdapter( listener, "Toggle Editable", icon );
        }

        /**
         * 
         */
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

        /**
         * @return
         */
        private JPanel createSelectionPanel()
        {
            JPanel panel = new JPanel( new GridBagLayout() );
            GridBagConstraints constraints;

            JScrollPane pane = new JScrollPane( itemsList );

            Dimension size = pane.getPreferredSize();
            size.width = 400;

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

        /**
         * @param itemsModel
         */
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
                new EndPointField( "End Point Form Field" ),
                new EndPoint( new IpAddress( 10, 10, 11, 12 ), 1234 ) ) );

            // -----------------------------------------------------------------
            // File Form Fields.
            // -----------------------------------------------------------------

            for( FileType ft : FileType.values() )
            {
                File f = null;

                switch( ft )
                {
                    case DIRECTORY:
                        f = new File( "/" );
                        break;

                    case FILE:
                        f = new File( "/Windows/notepad.exe" );
                        break;

                    case PATH:
                        f = new File( "/" );
                        break;
                }

                for( ExistenceType et : ExistenceType.values() )
                {
                    for( boolean ba : new boolean[] { true, false } )
                    {
                        String name = String.format(
                            "File Form Field (%s,%s,%s)", ft.name, et.name,
                            ba ? "true" : "false" );
                        FileFormField field = new FileFormField( name, ft, et,
                            ba );

                        itemsModel.addElement(
                            createFormFieldItem( field, f ) );
                    }
                }
            }

            // -----------------------------------------------------------------
            // Hex Form Fields.
            // -----------------------------------------------------------------

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

            itemsModel.addElement( createFormFieldItem(
                new IpAddressField( "IP Address Form Field" ),
                new IpAddress( 10, 10, 11, 12 ) ) );

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

        /**
         * @param <D>
         * @param field
         * @param data
         * @return
         */
        private static <D> IFormField createFormFieldItem(
            IDataFormField<D> field, D data )
        {
            field.setValue( data );

            return field;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public JFrame getView()
        {
            return frameView.getView();
        }
    }

    /***************************************************************************
     *
     **************************************************************************/
    private static final class ItemRenderer
        implements ListCellRenderer<IFormField>
    {
        /**  */
        private final DefaultListCellRenderer renderer = new DefaultListCellRenderer();

        /**
         * {@inheritDoc}
         */
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
        /**  */
        private final UiTestFrameView view;

        /**
         * @param view
         */
        public ItemSelectedListener( UiTestFrameView view )
        {
            this.view = view;
        }

        /**
         * {@inheritDoc}
         */
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
