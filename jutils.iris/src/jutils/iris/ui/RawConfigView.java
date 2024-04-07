package jutils.iris.ui;

import javax.swing.JComponent;

import jutils.core.ui.StandardFormView;
import jutils.core.ui.fields.BooleanFormField;
import jutils.core.ui.fields.ComboFormField;
import jutils.core.ui.fields.IntegerFormField;
import jutils.core.ui.fields.NamedItemDescriptor;
import jutils.core.ui.fields.UsableFormField;
import jutils.core.ui.model.IDataView;
import jutils.core.utils.ByteOrdering;
import jutils.iris.data.PixelFormat;
import jutils.iris.data.RawConfig;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RawConfigView implements IDataView<RawConfig>
{
    /**  */
    private final JComponent view;
    /**  */
    private final IntegerFormField skipField;
    /**  */
    private final IntegerFormField gapField;
    /**  */
    private final ComboFormField<ByteOrdering> endiannessField;
    /**  */
    private final UsableFormField<Integer> imageCountField;
    /**  */
    private final IntegerFormField widthField;
    /**  */
    private final IntegerFormField heightField;
    /**  */
    private final IntegerFormField bitDepthField;
    /**  */
    private final BooleanFormField packedField;
    /**  */
    private final ComboFormField<PixelFormat> formatField;

    /**  */
    private RawConfig config;

    /***************************************************************************
     * 
     **************************************************************************/
    public RawConfigView()
    {
        this.skipField = new IntegerFormField( "Skip Length", 0, null );
        this.gapField = new IntegerFormField( "Gap Length", 0, null );
        this.endiannessField = new ComboFormField<>( "Endianness",
            ByteOrdering.values(), new NamedItemDescriptor<>() );
        this.imageCountField = new UsableFormField<>(
            new IntegerFormField( "Image Count", 1, null ) );

        this.widthField = new IntegerFormField( "Width", 1, null );
        this.heightField = new IntegerFormField( "Height", 1, null );
        this.bitDepthField = new IntegerFormField( "Bit Depth", 7, 16 );
        this.packedField = new BooleanFormField( "Packed" );
        this.formatField = new ComboFormField<>( "Pixel Format",
            PixelFormat.values(), new NamedItemDescriptor<>() );

        this.view = createView();

        setData( new RawConfig() );

        skipField.setUpdater( ( d ) -> config.fileHeaderLen = d );
        gapField.setUpdater( ( d ) -> config.imageHeaderLen = d );
        endiannessField.setUpdater( ( d ) -> config.endianness = d );
        imageCountField.setUpdater( ( d ) -> config.imageCount.set( d ) );

        widthField.setUpdater( ( d ) -> config.width = d );
        heightField.setUpdater( ( d ) -> config.height = d );
        bitDepthField.setUpdater( ( d ) -> config.bitDepth = d );
        packedField.setUpdater( ( d ) -> config.packed = d );
        formatField.setUpdater( ( d ) -> config.format = d );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        StandardFormView form = new StandardFormView();

        form.addField( skipField );
        form.addField( gapField );
        form.addField( endiannessField );
        form.addField( imageCountField );

        form.addField( widthField );
        form.addField( heightField );
        form.addField( bitDepthField );
        form.addField( packedField );
        form.addField( formatField );

        return form.getView();
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    public void setEditable( boolean editable )
    {
        skipField.setEditable( editable );
        gapField.setEditable( editable );
        endiannessField.setEditable( editable );
        imageCountField.setEditable( editable );

        widthField.setEditable( editable );
        heightField.setEditable( editable );
        bitDepthField.setEditable( editable );
        packedField.setEditable( editable );
        formatField.setEditable( editable );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return this.view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public RawConfig getData()
    {
        return this.config;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( RawConfig data )
    {
        this.config = data;

        skipField.setValue( config.fileHeaderLen );
        gapField.setValue( config.imageHeaderLen );
        endiannessField.setValue( config.endianness );
        imageCountField.setValue( config.imageCount );

        widthField.setValue( config.width );
        heightField.setValue( config.height );
        bitDepthField.setValue( config.bitDepth );
        packedField.setValue( config.packed );
        formatField.setValue( config.format );
    }
}
