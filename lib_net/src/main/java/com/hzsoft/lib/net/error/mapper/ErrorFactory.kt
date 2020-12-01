package com.hzsoft.lib.net.error.mapper

import com.hzsoft.lib.net.error.Error

interface ErrorFactory {
    fun getError(errorCode: Int): Error
}
