package com.hzsoft.basedemo.di.provider

import com.hzsoft.lib.net.di.NetComponent

interface NetComponentProvider {
    fun provideNetComponent(): NetComponent
}