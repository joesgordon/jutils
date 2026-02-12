package jutils.core.iana;

import jutils.core.INamedValue;

/*******************************************************************************
 * As defined by
 * https://datatracker.ietf.org/doc/html/draft-richardson-opsawg-pcaplinktype-00
 ******************************************************************************/
public enum LinkType implements INamedValue
{
    /** BSD loopback encapsulation */
    NULL( 0, "Null" ),
    /** IEEE 802.3 Ethernet */
    ETHERNET( 1, "Ethernet" ),
    /** Xerox experimental 3Mb Ethernet */
    EXP_ETHERNET( 2, "Xerox Experimental 3Mb Ethernet" ),
    /** AX.25 packet */
    AX25( 3, "AX.25" ),
    /** Reserved for PRONET */
    PRONET( 4, "PRONET" ),
    /** Reserved for MIT CHAOSNET */
    CHAOS( 5, "MIT CHAOSNET" ),
    /** IEEE 802.5 Token Ring */
    IEEE802_5( 6, "IEEE 802.5" ),
    /** ARCNET Data Packets with BSD encapsulation */
    ARCNET_BSD( 7, "ARCNET_BSD" ),
    /** SLIP, w/LINKTYPE_SLIP header. */
    SLIP( 8, "SLIP" ),
    /** PPP, as per RFC 1661/RFC 1662 */
    PPP( 9, "PPP" ),
    /** FDDI: per ANSI INCITS 239-1994. */
    FDDI( 10, "FDDI" ),
    /** PPP in HDLC-like framing, as per RFC 1662 */
    PPP_HDLC( 50, "PPP HDLC" ),
    /** PPPoE; per RFC 2516 */
    PPP_ETHER( 51, "PPP Ethernet" ),
    /** Reserved for Symantec Enterprise Firewall */
    SYMANTEC_FIREWALL( 99, "Symantec Firewall" ),
    /** RFC 1483 LLC/SNAP-encapsulated ATM */
    ATM_RFC1483( 100, "ATM (RFC 1483)" ),
    /** Raw IP; begins with an IPv4 or IPv6 header */
    RAW( 101, "RAW" ),
    /** Reserved for BSD/OS SLIP BPF header */
    SLIP_BSDOS( 102, "SLIP BSDOS" ),
    /** Reserved for BSD/OS PPP BPF header */
    PPP_BSDOS( 103, "PPP BSDOS" ),
    /** Cisco PPP with HDLC framing, as per section 4.3.1 of RFC 1547 */
    C_HDLC( 104, "Cisco PPP w/ HDLC" ),
    /** IEEE 802.11 wireless LAN. */
    IEEE802_11( 105, "IEEE 802.11" ),
    /** ATM Classical IP, with no header preceding IP */
    ATM_CLIP( 106, "ATM Classical IP" ),
    /** Frame Relay LAPF frames */
    FRELAY( 107, "FRELAY" ),
    /** OpenBSD loopback encapsulation */
    LOOP( 108, "LOOP" ),
    /** Reserved for OpenBSD IPSEC encapsulation */
    ENC( 109, "ENC" ),
    /** Reserved for ATM LANE + 802.3 */
    LANE8023( 110, "LANE8023" ),
    /** Reserved for NetBSD HIPPI */
    HIPPI( 111, "HIPPI" ),
    /** Reserved for NetBSD HDLC framing */
    HDLC( 112, "HDLC" ),
    /** Linux "cooked" capture encapsulation */
    LINUX_SLL( 113, "LINUX_SLL" ),
    /** Apple LocalTalk */
    LTALK( 114, "LTALK" ),
    /** Reserved for Acorn Econet */
    ECONET( 115, "ECONET" ),
    /** Reserved for OpenBSD ipfilter */
    IPFILTER( 116, "IPFILTER" ),
    /** OpenBSD pflog; "struct pfloghdr" structure */
    PFLOG( 117, "PFLOG" ),
    /** Reserved for Cisco-internal use */
    CISCO_IOS( 118, "Cisco (Internal)" ),
    /** Prism monitor mode */
    IEEE802_11_PRISM( 119, "IEEE 802.11 Prism" ),
    /** Reserved for 802.11 + FreeFreeBSD Aironet radio metadata */
    IEEE802_11_AIRONET( 120, "IEEE 802.11 Aironet" ),
    /** Reserved for Siemens HiPath HDLC */
    HHDLC( 121, "HHDLC" ),
    /** RFC 2625 IP-over-Fibre Channel */
    IP_OVER_FC( 122, "IP-over-Fibre Channel (RFC 2625)" ),
    /** ATM traffic, / per SunATM devices */
    SUNATM( 123, "SunATM" ),
    /** Reserved for RapidIO */
    RIO( 124, "RapidIO" ),
    /** Reserved for PCI Express */
    PCI_EXP( 125, "PCI Express" ),
    /** Reserved for Xilinx Aurora link layer */
    AURORA( 126, "Xilinx Aurora" ),
    /** Radiotap header[Radiotap], followed by an 802.11 header */
    IEEE802_11_RADIOTAP( 127, "IEEE 802.11 Radiotap" ),
    /** Reserved for Tazmen Sniffer Protocol */
    TZSP( 128, "TZSP" ),
    /** ARCNET Data Packets, per RFC 1051 frames w/variations */
    ARCNET_LINUX( 129, "ARCNET Data Packets (RFC 1051)" ),
    /** Reserved for Juniper Networks */
    JUNIPER_MLPPP( 130, "Juniper MLPPP" ),
    /** Reserved for Juniper Networks */
    JUNIPER_MLFR( 131, "Juniper MLFR" ),
    /** Reserved for Juniper Networks */
    JUNIPER_ES( 132, "Juniper ES" ),
    /** Reserved for Juniper Networks */
    JUNIPER_GGSN( 133, "Juniper GGSN" ),
    /** Reserved for Juniper Networks */
    JUNIPER_MFR( 134, "Juniper MFR" ),
    /** Reserved for Juniper Networks */
    JUNIPER_ATM2( 135, "Juniper ATM2" ),
    /** Reserved for Juniper Networks */
    JUNIPER_SERVICES( 136, "Juniper Services" ),
    /** Reserved for Juniper Networks */
    JUNIPER_ATM1( 137, "Juniper ATM1" ),
    /** Apple IP-over-IEEE 1394 cooked header */
    APPLE_IP_OVER_IEEE1394( 138, "Apple IP-over-IEEE 1394" ),
    /** Signaling System 7 (SS7) Message Transfer Part Level ITU-T Q.703 */
    MTP2_WITH_PHDR( 139, "MTP2 with PHDR" ),
    /** SS7 Level 2, Q.703 */
    MTP2( 140, "MTP2" ),
    /** SS7 Level 3, Q.704 */
    MTP3( 141, "MTP3" ),
    /** SS7 Control Part, ITU-T Q.711/Q.712/Q.713/Q.714 */
    SCCP( 142, "SCCP" ),
    /** DOCSIS MAC frames, DOCSIS 3.1 */
    DOCSIS( 143, "DOCSIS" ),
    /** Linux-IrDA packets w/LINKTYPE_LINUX_IRDA header */
    LINUX_IRDA( 144, "Linux-IrDA" ),
    /** Reserved for IBM SP switch */
    IBM_SP( 145, "IBM SP" ),
    /** Reserved for IBM Next Federation switch */
    IBM_SN( 146, "IBM SN" ),
    /** For private use */
    RESERVED_01( 147, "RESERVED_01" ),
    /** For private use */
    RESERVED_02( 148, "RESERVED_02" ),
    /** For private use */
    RESERVED_03( 149, "RESERVED_03" ),
    /** For private use */
    RESERVED_04( 150, "RESERVED_04" ),
    /** For private use */
    RESERVED_05( 151, "RESERVED_05" ),
    /** For private use */
    RESERVED_06( 152, "RESERVED_06" ),
    /** For private use */
    RESERVED_07( 153, "RESERVED_07" ),
    /** For private use */
    RESERVED_08( 154, "RESERVED_08" ),
    /** For private use */
    RESERVED_09( 155, "RESERVED_09" ),
    /** For private use */
    RESERVED_10( 156, "RESERVED_10" ),
    /** For private use */
    RESERVED_11( 157, "RESERVED_11" ),
    /** For private use */
    RESERVED_12( 158, "RESERVED_12" ),
    /** For private use */
    RESERVED_13( 159, "RESERVED_13" ),
    /** For private use */
    RESERVED_14( 160, "RESERVED_14" ),
    /** For private use */
    RESERVED_15( 161, "RESERVED_15" ),
    /** For private use */
    RESERVED_16( 162, "RESERVED_16" ),
    /** AVS header[AVS], followed by an 802.11 header */
    IEEE802_11_AVS( 163, "IEEE 802.11 AVS" ),
    /** Reserved for Juniper Networks */
    JUNIPER_MONITOR( 164, "Juniper MONITOR" ),
    /** BACnet MS/TP frames, per 9.3 MS/TP Frame Format ANSI 135 */
    BACNET_MS_TP( 165, "BACnet MS/TP" ),
    /**
     * PPP in HDLC-like encapsulation, like LINKTYPE_PPP_HDLC, different
     * stuffing
     */
    PPP_PPPD( 166, "PPP PPPD" ),
    /** Reserved for Juniper Networks */
    JUNIPER_PPPOE( 167, "Juniper PPPOE" ),
    /** Reserved for Juniper Networks */
    JUNIPER_PPPOE_ATM( 168, "Juniper PPPOE_ATM" ),
    /**
     * General Packet Radio Service Logical Link Control, as per 3GPP TS 04.64
     */
    GPRS_LLC( 169, "GPRS LLC" ),
    /**
     * Transparent-mapped generic framing procedure, as specified by ITU-T
     * Recommendation G.7041/Y.1303
     */
    GPF_T( 170, "GPF T" ),
    /**
     * Frame-mapped generic framing procedure, as specified by ITU-T
     * Recommendation G.7041/Y.1303
     */
    GPF_F( 171, "GPF F" ),
    /** Reserved for Gcom T1/E1 line monitoring equipment */
    GCOM_T1E1( 172, "Gcom T1/E1" ),
    /** Reserved for Gcom T1/E1 line monitoring equipment */
    GCOM_SERIAL( 173, "Gcom T1/E1 SERIAL" ),
    /** Reserved for Juniper Networks */
    JUNIPER_PIC_PEER( 174, "Juniper PIC PEER" ),
    /** Endace ERF header followed by 802.3 Ethernet */
    ERF_ETH( 175, "ERF 802.3 Ethernet" ),
    /** Endace ERF header followed by Packet-over-SONET */
    ERF_POS( 176, "ERF Packet-over-SONET" ),
    /**
     * Link Access Procedures on the D Channel (LAPD) frames, as specified by
     * ITU-T Recommendation Q.920 and ITU-T Recommendation Q.921 , captured via
     * vISDN, with a LINKTYPE_LINUX_LAPD header , followed by the Q.921 frame,
     * starting with the address field.
     */
    LINUX_LAPD( 177, "LINUX LAPD" ),
    /** Reserved for Juniper Networks */
    JUNIPER_ETHER( 178, "Juniper ETHER" ),
    /** Reserved for Juniper Networks */
    JUNIPER_PPP( 179, "Juniper PPP" ),
    /** Reserved for Juniper Networks */
    JUNIPER_FRELAY( 180, "Juniper FRELAY" ),
    /** Reserved for Juniper Networks */
    JUNIPER_CHDLC( 181, "Juniper CHDLC" ),
    /**
     * FRF.16.1 Multi-Link Frame Relay frames, beginning with an FRF.12
     * Interface fragmentation format fragmentation header.
     */
    MFR( 182, "MFR" ),
    /** Reserved for Juniper Networks */
    JUNIPER_VP( 182, "Juniper VP" ),
    /** Reserved for Arinc 653 Interpartition Communication messages */
    A653_ICM( 185, "A653_ICM" ),
    /** USB packets, beginning with a FreeBSD USB header */
    USB_FREEBSD( 186, "USB_FREEBSD" ),
    /**
     * Bluetooth HCI UART transport layer; the frame contains an HCI packet
     * indicator byte, as specified by the UART Transport Layer portion of the
     * most recent Bluetooth Core specification , followed by an HCI packet of
     * the specified packet type, as specified by the Host Controller Interface
     * Functional Specification portion of the most recent Bluetooth Core
     * Specification.
     */
    BLUETOOTH_HCI_H4( 187, "BLUETOOTH_HCI_H4" ),
    /** Reserved for IEEE 802.16 MAC Common Part Sublayer */
    IEEE802_16_MAC_CPS( 188, "IEEE802_16_MAC_CPS" ),
    /**
     * USB packets, beginning with a Linux USB header, as specified by the
     * struct usbmon_packet in the Documentation/usb/usbmon.txt file in the
     * Linux source tree. Only the first 48 bytes of that header are present.
     * All fields in the header are in host byte order. When performing a live
     * capture, the host byte order is the byte order of the machine on which
     * the packets are captured. When reading a pcap file, the byte order is the
     * byte order for the file, as specified by the file's magic number; when
     * reading a pcapng file, the byte order is the byte order for the section
     * of the pcapng file, as specified by the Section Header Block.
     */
    USB_LINUX( 189, "USB_LINUX" ),
    /** Reserved for Controller Area Network (CAN) v. 2.0B packets */
    CAN20B( 190, "CAN20B" ),
    /**
     * IEEE 802.15.4, with address fields padded, as is done by Linux drivers
     */
    IEEE802_15_4_LINUX( 191, "IEEE802_15_4_LINUX" ),
    /**
     * Per-Packet Information information, as specified by the Per-Packet
     * Information Header Specification , followed by a packet with the
     * LINKTYPE_ value specified by the pph_dlt field of that header.
     */
    PPI( 192, "PPI" ),
    /** Reserved for 802.16 MAC Common Part Sublayer plus radio header */
    IEEE802_16_MAC_CPS_RADIO( 193, "IEEE802_16_MAC_CPS_RADIO" ),
    /** Reserved for Juniper Networks */
    JUNIPER_ISM( 194, "Juniper ISM" ),
    /**
     * IEEE 802.15.4 Low-Rate Wireless Networks, with each packet having the FCS
     * at the end of the frame.
     */
    IEEE802_15_4_WITHFCS( 195, "IEEE802_15_4_WITHFCS" ),
    /** Various link-layer types, with a pseudo-header , for SITA */
    SITA( 196, "SITA" ),
    /**
     * Various link-layer types, with a pseudo-header, for Endace DAG cards;
     * encapsulates Endace ERF records.
     */
    ERF( 197, "ERF" ),
    /** Reserved for Ethernet packets captured from a u10 Networks board */
    RAIF1( 198, "RAIF1" ),
    /** Reserved for IPMB packet for IPMI, with a 2-byte header */
    IPMB_KONTRON( 199, "IPMB_KONTRON" ),
    /** Reserved for Juniper Networks */
    JUNIPER_ST( 200, "Juniper ST" ),
    /**
     * Bluetooth HCI UART transport layer; the frame contains a 4-byte direction
     * field, in network byte order (big-endian), the low-order bit of which is
     * set if the frame was sent from the host to the controller and clear if
     * the frame was received by the host from the controller, followed by an
     * HCI packet indicator byte, as specified by the UART Transport Layer
     * portion of the most recent Bluetooth Core specification , followed by an
     * HCI packet of the specified packet type, as specified by the Host
     * Controller Interface Functional Specification portion of the most recent
     * Bluetooth Core Specification.
     */
    BLUETOOTH_HCI_H4_WITH_PHDR( 201, "BLUETOOTH_HCI_H4_WITH_PHDR" ),
    /** AX.25 packet, with a 1-byte KISS header containing a type indicator. */
    AX25_KISS( 202, "AX25_KISS" ),
    /**
     * Link Access Procedures on the D Channel (LAPD) frames, as specified by
     * ITU-T Recommendation Q.920 and ITU-T Recommendation Q.921 , starting with
     * the address field, with no pseudo-header.
     */
    LAPD( 203, "LAPD" ),
    /**
     * PPP, as per RFC 1661 and RFC 1662 , preceded with a one-byte
     * pseudo-header with a zero value meaning received by this host and a
     * non-zero value meaning sent by this host; if the first 2 bytes are 0xff
     * and 0x03, it's PPP in HDLC-like framing, with the PPP header following
     * those two bytes, otherwise it's PPP without framing, and the packet
     * begins with the PPP header. The data in the frame is not octet-stuffed or
     * bit-stuffed.
     */
    PPP_WITH_DIR( 204, "PPP_WITH_DIR" ),
    /**
     * Cisco PPP with HDLC framing, as per section 4.3.1 of RFC 1547 , preceded
     * with a one-byte pseudo-header with a zero value meaning received by this
     * host and a non-zero value meaning sent by this host.
     */
    C_HDLC_WITH_DIR( 205, "C_HDLC_WITH_DIR" ),
    /**
     * Frame Relay LAPF frames, beginning with a one-byte pseudo-header with a
     * zero value meaning received by this host (DCE->DTE) and a non-zero value
     * meaning sent by this host (DTE->DCE), followed by an ITU-T Recommendation
     * Q.922 LAPF header starting with the address field, and without an FCS at
     * the end of the frame.
     */
    FRELAY_WITH_DIR( 206, "FRELAY_WITH_DIR" ),
    /**
     * Link Access Procedure, Balanced (LAPB), as specified by ITU-T
     * Recommendation X.25 , preceded with a one-byte pseudo-header with a zero
     * value meaning received by this host (DCE->DTE) and a non-zero value
     * meaning sent by this host (DTE->DCE).
     */
    LAPB_WITH_DIR( 207, "LAPB_WITH_DIR" ),
    /** IPMB over an I2C circuit, with a Linux-specific pseudo-header */
    IPMB_LINUX( 209, "IPMB_LINUX" ),
    /** Reserved for FlexRay automotive bus */
    FLEXRAY( 210, "FLEXRAY" ),
    /** Reserved for Media Oriented Systems Transport (MOST) bus */
    MOST( 211, "MOST" ),
    /**
     * Reserved for Local Interconnect Network (LIN) bus for vehicle networks
     */
    LIN( 212, "LIN" ),
    /** Reserved for X2E serial line captures */
    X2E_SERIAL( 213, "X2E_SERIAL" ),
    /** Reserved for X2E Xoraya data loggers */
    X2E_XORAYA( 214, "X2E_XORAYA" ),
    /**
     * IEEE 802.15.4 Low-Rate Wireless Networks, with each packet having the FCS
     * at the end of the frame, and with the PHY-level data for the O-QPSK,
     * BPSK, GFSK, MSK, and RCC DSS BPSK PHYs (4 octets of 0 as preamble, one
     * octet of SFD, one octet of frame length + reserved bit) preceding the
     * MAC-layer data (starting with the frame control field).
     */
    IEEE802_15_4_NONASK_PHY( 215, "IEEE802_15_4_NONASK_PHY" ),
    /** Reserved for Linux evdev messages */
    LINUX_EVDEV( 216, "LINUX_EVDEV" ),
    /** Reserved for GSM Um interface, with gsmtap header */
    GSMTAP_UM( 217, "GSMTAP_UM" ),
    /** Reserved for GSM Abis interface, with gsmtap header */
    GSMTAP_ABIS( 218, "GSMTAP_ABIS" ),
    /** MPLS packets with MPLS label as the header */
    MPLS( 219, "MPLS" ),
    /**
     * USB packets, beginning with a Linux USB header, as specified by the
     * struct usbmon_packet in the Documentation/usb/usbmon.txt file in the
     * Linux source tree. All 64 bytes of the header are present. All fields in
     * the header are in host byte order. When performing a live capture, the
     * host byte order is the byte order of the machine on which the packets are
     * captured. When reading a pcap file, the byte order is the byte order for
     * the file, as specified by the file's magic number; when reading a pcapng
     * file, the byte order is the byte order for the section of the pcapng
     * file, as specified by the Section Header Block. For isochronous
     * transfers, the ndesc field specifies the number of isochronous
     * descriptors that follow.
     */
    USB_LINUX_MMAPPED( 220, "USB_LINUX_MMAPPED" ),
    /** Reserved for DECT packets, with a pseudo-header */
    DECT( 221, "DECT" ),
    /** Reserved for OS Space Data Link Protocol */
    AOS( 222, "AOS" ),
    /** Reserved for Wireless HART (Highway Addressable Remote Transducer) */
    WIHART( 223, "WIHART" ),
    /** Fibre Channel FC-2 frames, beginning with a Frame_Header. */
    FC_2( 224, "FC_2" ),
    /**
     * Fibre Channel FC-2 frames, beginning an encoding of the SOF, followed by
     * a Frame_Header, and ending with an encoding of the SOF. The encodings
     * represent the frame delimiters as 4-byte sequences representing the
     * corresponding ordered sets, with K28.5 represented as 0xBC, and the D
     * symbols as the corresponding byte values; for example, SOFi2, which is
     * K28.5 - D21.5 - D1.2 - D21.2, is represented as 0xBC 0xB5 0x55 0x55.
     */
    FC_2_WITH_FRAME_DELIMS( 225, "FC_2_WITH_FRAME_DELIMS" ),
    /** Solaris ipnet pseudo-header , followed by an IPv4 or IPv6 datagram. */
    IPNET( 226, "IPNET" ),
    /**
     * CAN (Controller Area Network) frames, with a pseudo-header followed by
     * the frame payload.
     */
    CAN_SOCKETCAN( 227, "CAN_SOCKETCAN" ),
    /** Raw IPv4; the packet begins with an IPv4 header. */
    IPV4( 228, "IPV4" ),
    /** Raw IPv6; the packet begins with an IPv6 header. */
    IPV6( 229, "IPV6" ),
    /**
     * IEEE 802.15.4 Low-Rate Wireless Network, without the FCS at the end of
     * the frame.
     */
    IEEE802_15_4_NOFCS( 230, "IEEE802_15_4_NOFCS" ),
    /**
     * Raw D-Bus messages , starting with the endianness flag, followed by the
     * message type, etc., but without the authentication handshake before the
     * message sequence.
     */
    DBUS( 231, "DBUS" ),
    /** Reserved for Juniper Networks */
    JUNIPER_VS( 232, "Juniper VS" ),
    /** Reserved for Juniper Networks */
    JUNIPER_SRX_E2E( 233, "Juniper SRX_E2E" ),
    /** Reserved for Juniper Networks */
    JUNIPER_FIBRECHANNEL( 234, "Juniper FIBRECHANNEL" ),
    /**
     * DVB-CI (DVB Common Interface for communication between a PC Card module
     * and a DVB receiver), with the message format specified by the PCAP format
     * for DVB-CI specification
     */
    DVB_CI( 235, "DVB_CI" ),
    /**
     * Variant of 3GPP TS 27.010 multiplexing protocol (similar to, but not the
     * same as, 27.010).
     */
    MUX27010( 236, "MUX27010" ),
    /**
     * D_PDUs as described by NATO standard STANAG 5066, starting with the
     * synchronization sequence, and including both header and data CRCs. The
     * current version of STANAG 5066 is backwards-compatible with the 1.0.2
     * version , although newer versions are classified.
     */
    STANAG_5066_D_PDU( 237, "STANAG_5066_D_PDU" ),
    /** Reserved for Juniper Networks */
    JUNIPER_ATM_CEMIC( 238, "Juniper ATM_CEMIC" ),
    /** Linux netlink NETLINK NFLOG socket log messages. */
    NFLOG( 239, "NFLOG" ),
    /**
     * Pseudo-header for Hilscher Gesellschaft fuer Systemautomation mbH
     * netANALYZER devices , followed by an Ethernet frame, beginning with the
     * MAC header and ending with the FCS.
     */
    NETANALYZER( 240, "NETANALYZER" ),
    /**
     * Pseudo-header for Hilscher Gesellschaft fuer Systemautomation mbH
     * netANALYZER devices , followed by an Ethernet frame, beginning with the
     * preamble, SFD, and MAC header, and ending with the FCS.
     */
    NETANALYZER_TRANSPARENT( 241, "NETANALYZER_TRANSPARENT" ),
    /** IP-over-InfiniBand, as specified by RFC 4391 section 6 */
    IPOIB( 242, "IPOIB" ),
    /**
     * MPEG-2 Transport Stream transport packets, as specified by ISO 13818-1/
     * ITU-T Recommendation H.222.0 (see table 2-2 of section 2.4.3.2 Transport
     * Stream packet layer).
     */
    MPEG_2_TS( 243, "MPEG_2_TS" ),
    /**
     * Pseudo-header for ng4T GmbH's UMTS Iub/Iur-over-ATM and Iub/Iur-over-IP
     * format as used by their ng40 protocol tester , followed by frames for the
     * Frame Protocol as specified by 3GPP TS 25.427 for dedicated channels and
     * 3GPP TS 25.435 for common/shared channels in the case of ATM AAL2 or UDP
     * traffic, by SSCOP packets as specified by ITU-T Recommendation Q.2110 for
     * ATM AAL5 traffic, and by NBAP packets for SCTP traffic.
     */
    NG40( 244, "NG40" ),
    /**
     * Pseudo-header for NFC LLCP packet captures , followed by frame data for
     * the LLCP Protocol as specified by NFCForum-TS-LLCP_1.1
     */
    NFC_LLCP( 245, "NFC_LLCP" ),
    /** Reserved for pfsync output */
    PFSYNC( 246, "PFSYNC" ),
    /**
     * Raw InfiniBand frames, starting with the Local Routing Header, as
     * specified in Chapter 5 Data packet format of InfiniBand[TM] Architectural
     * Specification Release 1.2.1 Volume 1 - General Specifications
     */
    INFINIBAND( 247, "INFINIBAND" ),
    /**
     * SCTP packets, as defined by RFC 4960 , with no lower-level protocols such
     * as IPv4 or IPv6.
     */
    SCTP( 248, "SCTP" ),
    /** USB packets, beginning with a USBPcap header */
    USBPCAP( 249, "USBPCAP" ),
    /**
     * Serial-line packet header for the Schweitzer Engineering Laboratories
     * RTAC product , followed by a payload for one of a number of industrial
     * control protocols.
     */
    RTAC_SERIAL( 250, "RTAC_SERIAL" ),
    /**
     * Bluetooth Low Energy air interface Link Layer packets, in the format
     * described in section 2.1 PACKET FORMAT of volume 6 of the Bluetooth
     * Specification Version 4.0 (see PDF page 2200), but without the Preamble.
     */
    BLUETOOTH_LE_LL( 251, "BLUETOOTH_LE_LL" ),
    /** Reserved for Wireshark */
    WIRESHARK_UPPER_PDU( 252, "WIRESHARK_UPPER_PDU" ),
    /** Linux Netlink capture encapsulation */
    NETLINK( 253, "NETLINK" ),
    /** Bluetooth Linux Monitor encapsulation of traffic for the BlueZ stack */
    BLUETOOTH_LINUX_MONITOR( 254, "BLUETOOTH_LINUX_MONITOR" ),
    /** Bluetooth Basic Rate and Enhanced Data Rate baseband packets */
    BLUETOOTH_BREDR_BB( 255, "BLUETOOTH_BREDR_BB" ),
    /** Bluetooth Low Energy link-layer packets */
    BLUETOOTH_LE_LL_WITH_PHDR( 256, "BLUETOOTH_LE_LL_WITH_PHDR" ),
    /**
     * PROFIBUS data link layer packets, as specified by IEC standard 61158-4-3,
     * beginning with the start delimiter, ending with the end delimiter, and
     * including all octets between them.
     */
    PROFIBUS_DL( 257, "PROFIBUS_DL" ),
    /** Apple PKTAP capture encapsulation */
    PKTAP( 258, "PKTAP" ),
    /**
     * Ethernet-over-passive-optical-network packets, starting with the last 6
     * octets of the modified preamble as specified by 65.1.3.2 Transmit in
     * Clause 65 of Section 5 of IEEE 802.3 , followed immediately by an
     * Ethernet frame.
     */
    EPON( 259, "EPON" ),
    /**
     * IPMI trace packets, as specified by Table 3-20 Trace Data Block Format in
     * the PICMG HPM.2 specification The time stamps for packets in this format
     * must match the time stamps in the Trace Data Blocks.
     */
    IPMI_HPM_2( 260, "IPMI_HPM_2" ),
    /**
     * Z-Wave RF profile R1 and R2 packets , as specified by ITU-T
     * Recommendation G.9959 , with some MAC layer fields moved.
     */
    ZWAVE_R1_R2( 261, "ZWAVE_R1_R2" ),
    /**
     * Z-Wave RF profile R3 packets , as specified by ITU-T Recommendation
     * G.9959 , with some MAC layer fields moved.
     */
    ZWAVE_R3( 262, "ZWAVE_R3" ),
    /**
     * Formats for WattStopper Digital Lighting Management (DLM) and Legrand
     * Nitoo Open protocol common packet structure captures.
     */
    WATTSTOPPER_DLM( 263, "WATTSTOPPER_DLM" ),
    /**
     * Messages between ISO 14443 contactless smartcards (Proximity Integrated
     * Circuit Card, PICC) and card readers (Proximity Coupling Device, PCD),
     * with the message format specified by the PCAP format for ISO14443
     * specification
     */
    ISO_14443( 264, "ISO_14443" ),
    /**
     * Radio data system (RDS) groups, as per IEC 62106, encapsulated in this
     * form
     */
    RDS( 265, "RDS" ),
    /** USB packets, beginning with a Darwin (macOS, etc.) USB header */
    USB_DARWIN( 266, "USB_DARWIN" ),
    /** Reserved for OpenBSD DLT_OPENFLOW */
    OPENFLOW( 267, "OPENFLOW" ),
    /**
     * SDLC packets, as specified by Chapter 1, DLC Links, section Synchronous
     * Data Link Control (SDLC) of Systems Network Architecture Formats,
     * GA27-3136-20 , without the flag fields, zero-bit insertion, or Frame
     * Check Sequence field, containing SNA path information units (PIUs) as the
     * payload.
     */
    SDLC( 268, "SDLC" ),
    /** Reserved for Texas Instruments protocol sniffer */
    TI_LLN_SNIFFER( 269, "TI_LLN_SNIFFER" ),
    /**
     * LoRaTap pseudo-header , followed by the payload, which is typically the
     * PHYPayload from the LoRaWan specification
     */
    LORATAP( 270, "LORATAP" ),
    /**
     * Protocol for communication between host and guest machines in VMware and
     * KVM hypervisors.
     */
    VSOCK( 271, "VSOCK" ),
    /**
     * Messages to and from a Nordic Semiconductor nRF Sniffer for Bluetooth LE
     * packets, beginning with a pseudo-header
     */
    NORDIC_BLE( 272, "NORDIC_BLE" ),
    /**
     * DOCSIS packets and bursts, preceded by a pseudo-header giving metadata
     * about the packet
     */
    DOCSIS31_XRA31( 273, "DOCSIS31_XRA31" ),
    /**
     * mPackets, as specified by IEEE 802.3br Figure 99-4, starting with the
     * preamble and always ending with a CRC field.
     */
    ETHERNET_MPACKET( 274, "ETHERNET_MPACKET" ),
    /**
     * DisplayPort AUX channel monitoring data as specified by VESA
     * DisplayPort(DP) Standard preceded by a pseudo-header
     */
    DISPLAYPORT_AUX( 275, "DISPLAYPORT_AUX" ),
    /** Linux cooked capture encapsulation v2 */
    LINUX_SLL2( 276, "LINUX_SLL2" ),
    /** Reserved for Sercos Monitor */
    SERCOS_MONITOR( 277, "SERCOS_MONITOR" ),
    /** Openvizsla FPGA-based USB sniffer */
    OPENVIZSLA( 278, "OPENVIZSLA" ),
    /** Elektrobit High Speed Capture and Replay (EBHSCR) format */
    EBHSCR( 279, "EBHSCR" ),
    /**
     * Records in traces from the http://fd.io VPP graph dispatch tracer, in the
     * the graph dispatcher trace format
     */
    VPP_DISPATCH( 280, "VPP_DISPATCH" ),
    /**
     * Ethernet frames, with a switch tag inserted between the source address
     * field and the type/length field in the Ethernet header.
     */
    DSA_TAG_BRCM( 281, "DSA_TAG_BRCM" ),
    /**
     * Ethernet frames, with a switch tag inserted before the destination
     * address in the Ethernet header.
     */
    DSA_TAG_BRCM_PREPEND( 282, "DSA_TAG_BRCM_PREPEND" ),
    /**
     * IEEE 802.15.4 Low-Rate Wireless Networks, with a pseudo-header containing
     * TLVs with metadata preceding the 802.15.4 header.
     */
    IEEE802_15_4_TAP( 283, "IEEE802_15_4_TAP" ),
    /**
     * Ethernet frames, with a switch tag inserted between the source address
     * field and the type/length field in the Ethernet header.
     */
    DSA_TAG_DSA( 284, "DSA_TAG_DSA" ),
    /**
     * Ethernet frames, with a programmable Ethernet type switch tag inserted
     * between the source address field and the type/length field in the
     * Ethernet header.
     */
    DSA_TAG_EDSA( 285, "DSA_TAG_EDSA" ),
    /**
     * Payload of lawful intercept packets using the ELEE protocol The packet
     * begins with the ELEE header; it does not include any transport-layer or
     * lower-layer headers for protcols used to transport ELEE packets.
     */
    ELEE( 286, "ELEE" ),
    /**
     * Serial frames transmitted between a host and a Z-Wave chip over an RS-232
     * or USB serial connection, as described in section 5 of the Z-Wave Serial
     * API Host Application Programming Guide
     */
    Z_WAVE_SERIAL( 287, "Z-Wave Serial" ),
    /**
     * USB 2.0, 1.1, or 1.0 packet, beginning with a PID, as described by
     * Chapter 8 Protocol Layer of the the Universal Serial Bus Specification
     * Revision 2.0
     */
    USB_2_0( 288, "USB 2.0" ),
    /**
     * ATSC Link-Layer Protocol frames, as described in section 5 of the A/330
     * Link-Layer Protocol specification, found at the ATSC 3.0 standards page ,
     * beginning with a Base Header
     */
    ATSC_ALP( 289, "ATSC ALP" ),;

    /**  */
    public final int value;
    /**  */
    public final String name;

    /***************************************************************************
     * @param value
     * @param name
     **************************************************************************/
    private LinkType( int value, String name )
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
    public static LinkType fromValue( int value )
    {
        return INamedValue.fromValue( value, LinkType.values(), NULL );
    }

    /***************************************************************************
     * @param value
     * @return
     **************************************************************************/
    public static LinkType fromValue( short value )
    {
        return fromValue( value & 0xFFFF );
    }
}
