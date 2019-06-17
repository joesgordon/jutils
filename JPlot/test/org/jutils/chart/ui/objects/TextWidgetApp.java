package org.jutils.chart.ui.objects;

import java.awt.*;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.chart.model.HorizontalAlignment;
import org.jutils.chart.model.TextLabel;
import org.jutils.chart.ui.IChartWidget;
import org.jutils.chart.ui.WidgetPanel;
import org.jutils.ui.JGoodiesToolBar;
import org.jutils.ui.StandardFrameView;
import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;

public class TextWidgetApp
{
    /***************************************************************************
     * 
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new IFrameApp()
        {
            @Override
            public void finalizeGui()
            {
            }

            @Override
            public JFrame createFrame()
            {
                StandardFrameView frameView = new StandardFrameView();
                JFrame frame = frameView.getView();
                TextLabel label = new TextLabel();
                final TextWidget w = new TextWidget( label,
                    TextDirection.RIGHT );
                JPanel panel = new JPanel( new GridBagLayout() );
                WidgetPanel wp = new WidgetPanel( new IChartWidget()
                {
                    @Override
                    public void draw( Graphics2D graphics, Point location,
                        Dimension size )
                    {
                        graphics = ( Graphics2D )graphics.create( location.x,
                            location.y, size.width, size.height );

                        Color c = graphics.getColor();
                        graphics.setColor( Color.white );
                        graphics.fillRect( 0, 0, size.width, size.height );
                        graphics.setColor( c );

                        size.width--;
                        size.height -= 2;

                        final Dimension ts = w.calculateSize( size );

                        Point p;
                        Dimension s;

                        p = new Point( 100, 0 );
                        s = new Dimension( ts.width, size.height );
                        w.draw( graphics, p, s );

                        // p = new Point( 200, 200 );
                        // s = ts;
                        // w.draw( graphics, p, s );

                        graphics.dispose();
                    }

                    @Override
                    public Dimension calculateSize( Dimension canvasSize )
                    {
                        return null;
                    }
                } );

                GridBagConstraints constraints;

                constraints = new GridBagConstraints( 1, 1, 1, 1, 1.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets( 0, 0, 0, 0 ), 0, 0 );
                panel.add( createToolbar(), constraints );
                constraints = new GridBagConstraints( 1, 2, 1, 1, 1.0, 1.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets( 0, 0, 0, 0 ), 0, 0 );
                panel.add( wp.getView(), constraints );

                label.alignment = HorizontalAlignment.CENTER;

                frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                frame.setSize( 500, 500 );
                frameView.setContent( panel );

                label.visible = true;
                label.text = "Y Values";

                frame.setTitle( "Testing TextWidget" );

                return frame;
            }

            private Component createToolbar()
            {
                JToolBar tb = new JGoodiesToolBar();

                JButton button = new JButton(
                    IconConstants.getIcon( IconConstants.OPEN_FILE_16 ) );

                tb.add( button );

                return tb;
            }
        } );
    }
}
