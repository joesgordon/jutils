package jutils.core.ui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/*******************************************************************************
 * Defines a {@link JFileChooser} that supports an additional component on the
 * right hand side.
 ******************************************************************************/
public class ExtendedFileChooser extends JFileChooser
{
    /** Generated UID. */
    private static final long serialVersionUID = 407583092267479562L;

    /***************************************************************************
     * Creates the chooser with default options.
     **************************************************************************/
    public ExtendedFileChooser()
    {
        super();

        // Color [] colors = { Color.red, Color.green, Color.blue,
        // Color.black };
        //
        // for( int i = 0; i < super.getComponentCount(); i++ )
        // {
        // JComponent comp = ( JComponent )super.getComponent( i );
        //
        // comp.setBorder( new LineBorder( colors[i] ) );
        //
        // LogUtils.printDebug( "Child %d is a %s", i, comp.getClass() );
        // }
    }

    /***************************************************************************
     * Sets a component to show additional options to the right of the standard
     * chooser fields.
     * @param comp the component containing fields for additional options.
     **************************************************************************/
    public void setAdditional( JComponent comp )
    {
        BorderLayout layout = ( BorderLayout )super.getLayout();
        BorderLayout newLayout = new BorderLayout();
        JPanel panel = new JPanel( newLayout );

        newLayout.setHgap( layout.getHgap() );
        newLayout.setVgap( layout.getVgap() );

        while( super.getComponentCount() > 0 )
        {
            Component c = super.getComponent( 0 );
            Object constraints = layout.getConstraints( c );

            // LogUtils.printDebug( "Contraints: " + constraints.toString()
            // );

            super.remove( c );
            panel.add( c, constraints );
        }

        Border border = comp.getBorder();

        if( border == null )
        {
            border = new EtchedBorder();
        }

        // int rightBorder = onTop ? 0 : 8;
        // int bottomBorder = onTop ? 8 : 0;

        comp.setBorder(
            new CompoundBorder( new EmptyBorder( 0, 8, 0, 0 ), border ) );

        // String compLoc = onTop ? BorderLayout.NORTH : BorderLayout.WEST;
        // String existingLoc = onTop ? BorderLayout.SOUTH :
        // BorderLayout.EAST;

        super.add( panel, BorderLayout.CENTER );
        super.add( comp, BorderLayout.EAST );

        super.doLayout();
    }

    /***************************************************************************
     * Adds an extension to the dialog list of file types.
     * @param description the description of files with the extension
     * @param extensions the extension to be added sans '.'
     **************************************************************************/
    public void addExtension( String description, String... extensions )
    {
        StringBuilder desc = new StringBuilder();

        desc.append( description );
        desc.append( " " );

        for( int i = 0; i < extensions.length; i++ )
        {
            if( i > 0 )
            {
                desc.append( ", " );
            }
            desc.append( "(*." );
            desc.append( extensions[i] );
            desc.append( ')' );
        }

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            desc.toString(), extensions );
        addChoosableFileFilter( filter );
    }

    /***************************************************************************
     * Removes all file extensions.
     **************************************************************************/
    public void removeAllExtensions()
    {
        FileFilter [] filters = getChoosableFileFilters();

        for( FileFilter f : filters )
        {
            removeChoosableFileFilter( f );
        }
    }
}
