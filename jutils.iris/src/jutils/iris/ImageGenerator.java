package jutils.iris;

import java.io.File;
import java.io.IOException;

import jutils.core.io.DataStream;
import jutils.core.io.FileStream;
import jutils.core.utils.ByteOrdering;
import jutils.iris.data.BayerOrder;

public class ImageGenerator
{
    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private ImageGenerator()
    {
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        for( BayerOrder order : BayerOrder.values() )
        {
            String ext;
            File file;
            IPixelGenerator pgen;

            ext = order.getExtension( 12, false );
            file = new File( "./test_green." + ext );
            pgen = ( x, y, g1, r, b,
                g2 ) -> ( short )( ( g1 || g2 ) ? 0xFFF : 0 );
            createFile( file, order, pgen );

            ext = order.getExtension( 12, false );
            file = new File( "./test_blue." + ext );
            pgen = ( x, y, g1, r, b, g2 ) -> ( short )( b ? 0xFFF : 0 );
            createFile( file, order, pgen );

            ext = order.getExtension( 12, false );
            file = new File( "./test_red." + ext );
            pgen = ( x, y, g1, r, b, g2 ) -> ( short )( r ? 0xFFF : 0 );
            createFile( file, order, pgen );
        }
    }

    /**
     * @param file
     * @param order
     * @param generator
     */
    public static void createFile( File file, BayerOrder order,
        IPixelGenerator generator )
    {
        int w = 512;
        int h = 512;

        try( FileStream fs = new FileStream( file );
             DataStream stream = new DataStream( fs,
                 ByteOrdering.LITTLE_ENDIAN ) )
        {
            boolean isEvenRow = true;
            for( int y = 0; y < h; y++ )
            {
                boolean isEvenCol = true;

                for( int x = 0; x < w; x++ )
                {
                    boolean isG1 = order.isGreen1( isEvenRow, isEvenCol );
                    boolean isR = order.isRed( isEvenRow, isEvenCol );
                    boolean isB = order.isBlue( isEvenRow, isEvenCol );
                    boolean isG2 = order.isGreen2( isEvenRow, isEvenCol );

                    short p = generator.generate( x, y, isG1, isR, isB, isG2 );

                    stream.writeShort( p );

                    isEvenCol = !isEvenCol;
                }
                isEvenRow = !isEvenRow;
            }
        }
        catch( IOException ex )
        {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static interface IPixelGenerator
    {
        /**
         * @param x
         * @param y
         * @param g1
         * @param r
         * @param b
         * @param g2
         * @return
         */
        public short generate( int x, int y, boolean g1, boolean r, boolean b,
            boolean g2 );
    }
}
