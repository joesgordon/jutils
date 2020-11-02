package org.jutils.plot.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;

import org.jutils.core.IconConstants;
import org.jutils.core.OptionUtils;
import org.jutils.core.SwingUtils;
import org.jutils.core.Utils;
import org.jutils.core.ValidationException;
import org.jutils.core.io.IOUtils;
import org.jutils.core.io.options.OptionsSerializer;
import org.jutils.core.ui.JGoodiesToolBar;
import org.jutils.core.ui.OkDialogView;
import org.jutils.core.ui.OkDialogView.OkDialogButtons;
import org.jutils.core.ui.RecentFilesViews;
import org.jutils.core.ui.event.ActionAdapter;
import org.jutils.core.ui.event.FileChooserListener;
import org.jutils.core.ui.event.FileChooserListener.IFilesSelected;
import org.jutils.core.ui.event.FileChooserListener.ILastFiles;
import org.jutils.core.ui.event.FileDropTarget;
import org.jutils.core.ui.event.FileDropTarget.DropActionType;
import org.jutils.core.ui.event.ItemActionList;
import org.jutils.core.ui.event.ItemActionListener;
import org.jutils.core.ui.model.IDataView;
import org.jutils.core.ui.model.IView;
import org.jutils.plot.ChartIcons;
import org.jutils.plot.IPalette;
import org.jutils.plot.PresetPalette;
import org.jutils.plot.app.PlotConstants;
import org.jutils.plot.app.UserData;
import org.jutils.plot.data.SaveOptions;
import org.jutils.plot.io.DataFileReader;
import org.jutils.plot.model.Chart;
import org.jutils.plot.model.IDataPoint;
import org.jutils.plot.model.ISeriesData;
import org.jutils.plot.model.Interval;
import org.jutils.plot.model.Series;
import org.jutils.plot.ui.event.ChartMouseListenter;
import org.jutils.plot.ui.event.SaveSeriesDataListener;
import org.jutils.plot.ui.objects.ChartWidget;
import org.jutils.plot.ui.objects.PlotContext;
import org.jutils.plot.ui.objects.PlotWidget;

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
     * Creates a new chart view that allows open and uses a gradient (instead of
     * flat) toolbar.
     **************************************************************************/
    public ChartView()
    {
        this( true, true );
    }

    /***************************************************************************
     * Creates a new chart.
     * @param allowOpen if {@code true}, an Open button is added to the toolbar
     * and a drop target is added to the chart that allows the user to drag
     * files to be read.
     * @param gradientToolbar paints the background on the toolbar that is
     * gradient (if {@code true}) or flat (if {@code false}).
     **************************************************************************/
    public ChartView( boolean allowOpen, boolean gradientToolbar )
    {
        this( allowOpen, gradientToolbar, true );
    }

    /***************************************************************************
     * Creates a new chart.
     * @param allowOpen if {@code true}, an Open button is added to the toolbar
     * and a drop target is added to the chart that allows the user to drag
     * files to be read.
     * @param gradientToolbar paints the background on the toolbar that is
     * gradient (if {@code true}) or flat (if {@code false}).
     * @param showConfigurationButton
     **************************************************************************/
    public ChartView( boolean allowOpen, boolean gradientToolbar,
        boolean showConfigurationButton )
    {
        this( allowOpen, gradientToolbar, showConfigurationButton, true );
    }

    /***************************************************************************
     * Creates a new chart.
     * @param allowOpen if {@code true}, an Open button is added to the toolbar
     * and a drop target is added to the chart that allows the user to drag
     * files to be read.
     * @param gradientToolbar paints the background on the toolbar that is
     * gradient (if {@code true}) or flat (if {@code false}).
     * @param showConfigurationButton
     * @param allowSave
     **************************************************************************/
    public ChartView( boolean allowOpen, boolean gradientToolbar,
        boolean showConfigurationButton, boolean allowSave )
    {
        this.options = PlotConstants.getOptions();
        this.chart = new Chart();
        this.mainPanel = new WidgetPanel();
        this.chartWidget = new ChartWidget( chart );
        this.palette = new PresetPalette();
        this.propertiesView = new PropertiesView( chart );
        this.recentFiles = new RecentFilesViews();

        this.toolbar = createToolbar( allowOpen, gradientToolbar,
            showConfigurationButton, allowSave );
        this.separator = new JSeparator();
        this.view = createView();

        this.fileLoadedListeners = new ItemActionList<>();

        this.propertiesDialog = null;

        recentFiles.setData( options.getOptions().recentFiles.toList() );
        recentFiles.setListeners( ( f, c ) -> handleImport( f, c ) );

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
            addFileLoadedListener( ( e ) -> handleFileLoaded( e.getItem() ) );
            mainComp.setDropTarget( new FileDropTarget(
                ( e ) -> handleImport( e.getItem().getFiles(),
                    e.getItem().getActionType() == DropActionType.COPY ) ) );
        }

        mainComp.setFocusable( true );

        Action action = new ActionAdapter( ( e ) -> removeSelectedPoints(),
            "delete_point", null );

        SwingUtils.addKeyListener( mainComp, "DELETE", action,
            JComponent.WHEN_FOCUSED );

        mainComp.setMinimumSize( new Dimension( 150, 150 ) );
    }

    /***************************************************************************
     * @param f
     **************************************************************************/
    private void handleFileLoaded( File f )
    {
        options.getOptions().recentFiles.push( f );
        options.write();
        recentFiles.setData( options.getOptions().recentFiles.toList() );
    }

    /***************************************************************************
     * @return the main view for this control.
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
     * @param allowOpen installs the {@link #recentFiles} on the toolbar if
     * {@code true}.
     * @param gradientToolbar creates a gradient toolbar if {@code true}; flat
     * if {@code false}.
     * @param showConfigurationButton
     * @param allowSave
     * @return the new toolbar.
     **************************************************************************/
    private JToolBar createToolbar( boolean allowOpen, boolean gradientToolbar,
        boolean showConfigurationButton, boolean allowSave )
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

        if( allowSave )
        {
            SwingUtils.addActionToToolbar( toolbar, createSaveAction() );

            SwingUtils.addActionToToolbar( toolbar, createSaveDataAction() );
        }

        if( toolbar.getComponentCount() > 0 )
        {
            toolbar.addSeparator();
        }

        if( showConfigurationButton )
        {
            SwingUtils.addActionToToolbar( toolbar, createPropertiesAction() );

            toolbar.addSeparator();
        }

        SwingUtils.addActionToToolbar( toolbar, createZoomInAction() );

        SwingUtils.addActionToToolbar( toolbar, createZoomOutAction() );

        // JPanel panel = new JPanel( new FlowLayout( FlowLayout.CENTER, 0, 0 )
        // );
        //
        // pointsField.setValue( PointRemovalMethod.NAN );
        //
        // panel.setOpaque( false );
        // panel.add( new JLabel( pointsField.getName() ) );
        // panel.add( pointsField.getView() );
        // panel.setMaximumSize( panel.getPreferredSize() );
        // toolbar.add( panel );

        // toolbar.add( Box.createHorizontalGlue() );

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

    /***************************************************************************
     * @return
     **************************************************************************/
    private ActionListener createOpenListener()
    {
        ILastFiles ilf = () -> {
            File f = options.getOptions().recentFiles.first();
            return f == null ? new File[] {} : new File[] { f };
        };

        IFilesSelected ifs = ( files ) -> {
            List<File> fileList = Arrays.asList( files );
            handleImport( fileList );
        };

        FileChooserListener listener = new FileChooserListener( getView(),
            "Choose Data File", false, ifs, ilf );

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
        listener = ( e ) -> saveImage();
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
        listener = ( e ) -> saveData();
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
        listener = ( e ) -> showProperties();
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
        listener = ( e ) -> zoomIn( ZoomDirection.get( e ) );
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
        listener = ( e ) -> zoomOut( ZoomDirection.get( e ) );
        action = new ActionAdapter( listener, name, icon );

        return action;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void showProperties()
    {
        OkDialogView okView = new OkDialogView( getView(),
            propertiesView.getView(), ModalityType.MODELESS,
            OkDialogButtons.OK_APPLY );

        propertiesDialog = okView.getView();

        ItemActionListener<Boolean> applyListener = ( b ) -> {
            chartWidget.calculateAutoBounds();
            chartWidget.latchBounds();
            repaintChart();
            zoomRestore();
        };

        okView.addOkListener( applyListener );

        okView.show( "Chart Properties", ChartIcons.getChartImages(),
            new Dimension( 650, 400 ) );
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
    private void handleImport( List<File> files )
    {
        handleImport( files, false );
    }

    /***************************************************************************
     * @param files the list of files dropped.
     * @param addData whether the files are to be added to the current series if
     * {@code true}, or replaces the current series if {@code false}.
     **************************************************************************/
    private void handleImport( List<File> files, boolean addData )
    {
        SwingUtils.getComponentsWindow( getView() ).setCursor(
            new Cursor( Cursor.WAIT_CURSOR ) );

        importData( files, addData );

        SwingUtils.getComponentsWindow( getView() ).setCursor(
            new Cursor( Cursor.DEFAULT_CURSOR ) );
    }

    /***************************************************************************
     * @param file
     * @param addData
     **************************************************************************/
    private void handleImport( File file, boolean addData )
    {
        SwingUtils.getComponentsWindow( getView() ).setCursor(
            new Cursor( Cursor.WAIT_CURSOR ) );

        importData( file, addData );

        SwingUtils.getComponentsWindow( getView() ).setCursor(
            new Cursor( Cursor.DEFAULT_CURSOR ) );
    }

    /***************************************************************************
     * @param files
     * @param addData
     **************************************************************************/
    public void importData( List<File> files, boolean addData )
    {
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
            OptionUtils.showErrorMessage( mainPanel.getView(),
                "The file was not found: " + file.getAbsolutePath(),
                "File Not Found" );
            return;
        }
        catch( IOException ex )
        {
            OptionUtils.showErrorMessage( mainPanel.getView(),
                "I/O Exception: " + ex.getMessage(), "I/O Exception" );
            return;
        }
        catch( ValidationException ex )
        {
            OptionUtils.showErrorMessage( mainPanel.getView(),
                "Format Error: " + ex.getMessage(), "Format Error" );
            return;
        }

        if( chartWidget.plots.plots.size() < 2 )
        {
            setTitle( file.getName() );
        }

        fileLoadedListeners.fireListeners( this, file );
    }

    /***************************************************************************
     * @param data
     * @param name
     * @param resource
     * @param file
     * @param addData
     * @return
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
     * Adds a method to draw on top of the plots.
     * @param drawer the callback invoked after the plots are drawn.
     **************************************************************************/
    public void addOverDrawCallback( IPlotDrawer drawer )
    {
        chartWidget.addOverDrawCallback( drawer );
    }

    /***************************************************************************
     * Adds a method to draw under the plots.
     * @param drawer the callback invoked after the are drawn.
     **************************************************************************/
    public void addUnderDrawCallback( IPlotDrawer drawer )
    {
        chartWidget.addUnderDrawCallback( drawer );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public JToolBar getToolbar()
    {
        return toolbar;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void clear()
    {
        chart.clear();

        propertiesView.clear();

        chartWidget.clear();

        // restoreAndRepaintChart();
        chartWidget.calculateAutoBounds();
        chartWidget.latchBounds();
        repaintChart();
    }

    /**
     * 
     */
    public void saveImage()
    {
        SaveView saveView = new SaveView();
        SaveOptions options = new SaveOptions();
        OkDialogView okView = new OkDialogView( getView(), saveView.getView(),
            ModalityType.DOCUMENT_MODAL, OkDialogButtons.OK_CANCEL );
        JDialog dialog = okView.getView();

        options.file = getDefaultFile();
        options.size.width = mainPanel.getView().getWidth();
        options.size.height = mainPanel.getView().getHeight();

        saveView.setData( options );

        dialog.setTitle( "Save Chart" );

        Window w = SwingUtils.getComponentsWindow( getView() );

        if( okView.show( "Save Chart", w.getIconImages() ) )
        {
            options = saveView.getData();
            File file = options.file;

            String ext = IOUtils.getFileExtension( file );

            if( ext.isEmpty() )
            {
                String name = file.getName();

                ext = name.endsWith( "." ) ? "png" : ".png";
                file = new File( file.getParentFile(), name + ext );
            }

            this.options.getOptions().lastImageFile = file;
            this.options.write();

            try
            {
                saveAsImage( file, options.size );
            }
            catch( IllegalArgumentException ex )
            {
                OptionUtils.showErrorMessage( getView(), ex.getMessage(),
                    "Save Error" );
            }
        }
    }

    /**
     * @return
     */
    private File getDefaultFile()
    {
        File f = options.getOptions().lastImageFile;

        if( f == null )
        {
            f = options.getOptions().recentFiles.first();

            if( f != null )
            {
                f = IOUtils.replaceExtension( f, "png" );
            }
        }

        return f;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void saveData()
    {
        FileChooserListener listener;

        for( Series s : chart.series )
        {
            IDataView<Series> sv = new SeriesViewAdapter( s, this );
            SaveSeriesDataListener ssdl = new SaveSeriesDataListener( sv,
                chart.options.removalMethod );
            listener = new FileChooserListener( getView(),
                "Choose File to Save", true, ssdl, ssdl );

            listener.actionPerformed( new ActionEvent( this, 0, null ) );
        }
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

        if( !chart.options.removalEnabled )
        {
            return;
        }

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
        throws IllegalArgumentException
    {
        view.setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );

        String ext = IOUtils.getFileExtension(
            file.getAbsoluteFile() ).toUpperCase();

        boolean isTransparent = false;

        switch( ext )
        {
            case "PNG":
                ext = "PNG";
                isTransparent = true;
                break;

            case "JPG":
                ext = "JPEG";
                isTransparent = false;
                break;

            case "BMP":
                ext = "BMP";
                isTransparent = false;
                break;

            default:
                throw new IllegalArgumentException(
                    "Cannot save images of type " + ext );
        }

        int w = size.width;
        int h = size.height;

        BufferedImage image = isTransparent
            ? Utils.createTransparentImage( w, h )
            : Utils.createImage( w, h, Transparency.OPAQUE );
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
            if( !ImageIO.write( image, ext, file ) )
            {
                throw new IllegalArgumentException(
                    "No writer found for " + ext + " files!" );
            }
        }
        catch( IOException ex )
        {
            OptionUtils.showErrorMessage( mainPanel.getView(),
                "I/O Error: " + ex.getMessage(), "I/O Error" );
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
     * @param enabled
     **************************************************************************/
    public void setRemovalEnabled( boolean enabled )
    {
        chart.options.removalEnabled = enabled;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class SeriesViewAdapter implements IDataView<Series>
    {
        /**  */
        private final Series s;
        /**  */
        private final ChartView view;

        /**
         * @param s
         * @param view
         */
        public SeriesViewAdapter( Series s, ChartView view )
        {
            this.s = s;
            this.view = view;
        }

        /**
         * @{@inheritDoc}
         */
        @Override
        public Component getView()
        {
            return view.getView();
        }

        /**
         * @{@inheritDoc}
         */
        @Override
        public Series getData()
        {
            return s;
        }

        /**
         * @{@inheritDoc}
         */
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
        /**  */
        private final ChartView view;

        /**
         * @param view
         */
        public ChartComponentListener( ChartView view )
        {
            this.view = view;
        }

        /**
         * @{@inheritDoc}
         */
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
}
