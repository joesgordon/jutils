package org.jutils.ui.fields;

import java.io.File;

import javax.swing.JComponent;

import org.jutils.io.parsers.ExistenceType;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.validation.*;

/*******************************************************************************
 * Defines a form field that edits a file path.
 ******************************************************************************/
public class FileFormField implements IDataFormField<File>
{
    /** The name of this field. */
    private final String name;
    /**
     * The validation view that provides feedback to the user when the field is
     * invalid.
     */
    private final ValidationView view;
    /** The file validation field. */
    private final FileField field;

    /** The callback invoked when the control is changed by the user. */
    private IUpdater<File> updater;
    /**
     * If {@code true}, the data is being set programmatically; {@code false}
     * indicates that data is being set by the user.
     */
    private boolean settingData;

    /***************************************************************************
     * Creates a file form field with the provided name and an
     * {@link ExistenceType} of {@link ExistenceType#FILE_ONLY} that is required
     * which shows "Save" text and a browse button.
     * @param name the name of this field.
     **************************************************************************/
    public FileFormField( String name )
    {
        this( name, ExistenceType.FILE_ONLY, true, false );
    }

    /***************************************************************************
     * Creates a file form field with the provided name and
     * {@link ExistenceType} that ensures the file exists if the existence type
     * is {@link ExistenceType#FILE_ONLY} and shows "Save" text and a browse
     * button.
     * @param name the name of this field.
     * @param existence type of existence to be checked: file/dir/either/none.
     **************************************************************************/
    public FileFormField( String name, ExistenceType existence )
    {
        this( name, existence, true, existence != ExistenceType.FILE_ONLY );
    }

    /***************************************************************************
     * Creates a file form field with the provided name and
     * {@link ExistenceType} that ensures the file exists if {@code required}
     * and shows "Save" text and a browse button.
     * @param name the name of this field.
     * @param existence type of existence to be checked: file/dir/either/none.
     * @param required if the path can be empty or is required.
     **************************************************************************/
    public FileFormField( String name, ExistenceType existence,
        boolean required )
    {
        this( name, existence, required, true );
    }

    /***************************************************************************
     * Creates a file form field with the provided name and
     * {@link ExistenceType} that ensures the file exists if {@code required}
     * and shows "Save" text if {@code isSave} and shows a browse button.
     * @param name the name of this field.
     * @param existence type of existence to be checked.
     * @param required if the path can be empty or is required.
     * @param isSave if the path is to be be save to (alt. read from).
     **************************************************************************/
    public FileFormField( String name, ExistenceType existence,
        boolean required, boolean isSave )
    {
        this( name, existence, required, isSave, true );
    }

    /***************************************************************************
     * Creates a file form field with the provided name and
     * {@link ExistenceType} that ensures the file exists if {@code required}
     * and shows "Save" text if {@code isSave} and shows a browse button if
     * {@code showBrowse}.
     * @param name the name of this field.
     * @param existence type of existence to be checked.
     * @param required if the path can be empty or is required.
     * @param isSave if the path is to be be save to (alt. read from).
     * @param showBrowse denotes whether the browse button should be shown.
     **************************************************************************/
    public FileFormField( String name, ExistenceType existence,
        boolean required, boolean isSave, boolean showBrowse )
    {
        this.name = name;
        this.field = new FileField( existence, required, isSave, showBrowse );
        this.view = new ValidationView( field );
        this.settingData = false;
    }

    /***************************************************************************
     * Calls the updater with the provided file if non-{@code null}.
     * @param file the file used when calling the updater.
     **************************************************************************/
    private void callUpdater( File file )
    {
        if( !settingData && updater != null )
        {
            updater.update( file );
        }
    }

    /***************************************************************************
     * Adds the provided extension description and list of exts. <p><code>
     * addExtension( "JPEG Files", "jpg", jpeg" ); </code></p>
     * @param description a short description of the type of file denoted by the
     * provided list of extensions.
     * @param extensions list of extensions with no '.'.
     **************************************************************************/
    public void addExtension( String description, String... extensions )
    {
        field.addExtension( description, extensions );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return name;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public File getValue()
    {
        return field.getData();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( File value )
    {
        this.settingData = true;
        field.setData( value );
        this.settingData = false;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<File> updater )
    {
        this.updater = updater;

        field.addUpdater( ( f ) -> callUpdater( f ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<File> getUpdater()
    {
        return updater;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        field.setEditable( editable );
        view.setEditable( editable );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        field.addValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        field.removeValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return field.getValidity();
    }
}
