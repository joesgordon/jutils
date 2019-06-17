package org.jutils;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UtilsTest
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_splitSkip_emptyString_isEmpty()
    {
        String str = "";
        List<String> fields = Utils.splitSkip( str );

        Assert.assertEquals( true, fields.isEmpty() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_splitSkip_variableDelimsInMiddle_allSkipped()
    {
        String str = "f0 f1 \t f2   f3\tf4";
        List<String> fields = Utils.splitSkip( str );

        Assert.assertEquals( false, fields.isEmpty() );

        for( int i = 0; i < 5; i++ )
        {
            Assert.assertEquals( "f" + i, fields.get( i ) );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_splitSkip_delimsOnLeft_skipped()
    {
        String str = " \tf0 \t f1   f2\tf3";
        List<String> fields = Utils.splitSkip( str );

        Assert.assertEquals( false, fields.isEmpty() );

        for( int i = 0; i < 4; i++ )
        {
            Assert.assertEquals( "f" + i, fields.get( i ) );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_splitSkip_delimsOnRight_skipped()
    {
        String str = "f0 f1 \t f2   f3\tf4    ";
        List<String> fields = Utils.splitSkip( str );

        Assert.assertEquals( false, fields.isEmpty() );

        for( int i = 0; i < 5; i++ )
        {
            Assert.assertEquals( "f" + i, fields.get( i ) );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_split_emptyString_singleEmptyEntry()
    {
        String str = "";
        List<String> fields = Utils.split( str );

        Assert.assertArrayEquals( new String[] { "" }, fields.toArray() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_split_multDelimsInMiddle_retained()
    {
        String [] expected = { "f0", "f1", "", "", "f2", "", "", "f3", "f4" };
        String str = Utils.arrayToString( expected, " " );
        List<String> fields = Utils.split( str );

        Assert.assertArrayEquals( expected, fields.toArray() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_split_delimsOnLeft_retained()
    {
        String [] expected = { "", "", "", "", "f0", "f1", "f2", "f3", "f4" };
        String str = Utils.arrayToString( expected, " " );
        List<String> fields = Utils.split( str );

        Assert.assertArrayEquals( expected, fields.toArray() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_split_delimsOnRight_retained()
    {
        String [] expected = { "f0", "f1", "f2", "f3", "f4", "", "", "", "" };
        String str = Utils.arrayToString( expected, " " );
        List<String> fields = Utils.split( str );

        Assert.assertArrayEquals( expected, fields.toArray() );
    }
}
