package org.jutils.insomnia.data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * 
 ******************************************************************************/
public class InsomniaStatus
{
    /**  */
    public final InsomniaMetrics metrics;
    /**  */
    public final Activity currentActivity;
    /**  */
    public final List<Activity> log;

    /**  */
    public LocalDateTime startTime;

    /***************************************************************************
     * 
     **************************************************************************/
    public InsomniaStatus()
    {
        this.metrics = new InsomniaMetrics();
        this.currentActivity = new Activity();
        this.log = new ArrayList<>();
        this.startTime = LocalDateTime.now( ZoneOffset.UTC );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public final class Activity
    {
        /**  */
        public ActiveState state;
        /**  */
        public long duration;

        /**
         * 
         */
        public Activity()
        {
            this.state = ActiveState.ACTIVE;
            this.duration = 0L;
        }
    }
}
