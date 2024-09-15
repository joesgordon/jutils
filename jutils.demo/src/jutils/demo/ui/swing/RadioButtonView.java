package jutils.demo.ui.swing;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import jutils.core.ui.StandardFormView;
import jutils.core.ui.model.IView;

/*******************************************************************************
 * Defines a view that displays all states of a {@link JRadioButton}.
 ******************************************************************************/
public class RadioButtonView implements IView<JComponent>
{
    /**  */
    private final JPanel view;

    /***************************************************************************
     * Creates a view that displays all states of a {@link JRadioButton}.
     **************************************************************************/
    public RadioButtonView()
    {
        this.view = createView();
    }

    /***************************************************************************
     * Creates a panel to display
     * @return the component for this view.
     **************************************************************************/
    private static JPanel createView()
    {
        StandardFormView form = new StandardFormView();
        JRadioButton btn;
        ButtonGroup group = new ButtonGroup();

        btn = new JRadioButton( "Button Text" );
        form.addField( "Unselected", btn );
        group.add( btn );

        btn = new JRadioButton( "Button Text" );
        btn.setSelected( true );
        form.addField( "Selected", btn );
        group.add( btn );

        btn = new JRadioButton( "Button Text" );
        btn.setEnabled( false );
        form.addField( "Unselected (Disabled)", btn );
        group.add( btn );

        btn = new JRadioButton( "Button Text" );
        btn.setSelected( true );
        btn.setEnabled( false );
        form.addField( "Selected (Disabled)", btn );

        return form.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

}
