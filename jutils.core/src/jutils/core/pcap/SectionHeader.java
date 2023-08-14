package jutils.core.pcap;

/*******************************************************************************
 *
 ******************************************************************************/
public class SectionHeader extends IBlock
{
    /**  */
    public int byteOrderCode;
    /**  */
    public short majorVersion;
    /**  */
    public short minorVersion;
    /**  */
    public long sectionLength;
    /**  */
    public final SectionOptions options;

    /***************************************************************************
     * 
     **************************************************************************/
    public SectionHeader()
    {
        super( BlockType.SECTION_HEADER );

        this.byteOrderCode = 0;
        this.majorVersion = 0;
        this.minorVersion = 0;
        this.sectionLength = 0;
        this.options = new SectionOptions();
    }
}
