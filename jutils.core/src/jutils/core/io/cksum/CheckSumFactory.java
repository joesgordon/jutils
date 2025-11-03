package jutils.core.io.cksum;

import jutils.core.io.cksum.Crc16.Crc16Algorithm;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CheckSumFactory
{
    /***************************************************************************
     * @param type
     * @return
     **************************************************************************/
    public static IChecksum createSummer( ChecksumType type )
    {
        switch( type )
        {
            case MD5:
                return new Md5Checksum();

            case CRC16:
                return new Crc16( Crc16Algorithm.MODBUS.getConfig() );

            case CRC32:
                return new Crc32Checksum();

            case SHA_256:
                return new Sha256Checksum();

            case SHA_1:
                return new Sha1Checksum();
        }

        throw new IllegalArgumentException(
            "Unsupported checksum type: " + type );
    }
}
