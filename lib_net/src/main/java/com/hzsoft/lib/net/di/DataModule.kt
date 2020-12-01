package com.hzsoft.lib.net.di

import com.hzsoft.lib.net.DataRepository
import com.hzsoft.lib.net.DataRepositorySource
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

// Tells Dagger this is a Dagger module
@Module
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun provideDataRepository(dataRepository: DataRepository): DataRepositorySource
}
