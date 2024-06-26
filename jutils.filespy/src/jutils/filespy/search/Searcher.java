package jutils.filespy.search;

import jutils.core.concurrent.TaskThread;
import jutils.core.ui.StatusBarPanel;
import jutils.core.ui.event.ItemActionEvent;
import jutils.core.ui.event.ItemActionListener;
import jutils.core.utils.Stopwatch;
import jutils.filespy.data.SearchParams;
import jutils.filespy.ui.ResultsView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Searcher
{
    /**  */
    private final ResultsView resultsView;
    /**  */
    private final StatusBarPanel statusBar;

    /**  */
    private TaskThread searchThread;

    /***************************************************************************
     * @param resultsView
     * @param fileSpyFrameView
     * @param statusBar
     **************************************************************************/
    public Searcher( ResultsView resultsView, StatusBarPanel statusBar )
    {
        this.resultsView = resultsView;
        this.statusBar = statusBar;
    }

    /***************************************************************************
     * @param params
     * @param finishedListener
     **************************************************************************/
    public void search( SearchParams params,
        ItemActionListener<Long> finishedListener )
    {
        Stopwatch stopwatch = new Stopwatch();
        ConsumerFinalizer finalizer = new ConsumerFinalizer( stopwatch,
            finishedListener );
        SearchResultsHandler handler = new SearchResultsHandler( resultsView,
            statusBar );

        statusBar.setStatusText( "" );
        resultsView.clearPanel();

        SearchTask searchTask = new SearchTask( handler, params, finalizer );

        searchThread = new TaskThread( searchTask, "FileSpy Search Thread" );
        searchThread.start();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void cancel()
    {
        searchThread.stop();
        searchThread.interrupt();
        searchThread.stopAndWait();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ConsumerFinalizer implements Runnable
    {
        /**  */
        private final Stopwatch stopwatch;
        /**  */
        private final ItemActionListener<Long> finishedListener;

        public ConsumerFinalizer( Stopwatch stopwatch,
            ItemActionListener<Long> finishedListener )
        {
            this.stopwatch = stopwatch;
            this.finishedListener = finishedListener;
        }

        @Override
        public void run()
        {
            stopwatch.stop();
            finishedListener.actionPerformed(
                new ItemActionEvent<Long>( this, stopwatch.getElapsed() ) );
        }
    }
}
