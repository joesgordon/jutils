package org.jutils.core.ui;

import org.jutils.core.Utils;

public class VerboseMessageViewTestApp
{
    public static void main( String [] args )
    {
        VerboseMessageView.invoke( null, "Title", "Message",
            "Here is a super long message" + Utils.NEW_LINE +
                "Multiline even." );
    }
}
