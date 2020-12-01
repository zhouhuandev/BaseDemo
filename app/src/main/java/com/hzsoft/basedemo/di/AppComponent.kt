package com.hzsoft.basedemo.di

import android.content.Context
import com.hzsoft.basedemo.MyApp
import com.hzsoft.lib.net.di.NetComponent
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        ActivityModuleBuilder::class,
        ViewModelModule::class,
    ]
)
interface AppComponent : AndroidInjector<MyApp> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun netComponentFactory(): NetComponent.Factory
}