package org.jutils.core.pcap.ui;

import javax.swing.JFrame;

import org.jutils.core.ui.StandardFrameView;
import org.jutils.core.ui.model.IView;

/*******************************************************************************
 *
 ******************************************************************************/
public class PcapngParserFrame implements IView<JFrame>
{
    /**  */
    private final StandardFrameView view;
    /**  */
    private final PcapngBlockView parserView;

    /***************************************************************************
     * 
     **************************************************************************/
    public PcapngParserFrame()
    {
        this.view = new StandardFrameView();
        this.parserView = new PcapngBlockView();

        view.setContent( parserView.getView() );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return view.getView();
    }
}
