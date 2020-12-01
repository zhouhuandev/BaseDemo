package com.hzsoft.basedemo.di

import com.hzsoft.basedemo.MyApp
import com.hzsoft.lib.net.di.NetComponent
import com.hzsoft.lib.net.local.LocalData
import com.hzsoft.lib.net.utils.Network
import com.hzsoft.lib.net.utils.NetworkConnectivity
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module(subcomponents = [NetComponent::class])
class AppModule {

    @Provides
    @Singleton
    fun provideLocalRepository(): LocalData {
        return LocalData()
    }

    @Provides
    @Singleton
    fun provideCoroutineContext(): CoroutineContext {
        return Dispatchers.IO
    }

    @Provides
    @Singleton
    fun provideNetworkConnectivity(): NetworkConnectivity {
        return Network(MyApp.getContext())
    }
}
