package com.hzsoft.lib.net.di

import com.hzsoft.lib.net.error.mapper.ErrorFactory
import com.hzsoft.lib.net.error.mapper.ErrorManager
import com.task.data.error.mapper.ErrorMapper
import com.task.data.error.mapper.ErrorMapperInterface
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import javax.inject.Singleton

// with @Module we Telling Dagger that, this is a Dagger module
@Module
abstract class ErrorModule {

    // @IntoSet
    // @Provides
    // fun provideString(): String {
    //     return "error"
    // }

    @Binds
    @Singleton
    abstract fun provideErrorFactoryImpl(errorManager: ErrorManager): ErrorFactory

    @Binds
    @Singleton
    abstract fun provideErrorMapper(errorMapper: ErrorMapper): ErrorMapperInterface
}
