package com.hzsoft.lib.common.utils.ext

import com.hzsoft.lib.common.utils.*
import com.hzsoft.lib.common.utils.func.getMimeType
import java.io.File
import java.nio.charset.Charset

/**
 * Describe:
 * <p></p>
 *
 * @author zhouhuan
 * @Date 2020/12/7
 */

val File.canListFiles: Boolean
    get() = canRead() and isDirectory

/**
 * Total size (include all subFile)
 */
val File.totalSize: Long
    get() = if (isFile) length() else FileUtils.getFolderSize(this)

/**
 * Formatted total size (include all subFile)
 */
val File.formatSize: String
    get() = FileUtils.getFormatFileSize(totalSize)

/**
 * Return file's mimeType, such as "png"
 */
val File.mimeType: String
    get() = getMimeType(extension, isDirectory)

/**
 * List sub files
 * @param isRecursive whether to list recursively
 * @param filter exclude some files
 */
fun File.listFiles(
    isRecursive: Boolean = false,
    filter: ((file: File) -> Boolean)? = null
): Array<out File> {
    val fileList = if (!isRecursive) listFiles() else FileUtils.getAllSubFile(this)
    var result: Array<File> = arrayOf()
    return if (filter == null) fileList
    else {
        for (file in fileList) {
            if (filter(file)) result = result.plus(file)
        }
        result
    }
}

/**
 * write some text to file
 * @param append whether to append or overwrite
 * @param charset default charset is utf-8
 */
fun File.writeText(append: Boolean = false, text: String, charset: Charset = Charsets.UTF_8) {
    if (append) appendText(text, charset) else writeText(text, charset)
}

/**
 * write some bytes to file
 * @param append whether to append or overwrite
 */
fun File.writeBytes(append: Boolean = false, bytes: ByteArray) {
    if (append) appendBytes(bytes) else writeBytes(bytes)
}

/**
 *  copy file
 *  @param destFile dest file/folder
 *  @param overwrite whether to override dest file/folder if exist
 *  @param reserve Whether to reserve source file/folder
 */
fun File.moveTo(destFile: File, overwrite: Boolean = true, reserve: Boolean = true): Boolean {
    val dest = copyRecursively(destFile, overwrite)
    if (!reserve) deleteRecursively()
    return dest
}

/**
 * copy file with progress callback
 * @param destFolder dest folder
 * @param overwrite whether to override dest file/folder if exist
 * @param func progress callback (from 0 to 100)
 */
fun File.moveToWithProgress(
    destFolder: File,
    overwrite: Boolean = true,
    reserve: Boolean = true,
    func: ((file: File, i: Int) -> Unit)? = null
) {

    if (isDirectory) FileUtils.copyFolder(this, File(destFolder, name), overwrite, func)
    else FileUtils.copyFile(this, File(destFolder, name), overwrite, func)

    if (!reserve) deleteRecursively()
}

/** Rename to newName */
fun File.rename(newName: String) =
    rename(File("$parent${File.separator}$newName"))

/** Rename to newFile's name */
fun File.rename(newFile: File) =
    if (newFile.exists()) false else renameTo(newFile)

/**
 * Convert the file to Base64
 */
fun File.encodeBase64File() = FileUtils.encodeBase64File(this)