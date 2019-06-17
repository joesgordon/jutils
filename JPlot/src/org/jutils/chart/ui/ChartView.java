package org.jutils.chart.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.Utils;
import org.jutils.ValidationException;
import org.jutils.chart.ChartIcons;
import org.jutils.chart.IPalette;
import org.jutils.chart.PresetPalette;
import org.jutils.chart.app.JChartAppConstants;
import org.jutils.chart.app.UserData;
import org.jutils.chart.data.SaveOptions;
import org.jutils.chart.io.DataFileReader;
import org.jutils.chart.model.Chart;
import org.jutils.chart.model.ChartOptions.PointRemovalMethod;
import org.jutils.chart.model.IDataPoint;
import org.jutils.chart.model.ISeriesData;
import org.jutils.chart.model.Interval;
import org.jutils.chart.model.Series;
import org.jutils.chart.ui.event.ChartMouseListenter;
import org.jutils.chart.ui.event.SaveSeriesDataListener;
import org.jutils.chart.ui.objects.ChartWidget;
import org.jutils.chart.ui.objects.PlotContext;
import org.jutils.chart.ui.objects.PlotWidget;
import org.jutils.io.IOUtils;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.ui.JGoodiesToolBar;
import org.jutils.ui.OkDialogView;
import org.jutils.ui.OkDialogView.OkDialogButtons;
import org.jutils.ui.RecentFilesViews;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.event.FileChooserListener;
import org.jutils.ui.event.FileChooserListener.IFilesSelected;
import org.jutils.ui.event.FileChooserListener.ILastFiles;
import org.jutils.ui.event.FileDropTarget;
import org.jutils.ui.event.FileDropTarget.DropActionType;
import org.jutils.ui.event.FileDropTarget.IFileDropEvent;
import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.fields.ComboFormField;
import org.jutils.ui.fields.NamedItemDescriptor;
import org.jutils.ui.model.IDataView;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChartView implements IView<JComponent>
{
    /**  */
    private final JPanel view;
    /**  */
    private final WidgetPanel mainPanel;
    /**  */
    private final ChartWidget chartWidget;
    /**  */
    private final IPalette palette;
    /**  */
    private final PropertiesView propertiesView;
    /**  */
    private final JToolBar toolbar;
    /**  */
    private final ComboFormField<PointRemovalMethod> pointsField;
    /**  */
    private final JSeparator separator;

    /**  */
    private final RecentFilesViews recentFiles;

    /**  */
    private final ItemActionList<File> fileLoadedListeners;

    /**  */
    private final OptionsSerializer<UserData> options;

    /**  */
    public final Chart chart;

    /**  */
    public JDialog propertiesDialog;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChartView()
    {
        this( true, true );
    }

    /***************************************************************************
     * @param allowOpen
     * @param gradientToolbar
     **************************************************************************/
    public ChartView( boolean allowOpen, boolean gradientToolbar )
    {
        this.options = JChartAppConstants.getOptions();
        this.chart = new Chart();
        this.mainPanel = new WidgetPanel();
        this.chartWidget = new ChartWidget( chart );
        this.palette = new PresetPalette();
        this.propertiesView = new PropertiesView( chart );
        this.recentFiles = new RecentFilesViews();

        this.pointsField = new ComboFormField<>( "Point Removal: ",
            PointRemovalMethod.values(), new NamedItemDescriptor<>() );
        this.toolbar = createToolbar( allowOpen, gradientToolbar );
        this.separator = new JSeparator();
        this.view = createView();

        this.fileLoadedListeners = new ItemActionList<>();

        recentFiles.setData( options.getOptions().recentFiles.toList() );
        recentFiles.setListeners( ( f, c ) -> importData( f, c ) );

        // mainPanel.setBorder( new LineBorder( Color.blue, 4 ) );
        mainPanel.setObject( chartWidget );

        JComponent mainComp = mainPanel.getView();

        ChartMouseListenter ml = new ChartMouseListenter( this, chartWidget,
            mainComp );

        mainComp.addComponentListener( new ChartComponentListener( this ) );
        mainComp.addMouseListener( ml );
        mainComp.addMouseMotionListener( ml );
        mainComp.addMouseWheelListener( ml );

        if( allowOpen )
        {
            addFileLoadedListener( new FileLoadedListener( this ) );
            mainComp.setDropTarget(
                new FileDropTarget( new ChartDropTarget( this ) ) );
        }

        mainComp.setFocusable( true );

        Action action = new ActionAdapter( ( e ) -> removeSelectedPoints(),
            "delete_point", null );

        SwingUtils.addKeyListener( mainComp, "DELETE", action,
            JComponent.WHEN_FOCUSED );

        mainComp.setMinimumSize( new Dimension( 150, 150 ) );
    }

    /***************************************************************************
     * @param allowOpen
     * @param gradientToolbar
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( toolbar, constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( separator, constraints );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( mainPanel.getView(), constraints );

        return panel;
    }

    /***************************************************************************
     * @param allowOpen
     * @param gradientToolbar
     * @return
     **************************************************************************/
    private JToolBar createToolbar( boolean allowOpen, boolean gradientToolbar )
    {
        JToolBar toolbar;

        if( gradientToolbar )
        {
            toolbar = new JGoodiesToolBar();
        }
        else
        {
            toolbar = new JToolBar();
            SwingUtils.setToolbarDefaults( toolbar );
        }

        if( allowOpen )
        {
            recentFiles.install( toolbar, createOpenListener() );
        }

        SwingUtils.addActionToToolbar( toolbar, createSaveAction() );

        SwingUtils.addActionToToolbar( toolbar, createSaveDataAction() );

        toolbar.addSeparator();

        SwingUtils.addActionToToolbar( toolbar, createPropertiesAction() );

        toolbar.addSeparator();

        SwingUtils.addActionToToolbar( toolbar, createZoomInAction() );

        SwingUtils.addActionToToolbar( toolbar, createZoomOutAction() );

        JPanel panel = new JPanel( new FlowLayout( FlowLayout.CENTER, 0, 0 ) );

        pointsField.setValue( PointRemovalMethod.NAN );

        panel.setOpaque( false );
        panel.add( new JLabel( pointsField.getName() ) );
        panel.add( pointsField.getView() );
        panel.setMaximumSize( panel.getPreferredSize() );
        toolbar.add( panel );

        toolbar.add( Box.createHorizontalGlue() );

        return toolbar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Action createOpenAction()
    {
        Action action;
        Icon icon;
        String name;

        name = "Open";
        icon = IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 );
        action = new ActionAdapter( createOpenListener(), name, icon );

        return action;
    }

    private ActionListener createOpenListener()
    {
        OpenListener ol = new OpenListener( this );
        FileChooserListener listener = new FileChooserListener( getView(),
            "Choose Data File", false, ol, ol );
        // TODO Auto-generated method stub
        return listener;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Action createSaveAction()
    {
        Action action;
        ActionListener listener;
        Icon icon;
        String name;

        name = "Save";
        icon = IconConstants.getIcon( IconConstants.SAVE_16 );
        listener = new SaveListener( this );
        action = new ActionAdapter( listener, name, icon );

        return action;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Action createSaveDataAction()
    {
        Action action;
        ActionListener listener;
        Icon icon;
        String name;

        name = "Save Data";
        icon = IconConstants.getIcon( IconConstants.SAVE_AS_16 );
        listener = new SaveDataListener( this );
        action = new ActionAdapter( listener, name, icon );

        return action;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Action createPropertiesAction()
    {
        Action action;
        ActionListener listener;
        Icon icon;
        String name;

        name = "Properties";
        icon = IconConstants.getIcon( IconConstants.CONFIG_16 );
        listener = new PropertiesDialogListener( this );
        action = new ActionAdapter( listener, name, icon );

        return action;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createZoomInAction()
    {
        Action action;
        ActionListener listener;
        Icon icon;
        String name;

        name = "Zoom In";
        icon = ChartIcons.getIcon( ChartIcons.ZOOM_IN_016 );
        listener = new ZoomInListener( this );
        action = new ActionAdapter( listener, name, icon );

        return action;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createZoomOutAction()
    {
        Action action;
        ActionListener listener;
        Icon icon;
        String name;

        name = "Zoom Out";
        icon = ChartIcons.getIcon( ChartIcons.ZOOM_OUT_016 );
        listener = new ZoomOutListener( this );
        action = new ActionAdapter( listener, name, icon );

        return action;
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addFileLoadedListener( ItemActionListener<File> l )
    {
        fileLoadedListeners.addListener( l );
    }

    /***************************************************************************
     * @param s
     **************************************************************************/
    public void addSeries( Series s )
    {
        addSeries( s, false );
    }

    /***************************************************************************
     * @param s
     * @param addData
     **************************************************************************/
    public void addSeries( Series s, boolean addData )
    {
        if( !addData )
        {
            clear();
            propertiesView.removeAllSeries();
        }

        if( s.data.getCount() == 0 )
        {
            return;
        }

        chart.series.add( s );
        chartWidget.plots.plots.add( new PlotWidget( s, chartWidget.context ) );
        propertiesView.addSeries( s, chart.series.size() );

        chartWidget.context.calculateAutoBounds( chart.series );
        zoomRestore();
        repaintChart();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void recalculateBounds()
    {
        chartWidget.context.calculateAutoBounds( chart.series );
        zoomRestore();
        repaintChart();
    }

    /***************************************************************************
     * @param files
     **************************************************************************/
    public void importData( List<File> files )
    {
        boolean addData = false;

        clear();

        for( File file : files )
        {
            importData( file, addData );
            addData = true;
        }
    }

    /***************************************************************************
     * @param file
     * @param addData
     **************************************************************************/
    public Series addSeries( ISeriesData<?> data, String name, String resource,
        boolean addData )
    {
        Series s = new Series( data );

        Color c;
        if( addData )
        {
            c = palette.next();
        }
        else
        {
            palette.reset();
            c = palette.next();
        }

        s.name = name;
        s.resource = resource;
        s.marker.color = c;
        s.highlight.color = c;
        s.line.color = c;

        addSeries( s, addData );

        // LogUtils.printDebug( String.format( "x => (%f,%f)",
        // chart.plot.context.xMin, chart.plot.context.xMax ) );
        //
        // LogUtils.printDebug( String.format( "y => (%f,%f)",
        // chart.plot.context.yMin, chart.plot.context.yMax ) );
        // LogUtils.printDebug( "" );

        return s;
    }

    /***************************************************************************
     * @param file
     * @param addData
     **************************************************************************/
    public void importData( File file, boolean addData )
    {
        if( !addData )
        {
            palette.reset();
        }

        try
        {
            DataFileReader reader = new DataFileReader();
            ISeriesData<?> data = reader.read( file );

            addSeries( data, IOUtils.removeFilenameExtension( file ),
                file.getAbsolutePath(), addData );
        }
        catch( FileNotFoundException ex )
        {
            JOptionPane.showMessageDialog( mainPanel.getView(),
                "The file was not found: " + file.getAbsolutePath(),
                "File Not Found", JOptionPane.ERROR_MESSAGE );
            return;
        }
        catch( IOException ex )
        {
            JOptionPane.showMessageDialog( mainPanel.getView(),
                "I/O Exception: " + ex.getMessage(), "I/O Exception",
                JOptionPane.ERROR_MESSAGE );
            return;
        }
        catch( ValidationException ex )
        {
            JOptionPane.showMessageDialog( mainPanel.getView(),
                "Format Error: " + ex.getMessage(), "Format Error",
                JOptionPane.ERROR_MESSAGE );
            return;
        }

        if( chartWidget.plots.plots.size() < 2 )
        {
            setTitle( file.getName() );
        }

        fileLoadedListeners.fireListeners( this, file );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void clear()
    {
        chart.title.text = "Title";
        chart.title.visible = false;
        chart.domainAxis.title.text = "Domain";
        chart.domainAxis.title.visible = false;
        chart.rangeAxis.title.text = "Range";
        chart.rangeAxis.title.visible = false;
        chart.secDomainAxis.title.text = "Secondary Domain";
        chart.secDomainAxis.title.visible = false;
        chart.secRangeAxis.title.text = "Secondary Range";
        chart.secRangeAxis.title.visible = false;
        chart.legend.visible = false;

        propertiesView.clear();

        chart.series.clear();
        chartWidget.clear();

        // restoreAndRepaintChart();
        chartWidget.calculateAutoBounds();
        chartWidget.latchBounds();
        repaintChart();
    }

    /***************************************************************************
     * @param min
     * @param max
     **************************************************************************/
    public void setPrimaryRangeBounds( double min, double max )
    {
        chart.rangeAxis.setBounds( new Interval( min, max ) );

        chartWidget.context.latchCoords();

        chartWidget.plots.repaint();
        mainPanel.repaint();
    }

    /***************************************************************************
     * @param visible
     **************************************************************************/
    public void setToolbarVisible( boolean visible )
    {
        toolbar.setVisible( visible );
        separator.setVisible( visible );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void zoomRestore()
    {
        chartWidget.context.restoreAutoBounds();
        chartWidget.plots.repaint();
        chartWidget.axes.axesLayer.repaint = true;
        mainPanel.repaint();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void zoomIn( ZoomDirection dir )
    {
        if( dir.zoomHorizontal )
        {
            chart.domainAxis.setBounds( chart.domainAxis.getBounds().zoomIn() );

            if( chart.secDomainAxis.isUsed )
            {
                chart.secDomainAxis.setBounds(
                    chart.secDomainAxis.getBounds().zoomIn() );
            }
        }

        if( dir.zoomVertical )
        {
            chart.rangeAxis.setBounds( chart.rangeAxis.getBounds().zoomIn() );

            if( chart.secRangeAxis.isUsed )
            {
                chart.secRangeAxis.setBounds(
                    chart.secRangeAxis.getBounds().zoomIn() );
            }
        }

        chartWidget.context.latchCoords();

        chartWidget.plots.repaint();
        chartWidget.axes.axesLayer.repaint = true;
        mainPanel.repaint();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void zoomOut( ZoomDirection dir )
    {
        if( dir.zoomHorizontal )
        {
            chart.domainAxis.setBounds(
                chart.domainAxis.getBounds().zoomOut() );

            if( chart.secDomainAxis.isUsed )
            {
                chart.secDomainAxis.setBounds(
                    chart.secDomainAxis.getBounds().zoomOut() );
            }
        }

        if( dir.zoomVertical )
        {
            chart.rangeAxis.setBounds( chart.rangeAxis.getBounds().zoomOut() );

            if( chart.secRangeAxis.isUsed )
            {
                chart.secRangeAxis.setBounds(
                    chart.secRangeAxis.getBounds().zoomOut() );
            }
        }

        chartWidget.context.latchCoords();

        chartWidget.plots.repaint();
        chartWidget.axes.axesLayer.repaint = true;
        mainPanel.repaint();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    // private void restoreAndRepaintChart()
    // {
    // chartWidget.restoreAutoBounds();
    // repaintChart();
    // }

    /***************************************************************************
     * 
     **************************************************************************/
    private void repaintChart()
    {
        chartWidget.repaint();
        mainPanel.repaint();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void removeSelectedPoints()
    {
        // System.out.println( "Deleting points..." );

        for( PlotWidget series : chartWidget.plots.plots )
        {
            for( IDataPoint xy : series.series.data )
            {
                if( xy.isSelected() )
                {
                    xy.setHidden( true );
                    xy.setSelected( false );
                }
            }
        }

        PlotContext context = chartWidget.context;

        if( context.isAutoBounds() )
        {
            context.calculateAutoBounds( chart.series );
            context.restoreAutoBounds();
        }
        else
        {
            context.calculateAutoBounds( chart.series );
        }

        chartWidget.plots.repaint();
        chartWidget.axes.axesLayer.repaint = true;
        mainPanel.repaint();
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
     * @param title
     **************************************************************************/
    public void setTitle( String title )
    {
        chart.title.text = title;
        chartWidget.title.repaint();
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    public void saveAsImage( File file )
    {
        saveAsImage( file, new Dimension( 640, 480 ) );
    }

    /***************************************************************************
     * @param file
     * @param size
     **************************************************************************/
    public void saveAsImage( File file, Dimension size )
    {
        view.setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );

        int w = size.width;
        int h = size.height;

        BufferedImage image = Utils.createTransparentImage( w, h );
        Graphics2D g2d = image.createGraphics();

        g2d.setRenderingHint( RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BICUBIC );
        g2d.setRenderingHint( RenderingHints.KEY_FRACTIONALMETRICS,
            RenderingHints.VALUE_FRACTIONALMETRICS_ON );
        g2d.setRenderingHint( RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY );
        g2d.setRenderingHint( RenderingHints.KEY_STROKE_CONTROL,
            RenderingHints.VALUE_STROKE_PURE );

        repaintChart();
        chartWidget.setTrackingVisible( false );
        chartWidget.draw( g2d, new Point(), size );
        chartWidget.setTrackingVisible( true );

        try
        {
            if( !ImageIO.write( image, "png", file ) )
            {
                throw new IllegalStateException(
                    "No writer found for PNG files!" );
            }
        }
        catch( IOException ex )
        {
            JOptionPane.showMessageDialog( mainPanel.getView(),
                "I/O Error: " + ex.getMessage(), "I/O Error",
                JOptionPane.ERROR_MESSAGE );
        }

        view.setCursor( Cursor.getDefaultCursor() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void closeOptions()
    {
        clear();
        if( propertiesDialog != null )
        {
            propertiesDialog.dispose();
        }
    }

    /***************************************************************************
     * @param seriesIdx
     * @param pointIdx
     **************************************************************************/
    public void setSelectedSeries( int seriesIdx, int pointIdx )
    {
        propertiesView.setSelected( seriesIdx, pointIdx );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Component getOpenMenu()
    {
        return recentFiles.getMenu();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ChartDropTarget
        implements ItemActionListener<IFileDropEvent>
    {
        private final ChartView view;

        public ChartDropTarget( ChartView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ItemActionEvent<IFileDropEvent> event )
        {
            IFileDropEvent fde = event.getItem();
            List<File> files = fde.getFiles();

            boolean addData = fde.getActionType() == DropActionType.COPY;

            for( int i = 0; i < files.size(); i++ )
            {
                view.importData( files.get( i ), addData );
                addData = true;
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class OpenListener implements ILastFiles, IFilesSelected
    {
        private final ChartView view;

        public OpenListener( ChartView view )
        {
            this.view = view;
        }

        @Override
        public File [] getLastFiles()
        {
            File f = view.options.getOptions().recentFiles.first();
            return f == null ? new File[] {} : new File[] { f };
        }

        @Override
        public void filesChosen( File [] files )
        {
            List<File> fileList = Arrays.asList( files );

            view.importData( fileList );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class SaveListener
        implements ActionListener, ItemActionListener<Boolean>
    {
        private final ChartView view;

        private SaveView saveView;

        public SaveListener( ChartView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            saveView = new SaveView();
            SaveOptions options = new SaveOptions();
            OkDialogView okView = new OkDialogView( view.getView(),
                saveView.getView(), ModalityType.DOCUMENT_MODAL,
                OkDialogButtons.OK_CANCEL );
            JDialog dialog = okView.getView();

            options.file = getDefaultFile();
            options.size.width = view.mainPanel.getView().getWidth();
            options.size.height = view.mainPanel.getView().getHeight();

            saveView.setData( options );

            okView.addOkListener( this );

            dialog.setTitle( "Save Chart" );
            dialog.pack();
            dialog.setLocationRelativeTo( view.getView() );
            dialog.setVisible( true );
        }

        @Override
        public void actionPerformed( ItemActionEvent<Boolean> event )
        {
            if( !event.getItem() )
            {
                return;
            }

            SaveOptions options = saveView.getData();

            view.options.getOptions().lastImageFile = options.file;
            view.options.write();

            view.saveAsImage( options.file, options.size );
        }

        public File getDefaultFile()
        {
            File f = view.options.getOptions().lastImageFile;

            if( f == null )
            {
                f = view.options.getOptions().recentFiles.first();

                if( f != null )
                {
                    f = IOUtils.replaceExtension( f, "png" );
                }
            }

            return f;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class SaveDataListener implements ActionListener
    {
        private final ChartView view;

        public SaveDataListener( ChartView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            FileChooserListener listener;

            for( Series s : view.chart.series )
            {
                IDataView<Series> sv = new SeriesViewAdapter( s, view );
                SaveSeriesDataListener ssdl = new SaveSeriesDataListener( sv );
                listener = new FileChooserListener( view.getView(),
                    "Choose File to Save", true, ssdl, ssdl );

                listener.actionPerformed( new ActionEvent( this, 0, null ) );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class SeriesViewAdapter implements IDataView<Series>
    {
        private final Series s;
        private final ChartView view;

        public SeriesViewAdapter( Series s, ChartView view )
        {
            this.s = s;
            this.view = view;
        }

        @Override
        public Component getView()
        {
            return view.getView();
        }

        @Override
        public Series getData()
        {
            return s;
        }

        @Override
        public void setData( Series data )
        {
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ChartComponentListener extends ComponentAdapter
    {
        private final ChartView view;

        public ChartComponentListener( ChartView view )
        {
            this.view = view;
        }

        @Override
        public void componentResized( ComponentEvent e )
        {
            view.chartWidget.axes.axesLayer.repaint = true;
            view.chartWidget.plots.repaint();
            view.chartWidget.plots.highlightLayer.clear();
            view.chartWidget.plots.highlightLayer.repaint = false;
            view.mainPanel.repaint();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class SeriesChangedEvent
    {
        public final Series s;
        public final int index;
        public final boolean added;

        public SeriesChangedEvent( Series s, int index, boolean added )
        {
            this.s = s;
            this.index = index;
            this.added = added;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class PropertiesDialogListener
        implements ActionListener, ItemActionListener<Boolean>
    {
        private final ChartView view;

        public PropertiesDialogListener( ChartView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            JDialog dialog = createDialog();

            if( !dialog.isVisible() )
            {
                dialog.setVisible( true );
            }
            else
            {
                dialog.toFront();
            }
        }

        private JDialog createDialog()
        {
            // if( view.propertiesDialog == null )
            // {
            OkDialogView okView = new OkDialogView( view.getView(),
                view.propertiesView.getView(), ModalityType.MODELESS,
                OkDialogButtons.OK_APPLY );

            view.propertiesDialog = okView.getView();

            okView.addOkListener( this );

            view.propertiesDialog.setIconImages( ChartIcons.getChartImages() );
            view.propertiesDialog.setTitle( "Chart Properties" );
            view.propertiesDialog.setSize( 650, 400 );
            view.propertiesDialog.validate();
            view.propertiesDialog.setLocationRelativeTo( view.getView() );
            // }

            return view.propertiesDialog;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void actionPerformed( ItemActionEvent<Boolean> event )
        {
            if( event.getItem() )
            {
                view.chartWidget.calculateAutoBounds();
                view.chartWidget.latchBounds();
                view.repaintChart();
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ZoomInListener implements ActionListener
    {
        /**  */
        private final ChartView view;

        /**
         * @param view
         */
        public ZoomInListener( ChartView view )
        {
            this.view = view;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void actionPerformed( ActionEvent e )
        {
            int mods = e.getModifiers();
            boolean shift = ( mods & InputEvent.SHIFT_DOWN_MASK ) != 0;
            boolean ctrl = ( mods & InputEvent.CTRL_DOWN_MASK ) != 0;

            view.zoomIn( ZoomDirection.get( shift, ctrl ) );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ZoomOutListener implements ActionListener
    {
        /**  */
        private final ChartView view;

        /**
         * @param view
         */
        public ZoomOutListener( ChartView view )
        {
            this.view = view;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void actionPerformed( ActionEvent e )
        {
            int mods = e.getModifiers();
            boolean shift = ( mods & InputEvent.SHIFT_DOWN_MASK ) != 0;
            boolean ctrl = ( mods & InputEvent.CTRL_DOWN_MASK ) != 0;

            view.zoomOut( ZoomDirection.get( shift, ctrl ) );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FileLoadedListener implements ItemActionListener<File>
    {
        /**  */
        private final ChartView view;

        /**
         * @param view
         */
        public FileLoadedListener( ChartView view )
        {
            this.view = view;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void actionPerformed( ItemActionEvent<File> event )
        {
            view.options.getOptions().recentFiles.push( event.getItem() );
            view.options.write();
            view.recentFiles.setData(
                view.options.getOptions().recentFiles.toList() );
        }
    }
}
