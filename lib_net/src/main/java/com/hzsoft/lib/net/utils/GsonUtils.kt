package com.hzsoft.lib.net.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.hzsoft.lib.log.KLog
import java.lang.Exception
import java.lang.reflect.Type
import java.util.ArrayList

/**
 * Json 处理
 *
 * @author <a href="mailto: zhouhuandev@gmail.com" rel="nofollow">zhouhuan</a>
 * @since 2022/5/21
 */
object GsonUtils {

    private var gson: Gson? = null
        get() {
            if (field == null) {
                field = Gson()
            }
            return field
        }

    /**
     * Object -> Json
     */
    @JvmStatic
    fun toJson(`object`: Any?): String {
        return gson!!.toJson(`object`)
    }

    /**
     * Json -> Bean
     */
    @JvmStatic
    fun <T> fromJson(json: String?, cls: Class<T>?): T? {
        try {
            return gson!!.fromJson(json, cls)
        } catch (e: Exception) {
            KLog.e("GsonUtils", e.message ?: "")
        }
        return null
    }

    /**
     * 此方法将指定的Json反序列化为指定类型的对象。这种方法
     * 如果指定的对象是泛型类型，则非常有用。对于非通用对象，请使用
     * {@link #fromJson（String，Class）}代替。如果您在{@link Reader}中使用Json而不是
     * 一个字符串，改为使用{@link #fromJson（Reader，Type）}。
     *
     * @param <T>所需对象的类型
     * @param json       从中反序列化对象的字符串
     * @param typeOfT    src的特定通用类型。您可以使用以下方式获取此类型
     *                   {@link TypeToken}类。例如，获取类型
     *                   {@code Collection <Foo>}，您应该使用：
     *                   <pre>
     *                   类型typeOfT = new TypeToken＆lt; Collection＆lt; Foo＆gt;＆gt;（）{}。getType（）;
     *                    </ pre>
     *                    @从字符串中返回类型为T的对象。如果{@code json}是{@code null}，则返回{@code null}。
     *                    @throws JsonParseException 如果json不是typeOfT类型的对象的有效表示
     *                    @throws JsonSyntaxException 如果json不是类型对象的有效表示
     */
    @JvmStatic
    fun <T> fromJson(json: String?, typeOfT: Type?): T {
        val gsonBuilder = GsonBuilder().serializeNulls()
        return gsonBuilder.create().fromJson(json, typeOfT)
    }

    /**
     * Json -> Bean
     */
    @JvmStatic
    fun <T> fromJson(element: JsonElement, cls: Class<T>): T {
        return gson!!.fromJson(element, cls)
    }

    /**
     * Json -> List<T>
     */
    @JvmStatic
    fun <T> listFromJson(json: String, cls: Class<T>): List<T> {
        val list: MutableList<T> = ArrayList<T>()
        val array = JsonParser().parse(json).asJsonArray
        for (elem in array) {
            list.add(gson!!.fromJson(elem, cls))
        }
        return list
    }

    /**
     * Json -> List<T>
     */
    @JvmStatic
    fun <T> listFromJson(array: JsonArray, cls: Class<T>): List<T> {
        val list: MutableList<T> = ArrayList<T>()
        for (elem in array) {
            list.add(gson!!.fromJson(elem, cls))
        }
        return list
    }

    /**
     * Json -> List<Map<String, T>>
     */
    @JvmStatic
    fun <T> mapListFromJson(json: String): List<Map<String, T>>? {
        try {
            return gson!!.fromJson(json, object : TypeToken<List<Map<String, T>>>() {}.type)
        } catch (e: Exception) {
            KLog.e("", e.message ?: "")
        }
        return null
    }

    /**
     * Json -> Map<String, T>
     */
    @JvmStatic
    fun <T> mapFromJson(json: String?): Map<String, T>? {
        try {
            return gson!!.fromJson(json, object : TypeToken<Map<String?, T>?>() {}.type)
        } catch (e: Exception) {
            KLog.e("GsonUtils", e.message ?: "")
        }
        return null
    }
}


//快捷序列化
inline fun <reified T : Any> String.fromJson() = GsonUtils.fromJson(this, T::class.java)

//快捷反序列化
fun Any.toJson() = GsonUtils.toJson(this)