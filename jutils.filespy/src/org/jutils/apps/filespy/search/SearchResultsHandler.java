package org.jutils.apps.filespy.search;

import javax.swing.SwingUtilities;

import org.jutils.apps.filespy.data.SearchRecord;
import org.jutils.apps.filespy.ui.ResultsView;
import org.jutils.core.ui.StatusBarPanel;

/*******************************************************************************
 *
 ******************************************************************************/
public class SearchResultsHandler
{
    /**  */
    private final ResultsView resultsView;
    /**  */
    private final StatusBarPanel statusBar;

    /***************************************************************************
     * @param panel SearchPanel
     * @param statusBar
     **************************************************************************/
    public SearchResultsHandler( ResultsView resultsView,
        StatusBarPanel statusBar )
    {
        this.resultsView = resultsView;
        this.statusBar = statusBar;
    }

    /***************************************************************************
     * @param record SearchRecord
     **************************************************************************/
    public void addFile( SearchRecord record )
    {
        // LogUtils.printDebug( "Found record for file " +
        // record.getFile().getAbsolutePath() );
        SwingUtilities.invokeLater( new UiFileAdder( resultsView, record ) );
    }

    /***************************************************************************
     * @param list List
     **************************************************************************/
    // public void addFiles( List<? extends IExplorerItem> list )
    // {
    // SwingUtilities.invokeLater( new UiFilesAdder( resultsView, list ) );
    // }

    /***************************************************************************
     * @param messge
     **************************************************************************/
    public static void addErrorMessage( String message )
    {
        SwingUtilities.invokeLater( new UiErrorHandler( message ) );
    }

    /***************************************************************************
     * @param message
     **************************************************************************/
    public void updateStatus( String message )
    {
        SwingUtilities.invokeLater( new StatusUpdater( message, statusBar ) );

    }

    /***************************************************************************
     *
     **************************************************************************/
    private static class UiErrorHandler implements Runnable
    {
        private final String msg;

        public UiErrorHandler( String message )
        {
            msg = message;
        }

        @Override
        public void run()
        {
            System.err.println( msg );
            // TODO add error to UI.
        }
    }

    /***************************************************************************
     *
     **************************************************************************/
    private static class UiFileAdder implements Runnable
    {
        private final ResultsView resultsView;

        private final SearchRecord record;

        public UiFileAdder( ResultsView resultsView, SearchRecord record )
        {
            this.resultsView = resultsView;
            this.record = record;
        }

        @Override
        public void run()
        {
            resultsView.addRecord( record );
            // LogUtils.printDebug( "Adding record: " +
            // record.getFile().toString()
            // );
        }
    }

    /***************************************************************************
     *
     **************************************************************************/
    private static class StatusUpdater implements Runnable
    {
        /**  */
        private final StatusBarPanel statusBar;
        /**  */
        private final String msg;

        public StatusUpdater( String message, StatusBarPanel statusBar )
        {
            this.msg = message;
            this.statusBar = statusBar;
        }

        @Override
        public void run()
        {
            statusBar.setText( msg );
        }
    }
}
