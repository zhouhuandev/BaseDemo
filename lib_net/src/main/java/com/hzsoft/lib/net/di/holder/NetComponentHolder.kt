package com.hzsoft.basedemo.di.holder

import com.hzsoft.basedemo.di.provider.NetComponentProvider
import com.hzsoft.lib.common.BaseApplication
import com.hzsoft.lib.net.di.NetComponent


object NetComponentHolder {
    val netComponent: NetComponent by lazy {
        (BaseApplication.instance as NetComponentProvider).provideNetComponent()
    }
}

