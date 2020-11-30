package com.task.data.error.mapper

import android.content.Context
import com.hzsoft.lib.net.R
import com.hzsoft.lib.net.error.*
import javax.inject.Inject

class ErrorMapper @Inject constructor() : ErrorMapperInterface {

    fun getErrorString(context: Context, errorId: Int): String {
        return context.getString(errorId)
    }

    override fun getErrorString(errorId: Int): String {
        return ""
    }

    override val errorsMap: Map<Int, String>
        get() = mapOf(
            Pair(NO_INTERNET_CONNECTION, getErrorString(R.string.no_internet)),
            Pair(NETWORK_ERROR, getErrorString(R.string.network_error)),
            Pair(PASS_WORD_ERROR, getErrorString(R.string.invalid_password)),
            Pair(USER_NAME_ERROR, getErrorString(R.string.invalid_username)),
            Pair(CHECK_YOUR_FIELDS, getErrorString(R.string.invalid_username_and_password)),
            Pair(SEARCH_ERROR, getErrorString(R.string.search_error))
        ).withDefault { getErrorString(R.string.network_error) }
}
