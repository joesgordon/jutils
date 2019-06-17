package org.jutils.ui;

import org.jutils.Utils;

public class VerboseMessageViewTestApp
{
    public static void main( String [] args )
    {
        VerboseMessageView.invoke( null, "Title", "Message",
            "Here is a super long message" + Utils.NEW_LINE +
                "Multiline even." );
    }
}
