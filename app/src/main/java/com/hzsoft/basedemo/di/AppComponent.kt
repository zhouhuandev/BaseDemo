package com.hzsoft.basedemo.di

import android.content.Context
import com.hzsoft.lib.net.di.NetComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ViewModelModule::class])
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun netComponentFactory(): NetComponent.Factory
}