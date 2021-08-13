package com.hzsoft.lib.net.dto

import com.hzsoft.lib.net.error.SUCCESS

// A generic class that contains data and status about loading this data.
sealed class Resource<out T>(
    val data: T? = null,
    val errorCode: Int = SUCCESS
) {
    class Success<out T>(data: T) : Resource<T>(data)
    class Loading<out T>(data: T? = null) : Resource<T>(data)
    class DataError<out T>(errorCode: Int) : Resource<T>(null, errorCode)

    fun isSuccess(): Boolean {
        return errorCode == SUCCESS
    }

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is DataError -> "Error[exception=$errorCode]"
            is Loading<T> -> "Loading"
        }
    }
}
