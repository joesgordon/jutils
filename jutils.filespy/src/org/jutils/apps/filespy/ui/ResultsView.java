package org.jutils.apps.filespy.ui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import org.jutils.apps.filespy.data.LineMatch;
import org.jutils.apps.filespy.data.SearchRecord;
import org.jutils.core.OptionUtils;
import org.jutils.core.Utils;
import org.jutils.core.concurrent.GcThread;
import org.jutils.core.io.LogUtils;
import org.jutils.core.ui.ScrollableEditorPaneView;
import org.jutils.core.ui.explorer.ExplorerTable;
import org.jutils.core.ui.explorer.ExplorerTableModel;
import org.jutils.core.ui.explorer.IExplorerItem;
import org.jutils.core.ui.model.IView;

/*******************************************************************************
 *
 ******************************************************************************/
public class ResultsView implements IView<JComponent>
{
    /**  */
    private final JSplitPane view;
    /**  */
    private final ExplorerTable resultsTableView;
    /**  */
    private final ExplorerTableModel resultsTableModel;

    /**  */
    private final ScrollableEditorPaneView rightResultsPane;
    /**  */
    private final DefaultStyledDocument defStyledDocument;

    /***************************************************************************
     * 
     **************************************************************************/
    public ResultsView()
    {
        this.view = new JSplitPane( JSplitPane.VERTICAL_SPLIT );
        this.resultsTableView = new ExplorerTable();
        this.resultsTableModel = resultsTableView.getExplorerTableModel();
        this.rightResultsPane = new ScrollableEditorPaneView();
        this.defStyledDocument = new DefaultStyledDocument();

        JTable resultsTable = resultsTableView.getView();

        // resultsTable.setAutoCreateRowSorter( true );
        resultsTable.getSelectionModel().addListSelectionListener(
            ( e ) -> resultsSelected( e ) );
        ToolTipManager.sharedInstance().unregisterComponent( resultsTable );
        ToolTipManager.sharedInstance().unregisterComponent(
            resultsTable.getTableHeader() );
        resultsTable.setAutoCreateRowSorter( true );
        resultsTable.setBackground( Color.white );
        resultsTable.addMouseListener( new OpenResultsListener( this ) );

        JScrollPane leftResultsScroll = new JScrollPane();
        leftResultsScroll.setViewportView( resultsTable );
        leftResultsScroll.getViewport().setBackground( Color.white );
        leftResultsScroll.setMinimumSize( new Dimension( 150, 150 ) );

        JScrollPane rightResultsScroll = new JScrollPane();
        rightResultsScroll.setViewportView( rightResultsPane.getView() );
        rightResultsScroll.setMinimumSize( new Dimension( 150, 150 ) );

        rightResultsPane.setDocument( defStyledDocument );
        rightResultsPane.setEditable( false );
        rightResultsPane.setBackground( Color.white );

        view.setResizeWeight( 0.0 );
        view.setLeftComponent( leftResultsScroll );
        view.setRightComponent( rightResultsScroll );
        view.setDividerLocation( 300 );
    }

    /***************************************************************************
     * @param e ActionEvent
     **************************************************************************/
    private void resultsSelected( ListSelectionEvent e )
    {
        int index = resultsTableView.getView().getSelectedRow();

        if( !e.getValueIsAdjusting() && index > -1 )
        {
            SearchRecord record = ( SearchRecord )resultsTableView.getSelectedItem();
            java.util.List<LineMatch> lines = record.getLinesFound();
            Style plain = StyleContext.getDefaultStyleContext().getStyle(
                StyleContext.DEFAULT_STYLE );
            Style matchStyle = rightResultsPane.addStyle( "match", plain );
            Style headerStyle = rightResultsPane.addStyle( "header", plain );

            StyleConstants.setBold( matchStyle, true );
            StyleConstants.setUnderline( matchStyle, true );
            StyleConstants.setForeground( matchStyle, new Color( 0x0A246A ) );

            StyleConstants.setBold( headerStyle, true );
            StyleConstants.setUnderline( headerStyle, true );
            StyleConstants.setFontSize( headerStyle, 16 );

            // LogUtils.printDebug( record.getFile().getAbsolutePath() +
            // " clicked." );

            rightResultsPane.setText( "" );

            // Remove exception.

            try
            {
                rightResultsPane.appendText( record.getFile().getName(),
                    headerStyle );
                rightResultsPane.appendText( Utils.NEW_LINE, null );
                rightResultsPane.appendText( Utils.NEW_LINE, null );

                for( int i = 0; i < lines.size(); i++ )
                {
                    LineMatch line = lines.get( i );
                    // LogUtils.printDebug( "\tWriting line:" + i );

                    rightResultsPane.appendText( line.lineNumber + ": \t",
                        null );
                    rightResultsPane.appendText( line.preMatch, null );
                    rightResultsPane.appendText( line.match, matchStyle );
                    rightResultsPane.appendText( line.postMatch, null );
                    rightResultsPane.appendText( Utils.NEW_LINE, null );
                }

                // LogUtils.printDebug( "Text:" + rightResultsPane.getText() );
            }
            catch( Exception ex )
            {
                LogUtils.printError( "\tGot an exception!" );
                ex.printStackTrace();
            }
        }
    }

    /***************************************************************************
     *
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     *
     **************************************************************************/
    public void clearPanel()
    {
        resultsTableModel.clearModel();

        GcThread.createAndStart();
    }

    /***************************************************************************
     * @param records List
     **************************************************************************/
    public void addRecords( java.util.List<? extends IExplorerItem> records )
    {
        resultsTableModel.addFiles( records );
    }

    /***************************************************************************
     * @param record SearchRecord
     **************************************************************************/
    public void addRecord( SearchRecord record )
    {
        resultsTableModel.addFile( record );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getRecordCount()
    {
        return resultsTableModel.getRowCount();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class OpenResultsListener extends MouseAdapter
    {
        private final ResultsView panel;

        // private final JExplorerFrame explorer;

        public OpenResultsListener( ResultsView adaptee )
        {
            this.panel = adaptee;
            // this.explorer = new JExplorerFrame();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseClicked( MouseEvent e )
        {
            if( e.getClickCount() == 2 &&
                SwingUtilities.isLeftMouseButton( e ) )
            {
                File file = panel.resultsTableView.getSelectedFile();

                if( file != null )
                {
                    // if( file.isDirectory() )
                    // {
                    // Desktop.getDesktop().open( file );
                    // // showFile( file );
                    // if( explorer == null )
                    // {
                    // explorer.setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE
                    // );
                    // }
                    // else
                    // {
                    // explorer.setVisible( true );
                    // }
                    // }
                    // else
                    // {
                    try
                    {
                        Desktop.getDesktop().open( file );
                    }
                    catch( Exception ex )
                    {
                        OptionUtils.showErrorMessage( panel.view,
                            ex.getMessage(), "ERROR" );
                    }
                    // }
                }

            }
        }
    }
}
