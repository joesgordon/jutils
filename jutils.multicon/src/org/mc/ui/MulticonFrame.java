package org.mc.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.jutils.core.OptionUtils;
import org.jutils.core.SwingUtils;
import org.jutils.core.ui.StandardFrameView;
import org.jutils.core.ui.model.IView;
import org.mc.MulticonIcons;
import org.mc.ui.BindingFrameView.IBindableView;
import org.mc.ui.net.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MulticonFrame implements IView<JFrame>
{
    /**  */
    private final StandardFrameView frameView;
    /**  */
    private final List<IBindableView> views;

    /***************************************************************************
     * 
     **************************************************************************/
    public MulticonFrame()
    {
        this.frameView = new StandardFrameView();
        this.views = new ArrayList<>();

        frameView.getView().setIconImages( MulticonIcons.getMulticonImages() );

        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frameView.setSize( 400, 400 );
        frameView.setTitle( "Multicon" );
        frameView.setContent( createContent() );

        frameView.getView().addWindowListener( new ClosingListener( this ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Container createContent()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        int r = 0;
        ActionListener l;

        l = ( e ) -> showView(
            new ConnectionBindableView( new MulticastView() ) );
        addButton( panel, MulticastView.NAME, l, r++ );

        l = ( e ) -> showView( new ConnectionBindableView( new UdpView() ) );
        addButton( panel, UdpView.NAME, l, r++ );

        l = ( e ) -> showView( new TcpServerView() );
        addButton( panel, TcpServerView.NAME, l, r++ );

        l = ( e ) -> showView(
            new ConnectionBindableView( new TcpClientView() ) );
        addButton( panel, TcpClientView.NAME, l, r++ );

        return panel;
    }

    /***************************************************************************
     * @param panel
     * @param text
     * @param listener
     * @param row
     **************************************************************************/
    private static void addButton( JPanel panel, String text,
        ActionListener listener, int row )
    {
        JButton button = new JButton( text );
        button.addActionListener( listener );
        GridBagConstraints constraints = new GridBagConstraints( 0, row, 1, 1,
            0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 6, 6, 6, 6 ), 6, 6 );
        panel.add( button, constraints );
    }

    /***************************************************************************
     * @param view
     **************************************************************************/
    private void showView( IBindableView view )
    {
        views.add( view );

        showBindingFrame( view, getView() );
    }

    /***************************************************************************
     * @param view
     * @param parent
     * @return
     **************************************************************************/
    public static BindingFrameView showBindingFrame( IBindableView view,
        Component parent )
    {
        Window window = SwingUtils.getComponentsWindow( parent );
        BindingFrameView frame = new BindingFrameView( view, parent );

        frame.getView().addWindowListener(
            new BindableClosingListener( view, parent ) );

        frame.getView().setIconImages( window.getIconImages() );
        frame.getView().pack();
        frame.getView().setLocationRelativeTo( parent );
        frame.getView().setVisible( true );

        return frame;
    }

    /***************************************************************************
     * @param view
     **************************************************************************/
    private void closeView( IBindableView view )
    {
        try
        {
            view.unbind();
        }
        catch( IOException ex )
        {
            OptionUtils.showErrorMessage( getView(), ex.getMessage(),
                "Socket Close Error" );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return frameView.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class ClosingListener extends WindowAdapter
    {
        /**  */
        private final MulticonFrame frame;

        /**
         * @param parent
         */
        public ClosingListener( MulticonFrame parent )
        {
            this.frame = parent;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void windowClosing( WindowEvent e )
        {
            for( IBindableView view : frame.views )
            {
                frame.closeView( view );
            }
        }
    }
}
