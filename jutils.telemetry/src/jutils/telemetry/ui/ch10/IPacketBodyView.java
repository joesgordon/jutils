package jutils.telemetry.ui.ch10;

import jutils.core.ui.model.IDataView;
import jutils.telemetry.data.ch10.IPacketBody;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public interface IPacketBodyView<T extends IPacketBody> extends IDataView<T>
{
    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean hasScrollbar();
}
