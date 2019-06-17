package org.jutils.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*******************************************************************************
 *
 ******************************************************************************/
public class FindOptions
{
    /**  */
    public String textToFind = "";
    /**  */
    public boolean matchCase = false;
    /**  */
    public boolean wrapAround = true;
    /**  */
    public boolean useRegex = false;
    /**  */
    public transient Pattern pattern = null;
    /**  */
    public transient Matcher matcher = null;
}
