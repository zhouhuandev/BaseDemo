package com.hzsoft.lib.net.di

import com.hzsoft.lib.net.DataRepository
import com.hzsoft.lib.net.DataRepositorySource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import javax.inject.Singleton

// Tells Dagger this is a Dagger module
@Module
abstract class DataModule {
    // @IntoSet
    // @Provides
    // fun provideString(): String {
    //     return "data"
    // }

    @Binds
    @Singleton
    abstract fun provideDataRepository(dataRepository: DataRepository): DataRepositorySource
}
