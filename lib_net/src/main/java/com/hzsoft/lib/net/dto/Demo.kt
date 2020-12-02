package com.hzsoft.lib.net.dto

import android.os.Parcelable
import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize


/**
 * Describe:
 * <p></p>
 *
 * @author zhouhuan
 * @Date 2020/12/1
 */
@JsonClass(generateAdapter = true)
@Parcelize
data class Demo(
    @Json(name = "calories")
    val calories: String?,
    @Json(name = "carbos")
    val carbos: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "difficulty")
    val difficulty: Int?,
    @Json(name = "fats")
    val fats: String?,
    @Json(name = "headline")
    val headline: String?,
    @Json(name = "id")
    val id: String?,
    @Json(name = "image")
    val image: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "proteins")
    val proteins: String?,
    @Json(name = "thumb")
    val thumb: String?,
    @Json(name = "time")
    val time: String?
) : Parcelable
