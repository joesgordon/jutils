package org.jutils.core.io.cksum;

public class CheckSumFactory
{
    public static IChecksum createSummer( ChecksumType type )
    {
        switch( type )
        {
            case MD5:
                return new Md5Checksum();

            case CRC32:
                return new Crc32Checksum();

            case SHA_256:
                return new Sha256Checksum();

            case SHA_1:
                return new Sha1Checksum();

            default:
                throw new IllegalArgumentException(
                    "Unsupported checksum type: " + type );
        }
    }
}
