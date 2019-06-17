package org.jutils.io.parsers;

/*******************************************************************************
 * The type of existence to be checked (e.g. file/dir/either/none).
 ******************************************************************************/
public enum ExistenceType
{
    /** Do not check for existence. */
    DO_NOT_CHECK,
    /** The path must denote an existing file. */
    FILE_ONLY,
    /** The path must denote an existing directory. */
    DIRECTORY_ONLY,
    /**
     * The path must denote an existing file or directory (i.e. the path must
     * exist).
     */
    FILE_OR_DIRECTORY;
}
