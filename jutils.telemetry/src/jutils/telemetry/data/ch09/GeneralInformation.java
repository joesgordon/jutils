package jutils.telemetry.data.ch09;

import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * 
 ******************************************************************************/
public class GeneralInformation
{
    /**  */
    public String programName;
    /**  */
    public String filename;
    /**  */
    public String revisionLevel;
    /**  */
    public String originationDate;
    /**  */
    public String revisionNumber;
    /**  */
    public String revisionDate;
    /**  */
    public String updateNumber;
    /**  */
    public String testNumber;
    /**  */
    public final List<PointOfContact> pocs;
    /**  */
    public final List<DataSource> dataSources;
    /**  */
    public final TestInformation testInfo;
    /**  */
    public String checksum;
    /**  */
    public String comments;

    /***************************************************************************
     * 
     **************************************************************************/
    public GeneralInformation()
    {
        this.programName = "";
        this.filename = "";
        this.revisionLevel = "";
        this.originationDate = "";
        this.revisionNumber = "";
        this.revisionDate = "";
        this.updateNumber = "";
        this.testNumber = "";
        this.pocs = new ArrayList<>();
        this.dataSources = new ArrayList<>();
        this.testInfo = new TestInformation();
        this.checksum = "";
        this.comments = "";
    }
}
