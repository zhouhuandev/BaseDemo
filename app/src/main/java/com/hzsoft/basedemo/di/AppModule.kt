package com.hzsoft.basedemo.di

import com.hzsoft.lib.net.di.NetComponent
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module(subcomponents = [NetComponent::class])
class AppModule {

    @IntoSet
    @Provides
    fun provideString(): String {
        return "app"
    }

    // @Provides
    // @Singleton
    // fun provideLocalRepository(): LocalData {
    //     return LocalData()
    // }
    //
    // @Provides
    // @Singleton
    // fun provideCoroutineContext(): CoroutineContext {
    //     return Dispatchers.IO
    // }
    //
    // @Provides
    // @Singleton
    // fun provideNetworkConnectivity(): NetworkConnectivity {
    //     return Network(App.context)
    // }
}
