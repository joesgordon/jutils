package org.mc.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.IOException;

import javax.swing.*;

import org.jutils.core.*;
import org.jutils.core.ui.JGoodiesToolBar;
import org.jutils.core.ui.StandardFrameView;
import org.jutils.core.ui.model.IView;
import org.mc.MulticonIcons;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BindingFrameView implements IView<JFrame>
{
    /**  */
    public static final String BIND_TEXT = "Bind";
    /**  */
    public static final String UNBIND_TEXT = "Unbind";

    /**  */
    private final IBindableView bindableView;
    /**  */
    private final StandardFrameView frameView;
    /**  */
    private final JButton bindButton;

    /**  */
    private final Icon bindIcon;
    /**  */
    private final Icon unbindIcon;

    /**  */
    private boolean bound;

    /***************************************************************************
     * @param bindableView
     * @param parent
     **************************************************************************/
    public BindingFrameView( IBindableView bindableView, Component parent )
    {
        this.bindableView = bindableView;
        this.frameView = new StandardFrameView();
        this.bindButton = new JButton();

        this.bindIcon = MulticonIcons.getIcon( MulticonIcons.MULTICON_016 );
        this.unbindIcon = IconConstants.getIcon( IconConstants.STOP_16 );

        this.bound = false;

        JFrame baseFrame = SwingUtils.getComponentsJFrame( parent );

        if( baseFrame != null )
        {
            frameView.getView().setIconImages( baseFrame.getIconImages() );
        }

        frameView.setTitle( bindableView.getName() );
        frameView.setContent( createView( bindableView ) );

        setButtonBound( bindableView.isBound() );
    }

    /***************************************************************************
     * @param parent
     * @param bindableView2
     * @param configView
     * @return
     **************************************************************************/
    private JPanel createView( IBindableView bindableView )
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( createToolbar(), BorderLayout.NORTH );
        panel.add( bindableView.getView(), BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createToolbar()
    {
        JToolBar toolbar = new JGoodiesToolBar();

        SwingUtils.setToolbarDefaults( toolbar );

        toolbar.add( bindButton );
        bindButton.setIcon( bindIcon );
        bindButton.setText( BIND_TEXT );
        bindButton.addActionListener( ( e ) -> toggleBound() );

        return toolbar;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void toggleBound()
    {
        setButtonBound( !bound );

        try
        {
            if( bound )
            {
                bindableView.unbind();
            }
            else
            {
                bindableView.bind();
            }
            this.bound = !bound;
        }
        catch( IOException ex )
        {
            setButtonBound( bound );
            OptionUtils.showErrorMessage( getView(), ex.getMessage(),
                "Unable to bind" );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return frameView.getView();
    }

    /***************************************************************************
     * @param bound
     **************************************************************************/
    private void setButtonBound( boolean bound )
    {
        bindButton.setText( bound ? UNBIND_TEXT : BIND_TEXT );
        bindButton.setIcon( bound ? unbindIcon : bindIcon );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isBound()
    {
        return bound;
    }

    /***************************************************************************
     *
     **************************************************************************/
    public static interface IBindableView extends IView<JComponent>
    {
        /**
         * @return
         */
        public String getName();

        public boolean isBound();

        /**
         * @throws IOException
         */
        public void bind() throws IOException;

        /**
         * @throws IOException
         */
        public void unbind() throws IOException;
    }
}
