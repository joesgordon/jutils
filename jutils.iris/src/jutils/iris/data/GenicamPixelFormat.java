package jutils.iris.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum GenicamPixelFormat
{
    /**   */
    MONO8( 0x01080001, "PFNC", "Monochrome 8-bit" ),
    /**   */
    MONO8S( 0x01080002, "PFNC", "Monochrome 8-bit signed" ),
    /**   */
    MONO10( 0x01100003, "PFNC", "Monochrome 10-bit unpacked" ),
    /**   */
    MONO10PACKED( 0x010C0004, "GigE Vision 2.0", "Monochrome 10-bit packed" ),
    /**   */
    MONO12( 0x01100005, "PFNC", "Monochrome 12-bit unpacked" ),
    /**   */
    MONO12PACKED( 0x010C0006, "GigE Vision 2.0", "Monochrome 12-bit packed" ),
    /**   */
    MONO16( 0x01100007, "PFNC", "Monochrome 16-bit" ),
    /**   */
    BAYERGR8( 0x01080008, "PFNC", "Bayer Green-Red 8-bit" ),
    /**   */
    BAYERRG8( 0x01080009, "PFNC", "Bayer Red-Green 8-bit" ),
    /**   */
    BAYERGB8( 0x0108000A, "PFNC", "Bayer Green-Blue 8-bit" ),
    /**   */
    BAYERBG8( 0x0108000B, "PFNC", "Bayer Blue-Green 8-bit" ),
    /**   */
    BAYERGR10( 0x0110000C, "PFNC", "Bayer Green-Red 10-bit unpacked" ),
    /**   */
    BAYERRG10( 0x0110000D, "PFNC", "Bayer Red-Green 10-bit unpacked" ),
    /**   */
    BAYERGB10( 0x0110000E, "PFNC", "Bayer Green-Blue 10-bit unpacked" ),
    /**   */
    BAYERBG10( 0x0110000F, "PFNC", "Bayer Blue-Green 10-bit unpacked" ),
    /**   */
    BAYERGR12( 0x01100010, "PFNC", "Bayer Green-Red 12-bit unpacked" ),
    /**   */
    BAYERRG12( 0x01100011, "PFNC", "Bayer Red-Green 12-bit unpacked" ),
    /**   */
    BAYERGB12( 0x01100012, "PFNC", "Bayer Green-Blue 12-bit unpacked" ),
    /**   */
    BAYERBG12( 0x01100013, "PFNC", "Bayer Blue-Green 12-bit unpacked" ),
    /**   */
    RGB8( 0x02180014, "PFNC", "Red-Green-Blue 8-bit" ),
    /**   */
    BGR8( 0x02180015, "PFNC", "Blue-Green-Red 8-bit" ),
    /**   */
    RGBA8( 0x02200016, "PFNC", "Red-Green-Blue-alpha 8-bit" ),
    /**   */
    BGRA8( 0x02200017, "PFNC", "Blue-Green-Red-alpha 8-bit" ),
    /**   */
    RGB10( 0x02300018, "PFNC", "Red-Green-Blue 10-bit unpacked" ),
    /**   */
    BGR10( 0x02300019, "PFNC", "Blue-Green-Red 10-bit unpacked" ),
    /**   */
    RGB12( 0x0230001A, "PFNC", "Red-Green-Blue 12-bit unpacked" ),
    /**   */
    BGR12( 0x0230001B, "PFNC", "Blue-Green-Red 12-bit unpacked" ),
    /**   */
    RGB10V1PACKED( 0x0220001C, "GigE Vision 2.0",
        "Red-Green-Blue 10-bit packed - variant 1" ),
    /**   */
    RGB10P32( 0x0220001D, "PFNC", "Red-Green-Blue 10-bit packed into 32-bit" ),
    /**   */
    YUV411_8_UYYVYY( 0x020C001E, "PFNC", "YUV 4:1:1 8-bit" ),
    /**   */
    YUV422_8_UYVY( 0x0210001F, "PFNC", "YUV 4:2:2 8-bit" ),
    /**   */
    YUV8_UYV( 0x02180020, "PFNC", "YUV 4:4:4 8-bit" ),
    /**   */
    RGB8_PLANAR( 0x02180021, "PFNC", "Red-Green-Blue 8-bit planar" ),
    /**   */
    RGB10_PLANAR( 0x02300022, "PFNC", "Red-Green-Blue 10-bit unpacked planar" ),
    /**   */
    RGB12_PLANAR( 0x02300023, "PFNC", "Red-Green-Blue 12-bit unpacked planar" ),
    /**   */
    RGB16_PLANAR( 0x02300024, "PFNC", "Red-Green-Blue 16-bit planar" ),
    /**   */
    MONO14( 0x01100025, "PFNC", "Monochrome 14-bit unpacked" ),
    /**   */
    BAYERGR10PACKED( 0x010C0026, "GigE Vision 2.0",
        "Bayer Green-Red 10-bit packed" ),
    /**   */
    BAYERRG10PACKED( 0x010C0027, "GigE Vision 2.0",
        "Bayer Red-Green 10-bit packed" ),
    /**   */
    BAYERGB10PACKED( 0x010C0028, "GigE Vision 2.0",
        "Bayer Green-Blue 10-bit packed" ),
    /**   */
    BAYERBG10PACKED( 0x010C0029, "GigE Vision 2.0",
        "Bayer Blue-Green 10-bit packed" ),
    /**   */
    BAYERGR12PACKED( 0x010C002A, "GigE Vision 2.0",
        "Bayer Green-Red 12-bit packed" ),
    /**   */
    BAYERRG12PACKED( 0x010C002B, "GigE Vision 2.0",
        "Bayer Red-Green 12-bit packed" ),
    /**   */
    BAYERGB12PACKED( 0x010C002C, "GigE Vision 2.0",
        "Bayer Green-Blue 12-bit packed" ),
    /**   */
    BAYERBG12PACKED( 0x010C002D, "GigE Vision 2.0",
        "Bayer Blue-Green 12-bit packed" ),
    /**   */
    BAYERGR16( 0x0110002E, "PFNC", "Bayer Green-Red 16-bit" ),
    /**   */
    BAYERRG16( 0x0110002F, "PFNC", "Bayer Red-Green 16-bit" ),
    /**   */
    BAYERGB16( 0x01100030, "PFNC", "Bayer Green-Blue 16-bit" ),
    /**   */
    BAYERBG16( 0x01100031, "PFNC", "Bayer Blue-Green 16-bit" ),
    /**   */
    YUV422_8( 0x02100032, "PFNC", "YUV 4:2:2 8-bit" ),
    /**   */
    RGB16( 0x02300033, "PFNC", "Red-Green-Blue 16-bit" ),
    /**   */
    RGB12V1PACKED( 0x02240034, "GigE Vision 2.0",
        "Red-Green-Blue 12-bit packed - variant 1" ),
    /**   */
    RGB565P( 0x02100035, "PFNC", "Red-Green-Blue 5/6/5-bit packed" ),
    /**   */
    BGR565P( 0x02100036, "PFNC", "Blue-Green-Red 5/6/5-bit packed" ),
    /**   */
    MONO1P( 0x01010037, "PFNC", "Monochrome 1-bit packed" ),
    /**   */
    MONO2P( 0x01020038, "PFNC", "Monochrome 2-bit packed" ),
    /**   */
    MONO4P( 0x01040039, "PFNC", "Monochrome 4-bit packed" ),
    /**   */
    YCBCR8_CBYCR( 0x0218003A, "PFNC", "YCbCr 4:4:4 8-bit" ),
    /**   */
    YCBCR422_8( 0x0210003B, "PFNC", "YCbCr 4:2:2 8-bit" ),
    /**   */
    YCBCR411_8_CBYYCRYY( 0x020C003C, "PFNC", "YCbCr 4:1:1 8-bit" ),
    /**   */
    YCBCR601_8_CBYCR( 0x0218003D, "PFNC", "YCbCr 4:4:4 8-bit BT.601" ),
    /**   */
    YCBCR601_422_8( 0x0210003E, "PFNC", "YCbCr 4:2:2 8-bit BT.601" ),
    /**   */
    YCBCR601_411_8_CBYYCRYY( 0x020C003F, "PFNC", "YCbCr 4:1:1 8-bit BT.601" ),
    /**   */
    YCBCR709_8_CBYCR( 0x02180040, "PFNC", "YCbCr 4:4:4 8-bit BT.709" ),
    /**   */
    YCBCR709_422_8( 0x02100041, "PFNC", "YCbCr 4:2:2 8-bit BT.709" ),
    /**   */
    YCBCR709_411_8_CBYYCRYY( 0x020C0042, "PFNC", "YCbCr 4:1:1 8-bit BT.709" ),
    /**   */
    YCBCR422_8_CBYCRY( 0x02100043, "PFNC", "YCbCr 4:2:2 8-bit" ),
    /**   */
    YCBCR601_422_8_CBYCRY( 0x02100044, "PFNC", "YCbCr 4:2:2 8-bit BT.601" ),
    /**   */
    YCBCR709_422_8_CBYCRY( 0x02100045, "PFNC", "YCbCr 4:2:2 8-bit BT.709" ),
    /**   */
    MONO10P( 0x010A0046, "PFNC", "Monochrome 10-bit packed" ),
    /**   */
    MONO12P( 0x010C0047, "PFNC", "Monochrome 12-bit packed" ),
    /**   */
    BGR10P( 0x021E0048, "PFNC", "Blue-Green-Red 10-bit packed" ),
    /**   */
    BGR12P( 0x02240049, "PFNC", "Blue-Green-Red 12-bit packed" ),
    /**   */
    BGR14( 0x0230004A, "PFNC", "Blue-Green-Red 14-bit unpacked" ),
    /**   */
    BGR16( 0x0230004B, "PFNC", "Blue-Green-Red 16-bit" ),
    /**   */
    BGRA10( 0x0240004C, "PFNC", "Blue-Green-Red-alpha 10-bit unpacked" ),
    /**   */
    BGRA10P( 0x0228004D, "PFNC", "Blue-Green-Red-alpha 10-bit packed" ),
    /**   */
    BGRA12( 0x0240004E, "PFNC", "Blue-Green-Red-alpha 12-bit unpacked" ),
    /**   */
    BGRA12P( 0x0230004F, "PFNC", "Blue-Green-Red-alpha 12-bit packed" ),
    /**   */
    BGRA14( 0x02400050, "PFNC", "Blue-Green-Red-alpha 14-bit unpacked" ),
    /**   */
    BGRA16( 0x02400051, "PFNC", "Blue-Green-Red-alpha 16-bit" ),
    /**   */
    BAYERBG10P( 0x010A0052, "PFNC", "Bayer Blue-Green 10-bit packed" ),
    /**   */
    BAYERBG12P( 0x010C0053, "PFNC", "Bayer Blue-Green 12-bit packed" ),
    /**   */
    BAYERGB10P( 0x010A0054, "PFNC", "Bayer Green-Blue 10-bit packed" ),
    /**   */
    BAYERGB12P( 0x010C0055, "PFNC", "Bayer Green-Blue 12-bit packed" ),
    /**   */
    BAYERGR10P( 0x010A0056, "PFNC", "Bayer Green-Red 10-bit packed" ),
    /**   */
    BAYERGR12P( 0x010C0057, "PFNC", "Bayer Green-Red 12-bit packed" ),
    /**   */
    BAYERRG10P( 0x010A0058, "PFNC", "Bayer Red-Green 10-bit packed" ),
    /**   */
    BAYERRG12P( 0x010C0059, "PFNC", "Bayer Red-Green 12-bit packed" ),
    /**   */
    YCBCR411_8( 0x020C005A, "PFNC", "YCbCr 4:1:1 8-bit" ),
    /**   */
    YCBCR8( 0x0218005B, "PFNC", "YCbCr 4:4:4 8-bit" ),
    /**   */
    RGB10P( 0x021E005C, "PFNC", "Red-Green-Blue 10-bit packed" ),
    /**   */
    RGB12P( 0x0224005D, "PFNC", "Red-Green-Blue 12-bit packed" ),
    /**   */
    RGB14( 0x0230005E, "PFNC", "Red-Green-Blue 14-bit unpacked" ),
    /**   */
    RGBA10( 0x0240005F, "PFNC", "Red-Green-Blue-alpha 10-bit unpacked" ),
    /**   */
    RGBA10P( 0x02280060, "PFNC", "Red-Green-Blue-alpha 10-bit packed" ),
    /**   */
    RGBA12( 0x02400061, "PFNC", "Red-Green-Blue-alpha 12-bit unpacked" ),
    /**   */
    RGBA12P( 0x02300062, "PFNC", "Red-Green-Blue-alpha 12-bit packed" ),
    /**   */
    RGBA14( 0x02400063, "PFNC", "Red-Green-Blue-alpha 14-bit unpacked" ),
    /**   */
    RGBA16( 0x02400064, "PFNC", "Red-Green-Blue-alpha 16-bit" ),
    /**   */
    YCBCR422_10( 0x02200065, "PFNC", "YCbCr 4:2:2 10-bit unpacked" ),
    /**   */
    YCBCR422_12( 0x02200066, "PFNC", "YCbCr 4:2:2 12-bit unpacked" ),
    /**   */
    SCF1WBWG8( 0x01080067, "PFNC",
        "Sparse Color Filter #1 White-Blue-White-Green 8-bit" ),
    /**   */
    SCF1WBWG10( 0x01100068, "PFNC",
        "Sparse Color Filter #1 White-Blue-White-Green 10-bit unpacked" ),
    /**   */
    SCF1WBWG10P( 0x010A0069, "PFNC",
        "Sparse Color Filter #1 White-Blue-White-Green 10-bit packed" ),
    /**   */
    SCF1WBWG12( 0x0110006A, "PFNC",
        "Sparse Color Filter #1 White-Blue-White-Green 12-bit unpacked" ),
    /**   */
    SCF1WBWG12p( 0x010C006B, "PFNC",
        "Sparse Color Filter #1 White-Blue-White-Green 12-bit packed" ),
    /**   */
    SCF1WBWG14( 0x0110006C, "PFNC",
        "Sparse Color Filter #1 White-Blue-White-Green 14-bit unpacked" ),
    /**   */
    SCF1WBWG16( 0x0110006D, "PFNC",
        "Sparse Color Filter #1 White-Blue-White-Green 16-bit unpacked" ),
    /**   */
    SCF1WGWB8( 0x0108006E, "PFNC",
        "Sparse Color Filter #1 White-Green-White-Blue 8-bit" ),
    /**   */
    SCF1WGWB10( 0x0110006F, "PFNC",
        "Sparse Color Filter #1 White-Green-White-Blue 10-bit unpacked" ),
    /**   */
    SCF1WGWB10p( 0x010A0070, "PFNC",
        "Sparse Color Filter #1 White-Green-White-Blue 10-bit packed" ),
    /**   */
    SCF1WGWB12( 0x01100071, "PFNC",
        "Sparse Color Filter #1 White-Green-White-Blue 12-bit unpacked" ),
    /**   */
    SCF1WGWB12p( 0x010C0072, "PFNC",
        "Sparse Color Filter #1 White-Green-White-Blue 12-bit packed" ),
    /**   */
    SCF1WGWB14( 0x01100073, "PFNC",
        "Sparse Color Filter #1 White-Green-White-Blue 14-bit unpacked" ),
    /**   */
    SCF1WGWB16( 0x01100074, "PFNC",
        "Sparse Color Filter #1 White-Green-White-Blue 16-bit" ),
    /**   */
    SCF1WGWR8( 0x01080075, "PFNC",
        "Sparse Color Filter #1 White-Green-White-Red 8-bit" ),
    /**   */
    SCF1WGWR10( 0x01100076, "PFNC",
        "Sparse Color Filter #1 White-Green-White-Red 10-bit unpacked" ),
    /**   */
    SCF1WGWR10p( 0x010A0077, "PFNC",
        "Sparse Color Filter #1 White-Green-White-Red 10-bit packed" ),
    /**   */
    SCF1WGWR12( 0x01100078, "PFNC",
        "Sparse Color Filter #1 White-Green-White-Red 12-bit unpacked" ),
    /**   */
    SCF1WGWR12p( 0x010C0079, "PFNC",
        "Sparse Color Filter #1 White-Green-White-Red 12-bit packed" ),
    /**   */
    SCF1WGWR14( 0x0110007A, "PFNC",
        "Sparse Color Filter #1 White-Green-White-Red 14-bit unpacked" ),
    /**   */
    SCF1WGWR16( 0x0110007B, "PFNC",
        "Sparse Color Filter #1 White-Green-White-Red 16-bit" ),
    /**   */
    SCF1WRWG8( 0x0108007C, "PFNC",
        "Sparse Color Filter #1 White-Red-White-Green 8-bit" ),
    /**   */
    SCF1WRWG10( 0x0110007D, "PFNC",
        "Sparse Color Filter #1 White-Red-White-Green 10-bit unpacked" ),
    /**   */
    SCF1WRWG10p( 0x010A007E, "PFNC",
        "Sparse Color Filter #1 White-Red-White-Green 10-bit packed" ),
    /**   */
    SCF1WRWG12( 0x0110007F, "PFNC",
        "Sparse Color Filter #1 White-Red-White-Green 12-bit unpacked" ),
    /**   */
    SCF1WRWG12p( 0x010C0080, "PFNC",
        "Sparse Color Filter #1 White-Red-White-Green 12-bit packed" ),
    /**   */
    SCF1WRWG14( 0x01100081, "PFNC",
        "Sparse Color Filter #1 White-Red-White-Green 14-bit unpacked" ),
    /**   */
    SCF1WRWG16( 0x01100082, "PFNC",
        "Sparse Color Filter #1 White-Red-White-Green 16-bit" ),
    /**   */
    YCbCr10_CbYCr( 0x02300083, "PFNC", "YCbCr 4:4:4 10-bit unpacked" ),
    /**   */
    YCbCr10p_CbYCr( 0x021E0084, "PFNC", "YCbCr 4:4:4 10-bit packed" ),
    /**   */
    YCbCr12_CbYCr( 0x02300085, "PFNC", "YCbCr 4:4:4 12-bit unpacked" ),
    /**   */
    YCbCr12p_CbYCr( 0x02240086, "PFNC", "YCbCr 4:4:4 12-bit packed" ),
    /**   */
    YCbCr422_10p( 0x02140087, "PFNC", "YCbCr 4:2:2 10-bit packed" ),
    /**   */
    YCbCr422_12p( 0x02180088, "PFNC", "YCbCr 4:2:2 12-bit packed" ),
    /**   */
    YCbCr601_10_CbYCr( 0x02300089, "PFNC",
        "YCbCr 4:4:4 10-bit unpacked BT.601" ),
    /**   */
    YCbCr601_10p_CbYCr( 0x021E008A, "PFNC",
        "YCbCr 4:4:4 10-bit packed BT.601" ),
    /**   */
    YCbCr601_12_CbYCr( 0x0230008B, "PFNC",
        "YCbCr 4:4:4 12-bit unpacked BT.601" ),
    /**   */
    YCbCr601_12p_CbYCr( 0x0224008C, "PFNC",
        "YCbCr 4:4:4 12-bit packed BT.601" ),
    /**   */
    YCbCr601_422_10( 0x0220008D, "PFNC", "YCbCr 4:2:2 10-bit unpacked BT.601" ),
    /**   */
    YCbCr601_422_10p( 0x0214008E, "PFNC", "YCbCr 4:2:2 10-bit packed BT.601" ),
    /**   */
    YCbCr601_422_12( 0x0220008F, "PFNC", "YCbCr 4:2:2 12-bit unpacked BT.601" ),
    /**   */
    YCbCr601_422_12p( 0x02180090, "PFNC", "YCbCr 4:2:2 12-bit packed BT.601" ),
    /**   */
    YCbCr709_10_CbYCr( 0x02300091, "PFNC",
        "YCbCr 4:4:4 10-bit unpacked BT.709" ),
    /**   */
    YCbCr709_10p_CbYCr( 0x021E0092, "PFNC",
        "YCbCr 4:4:4 10-bit packed BT.709" ),
    /**   */
    YCbCr709_12_CbYCr( 0x02300093, "PFNC",
        "YCbCr 4:4:4 12-bit unpacked BT.709" ),
    /**   */
    YCbCr709_12p_CbYCr( 0x02240094, "PFNC",
        "YCbCr 4:4:4 12-bit packed BT.709" ),
    /**   */
    YCbCr709_422_10( 0x02200095, "PFNC", "YCbCr 4:2:2 10-bit unpacked BT.709" ),
    /**   */
    YCbCr709_422_10p( 0x02140096, "PFNC", "YCbCr 4:2:2 10-bit packed BT.709" ),
    /**   */
    YCbCr709_422_12( 0x02200097, "PFNC", "YCbCr 4:2:2 12-bit unpacked BT.709" ),
    /**   */
    YCbCr709_422_12p( 0x02180098, "PFNC", "YCbCr 4:2:2 12-bit packed BT.709" ),
    /**   */
    YCbCr422_10_CbYCrY( 0x02200099, "PFNC", "YCbCr 4:2:2 10-bit unpacked" ),
    /**   */
    YCbCr422_10p_CbYCrY( 0x0214009A, "PFNC", "YCbCr 4:2:2 10-bit packed" ),
    /**   */
    YCbCr422_12_CbYCrY( 0x0220009B, "PFNC", "YCbCr 4:2:2 12-bit unpacked" ),
    /**   */
    YCbCr422_12p_CbYCrY( 0x0218009C, "PFNC", "YCbCr 4:2:2 12-bit packed" ),
    /**   */
    YCbCr601_422_10_CbYCrY( 0x0220009D, "PFNC",
        "YCbCr 4:2:2 10-bit unpacked BT.601" ),
    /**   */
    YCbCr601_422_10p_CbYCrY( 0x0214009E, "PFNC",
        "YCbCr 4:2:2 10-bit packed BT.601" ),
    /**   */
    YCbCr601_422_12_CbYCrY( 0x0220009F, "PFNC",
        "YCbCr 4:2:2 12-bit unpacked BT.601" ),
    /**   */
    YCbCr601_422_12p_CbYCrY( 0x021800A0, "PFNC",
        "YCbCr 4:2:2 12-bit packed BT.601" ),
    /**   */
    YCbCr709_422_10_CbYCrY( 0x022000A1, "PFNC",
        "YCbCr 4:2:2 10-bit unpacked BT.709" ),
    /**   */
    YCbCr709_422_10p_CbYCrY( 0x021400A2, "PFNC",
        "YCbCr 4:2:2 10-bit packed BT.709" ),
    /**   */
    YCbCr709_422_12_CbYCrY( 0x022000A3, "PFNC",
        "YCbCr 4:2:2 12-bit unpacked BT.709" ),
    /**   */
    YCbCr709_422_12p_CbYCrY( 0x021800A4, "PFNC",
        "YCbCr 4:2:2 12-bit packed BT.709" ),
    /**   */
    BiColorRGBG8( 0x021000A5, "PFNC", "Bi-color Red/Green - Blue/Green 8-bit" ),
    /**   */
    BiColorBGRG8( 0x021000A6, "PFNC", "Bi-color Blue/Green - Red/Green 8-bit" ),
    /**   */
    BiColorRGBG10( 0x022000A7, "PFNC",
        "Bi-color Red/Green - Blue/Green 10-bit unpacked" ),
    /**   */
    BiColorRGBG10p( 0x021400A8, "PFNC",
        "Bi-color Red/Green - Blue/Green 10-bit packed" ),
    /**   */
    BiColorBGRG10( 0x022000A9, "PFNC",
        "Bi-color Blue/Green - Red/Green 10-bit unpacked" ),
    /**   */
    BiColorBGRG10p( 0x021400AA, "PFNC",
        "Bi-color Blue/Green - Red/Green 10-bit packed" ),
    /**   */
    BiColorRGBG12( 0x022000AB, "PFNC",
        "Bi-color Red/Green - Blue/Green 12-bit unpacked" ),
    /**   */
    BiColorRGBG12p( 0x021800AC, "PFNC",
        "Bi-color Red/Green - Blue/Green 12-bit packed" ),
    /**   */
    BiColorBGRG12( 0x022000AD, "PFNC",
        "Bi-color Blue/Green - Red/Green 12-bit unpacked" ),
    /**   */
    BiColorBGRG12p( 0x021800AE, "PFNC",
        "Bi-color Blue/Green - Red/Green 12-bit packed" ),
    /**   */
    Coord3D_A8( 0x010800AF, "PFNC", "3D coordinate A 8-bit" ),
    /**   */
    Coord3D_B8( 0x010800B0, "PFNC", "3D coordinate B 8-bit" ),
    /**   */
    Coord3D_C8( 0x010800B1, "PFNC", "3D coordinate C 8-bit" ),
    /**   */
    Coord3D_ABC8( 0x021800B2, "PFNC", "3D coordinate A-B-C 8-bit" ),
    /**   */
    Coord3D_ABC8_Planar( 0x021800B3, "PFNC",
        "3D coordinate A-B-C 8-bit planar" ),
    /**   */
    Coord3D_AC8( 0x021000B4, "PFNC", "3D coordinate A-C 8-bit" ),
    /**   */
    Coord3D_AC8_Planar( 0x021000B5, "PFNC", "3D coordinate A-C 8-bit planar" ),
    /**   */
    Coord3D_A16( 0x011000B6, "PFNC", "3D coordinate A 16-bit" ),
    /**   */
    Coord3D_B16( 0x011000B7, "PFNC", "3D coordinate B 16-bit" ),
    /**   */
    Coord3D_C16( 0x011000B8, "PFNC", "3D coordinate C 16-bit" ),
    /**   */
    Coord3D_ABC16( 0x023000B9, "PFNC", "3D coordinate A-B-C 16-bit" ),
    /**   */
    Coord3D_ABC16_Planar( 0x023000BA, "PFNC",
        "3D coordinate A-B-C 16-bit planar" ),
    /**   */
    Coord3D_AC16( 0x022000BB, "PFNC", "3D coordinate A-C 16-bit" ),
    /**   */
    Coord3D_AC16_Planar( 0x022000BC, "PFNC",
        "3D coordinate A-C 16-bit planar" ),
    /**   */
    Coord3D_A32f( 0x012000BD, "PFNC", "3D coordinate A 32-bit floating point" ),
    /**   */
    Coord3D_B32f( 0x012000BE, "PFNC", "3D coordinate B 32-bit floating point" ),
    /**   */
    Coord3D_C32f( 0x012000BF, "PFNC", "3D coordinate C 32-bit floating point" ),
    /**   */
    Coord3D_ABC32f( 0x026000C0, "PFNC",
        "3D coordinate A-B-C 32-bit floating point" ),
    /**   */
    Coord3D_ABC32f_Planar( 0x026000C1, "PFNC",
        "3D coordinate A-B-C 32-bit floating point planar" ),
    /**   */
    Coord3D_AC32f( 0x024000C2, "PFNC",
        "3D coordinate A-C 32-bit floating point" ),
    /**   */
    Coord3D_AC32f_Planar( 0x024000C3, "PFNC",
        "3D coordinate A-C 32-bit floating point planar" ),
    /**   */
    Confidence1( 0x010800C4, "PFNC", "Confidence 1-bit unpacked" ),
    /**   */
    Confidence1p( 0x010100C5, "PFNC", "Confidence 1-bit packed" ),
    /**   */
    Confidence8( 0x010800C6, "PFNC", "Confidence 8-bit" ),
    /**   */
    Confidence16( 0x011000C7, "PFNC", "Confidence 16-bit" ),
    /**   */
    Confidence32f( 0x012000C8, "PFNC", "Confidence 32-bit floating point" ),
    /**   */
    R8( 0x010800C9, "PFNC", "Red 8-bit" ),
    /**   */
    R10_Deprecated( 0x010A00CA, "PFNC",
        "Deprecated because size field is wrong" ),
    /**   */
    R12_Deprecated( 0x010C00CB, "PFNC",
        "Deprecated because size field is wrong" ),
    /**   */
    R16( 0x011000CC, "PFNC", "Red 16-bit" ),
    /**   */
    G8( 0x010800CD, "PFNC", "Green 8-bit" ),
    /**   */
    G10_Deprecated( 0x010A00CE, "PFNC",
        "Deprecated because size field is wrong" ),
    /**   */
    G12_Deprecated( 0x010C00CF, "PFNC",
        "Deprecated because size field is wrong" ),
    /**   */
    G16( 0x011000D0, "PFNC", "Green 16-bit" ),
    /**   */
    B8( 0x010800D1, "PFNC", "Blue 8-bit" ),
    /**   */
    B10_Deprecated( 0x010A00D2, "PFNC",
        "Deprecated because size field is wrong" ),
    /**   */
    B12_Deprecated( 0x010C00D3, "PFNC",
        "Deprecated because size field is wrong" ),
    /**   */
    B16( 0x011000D4, "PFNC", "Blue 16-bit" ),
    /**   */
    Coord3D_A10p( 0x010A00D5, "PFNC", "3D coordinate A 10-bit packed" ),
    /**   */
    Coord3D_B10p( 0x010A00D6, "PFNC", "3D coordinate B 10-bit packed" ),
    /**   */
    Coord3D_C10p( 0x010A00D7, "PFNC", "3D coordinate C 10-bit packed" ),
    /**   */
    Coord3D_A12p( 0x010C00D8, "PFNC", "3D coordinate A 12-bit packed" ),
    /**   */
    Coord3D_B12p( 0x010C00D9, "PFNC", "3D coordinate B 12-bit packed" ),
    /**   */
    Coord3D_C12p( 0x010C00DA, "PFNC", "3D coordinate C 12-bit packed" ),
    /**   */
    Coord3D_ABC10p( 0x021E00DB, "PFNC", "3D coordinate A-B-C 10-bit packed" ),
    /**   */
    Coord3D_ABC10p_Planar( 0x021E00DC, "PFNC",
        "3D coordinate A-B-C 10-bit packed planar" ),
    /**   */
    Coord3D_ABC12p( 0x022400DE, "PFNC", "3D coordinate A-B-C 12-bit packed" ),
    /**   */
    Coord3D_ABC12p_Planar( 0x022400DF, "PFNC",
        "3D coordinate A-B-C 12-bit packed planar" ),
    /**   */
    Coord3D_AC10p( 0x021400F0, "PFNC", "3D coordinate A-C 10-bit packed" ),
    /**   */
    Coord3D_AC10p_Planar( 0x021400F1, "PFNC",
        "3D coordinate A-C 10-bit packed planar" ),
    /**   */
    Coord3D_AC12p( 0x021800F2, "PFNC", "3D coordinate A-C 12-bit packed" ),
    /**   */
    Coord3D_AC12p_Planar( 0x021800F3, "PFNC",
        "3D coordinate A-C 12-bit packed planar" ),
    /**   */
    YCbCr2020_8_CbYCr( 0x021800F4, "PFNC", "YCbCr 4:4:4 8-bit BT.2020" ),
    /**   */
    YCbCr2020_10_CbYCr( 0x023000F5, "PFNC",
        "YCbCr 4:4:4 10-bit unpacked BT.2020" ),
    /**   */
    YCbCr2020_10p_CbYCr( 0x021E00F6, "PFNC",
        "YCbCr 4:4:4 10-bit packed BT.2020" ),
    /**   */
    YCbCr2020_12_CbYCr( 0x023000F7, "PFNC",
        "YCbCr 4:4:4 12-bit unpacked BT.2020" ),
    /**   */
    YCbCr2020_12p_CbYCr( 0x022400F8, "PFNC",
        "YCbCr 4:4:4 12-bit packed BT.2020" ),
    /**   */
    YCbCr2020_411_8_CbYYCrYY( 0x020C00F9, "PFNC", "YCbCr 4:1:1 8-bit BT.2020" ),
    /**   */
    YCbCr2020_422_8( 0x021000FA, "PFNC", "YCbCr 4:2:2 8-bit BT.2020" ),
    /**   */
    YCbCr2020_422_8_CbYCrY( 0x021000FB, "PFNC", "YCbCr 4:2:2 8-bit BT.2020" ),
    /**   */
    YCbCr2020_422_10( 0x022000FC, "PFNC",
        "YCbCr 4:2:2 10-bit unpacked BT.2020" ),
    /**   */
    YCbCr2020_422_10_CbYCrY( 0x022000FD, "PFNC",
        "YCbCr 4:2:2 10-bit unpacked BT.2020" ),
    /**   */
    YCbCr2020_422_10p( 0x021400FE, "PFNC",
        "YCbCr 4:2:2 10-bit packed BT.2020" ),
    /**   */
    YCbCr2020_422_10p_CbYCrY( 0x021400FF, "PFNC",
        "YCbCr 4:2:2 10-bit packed BT.2020" ),
    /**   */
    YCbCr2020_422_12( 0x02200100, "PFNC",
        "YCbCr 4:2:2 12-bit unpacked BT.2020" ),
    /**   */
    YCbCr2020_422_12_CbYCrY( 0x02200101, "PFNC",
        "YCbCr 4:2:2 12-bit unpacked BT.2020" ),
    /**   */
    YCbCr2020_422_12p( 0x02180102, "PFNC",
        "YCbCr 4:2:2 12-bit packed BT.2020" ),
    /**   */
    YCbCr2020_422_12p_CbYCrY( 0x02180103, "PFNC",
        "YCbCr 4:2:2 12-bit packed BT.2020" ),
    /**   */
    MONO14P( 0x010E0104, "PFNC", "Monochrome 14-bit packed" ),
    /**   */
    BAYERGR14P( 0x010E0105, "PFNC", "Bayer Green-Red 14-bit packed" ),
    /**   */
    BAYERRG14P( 0x010E0106, "PFNC", "Bayer Red-Green 14-bit packed" ),
    /**   */
    BAYERGB14P( 0x010E0107, "PFNC", "Bayer Green-Blue 14-bit packed" ),
    /**   */
    BAYERBG14P( 0x010E0108, "PFNC", "Bayer Blue-Green 14-bit packed" ),
    /**   */
    BAYERGR14( 0x01100109, "PFNC", "Bayer Green-Red 14-bit" ),
    /**   */
    BAYERRG14( 0x0110010A, "PFNC", "Bayer Red-Green 14-bit" ),
    /**   */
    BAYERGB14( 0x0110010B, "PFNC", "Bayer Green-Blue 14-bit" ),
    /**   */
    BAYERBG14( 0x0110010C, "PFNC", "Bayer Blue-Green 14-bit" ),
    /**   */
    BAYERGR4P( 0x0104010D, "PFNC", "Bayer Green-Red 4-bit packed" ),
    /**   */
    BAYERRG4P( 0x0104010E, "PFNC", "Bayer Red-Green 4-bit packed" ),
    /**   */
    BAYERGB4P( 0x0104010F, "PFNC", "Bayer Green-Blue 4-bit packed" ),
    /**   */
    BAYERBG4P( 0x01040110, "PFNC", "Bayer Blue-Green 4-bit packed" ),
    /**   */
    MONO32( 0x01200111, "PFNC", "Monochrome 32-bit" ),
    /**   */
    YCBCR420_8_YY_CBCR_SEMIPLANAR( 0x020C0112, "PFNC",
        "YCbCr 4:2:0 8-bit YY/CbCr Semiplanar" ),
    /**   */
    YCBCR422_8_YY_CBCR_SEMIPLANAR( 0x02100113, "PFNC",
        "YCbCr 4:2:2 8-bit YY/CbCr Semiplanar" ),
    /**   */
    YCBCR420_8_YY_CRCB_SEMIPLANAR( 0x020C0114, "PFNC",
        "YCbCr 4:2:0 8-bit YY/CrCb Semiplanar" ),
    /**   */
    YCBCR422_8_YY_CRCB_SEMIPLANAR( 0x02100115, "PFNC",
        "YCbCr 4:2:2 8-bit YY/CrCb Semiplanar" ),
    /**   */
    DATA8( 0x01080116, "PFNC", "Data 8-bit" ),
    /**   */
    DATA8S( 0x01080117, "PFNC", "Data 8-bit signed" ),
    /**   */
    DATA16( 0x01100118, "PFNC", "Data 16-bit" ),
    /**   */
    DATA16S( 0x01100119, "PFNC", "Data 16-bit signed" ),
    /**   */
    DATA32( 0x0120011A, "PFNC", "Data 32-bit" ),
    /**   */
    DATA32S( 0x0120011B, "PFNC", "Data 32-bit signed" ),
    /**   */
    DATA32F( 0x0120011C, "PFNC", "Data 32-bit floating point" ),
    /**   */
    DATA64( 0x0140011D, "PFNC", "Data 64-bit" ),
    /**   */
    DATA64S( 0x0140011E, "PFNC", "Data 64-bit signed" ),
    /**   */
    DATA64F( 0x0140011F, "PFNC", "Data 64-bit floating point" ),
    /**   */
    R10( 0x01100120, "PFNC", "Red 10-bit" ),
    /**   */
    R12( 0x01100121, "PFNC", "Red 12-bit" ),
    /**   */
    G10( 0x01100122, "PFNC", "Green 10-bit" ),
    /**   */
    G12( 0x01100123, "PFNC", "Green 12-bit" ),
    /**   */
    B10( 0x01100124, "PFNC", "Blue 10-bit" ),
    /**   */
    B12( 0x01100125, "PFNC", "Blue 12-bit" ),
    /**   */
    COORD3D_A64F( 0x01400126, "PFNC", "3D coordinate A 64-bit floating point" ),
    /**   */
    COORD3D_B64F( 0x01400127, "PFNC", "3D coordinate B 64-bit floating point" ),
    /**   */
    COORD3D_C64F( 0x01400128, "PFNC", "3D coordinate C 64-bit floating point" ),
    /**   */
    COORD3D_ABC64F( 0x02C00129, "PFNC",
        "3D coordinate A-B-C 64-bit floating point" ),
    /**   */
    COORD3D_ABC64F_PLANAR( 0x02C0012A, "PFNC",
        "3D coordinate A-B-C 64-bit floating point planar" ),
    /**   */
    COORD3D_AC64F( 0x0280012B, "PFNC",
        "3D coordinate A-C 64-bit floating point" ),
    /**   */
    COORD3D_AC64F_PLANAR( 0x0280012C, "PFNC",
        "3D coordinate A-C 64-bit floating point planar" );

    /**   */
    public final int value;
    /**   */
    public final String definition;
    /**   */
    public final String description;

    /***************************************************************************
     * @param value
     * @param definition
     * @param description
     **************************************************************************/
    private GenicamPixelFormat( int value, String definition,
        String description )
    {
        this.value = value;
        this.definition = definition;
        this.description = description;
    }
}
