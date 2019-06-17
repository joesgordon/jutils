package org.jutils.ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.jutils.ui.fields.IFormField;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class StandardFormView implements IView<JPanel>
{
    public static final int DEFAULT_FIELD_MARGIN = 4;
    public static final int DEFAULT_FORM_MARGIN = 8;

    /**  */
    private final GridBagLayout layout;
    /**  */
    private final JPanel view;
    /**  */
    private final int formMargin;
    /**  */
    private final int fieldMargin;
    /**  */
    private final Component spacer;
    /**  */
    private final List<FieldInfo> fields;

    /**  */
    private final boolean vertical;

    /***************************************************************************
     * 
     **************************************************************************/
    public StandardFormView()
    {
        this( DEFAULT_FIELD_MARGIN );
    }

    /***************************************************************************
     * @param vertical
     **************************************************************************/
    public StandardFormView( boolean vertical )
    {
        this( DEFAULT_FIELD_MARGIN, DEFAULT_FORM_MARGIN, vertical );
    }

    /***************************************************************************
     * @param formMargin
     **************************************************************************/
    public StandardFormView( int fieldMargin )
    {
        this( fieldMargin, fieldMargin * 2 );
    }

    /***************************************************************************
     * @param fieldMargin
     * @param formMargin
     **************************************************************************/
    public StandardFormView( int fieldMargin, int formMargin )
    {
        this( fieldMargin, formMargin, false );
    }

    /***************************************************************************
     * @param fieldMargin
     * @param formMargin
     * @param horizontal
     **************************************************************************/
    public StandardFormView( int fieldMargin, int formMargin, boolean vertical )
    {
        this.formMargin = formMargin;
        this.fieldMargin = fieldMargin;
        this.vertical = vertical;

        this.fields = new ArrayList<FieldInfo>();
        this.layout = new GridBagLayout();
        this.view = new JPanel( layout );

        GridBagConstraints constraints;
        spacer = Box.createHorizontalStrut( 2 );

        constraints = new GridBagConstraints( 0, 0, 2, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( formMargin, 0, 0, 0 ), 0, 0 );

        view.add( spacer, constraints );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * @param field
     **************************************************************************/
    public void addField( IFormField field )
    {
        addField( field, fields.size() );
    }

    /***************************************************************************
     * @param name
     * @param comp
     **************************************************************************/
    public void addField( String name, JComponent comp )
    {
        addField( new DefaultFormField( name, comp ) );
    }

    /***************************************************************************
     * @param comp
     **************************************************************************/
    public void addComponent( JComponent comp )
    {
        addField( new DefaultFormField( null, comp ) );
    }

    /***************************************************************************
     * @param field
     * @param index
     **************************************************************************/
    public void addField( IFormField field, int index )
    {
        GridBagConstraints constraints;
        int top = formMargin;
        int left = formMargin;
        int right = vertical ? formMargin : fieldMargin;

        JLabel label = null;

        int fieldx = 0;
        int fieldy = vertical ? index * 2 + 1 : index * 2;
        int labelAnchor = vertical ? GridBagConstraints.WEST
            : GridBagConstraints.EAST;
        int colSpan = 2;

        if( field.getName() != null )
        {
            label = new JLabel( field.getName() + ":" );

            constraints = new GridBagConstraints( 0, index * 2, 1, 1, 0.0, 0.0,
                labelAnchor, GridBagConstraints.NONE,
                new Insets( formMargin, formMargin, 0, right ), 0, 0 );
            view.add( label, constraints );

            fieldx = vertical ? 0 : 1;
            top = vertical ? fieldMargin : formMargin;
            left = vertical ? formMargin : fieldMargin;
            colSpan = 1;
        }

        fields.add( new FieldInfo( label, field ) );

        constraints = new GridBagConstraints( fieldx, fieldy, colSpan, 1, 1.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( top, left, 0, formMargin ), 0, 0 );
        view.add( field.getView(), constraints );

        refreshForm( index + 1 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void removeAll()
    {
        for( int i = fields.size() - 1; i > -1; i-- )
        {
            removeField( i );
        }
    }

    /***************************************************************************
     * @param field
     **************************************************************************/
    public void removeField( IFormField field )
    {
        int formIndex = indexOfField( field );

        if( formIndex > -1 )
        {
            removeField( formIndex );
        }
    }

    /***************************************************************************
     * @param index
     **************************************************************************/
    public void removeField( int index )
    {
        FieldInfo info = fields.remove( index );

        if( info.label != null )
        {
            view.remove( info.label );
        }
        view.remove( info.field.getView() );

        refreshForm( index );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getFieldCount()
    {
        return fields.size();
    }

    /***************************************************************************
     * @param f
     * @param visible
     **************************************************************************/
    public void setVisible( IFormField f, boolean visible )
    {
        setVisible( f.getView(), visible );
    }

    /***************************************************************************
     * @param c
     * @param visible
     **************************************************************************/
    public void setVisible( Component c, boolean visible )
    {
        int index = indexOfField( c );
        FieldInfo info = fields.get( index );

        info.field.getView().setVisible( visible );
        if( info.label != null )
        {
            info.label.setVisible( visible );
        }
    }

    /***************************************************************************
     * @param field
     * @return
     **************************************************************************/
    private int indexOfField( IFormField field )
    {
        for( int i = 0; i < fields.size(); i++ )
        {
            if( fields.get( i ).field == field )
            {
                return i;
            }
        }

        return -1;
    }

    /***************************************************************************
     * @param c
     * @return
     **************************************************************************/
    private int indexOfField( Component c )
    {
        for( int i = 0; i < fields.size(); i++ )
        {
            if( fields.get( i ).field.getView() == c )
            {
                return i;
            }
        }

        return -1;
    }

    /***************************************************************************
     * @param index
     **************************************************************************/
    private void refreshForm( int index )
    {
        GridBagConstraints constraints;
        FieldInfo info;

        for( int i = index; i < fields.size(); i++ )
        {
            info = fields.get( i );

            if( info.field.getName() != null )
            {
                constraints = layout.getConstraints( info.label );
                constraints.gridy = i * 2;
                layout.setConstraints( info.label, constraints );
            }

            constraints = layout.getConstraints( info.field.getView() );
            constraints.gridy = i * 2 + 1;
            layout.setConstraints( info.field.getView(), constraints );
        }

        constraints = layout.getConstraints( spacer );
        constraints.gridy = fields.size() * 2;
        layout.setConstraints( spacer, constraints );

        view.revalidate();
        view.repaint();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FieldInfo
    {
        /**  */
        public final JLabel label;
        /**  */
        public final IFormField field;

        /**
         * @param label
         * @param field
         */
        public FieldInfo( JLabel label, IFormField field )
        {
            this.label = label;
            this.field = field;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class DefaultFormField implements IFormField
    {
        /**  */
        public String name;
        /**  */
        public JComponent comp;

        /**
         * @param name
         * @param comp
         */
        public DefaultFormField( String name, JComponent comp )
        {
            this.name = name;
            this.comp = comp;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getName()
        {
            return name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public JComponent getView()
        {
            return comp;
        }
    }
}
