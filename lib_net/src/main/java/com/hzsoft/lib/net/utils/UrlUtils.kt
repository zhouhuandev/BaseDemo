package com.hzsoft.lib.net.utils

import android.net.Uri
import android.util.LruCache
import com.hzsoft.lib.log.KLog

/**
 *
 *
 * @author <a href="mailto:zhouhuandev@gmail.com" rel="nofollow">zhouhuan</a>
 * @since 2024/3/15 15:31
 */
object UrlUtils {
    private const val TAG = "UrlUtils"
    private val sCachedUriMap = LruCache<String, Uri>(20)
    private val sCachedPurifyUrlMap = LruCache<String, String>(20)

    fun parseUrl(url: String?): Uri? {
        if (url.isNullOrEmpty()) return null
        var uri: Uri? = sCachedUriMap.get(url)
        if (uri != null) return uri
        try {
            uri = Uri.parse(url)
            sCachedUriMap.put(url, uri)
        } catch (e: Throwable) {
            KLog.e(TAG, "parse url exception!", e)
        }
        return uri
    }

    fun getHost(url: String?): String? {
        val uri = parseUrl(url)
        return uri?.host
    }

    fun getPath(url: String?): String? {
        val uri = parseUrl(url)
        return uri?.path
    }

    fun getScheme(url: String?): String? {
        val uri = parseUrl(url)
        return uri?.scheme
    }

    fun getQuery(url: String?): String? {
        val uri = parseUrl(url)
        return uri?.query
    }

    fun getPort(url: String?):Int? {
        val uri = parseUrl(url)
        return uri?.port
    }

    fun getCORSUrl(url: String?): String? {
        try {
            val uri = parseUrl(url) ?: return null
            var result = "${uri.scheme}://${uri.host}"
            if (uri.port != -1) {
                result = "${result}:${uri.port}"
            }
            return result
        } catch (e: Throwable){
            KLog.e(TAG, "getCORSUrl error!", e)
        }
        return null
    }
}