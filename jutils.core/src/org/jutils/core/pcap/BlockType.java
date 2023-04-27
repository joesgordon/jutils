package org.jutils.core.pcap;

import org.jutils.core.INamedValue;

/*******************************************************************************
 * Defines the types of blocks in a PCAP Next Generation file.
 ******************************************************************************/
public enum BlockType implements INamedValue
{
    /** Defines a {@link InterfaceDescription} block. */
    INTERFACE_DESC( 0x00000001, "Interface Description" ),
    /** Defines a {@link Packet} block. */
    PACKET( 0x00000002, "Packet" ),
    /** Defines a {@link SimplePacket} block. */
    SIMPLE_PACKET( 0x00000003, "Simple Packet" ),
    /** Defines a {@link NameResolution} block. */
    NAME_RESOLUTION( 0x00000004, "Name Resolution" ),
    /** Defines a {@link InterfaceStatistics} block. */
    INTERFACE_STATS( 0x00000005, "Interface Statistics" ),
    /** Defines a {@link EnhancedPacket} block. */
    ENHANCED_PACKET( 0x00000006, "Enhanced Packet" ),
    /** Defines a IrigTimestamp block. */
    IRIG_TIMESTAMP( 0x00000007, "IRIG Timestamp" ),
    /** Defines a Arinc429 block. */
    ARINC_429( 0x00000008, "ARINC 429" ),
    /** Defines a JournalExport block. */
    JOURNAL_EXPORT( 0x00000009, "Journal Export" ),
    /** Defines a {@link DecryptionSecrets} block. */
    DECRYPTION_SECRETS( 0x0000000A, "Decryption Secrets" ),
    /** Defines a HoneMachineInfo block. */
    HONE_MACHINE( 0x00000101, "Hone Project Machine Info" ),
    /** Defines a HoneConnectionInfo block. */
    HONE_CONNECTION( 0x00000102, "Hone Project Connection Event" ),
    /** Defines a SysdigMachineInfo block. */
    SYSDIG_MACHINE( 0x00000201, "Sysdig Machine Info" ),
    /** Defines a SysdigProcessInfoV1 block. */
    SYSDIG_PROCESS_V1( 0x00000202, "Sysdig Process Info v1" ),
    /** Defines a SysdigFdList block. */
    SYSDIG_FDS( 0x00000203, "Sysdig FD List" ),
    /** Defines a SysdigEvent block. */
    SYSDIG_EVENT( 0x00000204, "Sysdig Event" ),
    /** Defines a SysdigInterfaceList block. */
    SYSDIG_INTERFACES( 0x00000205, "Sysdig Interface List" ),
    /** Defines a SysdigUserList block. */
    SYSDIG_USERS( 0x00000206, "Sysdig User List" ),
    /** Defines a SysdigProcessInfoV2 block. */
    SYSDIG_PROCESS_V2( 0x00000207, "Sysdig Process Info v2" ),
    /** Defines a SysdigEventWithFlags block. */
    SYSDIG_EVENTF( 0x00000208, "Sysdig Event with Flags" ),
    /** Defines a SysdigProcessInfoV3 block. */
    SYSDIG_PROCESS_V3( 0x00000209, "Sysdig Process Info v3" ),
    /** Defines a SysdigProcessInfoV4 block. */
    SYSDIG_PROCESS_V4( 0x00000210, "Sysdig Process Info v4" ),
    /** Defines a SysdigProcessInfoV5 block. */
    SYSDIG_PROCESS_V5( 0x00000211, "Sysdig Process Info v5" ),
    /** Defines a SysdigProcessInfoV6 block. */
    SYSDIG_PROCESS_V6( 0x00000212, "Sysdig Process Info v6" ),
    /** Defines a SysdigProcessInfoV7 block. */
    SYSDIG_PROCESS_V7( 0x00000213, "Sysdig Process Info v7" ),
    /** Defines a {@link CustomUnlocked} block. */
    CUSTOM_UNLOCKED( 0x00000BAD, "Custom Copyable" ),
    /** Defines a {@link CustomLocked} block. */
    CUSTOM_LOCKED( 0x40000BAD, "Custom Leave" ),
    /** Defines a {@link SectionHeader} block. */
    SECTION_HEADER( 0x0A0D0D0A, "Section Header" );

    /** The serializable integer that represents this type. */
    public final int value;
    /** The user-friendly name of this type. */
    public final String name;

    /***************************************************************************
     * Creates a new block type correlating the provided ID and name.
     * @param value See {@link #value}.
     * @param name See {@link #name}.
     **************************************************************************/
    private BlockType( int value, String name )
    {
        this.value = value;
        this.name = name;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return name;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getValue()
    {
        return value;
    }

    /***************************************************************************
     * @param value
     * @return
     **************************************************************************/
    public static BlockType fromValue( int value )
    {
        return INamedValue.fromValue( value, BlockType.values(), null );
    }
}
