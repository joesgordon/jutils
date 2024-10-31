package jutils.demo.ui;

import java.util.function.Supplier;

import jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public interface IViewCreator extends Supplier<IView<?>>
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    IView<?> get();
}
