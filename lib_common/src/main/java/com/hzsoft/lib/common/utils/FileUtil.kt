package com.hzsoft.lib.common.utils

import android.content.Context
import android.os.Environment
import android.os.storage.StorageManager
import android.text.TextUtils
import android.util.Log
import com.hzsoft.lib.base.BaseApplication
import com.hzsoft.lib.common.BuildConfig
import com.hzsoft.lib.common.utils.ext.canListFiles
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.reflect.InvocationTargetException
import java.nio.ByteBuffer
import java.text.DecimalFormat
import java.util.regex.Pattern

/**
 * 文件操作工具类
 * @author zhouhuan
 * @time 2020/11/30 23:12
 */
object FileUtil {

    private const val TAG: String = "FileUtil"

    var storagePath: String? = null

    /**
     * 优化获取外置存储SD卡，当外置内存卡获取不到的情况下，使用内置SD卡
     *
     * @return 存储路径
     */
    fun getExternalStorageDirectory(): String? {
        if (!TextUtils.isEmpty(storagePath)) {
            return storagePath
        }
        if (!BuildConfig.DEBUG) {
            val storagePath = getStoragePath(BaseApplication.getContext(), true)
            if (!TextUtils.isEmpty(storagePath)) {
                return storagePath.also {
                    FileUtil.storagePath = it
                }
            }
        }
        return Environment.getExternalStorageDirectory().path
            .also { storagePath = it }
    }

    /**
     * 获取SD卡路径
     *
     * @param mContext     上下文
     * @param is_removable SD卡是否可移除，不可移除的是内置SD卡，可移除的是外置SD卡
     * @return 路径
     */
    fun getStoragePath(
        mContext: Context?,
        is_removable: Boolean
    ): String? {
        val mStorageManager =
            mContext!!.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        val storageVolumeClazz: Class<*>
        try {
            val getVolumeList =
                mStorageManager.javaClass.getMethod("getVolumeList")
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume")
            val getPath =
                storageVolumeClazz.getMethod("getPath")
            val isRemovable =
                storageVolumeClazz.getMethod("isRemovable")
            val getState =
                storageVolumeClazz.getMethod("getState")
            val result = getVolumeList.invoke(mStorageManager)
            val length = java.lang.reflect.Array.getLength(result)
            for (i in 0 until length) {
                val storageVolumeElement =
                    java.lang.reflect.Array.get(result, i)
                val path =
                    getPath.invoke(storageVolumeElement) as String
                val state =
                    getState.invoke(storageVolumeElement) as String
                val removable =
                    (isRemovable.invoke(storageVolumeElement) as Boolean)
                if (is_removable == removable) {
                    return path
                }
            }
        } catch (e: ClassNotFoundException) {
            Log.e(TAG, "getStoragePath: ", e)
        } catch (e: InvocationTargetException) {
            Log.e(TAG, "getStoragePath: ", e)
        } catch (e: NoSuchMethodException) {
            Log.e(TAG, "getStoragePath: ", e)
        } catch (e: IllegalAccessException) {
            Log.e(TAG, "getStoragePath: ", e)
        }
        return null
    }

    fun isImageFile(url: String): Boolean {
        if (TextUtils.isEmpty(url)) {
            return false
        }
        val reg = ".+(\\.jpeg|\\.jpg|\\.gif|\\.bmp|\\.png).*"
        val pattern = Pattern.compile(reg)
        val matcher = pattern.matcher(url.toLowerCase())
        return matcher.find()
    }

    fun isVideoFile(url: String): Boolean {
        if (TextUtils.isEmpty(url)) {
            return false
        }
        val reg =
            ".+(\\.avi|\\.wmv|\\.mpeg|\\.mp4|\\.mov|\\.mkv|\\.flv|\\.f4v|\\.m4v|\\.rmvb|\\.rm|\\.rmvb|\\.3gp|\\.dat|\\.ts|\\.mts|\\.vob).*"
        val pattern = Pattern.compile(reg)
        val matcher = pattern.matcher(url.toLowerCase())
        return matcher.find()
    }

    fun isUrl(url: String): Boolean {
        if (TextUtils.isEmpty(url)) {
            return false
        }
        val reg = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]"
        return url.matches(reg.toRegex())
    }

    /**
     * Return the file size, include all sub files
     */
    fun getFolderSize(file: File): Long {
        var total = 0L
        for (subFile in file.listFiles()) {
            total += if (subFile.isFile) subFile.length()
            else getFolderSize(subFile)
        }
        return total
    }

    /**
     * Return the formatted file size, like "4.78 GB"
     * @param unit 1000 or 1024, default to 1000
     */
    fun getFormatFileSize(size: Long, unit: Int = 1000): String {
        val formatter = DecimalFormat("####.00")
        return when {
            size < 0 -> "0 B"
            size < unit -> "$size B"
            size < unit * unit -> "${formatter.format(size.toDouble() / unit)} KB"
            size < unit * unit * unit -> "${formatter.format(size.toDouble() / unit / unit)} MB"
            else -> "${formatter.format(size.toDouble() / unit / unit / unit)} GB"
        }
    }

    /**
     * Return all subFile in the folder
     */
    fun getAllSubFile(folder: File): Array<File> {
        var fileList: Array<File> = arrayOf()
        if (!folder.canListFiles) return fileList
        for (subFile in folder.listFiles())
            fileList = if (subFile.isFile) fileList.plus(subFile)
            else fileList.plus(getAllSubFile(subFile))
        return fileList
    }

    /**
     * copy the [sourceFile] to the [destFile], only for file, not for folder
     * @param overwrite if the destFile is exist, whether to overwrite it
     */
    fun copyFile(
        sourceFile: File,
        destFile: File,
        overwrite: Boolean,
        func: ((file: File, i: Int) -> Unit)? = null
    ) {

        if (!sourceFile.exists()) return

        if (destFile.exists()) {
            val stillExists = if (!overwrite) true else !destFile.delete()

            if (stillExists) {
                return
            }
        }

        if (!destFile.exists()) destFile.createNewFile()

        val inputStream = FileInputStream(sourceFile)
        val outputStream = FileOutputStream(destFile)
        val iChannel = inputStream.channel
        val oChannel = outputStream.channel


        val totalSize = sourceFile.length()
        val buffer = ByteBuffer.allocate(1024)
        var hasRead = 0f
        var progress = -1
        while (true) {
            buffer.clear()
            val read = iChannel.read(buffer)
            if (read == -1)
                break
            buffer.limit(buffer.position())
            buffer.position(0)
            oChannel.write(buffer)
            hasRead += read

            func?.let {
                val newProgress = ((hasRead / totalSize) * 100).toInt()
                if (progress != newProgress) {
                    progress = newProgress
                    it(sourceFile, progress)
                }
            }
        }

        inputStream.close()
        outputStream.close()
    }

    /**
     * copy the [sourceFolder] to the [destFolder]
     * @param overwrite if the destFile is exist, whether to overwrite it
     */
    fun copyFolder(
        sourceFolder: File,
        destFolder: File,
        overwrite: Boolean,
        func: ((file: File, i: Int) -> Unit)? = null
    ) {
        if (!sourceFolder.exists()) return

        if (!destFolder.exists()) {
            val result = destFolder.mkdirs()
            if (!result) return
        }

        for (subFile in sourceFolder.listFiles()) {
            if (subFile.isDirectory) {
                copyFolder(
                    subFile,
                    File("${destFolder.path}${File.separator}${subFile.name}"),
                    overwrite,
                    func
                )
            } else {
                copyFile(subFile, File(destFolder, subFile.name), overwrite, func)
            }
        }
    }
}


