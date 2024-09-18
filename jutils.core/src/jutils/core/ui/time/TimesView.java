package jutils.core.ui.time;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import jutils.core.IconConstants;
import jutils.core.SwingUtils;
import jutils.core.time.TimeUtils;
import jutils.core.ui.StandardFormView;
import jutils.core.ui.StandardFrameView;
import jutils.core.ui.app.AppRunner;
import jutils.core.ui.event.ActionAdapter;
import jutils.core.ui.fields.IDataFormField;
import jutils.core.ui.model.IDataView;

/*******************************************************************************
 * Defines a view that allows a user to define times in a variety of ways.
 ******************************************************************************/
public class TimesView implements IDataView<LocalDateTime>
{
    /**  */
    private final JPanel view;

    /**  */
    private final DateAndTimeField dateAndTimeField;
    /**  */
    private final DateTimeField dateTimeField;
    /**  */
    private final YearSecondsField yearSecsField;

    // TODO Add Linux time

    // TODO Add Microsoft Filetime

    // TODO Add GPS Week

    // TODO Add Day of Week

    // TODO Add Week of Year

    // TODO Add Year/Day of Year/Seconds into Day

    // TODO Add GPS time

    // TODO Add Julian time

    // TODO Add Sidereal time

    /**  */
    private final List<IDataFormField<LocalDateTime>> fields;

    /**  */
    private LocalDateTime time;

    /***************************************************************************
     * 
     **************************************************************************/
    public TimesView()
    {
        this.dateAndTimeField = new DateAndTimeField( "Date & Time" );
        this.dateTimeField = new DateTimeField( "Date/Time" );
        this.yearSecsField = new YearSecondsField( "Year/Seconds" );

        this.fields = new ArrayList<>();

        fields.add( dateAndTimeField );
        fields.add( dateTimeField );
        // fields.add( yearSecsField );

        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( createToolbar(), BorderLayout.NORTH );
        panel.add( createForm(), BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createToolbar()
    {
        JToolBar toolbar = new JToolBar();

        SwingUtils.setToolbarDefaults( toolbar );

        SwingUtils.addActionToToolbar( toolbar, createNowAction() );

        return toolbar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createNowAction()
    {
        Icon icon = IconConstants.getIcon( IconConstants.TODAY_16 );
        ActionListener listener = ( e ) -> setData( TimeUtils.utcNow() );
        return new ActionAdapter( listener, "Now", icon );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createForm()
    {
        StandardFormView form = new StandardFormView();

        for( IDataFormField<LocalDateTime> f : fields )
        {
            form.addField( f );
        }

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

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public LocalDateTime getData()
    {
        return this.time;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( LocalDateTime data )
    {
        this.time = data;

        for( IDataFormField<LocalDateTime> f : fields )
        {
            f.setValue( time );
        }
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.DEFAULT_LAF = AppRunner.JGOODIES_LAF;

        AppRunner.invokeLater( () -> createFrame() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static JFrame createFrame()
    {
        StandardFrameView view = new StandardFrameView();
        JFrame frame = view.getView();
        TimesView times = new TimesView();

        view.setTitle( "Kitsune" );
        view.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        view.setSize( 500, 800 );
        view.setContent( times.getView() );

        return frame;
    }
}
