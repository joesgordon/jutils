package org.jutils.pattern;

import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.*;

import javax.swing.*;

import org.jutils.ui.event.RightClickListener;
import org.jutils.ValidationException;
import org.jutils.io.IParser;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.fields.*;
import org.jutils.ui.validation.IValidityChangedListener;
import org.jutils.ui.validation.Validity;

/*******************************************************************************
 * 
 ******************************************************************************/
public class StringPatternField implements IDataFormField<StringPattern>
{
    /**  */
    private final JPanel view;
    /**  */
    private final ParserFormField<String> patternField;
    /**  */
    private final ComboFormField<StringPatternType> typeField;
    /**  */
    private final JCheckBoxMenuItem caseMenuItem;
    /**  */
    private final JPopupMenu contextMenu;

    /**  */
    private StringPattern pattern;
    /**  */
    private IUpdater<StringPattern> updater;

    // TODO Re-validate pattern when type changes.

    /***************************************************************************
     * 
     **************************************************************************/
    public StringPatternField( String name )
    {
        PatternTextParser parser = new PatternTextParser( this );
        PatternTextDescriptor descriptor = new PatternTextDescriptor();

        this.pattern = new StringPattern();

        this.patternField = new ParserFormField<>( name, parser, descriptor );
        this.typeField = new ComboFormField<>( "Type",
            StringPatternType.values(), new NamedItemDescriptor<>() );
        this.view = createView();
        this.caseMenuItem = new JCheckBoxMenuItem();
        this.contextMenu = createContextMenu();

        setValue( this.pattern );

        patternField.setUpdater( ( t ) -> {
            if( pattern != null )
            {
                pattern.patternText = t;
            }
            invokeUpdater();
        } );

        typeField.setUpdater( ( d ) -> {
            if( pattern != null )
            {
                pattern.type = d;
            }
            invokeUpdater();
        } );

        MouseListener rcl = new RightClickListener(
            ( e ) -> showContextMenu( e.getComponent(), e.getPoint() ) );
        patternField.addMouseListener( rcl );

        addMouseListener( patternField.getView(),
            new FieldMouseListener( this ) );
    }

    /***************************************************************************
     * @param container
     * @param listener
     **************************************************************************/
    private static void addMouseListener( Container container,
        MouseListener listener )
    {
        container.addMouseListener( listener );

        for( int i = 0; i < container.getComponentCount(); i++ )
        {
            Component comp = container.getComponent( i );

            if( comp instanceof Container )
            {
                addMouseListener( ( Container )comp, listener );
            }
            else
            {
                comp.addMouseListener( listener );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void invokeUpdater()
    {
        if( updater != null )
        {
            updater.update( pattern );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 4 ), 0, 0 );
        panel.add( patternField.getView(), constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( typeField.getView(), constraints );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPopupMenu createContextMenu()
    {
        JPopupMenu menu = new JPopupMenu();

        caseMenuItem.setText( "Case Sensitive" );

        caseMenuItem.addActionListener(
            ( e ) -> pattern.isCaseSensitive = caseMenuItem.isSelected() );

        menu.add( caseMenuItem );

        return menu;
    }

    /***************************************************************************
     * @param invoker
     * @param p
     **************************************************************************/
    private void showContextMenu( Component invoker, Point p )
    {
        contextMenu.show( invoker, p.x, p.y );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public StringPattern getValue()
    {
        return pattern;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( StringPattern value )
    {
        this.pattern = value;

        if( value != null )
        {
            patternField.setValue( pattern.patternText );
            typeField.setValue( pattern.type );
            caseMenuItem.setSelected( pattern.isCaseSensitive );
        }
        else
        {
            patternField.setValue( null );
            typeField.setValue( StringPatternType.CONTAINS );
            caseMenuItem.setSelected( false );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<StringPattern> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<StringPattern> getUpdater()
    {
        return updater;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        patternField.setEditable( editable );
        typeField.setEditable( editable );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return patternField.getName();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        patternField.addValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        patternField.removeValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return patternField.getValidity();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class FieldMouseListener extends MouseAdapter
    {
        private final StringPatternField field;
        private final JCheckBoxMenuItem csButton;
        private final JPopupMenu popup;

        public FieldMouseListener( StringPatternField field )
        {
            this.field = field;
            this.csButton = new JCheckBoxMenuItem( "Case Sensitive" );
            this.popup = new JPopupMenu();

            popup.add( csButton );

            ActionListener listener = (
                e ) -> field.pattern.isCaseSensitive = csButton.isSelected();
            csButton.addActionListener( listener );
        }

        @Override
        public void mousePressed( MouseEvent e )
        {
            if( SwingUtilities.isRightMouseButton( e ) )
            {
                csButton.setSelected( field.pattern.isCaseSensitive );
                popup.show( e.getComponent(), e.getX(), e.getY() );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class PatternTextParser implements IParser<String>
    {
        private final StringPatternField field;

        public PatternTextParser( StringPatternField field )
        {
            this.field = field;
        }

        @Override
        public String parse( String str ) throws ValidationException
        {
            StringPattern pattern = new StringPattern( field.pattern );

            pattern.patternText = str;

            // LogUtils.printDebug( "Testing %s", pattern );

            pattern.createMatcher();

            return str;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class PatternTextDescriptor
        implements IDescriptor<String>
    {
        @Override
        public String getDescription( String item )
        {
            return item;
        }
    }
}
