package com.hzsoft.lib.net.di

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent


@Subcomponent(
    modules = [
        NetModule::class,
        DataModule::class,
        ErrorModule::class
    ]
)
interface NetComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): NetComponent
    }

    fun inject(context: Context)
}