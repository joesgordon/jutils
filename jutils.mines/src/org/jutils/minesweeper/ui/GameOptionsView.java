package org.jutils.minesweeper.ui;

import java.awt.Component;

import javax.swing.JComponent;

import org.jutils.core.ui.StandardFormView;
import org.jutils.core.ui.fields.BooleanFormField;
import org.jutils.core.ui.model.IDataView;
import org.jutils.minesweeper.data.GameOptions;

/***************************************************************************
 * 
 **************************************************************************/
public class GameOptionsView implements IDataView<GameOptions>
{
    /**  */
    private final JComponent view;

    /**  */
    private final BooleanFormField questionField;

    /**  */
    private GameOptions options;

    /***************************************************************************
     * 
     **************************************************************************/
    public GameOptionsView()
    {
        this.questionField = new BooleanFormField( "Question Marks" );

        this.view = createView();

        setData( new GameOptions() );

        questionField.setUpdater( ( d ) -> options.enableQuestion = d );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        StandardFormView form = new StandardFormView();

        form.addField( questionField );

        return form.getView();
    }

    /***************************************************************************
     * @{@inheritDoc}
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view;
    }

    /***************************************************************************
     * @{@inheritDoc}
     **************************************************************************/
    @Override
    public GameOptions getData()
    {
        return options;
    }

    /***************************************************************************
     * @{@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( GameOptions data )
    {
        this.options = data;

        questionField.setValue( data.enableQuestion );
    }
}
