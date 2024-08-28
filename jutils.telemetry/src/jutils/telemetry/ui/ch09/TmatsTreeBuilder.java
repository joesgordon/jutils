package jutils.telemetry.ui.ch09;

import java.util.List;

import jutils.telemetry.data.ch09.GeneralInformation;
import jutils.telemetry.data.ch09.Information;
import jutils.telemetry.data.ch09.Tmats;
import jutils.telemetry.data.ch09.TmatsFile;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TmatsTreeBuilder
{
    /***************************************************************************
     * @param f
     * @return
     **************************************************************************/
    public static DataNode<TmatsFile> buildTree( TmatsFile f )
    {
        DataNode<TmatsFile> node = new DataNode<TmatsFile>( f,
            ( d, c, p ) -> createTmatsNodes( d.tmats, c, p ),
            () -> new TmatsFileView(), () -> null, null, f.getName() );

        return node;
    }

    /***************************************************************************
     * @param data
     * @param parent
     * @param children
     **************************************************************************/
    private static void createTmatsNodes( Tmats data,
        List<DataNode<?>> children, DataNode<?> parent )
    {
        DataNode<GeneralInformation> generalNode;

        generalNode = new DataNode<GeneralInformation>( data.general,
            ( d, c, p ) -> createGeneralNodes( d, c, p ),
            () -> new GeneralInformationView(), () -> null, parent,
            "General Information" );

        children.add( generalNode );
    }

    /***************************************************************************
     * @param data
     * @param children
     * @param parent
     **************************************************************************/
    private static void createGeneralNodes( GeneralInformation data,
        List<DataNode<?>> children, DataNode<?> parent )
    {
        DataNode<Information> generalNode;

        generalNode = new DataNode<Information>( data.information,
            ( d, c, p ) -> createInfomationNodes( d, c, p ),
            () -> new InformationView(), () -> null, parent, "Information" );

        children.add( generalNode );
    }

    /***************************************************************************
     * @param data
     * @param children
     * @param parent
     **************************************************************************/
    private static void createInfomationNodes( Information data,
        List<DataNode<?>> children, DataNode<?> parent )
    {
    }
}
