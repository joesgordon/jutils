package org.jutils.apps.filespy.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JPanel;

import org.jutils.apps.filespy.data.SearchParams;
import org.jutils.core.SwingUtils;
import org.jutils.core.io.parsers.ExistenceType;
import org.jutils.core.pattern.StringPattern;
import org.jutils.core.pattern.StringPatternField;
import org.jutils.core.ui.StandardFormView;
import org.jutils.core.ui.calendar.DateField;
import org.jutils.core.ui.event.ItemActionList;
import org.jutils.core.ui.event.ItemActionListener;
import org.jutils.core.ui.fields.BooleanFormField;
import org.jutils.core.ui.fields.FileFormField;
import org.jutils.core.ui.fields.LongFormField;
import org.jutils.core.ui.fields.UsableFormField;
import org.jutils.core.ui.model.IDataView;

/*******************************************************************************
 *
 ******************************************************************************/
public class SearchParamsView implements IDataView<SearchParams>
{
    /**  */
    private final JPanel view;

    /**  */
    private final StringPatternField filenameField;
    /**  */
    private final UsableFormField<StringPattern> contentsField;
    /**  */
    private final FileFormField pathField;

    /**  */
    private final UsableFormField<Long> moreThanField;
    /**  */
    private final UsableFormField<Long> lessThanField;

    /**  */
    private final UsableFormField<LocalDate> afterField;
    /**  */
    private final UsableFormField<LocalDate> beforeField;

    /**  */
    private final BooleanFormField fileNotCheckBox;
    /**  */
    private final BooleanFormField subfoldersField;

    /**  */
    private final ItemActionList<SearchParams> startListeners;

    /**  */
    private SearchParams params;

    /***************************************************************************
     *
     **************************************************************************/
    public SearchParamsView()
    {
        this.filenameField = new StringPatternField( "Filename" );
        this.contentsField = new UsableFormField<>(
            new StringPatternField( "Contents" ) );
        this.pathField = new FileFormField( "Search In",
            ExistenceType.DIRECTORY_ONLY );
        this.subfoldersField = new BooleanFormField( "Search Sub-directories" );
        this.moreThanField = new UsableFormField<>(
            new LongFormField( "More Than", null, 10 ) );
        this.lessThanField = new UsableFormField<>(
            new LongFormField( "Less Than", null, 10 ) );
        this.afterField = new UsableFormField<>( new DateField( "After" ) );
        this.beforeField = new UsableFormField<>( new DateField( "Before" ) );
        this.fileNotCheckBox = new BooleanFormField( "Specify Not Condition" );

        this.view = createView();
        // new StartKeyListener( this )

        this.startListeners = new ItemActionList<>();

        setData( new SearchParams() );

        filenameField.setUpdater( ( s ) -> params.filename.set( s ) );
        contentsField.setUpdater( ( u ) -> params.contents.set( u ) );
        pathField.setUpdater( ( f ) -> params.path = f );
        subfoldersField.setUpdater( ( b ) -> params.searchSubfolders = b );

        moreThanField.setUpdater( ( u ) -> params.moreThan.set( u ) );
        lessThanField.setUpdater( ( u ) -> params.lessThan.set( u ) );

        afterField.setUpdater( ( u ) -> params.after.set( u ) );
        beforeField.setUpdater( ( u ) -> params.before.set( u ) );

        fileNotCheckBox.setUpdater( ( b ) -> params.filenameNot = b );

        SwingUtils.addKeyListener( view, "ENTER",
            ( e ) -> startListeners.fireListeners( this, params ),
            "Entertostartsearch", false );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( createSearchPanel(), constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 4, 4, 4 ), 0, 0 );
        panel.add( createOptionsPanel(), constraints );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createOptionsPanel()
    {
        JPanel optionsPanel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 2 ), 0, 0 );
        optionsPanel.add( createFileOptionsPanel(), constraints );

        constraints = new GridBagConstraints( 2, 0, 1, 1, 0.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 2 ), 0, 0 );
        optionsPanel.add( createSizePanel(), constraints );

        constraints = new GridBagConstraints( 3, 0, 1, 1, 0.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        optionsPanel.add( createTimePanel(), constraints );

        constraints = new GridBagConstraints( 4, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        optionsPanel.add( Box.createHorizontalStrut( 0 ), constraints );

        optionsPanel.setMinimumSize( optionsPanel.getPreferredSize() );

        return optionsPanel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createSizePanel()
    {
        StandardFormView form = new StandardFormView();

        form.addField( moreThanField );
        form.addField( lessThanField );

        JPanel panel = form.getView();

        panel.setBorder( BorderFactory.createTitledBorder( "Size (kb)" ) );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createTimePanel()
    {
        StandardFormView form = new StandardFormView();

        form.addField( afterField );
        form.addField( beforeField );

        JPanel panel = form.getView();

        panel.setBorder( BorderFactory.createTitledBorder( "Time Options" ) );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createFileOptionsPanel()
    {
        StandardFormView form = new StandardFormView();

        form.addField( fileNotCheckBox );
        form.addField( subfoldersField );

        JPanel panel = form.getView();

        panel.setBorder(
            BorderFactory.createTitledBorder( "Name/Path Options" ) );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createSearchPanel()
    {
        StandardFormView form = new StandardFormView();

        form.addField( filenameField );
        form.addField( contentsField );
        form.addField( pathField );

        return form.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public SearchParams getData()
    {
        return params;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( SearchParams params )
    {
        params = params != null ? params : new SearchParams();

        this.params = params;

        filenameField.setValue( params.filename );
        contentsField.setValue( params.contents );

        pathField.setValue( params.path );

        moreThanField.setValue( params.moreThan );
        lessThanField.setValue( params.lessThan );

        afterField.setValue( params.after );
        beforeField.setValue( params.before );

        fileNotCheckBox.setValue( params.filenameNot );
        subfoldersField.setValue( params.searchSubfolders );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addStartListener( ItemActionListener<SearchParams> l )
    {
        startListeners.addListener( l );
    }
}
