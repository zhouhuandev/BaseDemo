package com.hzsoft.lib.net.di

import dagger.Subcomponent


@Subcomponent(
    modules = [
        DataModule::class,
        ErrorModule::class
    ]
)
interface NetComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): NetComponent
    }
}