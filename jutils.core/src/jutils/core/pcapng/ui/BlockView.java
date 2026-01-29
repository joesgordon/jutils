package jutils.core.pcapng.ui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jutils.core.Utils;
import jutils.core.pcapng.IBlock;
import jutils.core.ui.fields.StringAreaFormField;
import jutils.core.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BlockView implements IDataView<IBlock>
{
    /**  */
    private final JComponent view;

    /**  */
    private final StringAreaFormField descriptionField;

    /**  */
    private IBlock block;

    /***************************************************************************
     * 
     **************************************************************************/
    public BlockView()
    {
        this.descriptionField = new StringAreaFormField( "" );
        this.view = new JPanel( new BorderLayout() );

        JScrollPane pane = new JScrollPane( descriptionField.getTextArea() );

        view.add( pane, BorderLayout.CENTER );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IBlock getData()
    {
        return block;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( IBlock data )
    {
        this.block = data;

        try
        {
            descriptionField.setValue( block.getDescription() );
        }
        catch( RuntimeException ex )
        {
            descriptionField.setValue( Utils.printStackTrace( ex ) );
        }

        descriptionField.getTextArea().setCaretPosition( 0 );
    }
}
