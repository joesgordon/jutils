package org.jutils.chart.ui;

import java.awt.Component;
import java.awt.Font;
import java.util.List;

import javax.swing.*;

import org.jutils.ui.FontChooserView;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.event.updater.ListUpdater;
import org.jutils.ui.model.IDataView;
import org.jutils.ui.model.LabelListCellRenderer;
import org.jutils.ui.model.LabelListCellRenderer.IListCellLabelDecorator;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FontView implements IDataView<Font>
{
    /**  */
    private final JPanel view;

    /**  */
    private final JList<String> namesField;
    /**  */
    private final JList<Integer> sizesField;

    /**  */
    private Font f;

    /***************************************************************************
     * 
     **************************************************************************/
    public FontView()
    {
        this.namesField = new JList<>(
            FontChooserView.FONT_NAMES.toArray( new String[] {} ) );
        this.sizesField = new JList<>(
            FontChooserView.FONT_SIZES.toArray( new Integer[] {} ) );
        this.view = createView();

        namesField.addListSelectionListener(
            new ListUpdater<String>( new NameUpdater( this ) ) );
        sizesField.addListSelectionListener(
            new ListUpdater<Integer>( new SizeUpdater( this ) ) );

        setData( new Font( Font.MONOSPACED, Font.PLAIN, 12 ) );
    }

    private JPanel createView()
    {
        StandardFormView form = new StandardFormView();
        JScrollPane namesPane = new JScrollPane( namesField );
        JScrollPane sizesPane = new JScrollPane( sizesField );

        namesField.setCellRenderer(
            new LabelListCellRenderer<>( new FontCellDecorator() ) );

        form.addField( "Name", namesPane );
        form.addField( "Size", sizesPane );

        return form.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Font getData()
    {
        return f;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( Font data )
    {
        this.f = data;

        namesField.setSelectedValue( f.getFamily(), true );
        sizesField.setSelectedValue( f.getSize(), true );

        // LogUtils.printDebug( "Font name: " + f.getFamily() );
        // LogUtils.printDebug( "Font size: " + f.getSize() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FontCellDecorator
        implements IListCellLabelDecorator<String>
    {
        @Override
        public void decorate( JLabel label, JList<? extends String> list,
            String value, int index, boolean isSelected, boolean cellHasFocus )
        {
            Font f = new Font( value.toString(), Font.PLAIN, 16 );

            if( f.canDisplay( 'a' ) )
            {
                label.setFont( f );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class NameUpdater implements IUpdater<List<String>>
    {
        private final FontView view;

        public NameUpdater( FontView view )
        {
            this.view = view;
        }

        @Override
        public void update( List<String> data )
        {
            if( !data.isEmpty() )
            {
                String name = data.get( 0 );
                view.f = new Font( name, view.f.getStyle(), view.f.getSize() );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class SizeUpdater implements IUpdater<List<Integer>>
    {
        private final FontView view;

        public SizeUpdater( FontView view )
        {
            this.view = view;
        }

        @Override
        public void update( List<Integer> data )
        {
            if( !data.isEmpty() )
            {
                Integer size = data.get( 0 );
                view.f = view.f.deriveFont( view.f.getStyle(), size );
            }
        }
    }
}
