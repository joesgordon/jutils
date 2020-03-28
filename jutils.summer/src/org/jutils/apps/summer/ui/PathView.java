package org.jutils.apps.summer.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

import org.jutils.core.io.IOUtils;
import org.jutils.core.ui.event.ItemActionList;
import org.jutils.core.ui.event.ItemActionListener;
import org.jutils.core.ui.model.IDataView;

/*******************************************************************************
 * 
 *******************************************************************************/
public class PathView implements IDataView<File>
{
    /**  */
    private static final FileSystemView FILE_SYSTEM = FileSystemView.getFileSystemView();

    /**  */
    private final JPanel view;
    /**  */
    private final List<FileButton> buttons;

    /**  */
    private final ItemActionList<File> selectedListeners;

    /**  */
    private File file;
    /**  */
    private File selectedFile;

    /***************************************************************************
     * 
     **************************************************************************/
    public PathView()
    {
        this.buttons = new ArrayList<>();
        this.selectedListeners = new ItemActionList<>();

        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static JPanel createView()
    {
        JPanel panel = new JPanel();
        JTextField field = new JTextField();

        panel.setBorder( field.getBorder() );
        panel.setBackground( field.getBackground() );

        Dimension dim;

        dim = field.getPreferredSize();
        dim.height += 2;
        panel.setPreferredSize( dim );

        dim = field.getMinimumSize();
        dim.height += 2;
        panel.setMinimumSize( dim );

        return panel;
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addSelectedListener( ItemActionListener<File> l )
    {
        selectedListeners.addListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public File getData()
    {
        return file;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( File file )
    {
        this.file = file;

        buttons.clear();
        view.removeAll();

        if( file != null )
        {
            List<File> files = IOUtils.getAncestors( file );

            view.setLayout( new GridBagLayout() );
            GridBagConstraints constraints = new GridBagConstraints( 0, 0, 1, 1,
                0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets( 0, 0, 0, 0 ), 0, 0 );

            for( int i = 0; i < files.size(); i++ )
            {
                File f = files.get( i );
                FileButton button = new FileButton( f, view.getBackground() );

                button.addActionListener( new FileButtonListener( this ) );

                constraints.gridx = i;
                view.add( button, constraints );
                buttons.add( button );
            }

            buttons.get( buttons.size() - 1 ).setSelected( true );

            constraints = new GridBagConstraints( files.size(), 0, 1, 1, 1.0,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 0, 0, 0, 0 ), 0, 0 );
            view.add( Box.createHorizontalStrut( 0 ), constraints );
        }

        view.invalidate();
        view.validate();
        view.repaint();
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    public void setSelected( File file )
    {
        boolean found = false;

        for( FileButton button : buttons )
        {
            if( button.file.equals( file ) )
            {
                button.setSelected( true );
                found = true;
            }
            else
            {
                button.setSelected( false );
            }
        }

        if( found )
        {
            selectedListeners.fireListeners( this, file );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public File getSelected()
    {
        return selectedFile;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FileButton extends JButton
    {
        private static final long serialVersionUID = -8910465157344216503L;
        private final Color pressedLight;
        private final Color pressedDark;
        public final File file;

        public FileButton( File file, Color bg )
        {
            super();
            super.setContentAreaFilled( false );

            this.file = file;

            String name = null;

            if( file != null )
            {
                name = FILE_SYSTEM.getSystemDisplayName( file );
                setIcon( FILE_SYSTEM.getSystemIcon( file ) );

                if( name == null )
                {
                    name = file.getName();
                }
            }

            setText( name );

            setBorderPainted( false );
            setBackground( bg );

            pressedLight = new Color( 0x33AAFF );
            pressedDark = new Color( 0x336B9A );
        }

        @Override
        protected void paintComponent( Graphics g )
        {
            if( getModel().isPressed() || getModel().isSelected() )
            {
                int width = getWidth();
                int height = getHeight();

                Graphics2D g2 = ( Graphics2D )g;
                Paint storedPaint = g2.getPaint();
                g2.setPaint( new GradientPaint( 0, 0, pressedLight, 0, height,
                    pressedDark ) );
                g2.fillRect( 0, 0, width, height );
                g2.setPaint( storedPaint );
            }
            else
            {
                g.setColor( getBackground() );

                g.fillRect( 0, 0, getWidth(), getHeight() );
            }

            super.paintComponent( g );
        }

        @Override
        public void setContentAreaFilled( boolean b )
        {
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FileButtonListener implements ActionListener
    {
        private final PathView view;

        public FileButtonListener( PathView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            FileButton button = ( FileButton )e.getSource();
            view.setSelected( button.file );
        }
    }
}
