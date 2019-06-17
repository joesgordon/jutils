package org.jutils.ui.model;

import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * @param <T> The type of items stored in the rows of the table.
 ******************************************************************************/
public class DefaultTableItemsConfig<T> implements ITableItemsConfig<T>
{
    /**  */
    private final List<ColCfg<T, ?>> cfgs;

    /***************************************************************************
     * 
     **************************************************************************/
    public DefaultTableItemsConfig()
    {
        this.cfgs = new ArrayList<>();
    }

    /***************************************************************************
     * @param <F>
     * @param name
     * @param clz
     * @param getter
     **************************************************************************/
    public <F> void addCol( String name, Class<F> clz,
        IFieldGetter<T, F> getter )
    {
        ColCfg<T, ?> cc = new ColCfg<>( name, clz, getter );
        cfgs.add( cc );
    }

    /***************************************************************************
     * @param <F>
     * @param name
     * @param clz
     * @param getter
     * @param setter
     **************************************************************************/
    public <F> void addCol( String name, Class<F> clz,
        IFieldGetter<T, F> getter, IFieldSetter<T, F> setter )
    {
        ColCfg<T, ?> cc = new ColCfg<>( name, clz, getter, setter );
        cfgs.add( cc );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String [] getColumnNames()
    {
        String [] names = new String[cfgs.size()];

        for( int i = 0; i < names.length; i++ )
        {
            names[i] = cfgs.get( i ).name;
        }

        return names;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Class<?> [] getColumnClasses()
    {
        Class<?> [] classes = new Class<?>[cfgs.size()];

        for( int i = 0; i < classes.length; i++ )
        {
            classes[i] = cfgs.get( i ).clz;
        }

        return classes;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Object getItemData( T item, int col )
    {
        return cfgs.get( col ).getter.get( item );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public <F> void setItemData( T item, int col, F field )
    {
        ColCfg<T, ?> ccobj = cfgs.get( col );

        if( ccobj.clz.equals( field.getClass() ) )
        {
            @SuppressWarnings( "unchecked")
            ColCfg<T, F> cc = ( ColCfg<T, F> )ccobj;
            cc.setter.set( item, field );
        }
        else
        {
            String msg = String.format(
                "Unable to cast an item of type %s to %s for column %s",
                field.getClass().toGenericString(), ccobj.clz.toGenericString(),
                ccobj.name );
            throw new IllegalArgumentException( msg );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean isCellEditable( T item, int col )
    {
        return cfgs.get( col ).isEditable;
    }

    /***************************************************************************
     * @param <ITEM>
     * @param <FIELD>
     **************************************************************************/
    public static interface IFieldGetter<ITEM, FIELD>
    {
        /**
         * @param field
         * @return
         */
        public FIELD get( ITEM item );
    }

    /***************************************************************************
     * @param <ITEM>
     * @param <FIELD>
     **************************************************************************/
    public static interface IFieldSetter<ITEM, FIELD>
    {
        /**
         * @param item
         * @param field
         */
        public void set( ITEM item, FIELD field );
    }

    /***************************************************************************
     * @param <I>
     * @param <F>
     **************************************************************************/
    private static final class VoidSetter<I, F> implements IFieldSetter<I, F>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public void set( I item, F field )
        {
        }
    }

    /***************************************************************************
     * @param <ITEM>
     * @param <FIELD>
     **************************************************************************/
    private static final class ColCfg<ITEM, FIELD>
    {
        /**  */
        public final String name;
        /**  */
        public final Class<FIELD> clz;
        /**  */
        public final IFieldGetter<ITEM, FIELD> getter;
        /**  */
        public final IFieldSetter<ITEM, FIELD> setter;
        /**  */
        public final boolean isEditable;

        /**
         * @param name
         * @param clz
         * @param getter
         */
        public ColCfg( String name, Class<FIELD> clz,
            IFieldGetter<ITEM, FIELD> getter )
        {
            this( name, clz, getter, null );
        }

        /**
         * @param name
         * @param clz
         * @param getter
         * @param setter
         */
        public ColCfg( String name, Class<FIELD> clz,
            IFieldGetter<ITEM, FIELD> getter, IFieldSetter<ITEM, FIELD> setter )
        {
            this.name = name;
            this.clz = clz;
            this.isEditable = setter != null;
            this.getter = getter;
            this.setter = isEditable ? setter : new VoidSetter<ITEM, FIELD>();
        }
    }
}
